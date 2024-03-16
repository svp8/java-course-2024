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
import edu.java.entity.AnswerEntity;
import edu.java.entity.ChatEntity;
import edu.java.entity.CommentEntity;
import edu.java.entity.LinkEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.stack.AnswerRepository;
import edu.java.repository.stack.CommentRepository;
import edu.java.utils.LinkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StackUpdater implements Updater {
    private final StackOverflowClient stackOverflowClient;
    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final BotClient botClient;

    public StackUpdater(
        StackOverflowClient stackOverflowClient,
        CommentRepository commentRepository,
        AnswerRepository answerRepository,
        ChatLinkRepository chatLinkRepository, LinkRepository linkRepository,
        BotClient botClient
    ) {
        this.stackOverflowClient = stackOverflowClient;
        this.commentRepository = commentRepository;
        this.answerRepository = answerRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.linkRepository = linkRepository;
        this.botClient = botClient;
    }

    @Override
    @Transactional
    public void update(LinkEntity linkEntity) {
        int id = LinkUtils.parseStackLink(linkEntity.getName()).id();

        //get from api
        GeneralResponse<AnswerDto> answersByQuestionId =
            stackOverflowClient.getAnswersByQuestionId(id);
        List<AnswerDto> answerDtoList = Arrays.asList(answersByQuestionId.getItems());
        GeneralResponse<CommentDto> commentsByQuestionId =
            stackOverflowClient.getCommentsByQuestionId(id);
        List<CommentDto> commentDtoList = Arrays.asList(commentsByQuestionId.getItems());
        //get from db
        List<AnswerEntity> answerEntityList = answerRepository.getAllByLinkId(linkEntity.getId());
        List<CommentEntity> commentEntityList = commentRepository.getAllByLinkId(linkEntity.getId());

        if (answerEntityList != null) {
            //delete from dtoList objects that already persisted
            List<AnswerEntity> finalAnswerEntityList = answerEntityList;
            List<AnswerDto> finalAnswerDtoList = answerDtoList;
            answerDtoList = answerDtoList.stream()
                .filter(x -> finalAnswerEntityList.stream().noneMatch(entity -> x.getAnswerId() == entity.getId()))
                .toList();
            //delete from entityList objects that are not in dtoList (they are deleted)
            answerEntityList = answerEntityList.stream()
                .filter(x -> finalAnswerDtoList.stream().noneMatch(dto -> dto.getAnswerId() == x.getId())).toList();

            for (AnswerEntity answerEntity : answerEntityList) {
                answerRepository.delete(answerEntity);
            }

        }

        if (commentEntityList != null) {
            //delete from dtoList objects that already persisted
            List<CommentEntity> finalCommentEntityList = commentEntityList;
            List<CommentDto> finalCommentDtoList = commentDtoList;
            commentDtoList = commentDtoList.stream()
                .filter(x -> finalCommentEntityList.stream().noneMatch(entity -> x.getCommentId() == entity.getId()))
                .toList();
            //delete from entityList objects that are not in dtoList (they are deleted)

            commentEntityList = commentEntityList.stream()
                .filter(x -> finalCommentDtoList.stream().noneMatch(dto -> dto.getCommentId() == x.getId())).toList();

            for (CommentEntity commentEntity : commentEntityList) {
                commentRepository.delete(commentEntity);
            }
        }
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        if (answerDtoList.size() > 0) {
            answerDtoList.forEach(x -> {
                answerRepository.add(AnswerEntity.builder().linkId(linkEntity.getId())
                        .creationDate(x.getCreationDate())
                        .id(x.getAnswerId())
                    .build());
                linkUpdates.add(new LinkUpdate("New answer was created at "+x.getCreationDate() ));
            });
        }
        if (commentDtoList.size() > 0) {
            commentDtoList.forEach(x -> {
                commentRepository.add(CommentEntity.builder().linkId(linkEntity.getId())
                    .creationDate(x.getCreationDate())
                    .id(x.getCommentId())
                    .build());
                linkUpdates.add(new LinkUpdate("New comment was created at "+x.getCreationDate() ));
            });
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
            //send to all chats update
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                botClient.sendUpdate(update);
            }
        }
    }
}
