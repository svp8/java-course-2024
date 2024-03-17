package edu.java.updater;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.LinkUpdate;
import edu.java.dto.Update;
import edu.java.dto.github.BranchDto;
import edu.java.dto.github.GithubLink;
import edu.java.dto.github.PullRequestDto;
import edu.java.entity.BranchEntity;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.entity.PullEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.github.BranchRepository;
import edu.java.repository.github.PullRepository;
import edu.java.service.LinkService;
import edu.java.utils.LinkUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GitHubUpdater implements Updater {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GitHubClient gitHubClient;
    private final PullRepository pullRepository;
    private final BranchRepository branchRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final BotClient botClient;
    private final LinkService linkService;

    public GitHubUpdater(
        GitHubClient gitHubClient,
        PullRepository pullRepository,
        BranchRepository branchRepository,
        ChatLinkRepository chatLinkRepository,
        BotClient botClient, LinkService linkService
    ) {
        this.gitHubClient = gitHubClient;
        this.pullRepository = pullRepository;
        this.branchRepository = branchRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.botClient = botClient;
        this.linkService = linkService;
    }

    @Transactional
    public void update(LinkEntity linkEntity) {
        GithubLink githubLink = LinkUtils.parseGithubLink(linkEntity.getName());
        String user = githubLink.user();
        String repo = githubLink.repo();

        List<BranchDto> branchDtos = gitHubClient.fetchBranchList(repo, user);
        List<PullRequestDto> pullRequestDtos = gitHubClient.fetchPullRequestList(repo, user);

        List<PullEntity> pullEntityList = pullRepository.getAllByLinkId(linkEntity.getId());
        List<BranchEntity> branchEntityList = branchRepository.getAllByLinkId(linkEntity.getId());

        if (pullEntityList != null) {
            List<PullEntity> finalPullEntityList = pullEntityList;
            List<PullRequestDto> finalPullRequestDtos = pullRequestDtos;
            //оставляем только те обновления, которых нет в бд
            pullRequestDtos = pullRequestDtos.stream()
                .filter(x -> finalPullEntityList.stream().noneMatch(entity -> x.getId() == entity.getId())).toList();
            //ищем записи, которых нет в ответе от апи (значит эти записи были удалены)
            pullEntityList = pullEntityList.stream()
                .filter(x -> finalPullRequestDtos.stream().noneMatch(dto -> dto.getId() == x.getId())).toList();
//удаляем эти записи из бд
            for (PullEntity pullEntity : pullEntityList) {
                pullRepository.delete(pullEntity);
            }

        }
        if (branchEntityList != null) {
            List<BranchEntity> finalBranchEntityList = branchEntityList;
            List<BranchDto> finalbranchDtos = branchDtos;
            //оставляем только те обновления, которых нет в бд
            branchDtos =
                branchDtos.stream().filter(x -> finalBranchEntityList.stream().noneMatch(entity -> Objects.equals(
                    x.getName(),
                    entity.getName()
                ))).toList();
            //ищем записи, которых нет в ответе от апи (значит эти записи были удалены)
            branchEntityList =
                branchEntityList.stream().filter(x -> finalbranchDtos.stream().noneMatch(dto -> dto.getName()
                    .equals(x.getName()))).toList();
            //удаляем эти записи из бд
            if (!branchEntityList.isEmpty()) {
                for (BranchEntity branchEntity : branchEntityList) {
                    branchRepository.delete(branchEntity);
                }
            }
        }
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        if (!pullRequestDtos.isEmpty()) {
            pullRequestDtos.forEach(x -> {
                pullRepository.add(new PullEntity(x.getId(), x.getTitle(), linkEntity.getId()));
                linkUpdates.add(new LinkUpdate("New pr was created with name " + x.getTitle()));
            });
        }
        if (!branchDtos.isEmpty()) {
            branchDtos.forEach(x -> {
                branchRepository.add(new BranchEntity(x.getName(), linkEntity.getId()));
                linkUpdates.add(new LinkUpdate("New branch was created with name " + x.getName()));
            });
        }
        if (!linkUpdates.isEmpty()) {
            List<ChatEntity> chats = chatLinkRepository.findChatsByLinkId(linkEntity.getId());
            Link link = linkService.update(linkEntity);
            //send to all chats update
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                botClient.sendUpdate(update);
            }
        }
    }
}
