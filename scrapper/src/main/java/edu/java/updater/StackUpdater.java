package edu.java.updater;

import edu.java.client.BotClient;
import edu.java.client.StackOverflowClient;
import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.LinkUpdate;
import edu.java.dto.Update;
import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.CommentDto;
import edu.java.dto.stack.GeneralResponse;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.entity.QuestionEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.stack.StackRepository;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class StackUpdater implements Updater {
    private final StackOverflowClient stackOverflowClient;
    private final StackRepository stackRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final BotClient botClient;
    public StackUpdater(StackOverflowClient stackOverflowClient, StackRepository stackRepository,
        ChatLinkRepository chatLinkRepository,
        BotClient botClient
    ) {
        this.stackOverflowClient = stackOverflowClient;
        this.stackRepository = stackRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.botClient = botClient;
    }

    @Override
    public void update(LinkEntity linkEntity) {
        Pattern pattern = Pattern.compile("https:\\/\\/stackoverflow\\.com\\/questions\\/(\\d*)\\/");
        Matcher matcher = pattern.matcher(linkEntity.getName());
        String id = null;
        while (matcher.find()) {
            id = matcher.group(1);
        }
        QuestionEntity questionEntity = stackRepository.getQuestion(Integer.parseInt(id));
        GeneralResponse<AnswerDto> answersByQuestionId =
            stackOverflowClient.getAnswersByQuestionId(questionEntity.getId());
        GeneralResponse<CommentDto> commentsByQuestionId =
            stackOverflowClient.getCommentsByQuestionId(questionEntity.getId());
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        if (questionEntity.getAnswerCount() != answersByQuestionId.getItems().length) {
            linkUpdates.add(new LinkUpdate("change amswers"));
        }
        if (questionEntity.getCommentCount() != commentsByQuestionId.getItems().length) {
            linkUpdates.add(new LinkUpdate("change pr"));
        }
        if (linkUpdates.size() != 0) {
            List<ChatEntity> chats = chatLinkRepository.findChatsByLinkId(linkEntity.getId());
            Link link;
            try {
                link = new Link(new URI(linkEntity.getName()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                botClient.sendUpdate(update);
            }
        }
    }
}
