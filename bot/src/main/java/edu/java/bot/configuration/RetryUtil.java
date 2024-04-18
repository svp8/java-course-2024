package edu.java.bot.configuration;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@UtilityClass
public class RetryUtil {
    static final BiFunction<Long, Retry.RetrySignal, Throwable> EXCEPTION_GENERATOR = (maxAttempts, rs) ->
        Exceptions.retryExhausted("Retries exhausted: " + (
                rs.totalRetries() + "/" + maxAttempts
            ), rs.failure()
        );

    /**
     * If statusCodes not specified, retry all. Else retry on codes in Set
     *
     * @param statusCodes status codes for retry
     * @return predicate that filters incoming exceptions
     */
    private Predicate<Throwable> getFilterStatusCode(@Nullable Set<Integer> statusCodes) {
        if (statusCodes == null || statusCodes.isEmpty()) {
            return throwable -> true;
        }
        return throwable -> {
            return throwable instanceof WebClientResponseException
                && statusCodes.contains(((WebClientResponseException) throwable).getStatusCode().value());
        };

    }

    public static @NotNull Retry buildConstantRetry(
        long maxAttempts,
        long baseDelayMs,
        Predicate<Throwable> filter
    ) {
        return Retry.fixedDelay(maxAttempts, Duration.ofMillis(baseDelayMs))
            .filter(filter);
    }

    public static @NotNull Retry buildExponentialRetry(
        long maxAttempts,
        long baseDelayMs,
        Predicate<Throwable> filter
    ) {
        return Retry.backoff(maxAttempts, Duration.ofMillis(baseDelayMs))
            .filter(filter);
    }

    public static @NotNull Retry buildLinearRetry(
        long maxAttempts,
        long baseDelayMs,
        @NotNull Predicate<Throwable> filter
    ) {
        final Duration minBackoff = Duration.ofMillis(baseDelayMs);
        return Retry.from(
            t -> Flux.deferContextual(cv ->
                t.contextWrite(cv)
                    .concatMap(retryWhenState -> {
                        Retry.RetrySignal copy = retryWhenState.copy();
                        Throwable currentFailure = copy.failure();
                        long iteration = copy.totalRetries();

                        if (currentFailure == null) {
                            return Mono.error(
                                new IllegalStateException("Retry.RetrySignal#failure() not expected to be null")
                            );
                        }

                        if (!filter.test(currentFailure)) {
                            return Mono.error(currentFailure);
                        }

                        if (iteration >= maxAttempts) {
                            return Mono.error(EXCEPTION_GENERATOR.apply(maxAttempts, copy));
                        }
                        //linear
                        Duration nextBackoff = minBackoff.multipliedBy(iteration);
                        return Mono.delay(nextBackoff, Schedulers.parallel());
                    })
            )
        );
    }

    public static final int DEFAULT_MAX_ATTEMPTS = 100;
    public static final int DEFAULT_BASE_DELAY_MS = 1000;

    public Retry getRetry(RetryConfig retry) {
        if (retry == null || !retry.enable()) {
            return Retry.max(0);
        }
        Predicate<Throwable> filter = getFilterStatusCode(retry.retryOnCodes());
        final int maxAttempts = retry.maxAttempts() == 0 ? DEFAULT_MAX_ATTEMPTS : retry.maxAttempts();
        final int baseDelayMs = retry.baseDelayMs() == 0 ? DEFAULT_BASE_DELAY_MS : retry.baseDelayMs();

        switch (retry.type()) {
            case CONSTANT -> {
                return buildConstantRetry(maxAttempts, baseDelayMs, filter);
            }
            case LINEAR -> {
                return buildLinearRetry(maxAttempts, baseDelayMs, filter);
            }
            case EXPONENTIAL -> {
                return buildExponentialRetry(maxAttempts, baseDelayMs, filter);
            }
            default -> throw new IllegalArgumentException(
                "Invalid retry type, should be constant, linear, exponential"
            );
        }
    }

}

