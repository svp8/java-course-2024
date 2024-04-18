package edu.java.bot.configuration;

import java.util.Set;

public record RetryConfig(
    boolean enable,
    RetryType type,
    int maxAttempts,
    int baseDelayMs,
    Set<Integer> retryOnCodes
) {
}
