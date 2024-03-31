package edu.java.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.model.scrapper.Chat;
import edu.java.bot.model.scrapper.Update;
import edu.java.bot.service.UpdateService;
import java.net.URI;
import java.util.Collections;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active:cache")
@AutoConfigureMockMvc
class BotControllerTest{
    private static final String REMOTE_ADDR = "127.0.0.1";
    @MockBean UpdateService updateService;
    @MockBean Bot bot;
    @MockBean ListCommand listCommand;
    @Autowired
    private MockMvc mockMvc;
    Update update = new Update(new Chat(0), new Link(URI.create("123")),
        null
    );

    @Test
    @DisplayName("Should throw exception if rate capacity is exhausted")
    void sendMessage() throws Exception {

//        Mockito.when(updateService.sendUpdate(update));

        String url = "/send";
        IntStream.rangeClosed(1, 5)
            .boxed()
            .sorted(Collections.reverseOrder())
            .forEach(counter -> {
                try {
                    successfulWebRequest(url, counter - 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        blockedWebRequestDueToRateLimit(url);
    }

    private void successfulWebRequest(String url, Integer remainingTries) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc
            .perform(post(url).with(request -> {
                    request.setRemoteAddr(REMOTE_ADDR);
                    return request;
                })
                .content(objectMapper.writeValueAsString(update))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().longValue("X-Rate-Limit-Remaining", remainingTries));

    }

    private void blockedWebRequestDueToRateLimit(String url) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc
            .perform(post(url).with(request -> {
                    request.setRemoteAddr(REMOTE_ADDR);
                    return request;
                })
                .content(objectMapper.writeValueAsString(update))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.TOO_MANY_REQUESTS.value()))
            .andExpect(content().string(containsString("\"message\": \"You have exhausted your API Request Quota\"")));
    }
}
