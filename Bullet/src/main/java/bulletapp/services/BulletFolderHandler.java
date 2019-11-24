package bulletapp.services;

import bulletapp.util.file.FileUtils;
import bulletapp.util.folder.Folder;
import bulletapp.util.folder.FolderUtils;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author David Hall, Carl Holmberg
 */
public final class BulletFolderHandler {

    @Getter
    private final static BulletFolderHandler instance = new BulletFolderHandler();

    private final Folder rootBulletFolder;

    private final Folder usersFolder;
    private final Map<String, Folder> userFolders;

    private BulletFolderHandler() {

        rootBulletFolder = FolderUtils.createFolderInUserHome(".bullet");
        usersFolder = FolderUtils.createFolder(rootBulletFolder, "users");

        userFolders = new HashMap<>(1);

    }

    public void createUserFolder(String username) {
        userFolders.put(username, FolderUtils.createFolder(usersFolder, username));
    }

    public void deleteUserFolder(String username) {
        userFolders.get(username).delete();
        userFolders.remove(username);
    }

    public void deleteDocument(String username, String documentName) {
        new Folder(getBulletDocumentFolderFile(username, documentName)).delete();
    }

    public List<Folder> getUserDocumentFolders(String userName) {
        List<Folder> bulletFolders = new ArrayList<>(1);
        userFolders.get(userName).getLocalSubFolders();
        userFolders.get(userName).getLocalSubFolders().forEach(dir -> {
            for (File file : dir.getLocalFiles()) {
                if (FileUtils.fileMatchesOneExtension(file, ".xml")) {
                    bulletFolders.add(dir);
                }
            }
        });

        return bulletFolders;
    }

    public File getBulletDocumentFolderFile(String ownerUsername, String documentName) {

        for (Folder documentFolder : userFolders.get(ownerUsername).getLocalSubFolders()) {

            if (documentFolder.getName().equals(documentName)) {
                return documentFolder.getFolderFile();
            }

        }

        return null;
    }

    public List<String> getLocalUsernames() {
        List<String> usernames = new ArrayList<>(1);
        usersFolder.getLocalSubFolders().forEach(folderFile -> usernames.add(folderFile.getName()));
        return usernames;
    }

    public void copyFileToUser(String username, File file) throws IOException {
        Folder userFolder = userFolders.get(username);
        if (userFolder != null) {
            userFolder.addByCopy(file);
        }
    }

    public void moveFileToUser(String username, File file) throws IOException {
        Folder userFolder = userFolders.get(username);
        if (userFolder != null) {
            userFolder.addByMove(file);
        }

    }

    public File getNewFileHandleForUser(String userName, String fileName) {
        Folder userFolder = userFolders.get(userName);
        File newFile = null;
        if (userFolder != null) {
            newFile = userFolder.createNewFileHandle(fileName);
        }

        return newFile;
    }

    public File getUserFolderAsFile(String userName) {
        Folder userFolder = userFolders.get(userName);
        return userFolder != null ? userFolder.getFolderFile() : null;
    }

}
