package edu.java.configuration;

import edu.java.mapper.ChatMapper;
import edu.java.mapper.LinkMapper;
import edu.java.repository.jooq.JooqAnswerRepository;
import edu.java.repository.jooq.JooqBranchRepository;
import edu.java.repository.jooq.JooqChatLinkRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqCommentRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.repository.jooq.JooqPullRepository;
import edu.java.service.jooq.JooqAnswerService;
import edu.java.service.jooq.JooqBranchService;
import edu.java.service.jooq.JooqChatService;
import edu.java.service.jooq.JooqCommentService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqPullService;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public JooqAnswerRepository jooqAnswerRepository(DSLContext dsl) {
        return new JooqAnswerRepository(dsl);
    }

    @Bean
    public JooqBranchRepository jooqBranchRepository(DSLContext dsl) {
        return new JooqBranchRepository(dsl);
    }

    @Bean
    public JooqChatLinkRepository jooqChatLinkRepository(
        DSLContext dsl,
        LinkMapper linkMapper,
        ChatMapper chatMapper
    ) {
        return new JooqChatLinkRepository(dsl);
    }

    @Bean
    public JooqChatRepository jooqChatRepository(DSLContext dsl) {
        return new JooqChatRepository(dsl);
    }

    @Bean
    public JooqCommentRepository jooqCommentRepository(DSLContext dsl) {
        return new JooqCommentRepository(dsl);
    }

    @Bean
    public JooqLinkRepository jooqLinkRepository(
        DSLContext dsl,
        JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqLinkRepository(chatLinkRepository, dsl);
    }

    @Bean
    public JooqPullRepository jooqPullRepository(DSLContext dsl) {
        return new JooqPullRepository(dsl);
    }

    @Bean
    public JooqAnswerService jooqAnswerService(JooqAnswerRepository answerRepository) {
        return new JooqAnswerService(answerRepository);
    }

    @Bean
    public JooqBranchService jooqBranchService(JooqBranchRepository repository) {
        return new JooqBranchService(repository);
    }

    @Bean
    public JooqChatService jooqChatService(JooqChatRepository repository, JooqChatLinkRepository chatLinkRepository) {
        return new JooqChatService(repository, chatLinkRepository);
    }

    @Bean
    public JooqCommentService jooqCommentService(JooqCommentRepository repository) {
        return new JooqCommentService(repository);
    }

    @Bean
    public JooqLinkService jooqLinkService(
        JooqLinkRepository linkRepository,
        JooqChatRepository jooqChatRepository,
        JooqChatLinkRepository jooqChatLinkRepository
    ) {
        return new JooqLinkService(linkRepository, jooqChatRepository, jooqChatLinkRepository);
    }

    @Bean
    public JooqPullService jooqPullService(JooqPullRepository repository) {
        return new JooqPullService(repository);
    }

}
