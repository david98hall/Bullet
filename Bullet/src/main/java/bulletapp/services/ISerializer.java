package bulletapp.services;

//import bulletapp.bullet.BulletPointParentWrapper;

import bulletapp.bullet.Bullet;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;

/**
 * @author Carl Holmberg
 * This is a generalized interface for saving the Bullet model state to a string.
 * Each serializer has a serialization algorithm and an associated file extension.
 */
public interface ISerializer {

    /**
     *
     * @param data A tree containting bullet data to serialize
     * @param name The name of the document
     * @return
     */
    String serializeTree(String name, Tree<BulletPointData> data);
    String getExtension();
}
