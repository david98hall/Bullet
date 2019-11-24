package bulletapp.bullet;

import bulletapp.content.ContentType;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * A document containing a tree of bullet points and bookmarked bullet points.
 *
 * @author Carl Holmberg, David Hall
 */
class BulletDocument {

    private final List<Tree<BulletPoint>> bookmarkedBulletPointTrees = new ArrayList<>(1);

    private final Tree<BulletPoint> bulletPointTree;

    @Getter
    @Setter
    private String documentName = "Untitled Document";

    BulletDocument(Tree<BulletPointData> tree) {
        bulletPointTree = parseToBulletPointTree(tree);
    }

    BulletDocument() {
        this(new Tree<>());
    }

    BulletDocument(String documentName) {
        this();
        setDocumentName(documentName);
    }

    BulletDocument(String documentName, Tree<BulletPointData> tree) {
        this(tree);
        setDocumentName(documentName);
    }

    List<BulletPoint> search(String searchTerm) {
        List<BulletPoint> matches = new ArrayList<>(1);
        // TODO
        return matches;
    }

    /**
     * Adds an ID to bookmarks
     *
     * @param id The ID to add.
     */
    void addBookmark(String id) {
        bookmarkedBulletPointTrees.add(bulletPointTree.getTreeById(id));
    }

    boolean isBookmark(String id) {
        Tree<BulletPoint> bulletPoint = bulletPointTree.getTreeById(id);
        return bulletPoint != null && bookmarkedBulletPointTrees.contains(bulletPoint);
    }

    /**
     * Returns the bookmarked IDs in this document
     *
     * @return The bookmarked IDs.
     */
    List<String> getBookmarkedIds() {
        List<String> ids = new ArrayList<>(bookmarkedBulletPointTrees.size());
        bookmarkedBulletPointTrees.forEach(bookmarkedSubTree -> ids.add(bookmarkedSubTree.getId()));
        return ids;
    }

    /**
     * Removes an ID from bookmarks
     *
     * @param id The ID to remove from the bookmarks
     * @return true, if the ID was removed correctly
     */
    boolean removeBookmark(String id) {
        return bookmarkedBulletPointTrees.remove(bulletPointTree.getTreeById(id));
    }

    /**
     * Adds a bullet point under the given ID.
     *
     * @param parentId The ID of the parent where the bullet point will be added.
     * @return The added bullet point's ID.
     */
    String addBulletPoint(String parentId) {
        Tree<BulletPoint> subTree = new Tree<>(new BulletPoint());
        bulletPointTree.getTreeById(parentId).addChild(subTree);
        return subTree.getId();
    }

    /**
     * Adds a bullet point under the given ID.
     *
     * @param parentId The ID of the parent where the bullet point will be added.
     * @return The added bullet point's ID.
     */
    String addBulletPoint(String parentId, int index) {
        Tree<BulletPoint> subTree = new Tree<>(new BulletPoint());
        bulletPointTree.getTreeById(parentId).addChild(subTree, index);
        return subTree.getId();
    }

    /**
     * Adds a bullet point under at the root of the document.
     *
     * @return The added bullet point's ID.
     */
    String addBulletPoint() {
        Tree<BulletPoint> subTree = new Tree<>(new BulletPoint());
        bulletPointTree.addChild(subTree);
        return subTree.getId();
    }

    /**
     * Adds a bullet point under at the specific index in the root of the document.
     * @param index The index where to add the bullet point.
     * @return The added bullet point's ID.
     */
    String addBulletPoint(int index) {
        Tree<BulletPoint> subTree = new Tree<>(new BulletPoint());
        bulletPointTree.addChild(subTree, index);
        return subTree.getId();
    }

    /**
     * Removes a bullet point at the given ID.
     *
     * @param id The ID of the bullet point to remove
     * @return The removed bullet point's content data.
     */
    Iterator<Content> removeBulletPoint(String id) {
        Tree<BulletPoint> subTree = bulletPointTree.getTreeById(id);
        if (subTree == null) {
            return null;
        }
        bulletPointTree.removeChild(id);
        return subTree.getData().getContents();
    }

    String moveBulletPoint(String id, String newParentId) {
        Tree<BulletPoint> newParentTree = bulletPointTree.getTreeById(newParentId);
        return moveBulletPoint(id, newParentId, newParentTree.getNumChildren());
    }

    String moveBulletPoint(String id, String newParentId, int index) {

        Tree<BulletPoint> tree = bulletPointTree.getTreeById(id);
        Tree<BulletPoint> newParent = bulletPointTree.getTreeById(newParentId);

        // Swap places if trying to add a parent to its child
        Tree<BulletPoint> oldParent = null;
        String parentId = Tree.getParentId(newParentId);
        if (parentId != null && parentId.equals(id)) {

            String oldParentId = Tree.getParentId(id);
            if (oldParentId != null) {
                oldParent = bulletPointTree.getTreeById(oldParentId);
            }

            newParent.requestRemoval();

            if (oldParent != null) {
                oldParent.addChild(newParent);
            }

        }

        newParent.addChild(tree, index);

        return tree.getId();
    }

