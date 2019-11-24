package bulletapp.services;

//import bulletapp.bullet.BulletPointParentWrapper;

import bulletapp.content.ContentType;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.file.FileUtils;
import bulletapp.util.folder.Folder;
import bulletapp.util.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;

/**
 * Implements the IDocumentWriter interface to archive a bullet point tree along with its data
 * @author Carl Holmberg
 */
class ArchiveWriter implements IDocumentWriter {

    private static final String fileEnding = ".bullet";
    private ISerializer serializer;

    public ArchiveWriter(ISerializer serializer) {
        this.serializer = serializer;
    }


    @Override
    public void save(String name, Tree<BulletPointData> subTree, File outputFile) {
        // Todo: implement methods

        // Get extension
        //String extension = FileUtils.getExtension(outputFile);

        if (outputFile.isDirectory()) return;

        // Create temporary subdir at path
        // Todo: implement folder class
        Folder tempDir = new Folder(outputFile.getParentFile());
        //File tempDir = FileUtils.createSubDirectory(path, fileName);

        // Copy local files to relative paths in subdir
        // Todo: This requires a recursive method that loops through the entire node
        //File dataDir = null;
        Folder dataDir = null;
        try {
            tempDir.addSubDirectory("data");
            dataDir = tempDir.getSubFolderByName("data");
            //dataDir = FileUtils.createSubDirectory(tempDir, "data");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dataDir != null) copyLocalFiles(subTree, dataDir);


        // Serialize
        String serializedData = serializer.serializeTree(name, subTree);

        // Write serialized string to temp dir
        String serializedFilePath = tempDir + "/bullet." + serializer.getExtension();
        File serializedFile = new File(serializedFilePath);
        FileUtils.overwriteFile(serializedFile, serializedData);

        // Create zip from temp dir
        String outputFilePath = outputFile.getAbsolutePath();
        try {
            CompressionUtils.zipDirectory(tempDir.getFolderFile(), outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Change extension
        FileUtils.changeExtension(new File(outputFilePath), "bullet");

        // Remove temp dir
        tempDir.delete();
    }


    private void copyLocalFiles(Tree<BulletPointData> subTree, Folder dataDir) {
        // Note: local files make use of URI:s, beginning with file:///

        BulletPointData data = subTree.getData();
        List<AbstractMap.SimpleEntry<ContentType,String>> simpleContentData = data.getAllContentData();

        for (AbstractMap.SimpleEntry<ContentType, String> dataEntry : simpleContentData) {
            if (!dataEntry.getValue().startsWith("http")) {
                File source = new File(dataEntry.getValue());
                String destination = dataDir.getAbsolutePath() + "/" + source.getName();
                System.out.println(destination);
                while (new File(dataDir.getFolderFile(), source.getName()).exists()) {
                    String extension = FileUtils.getExtension(new File(destination));
                    String fileName = FileUtils.nameWithoutExtension(new File(destination));
                    if (fileName.matches("_\\d\\d\\d$")){
                        int counter = Integer.parseInt(fileName.substring(fileName.length() - 3));
                        fileName = fileName.substring(0, fileName.length() - 3) + ++counter;
                    }
                    destination = dataDir + "/" + fileName + "." + extension;
                }

                try {
                    FileUtils.copyFile(source, new File(destination));
                } catch (IOException e) {
                    // Todo: catch exception
                    e.printStackTrace();
                }

                dataEntry.setValue("file:///" + new File(destination).getName());
            }
        }

        subTree.setData(new BulletPointData(data.isBookmarked(), simpleContentData, data.getCommentData()));

        // Repeat for children:
        for (Tree<BulletPointData> child : subTree) {
            copyLocalFiles(child, dataDir);
        }
    }

}
