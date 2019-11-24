package bulletapp.services;

import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.content.ContentType;
import bulletapp.util.dom.DOMDocumentUtils;
import bulletapp.util.tree.Tree;
//import bulletapp.util.tuple.Tuple;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.AbstractMap;
import java.util.List;

/**
 * @author Carl Holmberg
 */

final class XmlGenerator implements IXmlGenerator {


    @Override
    public Document generateXmlDoc(String name, Tree<BulletPointData> subTree) {
        // Generate blank document
        Document doc = DOMDocumentUtils.createDocument();

        if (doc == null) {
            return null;
        }

        // Create and attach the bullet and document elements
        Element bulletRootElement = doc.createElement("bullet");
        Element documentElement = doc.createElement("document");
        documentElement.setAttribute("name", name);
        bulletRootElement.appendChild(documentElement);
        doc.appendChild(bulletRootElement);

        // Create container for children
        Element children = doc.createElement("children");
        documentElement.appendChild(children);
        subTree.forEach(childBulletPoint -> {
            Element childNode = createXmlNode(childBulletPoint, doc);
            children.appendChild(childNode);
        });
        
        return doc;
    }


    private Element createXmlNode(Tree<BulletPointData> subTree, Document doc) {
        // Create the node element
        Element xmlNode = doc.createElement("node");

        // Set reference to data
        BulletPointData contentData = subTree.getData();

        // Add bookmark status
        Element bookmarkNode = doc.createElement("bookmarked");
        bookmarkNode.appendChild(doc.createTextNode(contentData.isBookmarked() ? "true" : "false"));
        xmlNode.appendChild(bookmarkNode);

        // Get the content and comment data as lists
        Element contentsNode = doc.createElement("contents");
        Element commentsNode = doc.createElement("comments");

        List<AbstractMap.SimpleEntry<ContentType, String>> contents = contentData.getAllContentData();
        List<AbstractMap.SimpleEntry<String, String>> comments = contentData.getCommentData();

        contents.forEach(contentEntry -> {
            Element contentElement = doc.createElement("content");

            String interpretedType = contentEntry.getKey().toString();
            Element contentTypeNode = doc.createElement("type");
            contentTypeNode.appendChild(doc.createTextNode(interpretedType));

            String data = contentEntry.getValue();
            Element contentDataNode = doc.createElement("data");
            contentDataNode.appendChild(doc.createTextNode(data));

            contentElement.appendChild(contentTypeNode);
            contentElement.appendChild(contentDataNode);
            contentsNode.appendChild(contentElement);
        });

        comments.forEach(commentEntry -> {
            Element commentElement = doc.createElement("comment");

            String username = commentEntry.getKey().toString();
            Element commentUsernameNode = doc.createElement("username");
            commentUsernameNode.appendChild(doc.createTextNode(username));

            String commentText = commentEntry.getValue();
            Element commentTextNode = doc.createElement("commentText");
            commentTextNode.appendChild(doc.createTextNode(commentText));

            commentElement.appendChild((commentUsernameNode));
            commentElement.appendChild((commentTextNode));
            commentsNode.appendChild(commentElement);
        });

        // Attach the contents and comments nodes
        xmlNode.appendChild(contentsNode);
        xmlNode.appendChild(commentsNode);

        // Create and populate the children node
        Element childrenNode = doc.createElement("children");
        subTree.forEach(childTree -> {
            childrenNode.appendChild(createXmlNode(childTree, doc));
        });

        // Attach the children node
        xmlNode.appendChild(childrenNode);

        // Return the node
        return xmlNode;
    }

}
