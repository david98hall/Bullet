package bulletapp.bullet;

import bulletapp.content.ContentType;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

//import bulletapp.util.tuple.Tuple;

/**
 * Creates trees with immutable BulletPointData entries
 *
 * @author Carl Holmberg
 */

public final class BulletPointTreeFactory {

    /**
     * A recursive method for parsing a Tree with BulletPoint to a Tree with BulletPointData
     *
     * @param bulletData A tree or subtree with BulletPoint objects
     * @return A new tree with immutable BulletPointData
     */
    static Tree<BulletPointData> getBulletPointDataTree(Tree<BulletPoint> bulletData) {

        // Create a container tree
        Tree<BulletPointData> subTree = new Tree<>();

        // Get the contained BulletPoint
        BulletPoint bulletPoint = bulletData.getData();

        // Collect the attached data
        List<AbstractMap.SimpleEntry<ContentType, String>> contentData = new ArrayList<>();
        List<AbstractMap.SimpleEntry<String, String>> commentData = new ArrayList<>();

        if (bulletPoint != null) {
            bulletPoint.getContents().forEachRemaining(content ->
                    contentData.add(new AbstractMap.SimpleEntry<>(content.getContentType(), content.getData())));

            bulletPoint.getComments().forEachRemaining(comment ->
                    commentData.add(new AbstractMap.SimpleEntry<>(comment.getUsername(), comment.getCommentText())));
        }

        // Set the bookmark info
        boolean isBookmark = Bullet.getInstance().isBookmark(bulletData.getId());

        // Add any children recursively
        for (Tree<BulletPoint> bulletChild : bulletData) {
            Tree<BulletPointData> child = getBulletPointDataTree(bulletChild);
            subTree.addChild(child);
        }

        // Attach the data
        subTree.setData(new BulletPointData(isBookmark, contentData, commentData));

        return subTree;
    }
}
