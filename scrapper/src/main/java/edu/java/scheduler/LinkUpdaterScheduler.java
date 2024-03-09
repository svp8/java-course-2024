package edu.java.scheduler;

import edu.java.entity.LinkEntity;
import edu.java.updater.GitHubUpdater;
import edu.java.repository.LinkRepository;
import java.util.List;
import edu.java.updater.StackUpdater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdaterScheduler {
    private static final Logger LOGGER = LogManager.getLogger();
    private final LinkRepository linkRepository;
    private final GitHubUpdater gitHubUpdater;
    private final StackUpdater stackUpdater;

    public LinkUpdaterScheduler(LinkRepository linkRepository,
        GitHubUpdater gitHubUpdater,
        StackUpdater stackUpdater
    ) {
        this.linkRepository = linkRepository;
        this.gitHubUpdater = gitHubUpdater;

        this.stackUpdater = stackUpdater;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        List<LinkEntity> list= linkRepository.findAllLastUpdated();
        for (LinkEntity linkEntity : list) {
            if(linkEntity.getName().startsWith("https//github.com")){
                gitHubUpdater.update(linkEntity);
            } else if (linkEntity.getName().startsWith("https//stackoverflow.com")) {
                stackUpdater.update(linkEntity);
            }
        }
        LOGGER.info("Link updated");
    }
}
