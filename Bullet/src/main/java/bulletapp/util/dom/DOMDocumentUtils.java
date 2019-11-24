package bulletapp.util.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Utilities for DOM documents.
 *
 * @author David Hall
 */
public final class DOMDocumentUtils {

    /**
     * Gets a document from the specified file path.
     *
     * @param xmlFilePath A file path to an XML file.
     * @return The document found at the file path.
     */
    public static Document getDocument(String xmlFilePath) {
        return getDocument(new File(xmlFilePath));
    }

    /**
     * Gets a document from the specified file.
     *
     * @param xmlFile An XML file.
     * @return The document in the file.
     */
    public static Document getDocument(File xmlFile) {
        try {
            return getDocument(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a document from the specified input stream.
     *
     * @param xmlInputStream An input stream to an XML file.
     * @return The document found with the input stream.
     */
    public static Document getDocument(InputStream xmlInputStream) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlInputStream);
            document.getDocumentElement().normalize();
            return document;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new document.
     *
     * @return The created document.
     */
    public static Document createDocument() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clones a document.
     *
     * @param document The document to copy.
     * @return The cloned document.
     */
    public static Document cloneDocument(Document document) {
        Document cloneDocument = createDocument();
        if (cloneDocument == null) {
            return null;
        }
        Node rootNode = cloneDocument.importNode(document.getDocumentElement(), true);
        cloneDocument.appendChild(rootNode);
        return cloneDocument;
    }

    /**
     * Saves the document to a file on the specified file path.
     *
     * @param document The document to save.
     * @param filePath The file path with the save location and name of the XMl file.
     * @return true if the save process was successful.
     */
    public static boolean saveDocumentToFile(Document document, String filePath) {
        return saveDocumentToFile(document, new File(filePath));
    }

    /**
     * Saves the document to a file.
     *
     * @param document The document to save.
     * @param file     The file to save the document in.
     * @return true if the save process was successful.
     */
    public static boolean saveDocumentToFile(Document document, File file) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source input = new DOMSource(document);
            Result output = new StreamResult(file);
            transformer.transform(input, output);
            return true;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return false;
    }

}
