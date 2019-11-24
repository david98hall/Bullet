package bulletapp.services;

//import bulletapp.bullet.BulletPointParentWrapper;
import bulletapp.bullet.Bullet;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import org.w3c.dom.Document;

/**
 * @author Carl Holmberg
 */

interface IXmlGenerator {
    //Document generateXmlDoc(Tree<BulletPointData> subTree);
    Document generateXmlDoc(String name, Tree<BulletPointData> subTree);
}
