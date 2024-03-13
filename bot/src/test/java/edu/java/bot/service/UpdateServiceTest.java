package edu.java.bot.service;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.exception.SendMessageException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.model.request.UpdateRequest;
import edu.java.bot.model.scrapper.LinkUpdate;
import edu.java.bot.model.scrapper.Update;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UpdateServiceTest {
    UpdateService updateService;
    Bot bot;

    @BeforeEach
    void setUp() {
        bot = Mockito.mock(Bot.class);
        updateService = new UpdateService(bot);
    }

    @Test
    void sendUpdates() throws URISyntaxException {
        //given
        SendResponse sendResponse = Mockito.mock(SendResponse.class);
        List<LinkUpdate> linkUpdates = List.of(new LinkUpdate(new Link(new URI("test")), "test desc"));
        UpdateRequest updateRequest = new UpdateRequest(List.of(new Update(1, linkUpdates)));
        Mockito.when(bot.sendMessage(1, updateService.formatUpdates(linkUpdates))).thenReturn(sendResponse);
        Mockito.when(sendResponse.isOk()).thenReturn(true);
        //when
        //then
        updateService.sendUpdates(updateRequest);
    }

    @Test
    void sendUpdatesThrows() throws URISyntaxException {
        //given
        SendResponse sendResponse = Mockito.mock(SendResponse.class);
        List<LinkUpdate> linkUpdates = List.of(new LinkUpdate(new Link(new URI("test")), "test desc"));
        UpdateRequest updateRequest = new UpdateRequest(List.of(new Update(1, linkUpdates)));
        Mockito.when(bot.sendMessage(1, updateService.formatUpdates(linkUpdates))).thenReturn(sendResponse);
        Mockito.when(sendResponse.isOk()).thenReturn(false);
        Mockito.when(sendResponse.errorCode()).thenReturn(400);
        Mockito.when(sendResponse.description()).thenReturn("123");
        //when
        //then
        Assertions.assertThrows(SendMessageException.class, () -> updateService.sendUpdates(updateRequest));
    }
}
