package edu.java.client;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientUtils {
    public static String getBaseUrl(String url, String defaultUrl) {
        if (url == null || url.isBlank()) {
            return defaultUrl;
        } else {
            return url;
        }
    }
}
