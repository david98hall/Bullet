package bulletapp.services;

//import bulletapp.bullet.BulletPointParentWrapper;
import bulletapp.bullet.Bullet;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.tree.Tree;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * @author Carl Holmberg
 */

class XmlSerializer implements ISerializer {

    private static final String extension = "xml";

    XmlSerializer(){}

    @Override
    public String serializeTree(String name, Tree<BulletPointData> data) {
        // Todo: update this to use the tree serializer
        //Document xmlDoc = null;
        Document xmlDoc = new XmlGenerator().generateXmlDoc(name, data);

        // Parse to string
        StringWriter writer = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();

        try {
            Transformer transformer = tf.newTransformer();
            transformer.transform( new DOMSource( xmlDoc ), new StreamResult( writer ) );
        } catch (TransformerException e) {
            // Todo handle exception or throw upward
            e.printStackTrace();
        }

        // Return string
        return writer.getBuffer().toString();

    }

    @Override
    public String getExtension() {
        return extension;
    }
}
