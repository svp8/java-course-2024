package edu.java.bot.model.response;

import edu.java.bot.model.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.net.URI;
import java.util.List;

@Getter
@AllArgsConstructor
public class LinkListResponse {
    private List<Link> links;
    private int size;
}
