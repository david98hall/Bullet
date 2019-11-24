package bulletapp.services;

import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
//import bulletapp.util.tuple.Tuple;

import java.util.AbstractMap;

/**
 * @author David Hall
 */
public interface IDocumentLoader {

    /**
     * Returns a Tuple containing the document's name and data.
     *
     * @return A Tuple containing the document's name data.
     */
    AbstractMap.SimpleEntry<String, Tree<BulletPointData>> loadBulletDocument();

    String loadBulletDocumentName();

    boolean isDocumentCompatible();

}
