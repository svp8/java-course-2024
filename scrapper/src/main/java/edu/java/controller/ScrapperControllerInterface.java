package edu.java.controller;

import edu.java.dto.Link;
import edu.java.dto.LinkListResponse;
import edu.java.dto.request.TrackLinkRequest;
import org.springframework.http.ResponseEntity;

public interface ScrapperControllerInterface {
    ResponseEntity<Link> track(TrackLinkRequest request);

    ResponseEntity<Void> untrack(TrackLinkRequest request);

    ResponseEntity<LinkListResponse> list(long chatId);

    ResponseEntity<Void> start(long chatId);

}
