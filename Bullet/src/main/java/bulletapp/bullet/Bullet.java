package bulletapp.bullet;


import bulletapp.content.ContentType;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import lombok.Getter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Singleton aggregate facade for Bullet.
 */
public final class Bullet {

    private static Bullet instance;

    private final List<User> users;

    private User currentUser;
    private BulletDocument currentDocument;

    @Getter
    private final String defaultUsername = "Default";

    private Bullet() {
        users = new ArrayList<>(1);
        init();
    }

    public static Bullet getInstance() {
        if (instance == null)
            instance = new Bullet();
        return instance;
    }

    private void init() {
        addUser(defaultUsername);
    }

    /**
     * Resets all data in Bullet and adds a new default user.
     */
    public void reset() {
        instance = new Bullet();
    }

    /**
     * Adds content to the bullet point with the given ID in the current document.
     *
     * @param bulletPointId The bullet point to add content to.
     * @param data          The initial content data.
     * @param contentType   The type of content to add
     */
    public void addContent(String bulletPointId, String data, ContentType contentType) {
        currentDocument.addContent(bulletPointId, data, contentType);
    }

    public void addContent(String bulletPointId, int index, ContentType contentType, String data) {
        currentDocument.addContent(bulletPointId, index, contentType, data);
    }

    /**
     * Removes content from a bullet point at the given index.
     *
     * @param bulletPointId The id of the bullet containing the content.
     * @param index         The content's index in the bullet point.
     */
    public void removeContent(String bulletPointId, int index) {
        currentDocument.removeContent(bulletPointId, index);
    }

    /**
     * Removes all content in a bullet point
     *
     * @param bulletPointId The id of the bullet point which content's will be cleared.
     */
    public void removeAllContent(String bulletPointId) {
        currentDocument.removeAllContent(bulletPointId);
    }

    /**
     * Moves a content to another index in a given bullet point.
     *
     * @param bulletPointId The id of the bullet point where the content will be moved within.
     * @param index         The index of the content that will be moved.
     * @param newIndex      The index where the content will be moved to.
     */
    public void moveContent(String bulletPointId, int index, int newIndex) {
        currentDocument.getBulletPoint(bulletPointId).moveContent(index, newIndex);
    }

    /**
     * Updates a content's data at the given content ID and bullet point ID.
     *
     * @param bulletPointId The id of the bullet point containing the relevant content.
     * @param contentId     The id of the content in the bullet point.
     * @param data          The new data to set in the content
     */
    public void updateContent(String bulletPointId, int contentId, String data) {
        currentDocument.updateContent(bulletPointId, contentId, data);
    }

    /**
     * Retrieves content data from the relevant content based on the bullet point and content ID:s
     *
     * @param bulletPointId The id of the bullet point containing the relevant content.
     * @param contentId     The id of the content in the bullet point.
     * @return The found content's data.
     */
    public AbstractMap.SimpleEntry<ContentType, String> getContentData(String bulletPointId, int contentId) {
        Iterator<Content> contentIterator = currentDocument.getBulletPoint(bulletPointId).getContents();
        while (contentId-- > 0)
            contentIterator.next();
        return convertContentToTuple(contentIterator.next());
    }

    /**
     * Retrieves all content data from within a bullet point with a specified id.
     *
     * @param bulletPointId The id of the bullet point, which content data will be returned
     * @return The content data in a List.
     */
    public List<AbstractMap.SimpleEntry<ContentType, String>> getAllContentData(String bulletPointId) {
        List<AbstractMap.SimpleEntry<ContentType, String>> contentData = new ArrayList<>(1);
        currentDocument.getBulletPoint(bulletPointId)
                .getContents()
                .forEachRemaining(content -> contentData.add(convertContentToTuple(content)));
        return contentData;
    }

    private AbstractMap.SimpleEntry<ContentType, String> convertContentToTuple(Content content) {
        return new AbstractMap.SimpleEntry<>(content.getContentType(), content.getData());
    }

    public boolean isCurrentUserDefault() {
        return currentUser.getUsername().equals(defaultUsername);
    }

    public void addUser(String username) {
        if (usernameExists(username)) {
            return;
        }
        User user = new User(username);
        if (currentUser == null) {
            currentUser = user;
        }
        users.add(user);
    }

