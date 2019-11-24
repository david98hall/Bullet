package bulletapp.bullet;

import bulletapp.content.ContentType;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

/**
 * A bullet point that contains all kinds of contents
 * and since it inherits from BulletPointParent, also other bullet points.
 *
 * @author David Hall
 */
class BulletPoint {

    private final List<Content> contentList = new ArrayList<>(1);

    private List<Comment> comments = new ArrayList<>(1);

    BulletPoint() {
    }

    /**
     * Copies the data of the passed BulletPoint
     *
     * @param bulletPoint The BulletPointNode to copy from
     */
    BulletPoint(BulletPoint bulletPoint) {
        for (int i = 0; i < bulletPoint.contentList.size(); i++) {
            contentList.add(bulletPoint.contentList.get(i).copy());
        }
        for (int i = 0; i < bulletPoint.comments.size(); i++) {
            comments.add(bulletPoint.comments.get(i).copy());
        }
    }

    void addContent(Content content) {
        contentList.add(content);
    }

    void addContent(Content content, int index) {
        contentList.add(index, content);
    }

    void addContent(ContentType contentType, String data) {
        addContent(new Content(contentType, data));
    }

    void addContents(List<? extends Content> contents) {
        this.contentList.addAll(contents);
    }

    void addContents(Content... contents) {
        addContents(Arrays.asList(contents));
    }

    AbstractMap.SimpleEntry<ContentType, String> removeContent(int contentIndex) {
        return contentList.remove(contentIndex).convertToSimpleEntry();
    }

    List<AbstractMap.SimpleEntry<ContentType, String>> removeAllContents() {
        List<AbstractMap.SimpleEntry<ContentType, String>> conversions =
                new ArrayList<>(contentList.size());
        contentList.forEach(content -> conversions.add(content.convertToSimpleEntry()));
        contentList.clear();
        return conversions;
    }

    void moveContent(int contentIndex, int newContentIndex) {
        Content contentToBeMoved = contentList.remove(contentIndex);
        contentList.add(newContentIndex, contentToBeMoved);
    }

    /**
     * Returns an Iterator over this bullet point's contents.
     *
     * @return The Iterator over this bullet point's contents.
     */
    Iterator<Content> getContents() {
        return contentList.iterator();
    }

    Content getContent(int index) {
        return contentList.get(index);
    }

    int getNumContents() {
        return contentList.size();
    }

    /**
     * Add a comment if there already is a comment, it will be added as a sub comment
     * @param text
     */
    void addComment(String username, String text) {
        comments.add(new Comment(username, text));
    }

    void addComment(Comment comment) {
        comments.add(comment);
    }

    void updateComment(String newText, int index) {
        comments.get(index).setCommentText(newText);
    }

    void removeComment(int commentIndex) {
        comments.remove(commentIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulletPoint that = (BulletPoint) o;
        return Objects.equals(contentList, that.contentList) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentList, comments);
    }

    Comment getComment(int index) {
        if (comments.isEmpty() || index >= comments.size()) {
            return null;
        }
        return comments.get(index);
    }

    Iterator<Comment> getComments() {
        return comments.iterator();
    }

}
