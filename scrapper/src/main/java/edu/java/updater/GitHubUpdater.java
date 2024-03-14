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
import edu.java.dto.github.RepositoryDto;
import edu.java.entity.BranchEntity;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.entity.PullEntity;
import edu.java.entity.RepositoryEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.github.BranchRepository;
import edu.java.repository.github.GitHubRepository;
import edu.java.repository.github.PullRepository;
import edu.java.utils.LinkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class GitHubUpdater implements Updater {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GitHubClient gitHubClient;
    private final PullRepository pullRepository;
    private final BranchRepository branchRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final BotClient botClient;

    public GitHubUpdater(
        GitHubClient gitHubClient,
        PullRepository pullRepository,
        BranchRepository branchRepository,
        ChatLinkRepository chatLinkRepository, LinkRepository linkRepository,
        BotClient botClient
    ) {
        this.gitHubClient = gitHubClient;
        this.pullRepository = pullRepository;
        this.branchRepository = branchRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.linkRepository = linkRepository;
        this.botClient = botClient;
    }

    public void update(LinkEntity linkEntity) {
        GithubLink githubLink = LinkUtils.parseGithubLink(linkEntity.getName());
        String user = githubLink.user();
        String repo = githubLink.repo();

        List<BranchDto> branchDtos = gitHubClient.fetchBranchList(repo, user);
        List<PullRequestDto> pullRequestDtos = gitHubClient.fetchPullRequestList(repo, user);

        List<PullEntity> pullEntityList=pullRepository.getAllByLinkId(linkEntity.getId());
        List<BranchEntity> branchEntityList=branchRepository.getAllByLinkId(linkEntity.getId());
        RepositoryEntity repoFromDb;
        if (pullEntityList!=null) {
            List<PullEntity> finalPullEntityList = pullEntityList;
            pullRequestDtos=pullRequestDtos.stream().filter(x-> finalPullEntityList.stream().noneMatch(entity-> x.getId() == entity.getId())).toList();
            List<PullRequestDto> finalPullRequestDtos = pullRequestDtos;
            pullEntityList=pullEntityList.stream().filter(x-> finalPullRequestDtos.stream().noneMatch(dto-> dto.getId() == x.getId())).toList();
            if(pullEntityList.size()>0){
                for (PullEntity pullEntity : pullEntityList) {
                    pullRepository.delete(pullEntity);
                }
            }
        }
        if (branchEntityList!=null) {
            List<BranchEntity> finalBranchEntityList = branchEntityList;
            branchDtos=branchDtos.stream().filter(x-> finalBranchEntityList.stream().noneMatch(entity-> Objects.equals(
                x.getName(),
                entity.getName()
            ))).toList();
            List<BranchDto> finalbranchDtos = branchDtos;
            branchEntityList=branchEntityList.stream().filter(x-> finalbranchDtos.stream().noneMatch(dto-> dto.getName()
                .equals(x.getName()))).toList();
            if(branchEntityList.size()>0){
                for (BranchEntity branchEntity : branchEntityList) {
                    branchRepository.delete(branchEntity);
                }
            }
        }
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        if (pullRequestDtos.size()>0) {
            pullRequestDtos.forEach(x->{
                pullRepository.add(new PullEntity(x.getId(),x.getTitle(),linkEntity.getId()));
                linkUpdates.add(new LinkUpdate(x.getTitle()+" created"));
            });
        }
        if (branchDtos.size()>0) {
            branchDtos.forEach(x->{
                branchRepository.add(new BranchEntity(x.getName(),linkEntity.getId()));
                linkUpdates.add(new LinkUpdate(x.getName()+" created"));
            });
        }
        if (!linkUpdates.isEmpty()) {
            List<ChatEntity> chats = chatLinkRepository.findChatsByLinkId(linkEntity.getId());
            Link link;
            try {
                link = new Link(new URI(linkEntity.getName()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            //change last_updated_at
            linkRepository.update(new LinkEntity(linkEntity.getId(), linkEntity.getName(), linkEntity.getCreatedAt(),
                OffsetDateTime.now()
            ));
            //send to all chats update
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                botClient.sendUpdate(update);
            }
        }
    }
}
