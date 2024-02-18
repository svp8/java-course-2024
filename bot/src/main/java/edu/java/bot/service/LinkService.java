package edu.java.bot.service;

import edu.java.bot.model.Link;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<Link> links;

    public LinkService(List<Link> links) {
        this.links = links;
    }

    public List<Link> getAllTrackedLinksByUserId(long id) {
        LOGGER.info("Get links of user " + id);
        return links;
    }
}
