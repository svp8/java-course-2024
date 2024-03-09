package edu.java.updater;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.LinkUpdate;
import edu.java.dto.Update;
import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.dto.github.RepositoryDto;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.entity.RepositoryEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.github.GitHubRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GitHubUpdater implements Updater {
    private final GitHubClient gitHubClient;
    private final GitHubRepository gitHubRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final BotClient botClient;

    public GitHubUpdater(
        GitHubClient gitHubClient, GitHubRepository gitHubRepository,
        ChatLinkRepository chatLinkRepository,
        BotClient botClient
    ) {
        this.gitHubClient = gitHubClient;
        this.gitHubRepository = gitHubRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.botClient = botClient;
    }

    public void update(LinkEntity linkEntity) {
        Pattern pattern = Pattern.compile("https:\\/\\/github\\.com\\/(.*?)\\/(.*)");
        Matcher matcher = pattern.matcher(linkEntity.getName());
        String user = null;
        String repo = null;
        while (matcher.find()) {
            user = matcher.group(1);
            repo = matcher.group(2);

        }
        RepositoryDto repositoryDto = gitHubClient.fetchRepository(repo, user);
        List<BranchDto> branchDtos = gitHubClient.fetchBranchList(repo, user);
        List<PullRequestDto> pullRequestDtos = gitHubClient.fetchPullRequestList(repo, user);
        RepositoryEntity repoFromDb = gitHubRepository.getRepo(repositoryDto.getId());
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        if (repoFromDb.getBranchCount() != branchDtos.size()) {
            linkUpdates.add(new LinkUpdate("change branch"));
        }
        if (repoFromDb.getPullCount() != pullRequestDtos.size()) {
            linkUpdates.add(new LinkUpdate("change pr"));
        }
        if (!linkUpdates.isEmpty()) {
            List<ChatEntity> chats = chatLinkRepository.findChatsByLinkId(linkEntity.getId());
            Link link;
            try {
                link = new Link(new URI(linkEntity.getName()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                botClient.sendUpdate(update);
            }
        }
    }
}
