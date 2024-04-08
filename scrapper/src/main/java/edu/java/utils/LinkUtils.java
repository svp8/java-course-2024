package edu.java.utils;

import edu.java.dto.github.GithubLink;
import edu.java.dto.stack.StackLink;
import edu.java.exception.InvalidLinkFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class LinkUtils {

    public static final String WRONG_LINK_FORMAT = "Wrong link format";

    public static boolean validate(String link) {
        return link.startsWith("https://github.com") || link.startsWith("https://stackoverflow.com/questions/");
    }

    public static GithubLink parseGithubLink(String link) {
        Pattern pattern = Pattern.compile("^https:\\/\\/github\\.com\\/(.*?)\\/([^\\/]*)");
        Matcher matcher = pattern.matcher(link);
        String user = null;
        String repo = null;
        while (matcher.find()) {
            user = matcher.group(1);
            repo = matcher.group(2);
        }
        if (user == null || repo == null) {
            throw new InvalidLinkFormatException(HttpStatus.BAD_REQUEST.value(), WRONG_LINK_FORMAT);
        }
        return new GithubLink(user, repo);
    }

    public static StackLink parseStackLink(String link) {
        Pattern pattern = Pattern.compile("^https:\\/\\/stackoverflow\\.com\\/questions\\/(\\d*)\\/");
        Matcher matcher = pattern.matcher(link);
        String idString = null;
        while (matcher.find()) {
            idString = matcher.group(1);
        }
        try {
            if (idString == null) {
                throw new InvalidLinkFormatException(HttpStatus.BAD_REQUEST.value(), WRONG_LINK_FORMAT);
            }
            int id = Integer.parseInt(idString);
            return new StackLink(id);
        } catch (NumberFormatException e) {
            throw new InvalidLinkFormatException(HttpStatus.BAD_REQUEST.value(), WRONG_LINK_FORMAT);
        }

    }
}
