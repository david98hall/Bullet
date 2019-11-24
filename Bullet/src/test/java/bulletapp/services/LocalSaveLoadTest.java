package bulletapp.services;

import bulletapp.bullet.Bullet;
import bulletapp.content.ContentType;
import bulletapp.controller.BulletController;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class LocalSaveLoadTest {

    @Test
    @Ignore
    public void xmlSaveTest() {
        Bullet model = Bullet.getInstance();

        String userName = "TestUser";
        String documentName = "TestDoc";
        Tree<BulletPointData> tree = new Tree<>();

        List<AbstractMap.SimpleEntry<ContentType, String>> contentDataList = new ArrayList<>();
        List<AbstractMap.SimpleEntry<String, String>> commentDataList = new ArrayList<>();

        String testFilePath = getClass().getResource("/image.jpg").getPath();
        File file = new File(testFilePath);
        boolean exists = file.exists();

        ContentType testType = ContentType.IMAGE;
        AbstractMap.SimpleEntry<ContentType, String> contentEntry = new AbstractMap.SimpleEntry<>(testType, testFilePath);
        contentDataList.add(contentEntry);

        AbstractMap.SimpleEntry<String, String> commentEntry = new AbstractMap.SimpleEntry<>("Adam", "Hello");
        commentDataList.add(commentEntry);

        BulletPointData dataSet = new BulletPointData(false, contentDataList, commentDataList);
        tree.setData(dataSet);

        // SAVE:

        // Create serializer
        ISerializer xmlSerializer = SerializerFactory.getXmlSerializer();

        // Get a file handle to write to
        BulletFolderHandler localFileSystem = BulletFolderHandler.getInstance();
        localFileSystem.createUserFolder(userName);
        File userFolder = localFileSystem.getUserFolderAsFile(userName);

        // Create a document writer
        IDocumentWriter writer = new LocalDocumentWriter(xmlSerializer);

        // Write
        try {
            writer.save(documentName,tree,userFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check output
        String nonSpacedDocName = documentName.replace("\\s+","_");
        File documentFolder = new File(userFolder,nonSpacedDocName);
        assertTrue(documentFolder.exists());

        File outputFile = new File(documentFolder, nonSpacedDocName + ".xml");
        assertTrue(outputFile.exists());
    }

    @Test
    @Ignore
    public void controllerSaveTest() {
        Bullet model = Bullet.getInstance();
        BulletController controller = BulletController.getInstance();

        String userName = "TestUser";
        model.addUser(userName);
        model.setCurrentUser(userName);

        assertEquals(model.getCurrentUserName(), userName);

        String documentName = "TestDoc";
        Tree<BulletPointData> tree = new Tree<>();

        List<AbstractMap.SimpleEntry<ContentType, String>> dataList = new ArrayList<>();
        String testFilePath = getClass().getResource("/image.jpg").getPath();
        File file = new File(testFilePath);
        assertTrue(file.exists());

        ContentType testType = ContentType.IMAGE;
        AbstractMap.SimpleEntry<ContentType, String> entry = new AbstractMap.SimpleEntry<>(testType, testFilePath);
        dataList.add(entry);

        List<AbstractMap.SimpleEntry<String, String>> commentDataList = new ArrayList<>();
        AbstractMap.SimpleEntry<String, String> commentEntry = new AbstractMap.SimpleEntry<>("Adam", "Hello");
        commentDataList.add(commentEntry);

        BulletPointData dataSet = new BulletPointData(false, dataList, commentDataList);
        tree.setData(dataSet);

        model.parseToBulletDocument(tree, documentName);
        assertEquals(model.getCurrentDocumentName(), documentName);

        controller.saveCurrentDocument();
        File expectedDocOutput = new File(BulletFolderHandler.getInstance().getUserFolderAsFile(userName), documentName);
        File expectedXmlOutput = new File(expectedDocOutput, documentName + ".xml");

        assertTrue(expectedXmlOutput.exists());
    }
}