    public void removeUser(String username) {
        int index = 0;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                break;
            }
            index++;
        }
        if (currentUser == users.get(index)) {
            setCurrentUser(defaultUsername);
        }
        users.remove(index);
    }

    public boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                currentUser = user;
                return;
            }
        }
    }

    public void switchToDefaultUser() {
        setCurrentUser(defaultUsername);
    }

    public String getCurrentUserName() {
        return currentUser.getUsername();
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>(users.size());
        users.forEach(user -> usernames.add(user.getUsername()));
        return usernames;
    }

    public int getNumUsers() {
        return users.size();
    }

    /**
     * Parses the given tree to a bullet document
     *
     * @param tree The Tree to parse
     * @return The index of the newly parsed bullet document in the current user's list of documents
     */
    public void parseToBulletDocument(Tree<BulletPointData> tree, String documentName) {
        if (!currentUserHasDocumentNamed(documentName)) {
            addDocument(new BulletDocument(documentName, tree));
        }
    }

    public boolean currentUserHasDocumentNamed(String documentName) {
        return getCurrentUserDocumentNames().contains(documentName);
    }

    private void addDocument(BulletDocument document) {
        currentUser.addDocument(document);
        currentDocument = document;
    }

    /**
     * Creates a new document for the current user.
     *
     * @param documentName The name of the document to create
     */
    public void createNewDocument(String documentName) {
        addDocument(new BulletDocument(documentName));
    }

    public boolean documentAlreadyExists(String documentName, String username) {
        if (!usernameExists(username)) {
            return false;
        }
        Iterator<BulletDocument> iterator = getUser(username).getDocuments();
            while(iterator.hasNext()) {
            if (documentName.equals(iterator.next().getDocumentName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the current document to the one found at the given index in the current user's document list.
     *
     * @param index The index of the document to make current
     */
    public void setCurrentDocument(int index) {
        currentDocument = currentUser.getDocumentAt(index);
    }

    public void setCurrentDocument(String documentName) {
        BulletDocument tempDocument = currentUser.getDocumentByName(documentName);
        if (tempDocument != null)
            currentDocument = tempDocument;
    }

    public String getCurrentDocumentName() {
        return currentDocument.getDocumentName();
    }

    public int getNumCurrentUserDocuments() {
        return currentUser.getNumDocuments();
    }

    /**
     * Removes the document with the specified name from a user's documents.
     *
     * @param ownerUsername The name of the document's owner.
     * @param documentName  The name of the document to remove.
     */
    public void removeDocument(String ownerUsername, String documentName) {
        currentUser.removeDocument(getUser(ownerUsername).getDocumentByName(documentName));
    }

    private User getUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) return user;
        }
        return null;
    }

    /**
     * Removes the document with the specified name from the current user's documents.
     *
     * @param documentName The name of the document to remove.
     */
    public void removeDocument(String documentName) {
        currentUser.removeDocument(currentUser.getDocumentByName(documentName));
    }

    public List<String> getCurrentUserDocumentNames() {
        List<String> documentNames = new ArrayList<>(currentUser.getNumDocuments());
        currentUser.getDocuments().forEachRemaining(document -> {
            documentNames.add(document.getDocumentName());
        });
        return documentNames;
    }

    /**
     * Retrieves all data of a bullet point with a specified ID.
     *
     * @param bulletPointId The id of the relevant bullet point.
     * @return The retrieved bullet point data.
     */
    public BulletPointData getBulletPointData(String bulletPointId) {
        Iterator<Content> contents = currentDocument.getContentData(bulletPointId);
        List<AbstractMap.SimpleEntry<ContentType, String>> contentData = new ArrayList<>(1);

        while (contents.hasNext()) {
            Content content = contents.next();
            AbstractMap.SimpleEntry<ContentType, String> dataEntry = new AbstractMap.SimpleEntry<>(content.getContentType(), content.getData());
            contentData.add(dataEntry);
        }

        Iterator<Comment> comments = currentDocument.getCommentData(bulletPointId);
        List<AbstractMap.SimpleEntry<String, String>> commentData = new ArrayList<>(1);

        while (comments.hasNext()) {
            Comment comment = comments.next();
            AbstractMap.SimpleEntry<String, String> commentDataEntry = new AbstractMap.SimpleEntry<>(comment.getUsername(), comment.getCommentText());
            commentData.add(commentDataEntry);
        }


        return new BulletPointData(isBookmark(bulletPointId), contentData, commentData);
    }

    /**
     * Adds a bullet point to the one with the specified id.
     *
     * @param parentBulletPointId The id of the parent bullet point where a new bullet point will be added.
     */
    public void addBulletPoint(String parentBulletPointId) {
        currentDocument.addBulletPoint(parentBulletPointId);
    }

    /**
     * Adds a bullet point to the one with the specified id.
     * The bullet point gets added to a specific index in the parent bullet point's children list.
     *
     * @param parentBulletPointId The id of the parent bullet point where a new bullet point will be added.
     * @param index               The index where the new bullet point will be added.
     */
    public void addBulletPoint(String parentBulletPointId, int index) {
        currentDocument.addBulletPoint(parentBulletPointId, index);
    }

    /**
     * Adds a bullet point to the top level of the current document.
     */
    public void addBulletPoint() {
        currentDocument.addBulletPoint();
    }

    /**
     * Adds a bullet point to the top level of the current document at the given index.
     */
    public void addBulletPoint(int index) {
        currentDocument.addBulletPoint(index);
    }

    /**
     * Removes a bullet point
     *
     * @param id The id of the bullet point to remove
     */
    public void removeBulletPoint(String id) {
        currentDocument.removeBookmark(id);
        currentDocument.removeBulletPoint(id);
    }

    public boolean currentDocumentContainsBulletPoint(String bulletPointId) {
        return currentDocument.containsBulletPoint(bulletPointId);
    }

    /**
     * Moves an existing bullet point to a new parent.
     *
     * @param bulletPointId The id of the bullet point to move
     * @param newParentId   The id of the new parent, where the bullet point will be moved.
     */
    public void moveBulletPoint(String bulletPointId, String newParentId) {
        currentDocument.moveBulletPoint(bulletPointId, newParentId);
    }

    /**
     * Moves an existing bullet point to a new parent.
     *
     * @param bulletPointId The id of the bullet point to move
     * @param newParentId   The id of the new parent, where the bullet point will be moved.
     * @param index         The index where the new bullet point will be moved to.
     */
    public void moveBulletPoint(String bulletPointId, String newParentId, int index) {
        currentDocument.moveBulletPoint(bulletPointId, newParentId, index);
    }

    /**
     * Removes a bullet point from the current document.
     *
     * @param bulletPointId The id of the bullet point to remove.
     * @return The data of the removed bullet point.
     */
    public BulletPointData removeBulletPointAndReturnData(String bulletPointId) {
        BulletPointData data = getBulletPointData(bulletPointId);
        currentDocument.removeBulletPoint(bulletPointId);
        return data;
    }

    public boolean bulletPointHasChildren(String bulletPointId) {
        return currentDocument.bulletPointHasChildren(bulletPointId);
    }

    public List<String> getChildBulletPoints(String bulletPointId) {
        return currentDocument.getChildrenId(bulletPointId);
    }

    public List<String> getBookmarkedIds() {
        return currentDocument.getBookmarkedIds();
    }

    /**
     * Adds a bookmark at the given bullet point id to the current document.
     *
     * @param bulletPointId The id of the bullet point to bookmark
     */
    public void addBookmark(String bulletPointId) {
        currentDocument.addBookmark(bulletPointId);
    }

    /**
     * Removes a bookmark at the given bullet point id from the current document.
     *
     * @param bulletPointId The id of the bullet point to stop bookmarking
     */
    public void removeBookmark(String bulletPointId) {
        currentDocument.removeBookmark(bulletPointId);
    }

    /**
     * @param bulletPointId The id of the bullet point to check for a bookmark.
     * @return true, if the bullet point is bookmarked in the current document.
     */
    public boolean isBookmark(String bulletPointId) {
        return currentDocument.isBookmark(bulletPointId);
    }

    public AbstractMap.SimpleEntry<String, Tree<BulletPointData>> getDocumentState(String documentName) {
        return currentUser.getDocumentByName(documentName).getState();
    }

    /**
     * @return A copy of all data related to the current document.
     * The SimpleEntry consists of the name of the document and the bullet point data.
     */
    public AbstractMap.SimpleEntry<String, Tree<BulletPointData>> getCurrentDocumentState() {
        return currentDocument.getState();
    }

    /**
     * Adds a comment with the given data for username and text to the bulletpoint at the given id
     *
     * @param commentText
     * @param bulletPointId
     */
    public void addComment(String commentText, String bulletPointId) {
        currentDocument.addComment(currentUser.getUsername(), commentText, bulletPointId);
    }

    public void updateComment(String newText, String bulletPointId, int commentIndex) {
        currentDocument.updateComment(newText, bulletPointId, commentIndex);
    }

    public void removeComment(String bulletPointID, int commentIndex) {
        currentDocument.removeComment(bulletPointID, commentIndex);
    }


}
