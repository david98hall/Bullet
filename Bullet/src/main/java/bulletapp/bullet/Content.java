package bulletapp.bullet;

import bulletapp.content.ContentType;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;
import java.util.Objects;

/**
 * @author David Hall
 */
class Content {

    @Getter
    private final ContentType contentType;

    @Getter
    private String data;

    Content(ContentType contentType) {
        this.contentType = contentType;
        setData("");
    }

    Content(ContentType contentType, String data) {
        this(contentType);
        setData(data);
    }

    public void setData(String data) {
        this.data = data;
    }

    Content copy() {
        return new Content(getContentType(), getData());
    }

    AbstractMap.SimpleEntry<ContentType, String> convertToSimpleEntry() {
        return new AbstractMap.SimpleEntry<>(contentType, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return contentType == content.contentType &&
                Objects.equals(data, content.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, data);
    }

}
