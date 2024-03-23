package edu.java.controller;

import edu.java.dto.Link;
import edu.java.dto.LinkListResponse;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.NoSuchLinkException;
import edu.java.exception.URIException;
import edu.java.scheduler.LinkUpdaterScheduler;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapperController implements ScrapperControllerInterface {
    public static final String CHAT_ISN_T_REGISTERED = "Chat isn`t registered";
    private final LinkService linkService;
    private final ChatService chatService;
    @Autowired(required = false) LinkUpdaterScheduler scheduler;

    public ScrapperController(LinkService linkService, ChatService chatService) {
        this.linkService = linkService;
        this.chatService = chatService;
    }

    @Override
    @PostMapping("/track")
    @Operation(summary = "Track link",
               description = "This method tracks link for chatId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok"),
        @ApiResponse(responseCode = "404", description = CHAT_ISN_T_REGISTERED, content =
            {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = InvalidChatIdException.class))
            }
        ),
        @ApiResponse(responseCode = "400", description = "Link is already tracked", content =
            {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = DuplicateLinkException.class))
            }
        ),
        @ApiResponse(responseCode = "400", description = "Bad Uri", content =
            {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = URIException.class))
            }
        ),
    })
    public ResponseEntity<Link> track(@RequestBody TrackLinkRequest request) {
        Link link = linkService.track(request.getLink(), request.getChatId());
        return ResponseEntity.ok(link);
    }

    @GetMapping("/update")
    public void update() {
        scheduler.update();
    }

    @Override
    @PostMapping("/untrack")
    @Operation(summary = "Untrack link",
               description = "This method untracks link for chatId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok"),
        @ApiResponse(responseCode = "404", description = CHAT_ISN_T_REGISTERED, content =
            {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = InvalidChatIdException.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "No such link", content =
            {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = NoSuchLinkException.class))
            }
        )
    })
    public ResponseEntity<Void> untrack(@RequestBody TrackLinkRequest request) {
        linkService.untrack(request.getLink(), request.getChatId());
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}/all")
    @Operation(summary = "List link",
               description = "This method sends list of links of chatId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok"),
        @ApiResponse(responseCode = "404", description = CHAT_ISN_T_REGISTERED, content =
            {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = InvalidChatIdException.class))
            }
        )
    })
    public ResponseEntity<LinkListResponse> list(@PathVariable("id") long chatId) {
        List<Link> links = linkService.getAllByChatId(chatId);
        return ResponseEntity.ok(new LinkListResponse(links, links.size()));
    }

    @Override
    @PostMapping("/register/{id}")
    @Operation(summary = "Start",
               description = "This method registers chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok"),
        @ApiResponse(responseCode = "404", description = "Chat already registered", content =
            {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = InvalidChatIdException.class))
            }
        )
    })
    public ResponseEntity<Void> start(@PathVariable("id") long chatId) {
        chatService.registerChat(chatId);
        return ResponseEntity.ok().build();
    }
}
