package bulletapp.bullet;

import bulletapp.content.ContentType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author David Hall, Isak Schwartz
 */
public class BulletPointTest {

    @Test
    public void equalsTest() {

        BulletPoint bulletPoint = new BulletPoint();
        Content content = new Content(ContentType.TEXT, "Hello, World!");
        Comment comment = new Comment("Adam", "suh dude");
        bulletPoint.addContent(content);
        bulletPoint.addComment(comment);

        BulletPoint bulletPoint1 = new BulletPoint(bulletPoint);

        assertEquals(bulletPoint, bulletPoint1);

    }

    @Test
    public void hashCodeTest() {

        BulletPoint bulletPoint = new BulletPoint();
        Content content = new Content(ContentType.TEXT, "Hello, World!");
        Comment comment = new Comment("Adam", "suh dude");
        bulletPoint.addContent(content);
        bulletPoint.addComment(comment);

        BulletPoint bulletPoint1 = new BulletPoint(bulletPoint);

        assertEquals(bulletPoint.hashCode(), bulletPoint1.hashCode());

    }

    @Test
    public void addContentTest() {

        BulletPoint bulletPoint = new BulletPoint();

        Content content = new Content(ContentType.TEXT, "Hello, World!");

        bulletPoint.addContent(content);

        assertEquals(1, bulletPoint.getNumContents());
        assertEquals(content, bulletPoint.getContents().next());

    }

    @Test
    public void addContentsTest() {

        BulletPoint bulletPoint = new BulletPoint();

        List<Content> contents = new ArrayList<>(Arrays.asList(
                new Content(ContentType.TEXT, "Hello"),
                new Content(ContentType.TEXT, ", "),
                new Content(ContentType.TEXT, "World!")
        ));
        bulletPoint.addContents(contents);

        assertEquals(contents.size(), bulletPoint.getNumContents());

    }

    @Test
    public void removeContentTest() {

        BulletPoint bulletPoint = new BulletPoint();

        Content content = new Content(ContentType.TEXT, "Hello, World!");
        bulletPoint.addContent(content);
        bulletPoint.removeContent(0);

        assertEquals(0, bulletPoint.getNumContents());

    }

    @Test
    public void removeAllContentsTest() {

        BulletPoint bulletPoint = new BulletPoint();

        List<Content> contents = new ArrayList<>(Arrays.asList(
                new Content(ContentType.TEXT, "Hello"),
                new Content(ContentType.TEXT, ", "),
                new Content(ContentType.TEXT, "World!")
        ));
        bulletPoint.addContents(contents);

        bulletPoint.removeAllContents();

        assertEquals(0, bulletPoint.getNumContents());

    }

    @Test
    public void moveContentTest() {

        BulletPoint bulletPoint = new BulletPoint();

        List<Content> contents = new ArrayList<>(Arrays.asList(
                new Content(ContentType.TEXT, "Hello"),
                new Content(ContentType.TEXT, ", "),
                new Content(ContentType.TEXT, "World!")
        ));
        bulletPoint.addContents(contents);

        bulletPoint.moveContent(0, 1);

        assertEquals(", ", bulletPoint.getContent(0).getData());
        assertEquals("Hello", bulletPoint.getContent(1).getData());

    }

}
