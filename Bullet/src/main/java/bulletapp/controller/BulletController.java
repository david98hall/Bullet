package bulletapp.controller;

import bulletapp.bullet.Bullet;
import bulletapp.content.ContentType;
import bulletapp.services.*;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;

/**
 * @author David Hall
 */
public class BulletController {

    @Getter
    private final static BulletController instance = new BulletController();

    @Getter
    @Setter
    private String selectedBulletPointId;

    private BulletController() {
    }

    /**
     * Resets all data in Bullet and adds a new default user.
     */
    public void resetBullet() {
        Bullet.getInstance().reset();
    }

    public void createBulletDocument(String documentName) {
        Bullet.getInstance().createNewDocument(documentName);
    }

    public void loadBulletDocumentFiles() {
        BulletFolderHandler
                .getInstance()
                .getUserDocumentFolders(Bullet.getInstance().getCurrentUserName())
                .forEach(bulletDocumentFolder -> {

                    try {

                        IDocumentLoader documentLoader = DocumentLoaderFactory
                                .getFileDocumentParser(bulletDocumentFolder.getAbsolutePath());

                        String documentName = documentLoader.loadBulletDocumentName();

                        if (documentName != null && !Bullet.getInstance().currentUserHasDocumentNamed(documentName)) {

                            Bullet.getInstance().parseToBulletDocument(
                                    documentLoader.loadBulletDocument().getValue(),
                                    documentName
                            );

                        }

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }


                });
    }

    public void selectBulletDocument(String documentName) {
        Bullet.getInstance().setCurrentDocument(documentName);
    }

    public void removeBulletDocument(String ownerUsername, String documentName) {
        Bullet.getInstance().removeDocument(ownerUsername, documentName);
        BulletFolderHandler.getInstance().deleteDocument(ownerUsername, documentName);
    }

    public void addBulletUser(String username) {
        Bullet.getInstance().addUser(username);
        BulletFolderHandler.getInstance().createUserFolder(username);
    }

    public void removeUser(String username) {
        Bullet.getInstance().removeUser(username);
        BulletFolderHandler.getInstance().deleteUserFolder(username);
    }

    public void loadLocalUsers() {
        BulletFolderHandler.getInstance().getLocalUsernames().forEach(username -> {
            if (!username.equals(Bullet.getInstance().getDefaultUsername())) {
                Bullet.getInstance().addUser(username);
            }
        });
    }

    public void selectBulletUser(String username, boolean select) {

        if (select) {
            Bullet.getInstance().setCurrentUser(username);
        } else if (Bullet.getInstance().getCurrentUserName().equals(username)) {
            Bullet.getInstance().switchToDefaultUser();
        }

    }

    public void addBulletPoint() {
        Bullet.getInstance().addBulletPoint();
    }

    public void addBulletPoint(int index) {
        Bullet.getInstance().addBulletPoint(index);
    }

    public void addBulletPoint(String parentId, int index) {
        Bullet.getInstance().addBulletPoint(parentId, index);
    }

    public void addBulletPoint(String parentId) {
        Bullet.getInstance().addBulletPoint(parentId);
    }

    public void removeBulletPoint(String bulletPointId) {
        Bullet.getInstance().removeBulletPoint(bulletPointId);
    }

    public void addContent(String bulletPointId, String data, ContentType contentType) {
        Bullet.getInstance().addContent(bulletPointId, data, contentType);
    }

    public void addContent(String bulletPointId, int index, String data, ContentType contentType) {
        Bullet.getInstance().addContent(bulletPointId, index, contentType, data);
    }

    public void removeContent(String bulletPointId, int index) {
        Bullet.getInstance().removeContent(bulletPointId, index);
    }

    public void removeAllContent(String bulletPointId) {
        Bullet.getInstance().removeAllContent(bulletPointId);
    }

    public void updateData(String bulletPointId, int index, String data) {
        Bullet.getInstance().updateContent(bulletPointId, index, data);
    }

    public AbstractMap.SimpleEntry<ContentType, String> getContentData(String bulletPointId, int contentId) {
        return Bullet.getInstance().getContentData(bulletPointId, contentId);
    }

    public String getCurrentUsername() {
        return Bullet.getInstance().getCurrentUserName();
    }

    public boolean documentAlreadyExists(String documentName, String username) {
        return Bullet.getInstance().documentAlreadyExists(documentName, username);
    }

    public boolean userAlreadyExists(String username) {
        return Bullet.getInstance().usernameExists(username);
    }

    public void moveContent(String bulletPointId, int index, int newIndex) {
        Bullet.getInstance().moveContent(bulletPointId, index, newIndex);
    }

    public void moveBulletPoint(String bulletPointId, String newParentId, int index) {
        Bullet.getInstance().moveBulletPoint(bulletPointId, newParentId, index);
    }

    public String getCurrentDocumentName() {
        return Bullet.getInstance().getCurrentDocumentName();
    }

    public AbstractMap.SimpleEntry<String, Tree<BulletPointData>> getCurrentDocumentState() {
        return Bullet.getInstance().getCurrentDocumentState();
    }

    public void createBulletPoint(String parentBulletPointId, int index) {
        Bullet.getInstance().addBulletPoint(parentBulletPointId, index);
    }

    public void parseToBulletDocument(Tree<BulletPointData> bulletPointDataTree, String documentName) {
        Bullet.getInstance().parseToBulletDocument(bulletPointDataTree, documentName);
    }

    public void addComment(String commentText, String bulletPointID) {
        Bullet.getInstance().addComment(commentText, bulletPointID);
    }

    public void updateComment(String newText, String bulletPointId, int commentIndex) {
        Bullet.getInstance().updateComment(newText, bulletPointId, commentIndex);
    }

    public void removeComment(String bulletPointID, int commentIndex) {
        Bullet.getInstance().removeComment(bulletPointID, commentIndex);
    }

    public void addBookmark(String bulletPointId) {
        Bullet.getInstance().addBookmark(bulletPointId);
    }

    public void removeBookmark(String bulletPointId) {
        Bullet.getInstance().removeBookmark(bulletPointId);
    }

    public void setBulletPointBookmark(String bulletPointId, boolean bookmark) {
        if (bookmark) {
            addBookmark(bulletPointId);
        } else {
            removeBookmark(bulletPointId);
        }
    }

    /**
     * Gets the currently selected document and outputs it to the corresponding user folder
     */
    public void saveCurrentDocument() {
        saveDocument(Bullet.getInstance().getCurrentDocumentName());
    }

    /**
     * Gets the currently selected document and outputs it to the corresponding user folder
     */
    public void saveDocument(String documentName) {
        String userName  = Bullet.getInstance().getCurrentUserName();
        AbstractMap.SimpleEntry<String, Tree<BulletPointData>> state = Bullet.getInstance().getDocumentState(documentName);
        Tree<BulletPointData> tree = state.getValue();

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
            writer.save(documentName, tree, userFolder);
        } catch (IOException e) {
            // Todo: Implement GUI warning
            e.printStackTrace();
        }
    }


}
