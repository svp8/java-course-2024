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
import edu.java.repository.LinkRepository;
import edu.java.repository.stack.StackRepository;
import edu.java.utils.LinkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class StackUpdater implements Updater {
    private final StackOverflowClient stackOverflowClient;
    private final StackRepository stackRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final BotClient botClient;

    public StackUpdater(
        StackOverflowClient stackOverflowClient, StackRepository stackRepository,
        ChatLinkRepository chatLinkRepository, LinkRepository linkRepository,
        BotClient botClient
    ) {
        this.stackOverflowClient = stackOverflowClient;
        this.stackRepository = stackRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.linkRepository = linkRepository;
        this.botClient = botClient;
    }

    @Override
    public void update(LinkEntity linkEntity) {
        int id = LinkUtils.parseStackLink(linkEntity.getName()).id();

        Optional<QuestionEntity> optionalQuestionEntity = stackRepository.getQuestion(id);
        QuestionEntity questionEntityFromDb;
        if (optionalQuestionEntity.isEmpty()) {
            questionEntityFromDb = stackRepository.add(new QuestionEntity(id, -1, -1));
        } else {
            questionEntityFromDb = optionalQuestionEntity.get();
        }
        GeneralResponse<AnswerDto> answersByQuestionId =
            stackOverflowClient.getAnswersByQuestionId(questionEntityFromDb.getId());
        GeneralResponse<CommentDto> commentsByQuestionId =
            stackOverflowClient.getCommentsByQuestionId(questionEntityFromDb.getId());
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        int answersLength = answersByQuestionId.getItems().length;
        if (questionEntityFromDb.getAnswerCount() != answersLength) {
            linkUpdates.add(new LinkUpdate("Answer count changed"));
        }
        int commentLength = commentsByQuestionId.getItems().length;
        if (questionEntityFromDb.getCommentCount() != commentLength) {
            linkUpdates.add(new LinkUpdate("Comment count changed"));
        }
        if (!linkUpdates.isEmpty()) {
            List<ChatEntity> chats = chatLinkRepository.findChatsByLinkId(linkEntity.getId());
            Link link;
            try {
                link = new Link(new URI(linkEntity.getName()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            //change last_updated_at
            linkRepository.update(new LinkEntity(linkEntity.getId(), linkEntity.getName(), linkEntity.getCreatedAt(),
                OffsetDateTime.now()
            ));
            //update repo in db
            stackRepository.update(new QuestionEntity(id, answersLength, commentLength));
            //send to all chats update
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                botClient.sendUpdate(update);
            }
        }
    }
}
