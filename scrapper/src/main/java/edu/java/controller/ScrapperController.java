package edu.java.controller;

import edu.java.dto.Link;
import edu.java.dto.LinkListResponse;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.service.LinkService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapperController implements ScrapperControllerInterface {
    private final LinkService linkService;

    public ScrapperController(LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    @PostMapping("/track")
    public ResponseEntity<Link> track(@RequestBody TrackLinkRequest request) {
        Link link = linkService.track(request.getLink(), request.getChatId());
        return ResponseEntity.ok(link);
    }

    @Override
    @DeleteMapping("/untrack")
    public ResponseEntity<Void> untrack(@RequestBody TrackLinkRequest request) {
        linkService.untrack(request.getLink(), request.getChatId());
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}/all")
    public ResponseEntity<LinkListResponse> list(@PathVariable("id") long chatId) {
        List<Link> links = linkService.getAllByChatId(chatId);
        return ResponseEntity.ok(new LinkListResponse(links, links.size()));
    }

    @Override
    @PostMapping("/register/{id}")
    public ResponseEntity<Void> start(@PathVariable("id") long chatId) {
        linkService.registerChatId(chatId);
        return ResponseEntity.ok().build();
    }
}
