package edu.java.bot.service;

import edu.java.bot.model.Link;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    private final List<Link> links;

    public LinkService(List<Link> links) {
        this.links = links;
    }

    public List<Link> getAllTrackedLinksByUserId(long id) {
        return links;
    }
}