    /**
     * @param id The ID of the bullet point to check
     * @return The number of children attached to the bullet point
     */
    boolean bulletPointHasChildren(String id) {
        Tree<BulletPoint> subTree = bulletPointTree.getTreeById(id);
        if (subTree == null) {
            return false;
        } else {
            return !bulletPointTree.getTreeById(id).isLeaf();
        }
    }

    BulletPoint getBulletPoint(String id) {
        Tree<BulletPoint> subTree = bulletPointTree.getTreeById(id);
        return subTree == null ? null : subTree.getData();
    }

    Iterator<Content> getContentData(String id) {
        BulletPoint child = bulletPointTree.getTreeById(id).getData();
        return child.getContents();
    }

    Iterator<Comment> getCommentData(String bulletPointId) {
        return bulletPointTree.getTreeById(bulletPointId).getData().getComments();
    }

    boolean containsBulletPoint(String id) {
        return bulletPointTree.getTreeById(id) != null;
    }

    void addContent(String bulletPointId, String data, ContentType contentType) {
        getBulletPoint(bulletPointId).addContent(new Content(contentType, data));
    }

    void addContent(String bulletPointId, int index, ContentType contentType, String data) {
        getBulletPoint(bulletPointId).addContent(new Content(contentType, data), index);
    }

    void removeContent(String bulletPointId, int index) {
        bulletPointTree.getTreeById(bulletPointId).getData().removeContent(index);
    }

    void removeAllContent(String bulletPointId) {
        bulletPointTree.getTreeById(bulletPointId).getData().removeAllContents();
    }

    void updateContent(String bulletPointId, int contentIndex, String data) {
       getBulletPoint(bulletPointId).getContent(contentIndex).setData(data);
    }

    private Tree<BulletPoint> parseToBulletPointTree(Tree<BulletPointData> dataTree) {

        Tree<BulletPoint> bulletPointTree = convertToBulletPointTree(dataTree);

        for (Tree<BulletPointData> subDataTree : dataTree) {

            Tree<BulletPoint> subBulletPointTree = convertToBulletPointTree(subDataTree);

            // Get child BulletPoint Trees
            for (Tree<BulletPointData> subDataTree2 : subDataTree) {
                subBulletPointTree.addChild(parseToBulletPointTree(subDataTree2));
            }

            bulletPointTree.addChild(subBulletPointTree);

        }

        return bulletPointTree;
    }

    private Tree<BulletPoint> convertToBulletPointTree(Tree<BulletPointData> dataTree) {

        Tree<BulletPoint> bulletPointTree = new Tree<>();

        if (dataTree.getData() != null) {

            // Create Bullet Point
            BulletPoint rootBulletPoint = new BulletPoint();
            // add content
            dataTree.getData().getAllContentData().forEach(contentData ->
                    rootBulletPoint.addContent(new Content(contentData.getKey(), contentData.getValue())));

            dataTree.getData().getCommentData().forEach(commentData ->
                    rootBulletPoint.addComment(new Comment(commentData.getKey(), commentData.getValue())));

            bulletPointTree.setData(rootBulletPoint);

            // Bookmark
            if (dataTree.getData().isBookmarked()) {
                bookmarkedBulletPointTrees.add(bulletPointTree);
            }

        }

        return bulletPointTree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BulletDocument that = (BulletDocument) o;
        return Objects.equals(bookmarkedBulletPointTrees, that.bookmarkedBulletPointTrees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bookmarkedBulletPointTrees);
    }

    /**
     * @param id - The ID of the tree/branch to be checked
     * @return - A list containing the absolute ID of all children if present, else null
     */
    List<String> getChildrenId(String id) {
        // TODO Replace List<String> with List<BulletPointId>
        Tree<BulletPoint> subTree = bulletPointTree.getTreeById(id);
        if (!bulletPointHasChildren(id)) {
            return null;
        }

        List<String> childList = new ArrayList<>(1);
        for (int i = 0; i < subTree.getNumChildren(); i++) {
            childList.add(subTree.getChildTree(i).getId());
        }

        return childList;
    }

    public AbstractMap.SimpleEntry<String, Tree<BulletPointData>> getState() {
        return new AbstractMap.SimpleEntry<>(documentName, BulletPointTreeFactory.getBulletPointDataTree(bulletPointTree));
    }

    void addComment(String username, String text, String bulletPointId) {
        bulletPointTree.getTreeById(bulletPointId).getData().addComment(username, text);
    }

    void updateComment(String newText, String bulletPointID, int commentIndex) {
        bulletPointTree.getTreeById(bulletPointID).getData().updateComment(newText, commentIndex);
    }

    void removeComment(String bulletPointID, int commentIndex) {
        bulletPointTree.getTreeById(bulletPointID).getData().removeComment(commentIndex);
    }

}
