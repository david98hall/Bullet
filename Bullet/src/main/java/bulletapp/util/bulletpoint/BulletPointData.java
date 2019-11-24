package bulletapp.util.bulletpoint;

import bulletapp.content.ContentType;
//import bulletapp.util.tuple.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author David Hall
 */
public class BulletPointData {

    @Getter
    @Setter
    private boolean isBookmarked;

    private final List<AbstractMap.SimpleEntry<ContentType, String>> contentData;

    private final List<AbstractMap.SimpleEntry<String,String>> commentData;

    public BulletPointData() {
        contentData = new ArrayList<>(1);
        commentData = new ArrayList<>(1);
    }

    /**
     * constructor with bookmark, contents and comments parameters, can interpret content data from given strings
     * @param isBookmarked
     * @param contentData
     * @param commentData
     */
    public BulletPointData(List<AbstractMap.SimpleEntry<String, String>> contentData, List<AbstractMap.SimpleEntry<String, String>> commentData, boolean isBookmarked) {
        setBookmarked(isBookmarked);
        this.commentData = copyEntryList(commentData);
        this.contentData = new ArrayList<>(contentData.size());
        contentData.forEach(data -> addContentData(ContentType.valueOf(data.getKey()), data.getValue()));
    }
    /**
     * constructor with bookmark, contents and comments parameters, interprets content data from given contentTypes and strings
     * @param isBookmarked
     * @param contentData
     * @param commentData
     */
    public BulletPointData(boolean isBookmarked, List<AbstractMap.SimpleEntry<ContentType, String>> contentData, List<AbstractMap.SimpleEntry<String,String>> commentData) {
        setBookmarked(isBookmarked);
        this.contentData = copyEntryList(contentData);
        this.commentData = copyEntryList(commentData);
    }


    public void addContentData(ContentType contentType, String data) {
        contentData.add(new AbstractMap.SimpleEntry<>(contentType, data));
    }


    public AbstractMap.SimpleEntry<ContentType, String> removeContentData(int index) {
        return contentData.remove(index);
    }

    public AbstractMap.SimpleEntry<ContentType, String> getContentData(int index) {
        return new AbstractMap.SimpleEntry<>(contentData.get(index));
    }

    public ContentType getContentType(int index) {
        return contentData.get(index).getKey();
    }

    public String getContentString(int index) {
        return contentData.get(index).getValue();
    }

    public List<AbstractMap.SimpleEntry<ContentType, String>> getAllContentData() {
        return copyEntryList(contentData);
    }

    /**
     *  Returns a list of a bulletpoint's comments in order, the keys represent the usernames and the value holds the
     *  comment text
     * @return a list of comments represented as username and text Strings
     */
    public List<AbstractMap.SimpleEntry<String,String>> getCommentData() {
        return copyEntryList(commentData);
    }


    private <K,V> List<AbstractMap.SimpleEntry<K,V>> copyEntryList(List<AbstractMap.SimpleEntry<K,V>> entryList) {
        if (entryList == null) {
            return null;
        }
        List<AbstractMap.SimpleEntry<K,V>> copy = new ArrayList<>(entryList.size());
        entryList.forEach(data -> copy.add(new AbstractMap.SimpleEntry<>(data)));
        return copy;
    }


    public int getNumContents() {
        return contentData.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulletPointData that = (BulletPointData) o;
        return isBookmarked == that.isBookmarked &&
                Objects.equals(contentData, that.contentData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isBookmarked, contentData);
    }

}
