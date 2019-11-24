package bulletapp.services;

import bulletapp.content.ContentType;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
//import bulletapp.util.tuple.Tuple;
import org.junit.Test;

import java.util.AbstractMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IDocumentLoaderTest {

    @Test
    public void loadXmlDocumentTest() {

        String testFilePath = getClass().getResource("/test_document.xml").getPath();
        IDocumentLoader documentLoader = DocumentLoaderFactory.getFileDocumentParser(testFilePath);

        assertNotNull(documentLoader);

        AbstractMap.SimpleEntry<String, Tree<BulletPointData>> documentTreeTuple = documentLoader.loadBulletDocument();

        assertNotNull(documentTreeTuple);

        Tree<BulletPointData> documentTree = documentTreeTuple.getValue();

        assertEquals("Cool Document", documentTreeTuple.getKey());

        assertEquals(1, documentTree.getNumChildren());

        assertEquals("Hello, World!", documentTree
                .getTreeById("0")
                .getData()
                .getContentString(0));

        assertEquals("THIS. IS. SPARTA!", documentTree
                .getTreeById("0;0")
                .getData()
                .getContentString(0));

        assertEquals("Totally a real image url", documentTree
                .getTreeById("0;0")
                .getData()
                .getContentString(1));

        assertEquals(ContentType.TEXT, documentTree
                .getTreeById("0")
                .getData()
                .getContentType(0));

        assertEquals(ContentType.TEXT, documentTree
                .getTreeById("0;0")
                .getData()
                .getContentType(0));

        assertEquals(ContentType.IMAGE, documentTree
                .getTreeById("0;0")
                .getData()
                .getContentType(1));

        assertEquals(ContentType.LATEX, documentTree
                .getTreeById("0;0;1")
                .getData()
                .getContentType(0));

    }

}
