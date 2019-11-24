package bulletapp.services;

import bulletapp.bullet.Bullet;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;

import java.io.File;
import java.io.IOException;

/**
 * @author Carl Holmberg
 */
public interface IDocumentWriter {
    void save(String name, Tree<BulletPointData> subTree, File outputFile) throws IOException;
}
