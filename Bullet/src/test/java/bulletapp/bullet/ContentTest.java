package bulletapp.bullet;

import bulletapp.content.ContentType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author David Hall
 */
public class ContentTest {

    @Test
    public void setGetDataTest() {
        Content content = new Content(ContentType.TEXT, "Hello, World!");
        content.setData("Run Forrest, run!");
        assertEquals("Run Forrest, run!", content.getData());
    }

    @Test
    public void copyTest() {
        Content content = new Content(ContentType.TEXT, "Run Forrest, run!");
        assertEquals("Run Forrest, run!", content.copy().getData());
    }

    @Test
    public void getContentTypeTest() {
        Content content = new Content(ContentType.TEXT, "Run Forrest, run!");
        assertEquals(ContentType.TEXT, content.getContentType());
    }

    @Test
    public void equalsTest() {
        Content content = new Content(ContentType.TEXT, "Run Forrest, run!");
        Content content1 = new Content(ContentType.TEXT, "Run Forrest, run!");
        assertEquals(content, content1);
    }

    @Test
    public void hashCodeTest() {
        Content content = new Content(ContentType.TEXT, "Run Forrest, run!");
        Content content1 = new Content(ContentType.TEXT, "Run Forrest, run!");
        assertEquals(content.hashCode(), content1.hashCode());
    }

}
