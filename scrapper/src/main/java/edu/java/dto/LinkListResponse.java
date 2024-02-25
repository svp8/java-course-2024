package edu.java.dto;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LinkListResponse {
    private List<Link> links;
    private int size;
}
