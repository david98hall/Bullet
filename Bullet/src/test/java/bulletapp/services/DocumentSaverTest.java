package bulletapp.services;

import bulletapp.bullet.Bullet;
import bulletapp.content.ContentType;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DocumentSaverTest {

    @Test
    @Ignore
    public void xmlExportTest() {
        Bullet model = Bullet.getInstance();

        String documentName = "TestDoc";
        Tree<BulletPointData> tree = new Tree<>();

        List<AbstractMap.SimpleEntry<ContentType, String>> dataList = new ArrayList<>();
        String testContent = "file:///test.jpg";
        ContentType testType = ContentType.IMAGE;
        AbstractMap.SimpleEntry<ContentType, String> entry = new AbstractMap.SimpleEntry<>(testType, testContent);

        dataList.add(entry);

        List<AbstractMap.SimpleEntry<String, String>> commentDataList = new ArrayList<>();
        AbstractMap.SimpleEntry<String, String> commentEntry = new AbstractMap.SimpleEntry<>("Adam", "Hello");
        commentDataList.add(commentEntry);

        BulletPointData dataSet = new BulletPointData(false, dataList, commentDataList);
        tree.setData(dataSet);

        // Serialize
        ISerializer serializer = new XmlSerializer();

        String serializedString = serializer.serializeTree(documentName, tree);

        String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><bullet><document name=\"TestDoc\"><children><node><bookmarked>false</bookmarked><contents><content><type>IMAGE</type><data>file:///test.jpg</data></content></contents><children/></node></children></document></bullet>";

        assertEquals(expectedString, serializedString);
    }

    /*
    @Test
    public void archiveSaveTest() {
        Bullet model = Bullet.getInstance();

        String documentName = "TestDoc";
        Tree<BulletPointData> tree = new Tree<>();

        List<AbstractMap.SimpleEntry<ContentType, String>> dataList = new ArrayList<>();
        String testFilePath = getClass().getResource("/image.jpg").getPath();
        File file = new File(testFilePath);


        boolean exists = file.exists();


        String testContent = testFilePath;
        ContentType testType = ContentType.IMAGE;
        AbstractMap.SimpleEntry<ContentType, String> entry = new AbstractMap.SimpleEntry<>(testType, testContent);

        dataList.add(entry);

        BulletPointData dataSet = new BulletPointData(false, dataList);
        tree.setData(dataSet);

        IDocumentWriter writer = new ArchiveWriter(new XmlSerializer());

        ISerializer serializer = new XmlSerializer();

        String serializedString = serializer.serializeTree(documentName, tree);

        String expectedString = null;
        try {
            expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><bullet><document name=\"TestDoc\"><children><node><bookmarked>false</bookmarked><contents><content><type>IMAGE</type><data>" + file.getCanonicalPath() + "</data></content></contents><children/></node></children></document></bullet>";
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(serializedString);

        assertEquals(expectedString, serializedString);

        writer.save(documentName,tree,"/Users/carl/Programmering/OOPProjekt/newImport/TDA367-Projekt/Bullet/src/test/java/bulletapp/services/testOutput","outputFile");
    }
*/

}