package edu.java.configuration;

import edu.java.mapper.ChatMapper;
import edu.java.mapper.LinkMapper;
import edu.java.repository.jdbc.JdbcAnswerRepository;
import edu.java.repository.jdbc.JdbcBranchRepository;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcCommentRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.repository.jdbc.JdbcPullRepository;
import edu.java.service.jdbc.JdbcAnswerService;
import edu.java.service.jdbc.JdbcBranchService;
import edu.java.service.jdbc.JdbcChatService;
import edu.java.service.jdbc.JdbcCommentService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcPullService;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public JdbcAnswerRepository jdbcAnswerRepository(DataSource dataSource) {
        return new JdbcAnswerRepository(dataSource);
    }

    @Bean
    public JdbcBranchRepository jdbcBranchRepository(DataSource dataSource) {
        return new JdbcBranchRepository(dataSource);
    }

    @Bean
    public JdbcChatLinkRepository jdbcChatLinkRepository(
        DataSource dataSource,
        LinkMapper linkMapper,
        ChatMapper chatMapper
    ) {
        return new JdbcChatLinkRepository(dataSource, linkMapper, chatMapper);
    }

    @Bean
    public JdbcChatRepository jdbcChatRepository(DataSource dataSource) {
        return new JdbcChatRepository(dataSource);
    }

    @Bean
    public JdbcCommentRepository jdbcCommentRepository(DataSource dataSource) {
        return new JdbcCommentRepository(dataSource);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository(
        DataSource dataSource,
        LinkMapper linkMapper,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        return new JdbcLinkRepository(dataSource, linkMapper, chatLinkRepository);
    }

    @Bean
    public JdbcPullRepository jdbcPullRepository(DataSource dataSource) {
        return new JdbcPullRepository(dataSource);
    }

    @Bean
    public JdbcAnswerService jdbcAnswerService(JdbcAnswerRepository answerRepository) {
        return new JdbcAnswerService(answerRepository);
    }

    @Bean
    public JdbcBranchService jdbcBranchService(JdbcBranchRepository repository) {
        return new JdbcBranchService(repository);
    }

    @Bean
    public JdbcChatService jdbcChatService(JdbcChatRepository repository, JdbcChatLinkRepository chatLinkRepository) {
        return new JdbcChatService(repository, chatLinkRepository);
    }

    @Bean
    public JdbcCommentService jdbcCommentService(JdbcCommentRepository repository) {
        return new JdbcCommentService(repository);
    }

    @Bean
    public JdbcLinkService jdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcChatRepository jdbcChatRepository,
        JdbcChatLinkRepository jdbcChatLinkRepository
    ) {
        return new JdbcLinkService(linkRepository, jdbcChatRepository, jdbcChatLinkRepository);
    }

    @Bean
    public JdbcPullService jdbcPullService(JdbcPullRepository repository) {
        return new JdbcPullService(repository);
    }

}
