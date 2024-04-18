package edu.java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.Link;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.LinkService;
import java.util.Collections;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
public class ScrapperControllerTest extends IntegrationTest {
    private static final String REMOTE_ADDR = "127.0.0.1";
    public static final String LINK = "123";
    @MockBean LinkService linkService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should throw exception if rate capacity is exhausted")
    void track() throws Exception {

        Mockito.when(linkService.track(LINK, 0))
            .thenReturn(Mockito.mock(Link.class));

        String url = "/track";
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
            TrackLinkRequest trackLinkRequest = new TrackLinkRequest(0, LINK);
            this.mockMvc
                .perform(post(url).with(request -> {
                        request.setRemoteAddr(REMOTE_ADDR);
                        return request;
                    })
                    .content(objectMapper.writeValueAsString(trackLinkRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().longValue("X-Rate-Limit-Remaining", remainingTries));

    }

    private void blockedWebRequestDueToRateLimit(String url) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TrackLinkRequest trackLinkRequest = new TrackLinkRequest(0, LINK);
        this.mockMvc
            .perform(post(url).with(request -> {
                    request.setRemoteAddr(REMOTE_ADDR);
                    return request;
                })
                .content(objectMapper.writeValueAsString(trackLinkRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.TOO_MANY_REQUESTS.value()))
            .andExpect(content().string(containsString("\"message\": \"You have exhausted your API Request Quota\"")));
    }
}
