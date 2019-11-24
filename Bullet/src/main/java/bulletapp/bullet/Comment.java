package bulletapp.bullet;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;
import java.util.Objects;

/**
 * @author Isak Schwartz
 * This class holds data representing a comment.
 */

public class Comment {

    @Getter(AccessLevel.PACKAGE)
    private String username;
    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private String commentText;

    Comment(String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
    }

    AbstractMap.SimpleEntry<String, String> getCommentData() {
        return new AbstractMap.SimpleEntry<>(username, commentText);
    }

    Comment copy() {
        return new Comment(username, commentText);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return username == comment.username &&
                Objects.equals(commentText, comment.commentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, commentText);
    }
}
