package bulletapp.bullet;

import bulletapp.services.DocumentLoaderFactory;
import bulletapp.services.IDocumentLoader;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
//import bulletapp.util.tuple.Tuple;
import org.junit.Test;

import java.util.AbstractMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Isak Schwartz, David Hall
 */

public class BulletTest {

    @Test
    public void parseToDocumentTest() {

        String testFilePath = getClass().getResource("/test_document.xml").getPath();
        IDocumentLoader documentLoader = DocumentLoaderFactory.getFileDocumentParser(testFilePath);

        assertNotNull(documentLoader);

        AbstractMap.SimpleEntry<String, Tree<BulletPointData>> documentTreeTuple = documentLoader.loadBulletDocument();

        Bullet.getInstance().parseToBulletDocument(documentTreeTuple.getValue(), documentTreeTuple.getKey());
        Bullet.getInstance().setCurrentDocument(documentTreeTuple.getKey());

        assertEquals("Cool Document", Bullet.getInstance().getCurrentDocumentName());

        assertEquals("THIS. IS. SPARTA!", Bullet.getInstance()
                .getBulletPointData("0;0")
                .getContentString(0));

    }

    @Test
    public void createRemoveDocumentTest() {


    }

    @Test
    public void appendRemoveBulletPointTest() {
        // TODO
    }

    @Test
    public void addRemoveBookmarkPointTest() {
        // TODO
    }

    @Test
    public void addCommentTest() {
        Bullet bullet = Bullet.getInstance();
        bullet.createNewDocument("document");
        bullet.setCurrentDocument("document");
        bullet.addBulletPoint();
        bullet.setCurrentUser("Default");
        bullet.addComment( "comment text","0");
        bullet.addComment( "test", "0");

        assertEquals("Default", bullet.getBulletPointData("0").getCommentData().get(0).getKey());
        assertEquals("comment text", bullet.getBulletPointData("0").getCommentData().get(0).getValue());
        assertEquals(2, bullet.getBulletPointData("0").getCommentData().size());
        bullet.removeDocument("document");
    }

    @Test
    public void updateCommentTest() {
        Bullet bullet = Bullet.getInstance();
        bullet.createNewDocument("document");
        bullet.setCurrentDocument("document");
        bullet.addBulletPoint();
        bullet.setCurrentUser("Default");
        bullet.addComment( "comment text","0");
        bullet.updateComment( "edit", "0",0);

        assertEquals("Default", bullet.getBulletPointData("0").getCommentData().get(0).getKey());
        assertEquals("edit", bullet.getBulletPointData("0").getCommentData().get(0).getValue());
        assertEquals(1, bullet.getBulletPointData("0").getCommentData().size());
        bullet.removeDocument("document");
    }

    @Test
    public void removeCommentTest() {
        Bullet bullet = Bullet.getInstance();
        bullet.createNewDocument("document");
        bullet.setCurrentDocument("document");
        bullet.addBulletPoint();
        bullet.setCurrentUser("Default");
        bullet.addComment( "comment text","0");
        bullet.addComment( "test", "0");
        bullet.removeComment( "0", 0);

        assertEquals("Default", bullet.getBulletPointData("0").getCommentData().get(0).getKey());
        assertEquals("test", bullet.getBulletPointData("0").getCommentData().get(0).getValue());
        assertEquals(1, bullet.getBulletPointData("0").getCommentData().size());
        bullet.removeDocument("document");
    }


}
