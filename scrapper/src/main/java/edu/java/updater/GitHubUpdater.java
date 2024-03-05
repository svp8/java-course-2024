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
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.entity.RepositoryEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.github.GitHubRepository;
import edu.java.utils.LinkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class GitHubUpdater implements Updater {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GitHubClient gitHubClient;
    private final GitHubRepository gitHubRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final BotClient botClient;

    public GitHubUpdater(
        GitHubClient gitHubClient, GitHubRepository gitHubRepository,
        ChatLinkRepository chatLinkRepository, LinkRepository linkRepository,
        BotClient botClient
    ) {
        this.gitHubClient = gitHubClient;
        this.gitHubRepository = gitHubRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.linkRepository = linkRepository;
        this.botClient = botClient;
    }

    public void update(LinkEntity linkEntity) {
        GithubLink githubLink = LinkUtils.parseGithubLink(linkEntity.getName());
        String user = githubLink.user();
        String repo = githubLink.repo();

        RepositoryDto repositoryDto = gitHubClient.fetchRepository(repo, user);

        List<BranchDto> branchDtos = gitHubClient.fetchBranchList(repo, user);
        List<PullRequestDto> pullRequestDtos = gitHubClient.fetchPullRequestList(repo, user);
        Optional<RepositoryEntity> optionalRepositoryEntity = gitHubRepository.getRepo(repositoryDto.getId());
        RepositoryEntity repoFromDb;
        if (optionalRepositoryEntity.isEmpty()) {
            repoFromDb = new RepositoryEntity(repositoryDto.getId(), -1, -1);
            gitHubRepository.add(repoFromDb);
        } else {
            repoFromDb = optionalRepositoryEntity.get();
        }
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        if (repoFromDb.getBranchCount() != branchDtos.size()) {
            linkUpdates.add(new LinkUpdate("Branch count changed"));
        }
        if (repoFromDb.getPullCount() != pullRequestDtos.size()) {
            linkUpdates.add(new LinkUpdate("Pull count changed"));
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
            //update repo in db
            gitHubRepository.update(new RepositoryEntity(
                repoFromDb.getId(),
                branchDtos.size(),
                pullRequestDtos.size()
            ));
            //send to all chats update
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                botClient.sendUpdate(update);
            }
        }
    }
}
