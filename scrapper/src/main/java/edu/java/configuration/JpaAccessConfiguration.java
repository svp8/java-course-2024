package edu.java.configuration;

import edu.java.repository.jpa.JpaAnswerRepository;
import edu.java.repository.jpa.JpaBranchRepository;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaCommentRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaPullRepository;
import edu.java.service.jpa.JpaAnswerService;
import edu.java.service.jpa.JpaBranchService;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaCommentService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaPullService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public JpaAnswerService jpaAnswerService(JpaAnswerRepository answerRepository) {
        return new JpaAnswerService(answerRepository);
    }

    @Bean
    public JpaBranchService jpaBranchService(JpaBranchRepository repository) {
        return new JpaBranchService(repository);
    }

    @Bean
    public JpaChatService jpaChatService(JpaChatRepository repository) {
        return new JpaChatService(repository);
    }

    @Bean
    public JpaCommentService jpaCommentService(JpaCommentRepository repository) {
        return new JpaCommentService(repository);
    }

    @Bean
    public JpaLinkService jpaLinkService(
        JpaLinkRepository linkRepository,
        JpaChatRepository jpaChatRepository
    ) {
        return new JpaLinkService(linkRepository, jpaChatRepository);
    }

    @Bean
    public JpaPullService jpaPullService(JpaPullRepository repository) {
        return new JpaPullService(repository);
    }

}
