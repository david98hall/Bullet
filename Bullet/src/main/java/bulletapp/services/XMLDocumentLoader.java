package bulletapp.services;

import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.dom.DOMDocumentUtils;
import bulletapp.util.dom.DOMNodeUtils;
import bulletapp.util.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author David Hall
 */
public class XMLDocumentLoader implements IDocumentLoader {

    private final Document xmlDocument;

    private final boolean isCompatibleWithBullet;

    private final String xmlFilePath;

    XMLDocumentLoader(String filePath) {
        this.xmlFilePath = filePath;
        xmlDocument = DOMDocumentUtils.getDocument(filePath);
        isCompatibleWithBullet = xmlDocument.getChildNodes().getLength() > 0
                && xmlDocument.getChildNodes().item(0).getNodeName().equalsIgnoreCase("bullet");
    }

    @Override
    public String loadBulletDocumentName() {
        List<Element> documentNodes = DOMNodeUtils.extractAllElements(xmlDocument.getElementsByTagName("document"));
        if (!documentNodes.isEmpty()) {
            return documentNodes.get(0).getAttribute("name");
        }
        return null;
    }

    @Override
    public AbstractMap.SimpleEntry<String, Tree<BulletPointData>> loadBulletDocument() {

        Tree<BulletPointData> dataTree = null;
        List<Element> documentNodes = DOMNodeUtils.extractAllElements(xmlDocument.getElementsByTagName("document"));
        if (!documentNodes.isEmpty()) {
            Element documentNode = documentNodes.get(0);
            dataTree = loadSubBulletPointTree(documentNode);
            return new AbstractMap.SimpleEntry<>(documentNode.getAttribute("name"), dataTree);
        }

        return null;
    }

    private Tree<BulletPointData> loadSubBulletPointTree(Element parentElement) {

        Tree<BulletPointData> subTree = new Tree<>();

        List<Element> childrenElements = DOMNodeUtils.extractAllElements(
                parentElement.getElementsByTagName("children"));

        if (!childrenElements.isEmpty()) {
            for (Element bulletPointElement : DOMNodeUtils.extractAllElements(childrenElements.get(0).getChildNodes())) {

                Tree<BulletPointData> bulletPointTree = loadSubBulletPointTree(bulletPointElement);

                boolean isBookmark = Boolean.valueOf(bulletPointElement
                        .getElementsByTagName("bookmarked")
                        .item(0)
                        .getTextContent());

                bulletPointTree.setData(new BulletPointData(getContents(bulletPointElement), getComments(bulletPointElement), isBookmark));
                subTree.addChild(bulletPointTree);
            }
        }

        return subTree;
    }

    private List<AbstractMap.SimpleEntry<String, String>> getContents(Element bulletPointElement) {

        List<AbstractMap.SimpleEntry<String, String>> contents = new ArrayList<>(1);

        List<Element> contentElements = DOMNodeUtils.extractAllElements(
                bulletPointElement.getElementsByTagName("contents"));

        if (!contentElements.isEmpty()) {

            DOMNodeUtils.extractAllElements(contentElements.get(0)
                    .getElementsByTagName("content"))
                    .forEach(contentElement ->
                            contents.add(getContent(contentElement)));

        }

        return contents;
    }

    private AbstractMap.SimpleEntry<String, String> getContent(Element contentElement) {

        String contentType = contentElement.getElementsByTagName("type").item(0).getTextContent();

        String data = contentElement.getElementsByTagName("data").item(0).getTextContent();

        // If filepath, convert to absolute filepath
        if (data.startsWith("./")) {
            String canonicalFilePath = convertBulletDataFilePathToCanonical(data);
            if (canonicalFilePath != null) {
                data = canonicalFilePath;
            }
        }

        return new AbstractMap.SimpleEntry<>(contentType, data);
    }

    private List<AbstractMap.SimpleEntry<String, String>> getComments(Element bulletPointElement) {

        List<AbstractMap.SimpleEntry<String, String>> comments = new ArrayList<>(1);

        List<Element> commentElements = DOMNodeUtils.extractAllElements(
                bulletPointElement.getElementsByTagName("comments"));

        if (!commentElements.isEmpty()) {

            DOMNodeUtils.extractAllElements(commentElements.get(0)
                    .getElementsByTagName("comment"))
                    .forEach(commentElement ->
                            comments.add(getComment(commentElement)));
        }
        return comments;
    }

    private AbstractMap.SimpleEntry<String, String> getComment(Element commentElement) {
        String commentUsername = commentElement.getElementsByTagName("username").item(0).getTextContent();

        String commentText = commentElement.getElementsByTagName("commentText").item(0).getTextContent();

        return new AbstractMap.SimpleEntry<>(commentUsername, commentText);
    }

    private String convertBulletDataFilePathToCanonical(String dataFilepath) {
        String path = xmlFilePath.substring(0, xmlFilePath.lastIndexOf("\\"));
        File dataFolderFile = new File(path);
        if (dataFolderFile.exists()) {
            try {
                return new File(dataFolderFile, dataFilepath.replace("./", "")).getCanonicalPath();
            } catch (IOException e) {
            }
        }
        return null;
    }

    @Override
    public boolean isDocumentCompatible() {
        return isCompatibleWithBullet;
    }

}
