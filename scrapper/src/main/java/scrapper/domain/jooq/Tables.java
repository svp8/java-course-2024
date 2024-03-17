/*
 * This file is generated by jOOQ.
 */

package scrapper.domain.jooq;

import javax.annotation.processing.Generated;
import scrapper.domain.jooq.tables.Answer;
import scrapper.domain.jooq.tables.Branch;
import scrapper.domain.jooq.tables.Chat;
import scrapper.domain.jooq.tables.ChatLink;
import scrapper.domain.jooq.tables.Comment;
import scrapper.domain.jooq.tables.Link;
import scrapper.domain.jooq.tables.PullRequest;

/**
 * Convenience access to all tables in the default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Tables {

    /**
     * The table <code>ANSWER</code>.
     */
    public static final Answer ANSWER = Answer.ANSWER;

    /**
     * The table <code>BRANCH</code>.
     */
    public static final Branch BRANCH = Branch.BRANCH;

    /**
     * The table <code>CHAT</code>.
     */
    public static final Chat CHAT = Chat.CHAT;

    /**
     * The table <code>CHAT_LINK</code>.
     */
    public static final ChatLink CHAT_LINK = ChatLink.CHAT_LINK;

    /**
     * The table <code>COMMENT</code>.
     */
    public static final Comment COMMENT = Comment.COMMENT;

    /**
     * The table <code>LINK</code>.
     */
    public static final Link LINK = Link.LINK;

    /**
     * The table <code>PULL_REQUEST</code>.
     */
    public static final PullRequest PULL_REQUEST = PullRequest.PULL_REQUEST;
}