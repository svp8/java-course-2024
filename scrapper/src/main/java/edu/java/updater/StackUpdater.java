package edu.java.updater;

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
import edu.java.service.AnswerService;
import edu.java.service.ChatService;
import edu.java.service.CommentService;
import edu.java.service.LinkService;
import edu.java.service.MessageService;
import edu.java.utils.LinkUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StackUpdater implements Updater {
    private final StackOverflowClient stackOverflowClient;
    private final CommentService commentService;
    private final AnswerService answerService;
    private final ChatService chatService;
    private final LinkService linkService;
    private final MessageService messageService;

    public StackUpdater(
        StackOverflowClient stackOverflowClient,
        CommentService commentService, AnswerService answerService,
        ChatService chatService, LinkService linkService,
        MessageService messageService
    ) {
        this.stackOverflowClient = stackOverflowClient;
        this.commentService = commentService;
        this.answerService = answerService;
        this.chatService = chatService;
        this.linkService = linkService;
        this.messageService = messageService;
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
        List<AnswerEntity> answerEntityList = answerService.getAllByLinkId(linkEntity.getId());
        List<CommentEntity> commentEntityList = commentService.getAllByLinkId(linkEntity.getId());

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
                answerService.delete(answerEntity);
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
                commentService.delete(commentEntity);
            }
        }
        List<LinkUpdate> linkUpdates = new ArrayList<>();
        if (!answerDtoList.isEmpty()) {
            answerDtoList.forEach(x -> {
                answerService.add(AnswerEntity.builder().linkId(linkEntity.getId())
                    .creationDate(x.getCreationDate())
                    .id(x.getAnswerId())
                    .build());
                linkUpdates.add(new LinkUpdate("New answer was created at " + x.getCreationDate()));
            });
        }
        if (!commentDtoList.isEmpty()) {
            commentDtoList.forEach(x -> {
                commentService.add(CommentEntity.builder().linkId(linkEntity.getId())
                    .creationDate(x.getCreationDate())
                    .id(x.getCommentId())
                    .build());
                linkUpdates.add(new LinkUpdate("New comment was created at " + x.getCreationDate()));
            });
        }
        if (!linkUpdates.isEmpty()) {
            List<ChatEntity> chats = chatService.findChatsByLinkId(linkEntity.getId());
            Link link = linkService.update(linkEntity);
            //send to all chats update
            for (ChatEntity chat : chats) {
                Update update = new Update(new Chat(chat.getId()), link, linkUpdates);
                messageService.sendUpdate(update);
            }
        }
    }
}
