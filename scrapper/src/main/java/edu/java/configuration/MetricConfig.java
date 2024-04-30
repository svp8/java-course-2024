package edu.java.configuration;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy()
public class MetricConfig {
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        CountedAspect countedAspect = new CountedAspect(registry);
        return countedAspect;
    }
}
