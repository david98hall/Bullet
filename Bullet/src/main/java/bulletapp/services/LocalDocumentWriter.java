package bulletapp.services;

import bulletapp.content.ContentType;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.file.FileUtils;
import bulletapp.util.folder.Folder;
import bulletapp.util.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Carl Holmberg
 * Implementation of the IDocumentWriter interface for local storage.
 * Can use any serializer class implementing ISerializer.
 */
public class LocalDocumentWriter implements IDocumentWriter {
    private static final ContentType[] localContentTypes = {ContentType.VIDEO, ContentType.IMAGE, ContentType.PDF};
    private ISerializer serializer;

    public LocalDocumentWriter(ISerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Saves a Tree of BulletPointData to a requested output path
     * @param name The name of the document
     * @param subTree The tree to be exported
     * @param outputFile The folder (as a File handle) where the document will be saved
     * @throws IOException In case any files or folders cannot be created
     */
    @Override
    public void save(String name, Tree<BulletPointData> subTree, File outputFile) throws IOException {
        // Interpret the output file as a folder
        File documentFile = new File(outputFile, name);
        if (!documentFile.exists()) documentFile.mkdir();
        Folder localFolder = new Folder(documentFile);

        // Create a data folder
        localFolder.addSubDirectory("data");
        Folder dataFolder = localFolder.getSubFolderByName("data");

        // Check for success
        if (dataFolder == null) {
            throw new IOException("Failed to create data folder");
        }

        // Copy the local data
        copyLocalFilesAndUpdateTree(subTree, dataFolder);

        // Serialize the string
        String serializedString = serializer.serializeTree(name, subTree);

        // Create write the string to a new file
        File serializedOutputFile = localFolder.createNewFileHandle(
                name.replace("\\s+","_") + "." + serializer.getExtension()
        );

        // Write to the new file
        FileUtils.overwriteFile(serializedOutputFile, serializedString);
    }

    private void copyLocalFilesAndUpdateTree(Tree<BulletPointData> subTree, Folder dataFolder) throws IOException {

        // List to hold the replacement data
        List<AbstractMap.SimpleEntry<ContentType, String>> replacementData = new ArrayList<>();

        // Extract the root data
        List<AbstractMap.SimpleEntry<ContentType, String>> data = subTree.getData().getAllContentData();


        // Parse the data
        for (AbstractMap.SimpleEntry<ContentType, String> entry : data) {

            boolean remoteData = isRemoteContent(entry);
            // Check if the content type can be locally stored
            if (isLocalContent(entry) && !inDataFolder(entry, dataFolder)) {

                // Copy the file
                File source = new File(entry.getValue());
                dataFolder.addByCopy(source);

                // Create a new content string
                String newData = "./data/" + source.getName();

                // Replace the value
                entry.setValue(newData);
            }

            replacementData.add(entry);

        }
        subTree.setData(new BulletPointData(subTree.getData().isBookmarked(), replacementData, subTree.getData().getCommentData()));

        // Parse the children
        for (Tree<BulletPointData> child : subTree) {
            copyLocalFilesAndUpdateTree(child, dataFolder);
        }
    }

    /**
     * Checks whether a file has already been copied to the local data dir
     * @param entry
     * @param dataFolder
     * @return The truth value for checking the existence of the file
     */
    private boolean inDataFolder(AbstractMap.SimpleEntry<ContentType, String> entry, Folder dataFolder) {
        String fileName = new File(entry.getValue()).getName();
        return new File(dataFolder.getFolderFile(), fileName).exists();

    }

    /**
     * Checks whether a content entry is stored remotely
     * @param entry A content entry (tuple)
     * @return The truth value for the check
     */
    private boolean isRemoteContent(AbstractMap.SimpleEntry<ContentType, String> entry) {
        return entry.getValue().startsWith("http");
    }

    /**
     * Checks whether a content entry is stored locally
     * @param entry A content entry (tuple)
     * @return The truth value for the check
     */
    private boolean isLocalContent(AbstractMap.SimpleEntry<ContentType, String> entry) {
        boolean referenceType = Arrays.asList(localContentTypes).contains(entry.getKey());
        boolean localStorage = !entry.getValue().startsWith("http");

        return referenceType && localStorage;
    }
}
