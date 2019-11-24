package bulletapp.bullet;

import bulletapp.content.ContentType;
import bulletapp.services.DocumentLoaderFactory;
import bulletapp.services.IDocumentLoader;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import org.junit.Test;

import java.util.AbstractMap;

import static org.junit.Assert.*;

/**
 * @author Carl Holmberg, David Hall
 */

public class BulletDocumentTest {

    // Todo: Add test logic, finish or expand on required classes

    public void searchTest() {
        // Todo: implement toString() in BulletPointNode before this method is implementable
    }

    public void constructFromBulletPoint() {
        // Todo: construct new document from an existing bulletPoint
    }

    public void constructFromListTest() {
        // Todo: construct new document from a list of existing BulletPointNode objects
    }

    @Test
    public void addBulletPointTest() {

        BulletDocument bulletDocument = new BulletDocument();
        String bulletPointId = bulletDocument.addBulletPoint();

        assertEquals("0", bulletPointId);
        assertNotNull(bulletDocument.getBulletPoint(bulletPointId));
        assertNotNull(bulletDocument.getContentData(bulletPointId));

        String subPointId = bulletDocument.addBulletPoint(bulletPointId);

        assertEquals("0;0", subPointId);
        assertNotNull(bulletDocument.getBulletPoint(subPointId));
        assertNotNull(bulletDocument.getContentData(subPointId));

        String subPointId2 = bulletDocument.addBulletPoint(bulletPointId);

        assertEquals("0;1", subPointId2);
        assertNotNull(bulletDocument.getBulletPoint(subPointId2));
        assertNotNull(bulletDocument.getContentData(subPointId2));

    }

    @Test
    public void removeBulletPointTest() {

        BulletDocument bulletDocument = new BulletDocument();
        String bulletPointId = bulletDocument.addBulletPoint();
        String subPointId = bulletDocument.addBulletPoint(bulletPointId);
        String subPointId2 = bulletDocument.addBulletPoint(bulletPointId);

        bulletDocument.removeBulletPoint(subPointId2);
        assertNull(bulletDocument.getBulletPoint(subPointId2));

        bulletDocument.removeBulletPoint(bulletPointId);
        assertNull(bulletDocument.getBulletPoint(bulletPointId));

        bulletDocument.removeBulletPoint(subPointId);
        assertNull(bulletDocument.getBulletPoint(subPointId));

    }

    @Test
    public void bookmarkTest() {

        BulletDocument bulletDocument = new BulletDocument();
        String bulletPointId = bulletDocument.addBulletPoint();
        String subPointId = bulletDocument.addBulletPoint(bulletPointId);

        assertFalse(bulletDocument.isBookmark(subPointId));
        bulletDocument.addBookmark(subPointId);
        assertTrue(bulletDocument.isBookmark(subPointId));

        bulletDocument.removeBookmark(subPointId);
        assertFalse(bulletDocument.isBookmark(subPointId));

    }

    @Test
    public void moveBulletPointTest() {

        BulletDocument bulletDocument = new BulletDocument();
        String bulletPointId = bulletDocument.addBulletPoint();
        String bulletPointId2 = bulletDocument.addBulletPoint();

        bulletDocument.getBulletPoint(bulletPointId).addContent(ContentType.TEXT, "Hello!");

        String subPointId = bulletDocument.addBulletPoint(bulletPointId);

        String subPointId2 = bulletDocument.addBulletPoint(bulletPointId);

        String newId = bulletDocument.moveBulletPoint(subPointId, subPointId2);

        assertEquals("0;0;0", newId);

        assertEquals("0;0;0", bulletDocument.moveBulletPoint("0;0", newId));

        bulletDocument.moveBulletPoint(bulletPointId, bulletPointId2);

        assertEquals("Hello!", bulletDocument.getBulletPoint("0;0").getContent(0).getData());

    }

    @Test
    public void parseTreeConstructorTest() {

        String testFilePath = getClass().getResource("/test_document.xml").getPath();
        IDocumentLoader documentLoader = DocumentLoaderFactory.getFileDocumentParser(testFilePath);

        assertNotNull(documentLoader);

        AbstractMap.SimpleEntry<String, Tree<BulletPointData>> documentTree = documentLoader.loadBulletDocument();

        BulletDocument bulletDocument = new BulletDocument(documentTree.getKey(), documentTree.getValue());

        assertEquals("Cool Document", bulletDocument.getDocumentName());

        assertEquals("THIS. IS. SPARTA!", bulletDocument
                .getBulletPoint("0;0")
                .getContents()
                .next()
                .getData());

    }

}
