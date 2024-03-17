package edu.java.scheduler;

import edu.java.entity.LinkEntity;
import edu.java.repository.LinkRepository;
import edu.java.updater.GitHubUpdater;
import edu.java.updater.StackUpdater;
import java.time.Duration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.scheduler.enable")
public class LinkUpdaterScheduler {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String GITHUB_COM = "https://github.com";
    public static final String STACKOVERFLOW_COM = "https://stackoverflow.com";
    public static final int SECONDS = 20 * 60;
    private final LinkRepository linkRepository;
    private final GitHubUpdater gitHubUpdater;
    private final StackUpdater stackUpdater;
    private final Duration interval;

    public LinkUpdaterScheduler(
        LinkRepository linkRepository,
        GitHubUpdater gitHubUpdater,
        StackUpdater stackUpdater,
        @Value("${app.scheduler.interval}") Duration interval
    ) {
        this.linkRepository = linkRepository;
        this.gitHubUpdater = gitHubUpdater;
        this.stackUpdater = stackUpdater;
        this.interval = interval;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        List<LinkEntity> list = linkRepository.findAllLastUpdated(interval);
        if (list != null) {
            for (LinkEntity linkEntity : list) {
                if (linkEntity.getName().startsWith(GITHUB_COM)) {
                    gitHubUpdater.update(linkEntity);
                } else if (linkEntity.getName().startsWith(STACKOVERFLOW_COM)) {
                    stackUpdater.update(linkEntity);
                }
            }
        }
        LOGGER.info("Link updated");
    }
}
