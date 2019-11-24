package bulletapp.util.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for DOM nodes.
 *
 * @author David Hall
 */
public final class DOMNodeUtils {

    /**
     * Gets the direct child nodes of the specified parent.
     *
     * @param parentNode The parent node.
     * @return The parent node's children.
     */
    public static List<Node> getNodeChildren(Node parentNode) {
        List<Node> children = new ArrayList<>(1);
        children.addAll(convertToListOfNodes(parentNode.getChildNodes()));
        return children;
    }

    /**
     * Gets all child nodes (direct children and children's children, etc) of the specified parent.
     *
     * @param parentNode The parent node.
     * @return All of the parent node's children.
     */
    public static List<Node> getAllNodeChildren(Node parentNode) {
        List<Node> children = getNodeChildren(parentNode);

        List<Node> subChildren = new ArrayList<>(1);
        for (Node child : extractAllElements(parentNode.getChildNodes())) {
            subChildren.addAll(getAllNodeChildren(child));
        }
        children.addAll(subChildren);

        return children;
    }

    /**
     * Appends child nodes to the specified parent.
     *
     * @param parentNode The parent node.
     * @param childNodes The child nodes to append.
     */
    public static void appendChildren(Node parentNode, Node... childNodes) {
        appendChildren(parentNode, Arrays.asList(childNodes));
    }

    /**
     * Appends child nodes to the specified parent.
     *
     * @param parentNode The parent node.
     * @param nodeList   The child nodes to append.
     */
    public static void appendChildren(Node parentNode, NodeList nodeList) {
        appendChildren(parentNode, convertToListOfNodes(nodeList));
    }

    /**
     * Appends child nodes to the specified parent.
     *
     * @param parentNode The parent node.
     * @param childNodes The child nodes to append.
     */
    public static void appendChildren(Node parentNode, List<? extends Node> childNodes) {
        for (Node child : childNodes) {
            parentNode.appendChild(child);
        }
    }

    /**
     * Gets all direct child elements to the parent and returns the ones that have the specified tag name.
     *
     * @param parentNode The parent node.
     * @param tagName    The tag name to look for.
     * @return The child elements with the tag name.
     */
    public static List<Element> getChildElementsByTagName(Node parentNode, String tagName) {
        List<Element> tempElements = extractAllElements(parentNode.getChildNodes());
        List<Element> elements = new ArrayList<>(1);
        tempElements.forEach(element -> {
            if (element.getTagName().equals(tagName))
                elements.add(element);
        });
        return elements;
    }

    /**
     * Gets all child elements (direct children, children's children, etc)
     * to the parent and returns the ones that have the specified tag name.
     *
     * @param parentNode The parent node.
     * @param tagName    The tag name to look for.
     * @return The child elements with the tag name.
     */
    public static List<Element> getAllChildElementsByTagName(Node parentNode, String tagName) {
        List<Element> children = getChildElementsByTagName(parentNode, tagName);

        List<Element> subChildren = new ArrayList<>(1);
        for (Element child : extractAllElements(parentNode.getChildNodes())) {
            subChildren.addAll(getAllChildElementsByTagName(child, tagName));
        }
        children.addAll(subChildren);

        return children;
    }

    /**
     * Gets all child elements (direct children, children's children, etc) with the specified attributes.
     *
     * @param parentNode The parent node to get the child elements from.
     * @param attributes The attributes to look for.
     * @return The child elements with the specified attributes.
     */
    public static List<Element> getAllChildElementsByAttributes(Node parentNode, String... attributes) {
        return getAllChildElementsByAttributes(parentNode, Arrays.asList(attributes));
    }

    /**
     * Gets all child elements (direct children, children's children, etc) with the specified attributes.
     *
     * @param parentNode The parent node to get the child elements from.
     * @param attributes The attributes to look for.
     * @return The child elements with the specified attributes.
     */
    public static List<Element> getAllChildElementsByAttributes(Node parentNode, List<String> attributes) {
        List<Element> attributeChildren = getChildElementsByAttributes(parentNode, attributes);

        List<Element> subChildren = new ArrayList<>(1);
        for (Element child : extractAllElements(parentNode.getChildNodes())) {
            subChildren.addAll(getAllChildElementsByAttributes(child, attributes));
        }
        attributeChildren.addAll(subChildren);

        return attributeChildren;
    }

    /**
     * Gets direct child elements with the specified attributes.
     *
     * @param parentNode The parent node to get the child elements from.
     * @param attributes The attributes to look for.
     * @return The child elements with the specified attributes.
     */
    public static List<Element> getChildElementsByAttributes(Node parentNode, String... attributes) {
        return getChildElementsByAttributes(parentNode, Arrays.asList(attributes));
    }

    /**
     * Gets direct child elements with the specified attributes.
     *
     * @param parentNode The parent node to get the child elements from.
     * @param attributes The attributes to look for.
     * @return The child elements with the specified attributes.
     */
    public static List<Element> getChildElementsByAttributes(Node parentNode, List<String> attributes) {
        List<Element> attributeElements = new ArrayList<>(1);

        extractAllElements(parentNode.getChildNodes()).forEach(element -> {
            if (elementHasAllAttributes(element, attributes))
                attributeElements.add(element);
        });

        return attributeElements;
    }

    /**
     * Gets all child elements (direct children, children's children, etc) with the specified attribute and value.
     *
     * @param parentNode The parent node to get the child elements from.
     * @param attribute  The attributes to look for.
     * @param value      The value the attribute must have.
     * @return The child elements with the specified attribute and value.
     */
    public static List<Element> getAllChildElementsByAttributeAndValue(Node parentNode, String attribute, String value) {
        List<Element> children = getChildElementsByAttributeAndValue(parentNode, attribute, value);

        List<Element> subChildren = new ArrayList<>(1);
        for (Element child : extractAllElements(parentNode.getChildNodes())) {
            subChildren.addAll(getAllChildElementsByAttributeAndValue(child, attribute, value));
        }
        children.addAll(subChildren);

        return children;
    }

    /**
     * Gets direct child elements with the specified attribute and value.
     *
     * @param parentNode The parent node to get the child elements from.
     * @param attribute  The attributes to look for.
     * @param value      The value the attribute must have.
     * @return The child elements with the specified attribute and value.
     */
    public static List<Element> getChildElementsByAttributeAndValue(Node parentNode, String attribute, String value) {
        List<Element> elements = new ArrayList<>(1);

        getChildElementsByAttributes(parentNode, attribute).forEach(element -> {
            if (element.getAttribute(attribute).equals(value))
                elements.add(element);
        });

        return elements;
    }

    /**
     * Checks if the given node is of the specified type.
     *
     * @param node     The node to check.
     * @param nodeType The node type to check for.
     * @return true if the node's node type matches the specified type.
     */
    public static boolean isNodeOfType(Node node, short nodeType) {
        return node != null && node.getNodeType() == nodeType;
    }

    /**
     * Converts a NodeList to a List<Node>.
     *
     * @param nodeList The node list to convert.
     * @return The converted list of nodes.
     */
    public static List<Node> convertToListOfNodes(NodeList nodeList) {
        List<Node> nodes = new ArrayList<>(nodeList.getLength());
        int i = 0;
        while (nodes.size() < nodeList.getLength())
            nodes.add(nodeList.item(i++));
        return nodes;
    }

    /**
     * Extracts all elements in the node list.
     *
     * @param nodeList The node list to extract elements from.
     * @return All elements in the node list.
     */
    public static List<Element> extractAllElements(NodeList nodeList) {
        return extractAllElements(convertToListOfNodes(nodeList));
    }

    /**
     * Extracts all elements in the node list.
     *
     * @param nodes The node list to extract elements from.
     * @return All elements in the node list.
     */
    public static List<Element> extractAllElements(List<Node> nodes) {
        List<Element> elements = new ArrayList<>(1);
        nodes.forEach(node -> {
            if (node.getNodeType() == Node.ELEMENT_NODE)
                elements.add((Element) node);
        });
        return elements;
    }

    /**
     * Checks if the given element has all of the specified attributes.
     *
     * @param element    The element to check.
     * @param attributes The attributes to look for.
     * @return true if the element has all of the attributes.
     */
    public static boolean elementHasAllAttributes(Element element, String... attributes) {
        return elementHasAllAttributes(element, Arrays.asList(attributes));
    }

    /**
     * Checks if the given element has all of the specified attributes.
     *
     * @param element    The element to check.
     * @param attributes The attributes to look for.
     * @return true if the element has all of the attributes.
     */
    public static boolean elementHasAllAttributes(Element element, List<String> attributes) {
        NamedNodeMap attributeNodeMap = element.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            if (attributeNodeMap.getNamedItem(attributes.get(i)) == null)
                return false;
        }
        return true;
    }

    /**
     * Gets direct child elements with the specified tag name and text content.
     *
     * @param parentNode  The parent node to get the child elements from.
     * @param tagName     The tag name to look for.
     * @param textContent The text content the element must have.
     * @return The child elements with the specified tag name and text content.
     */
    public static List<Element> getChildElementsWithTagNameAndTextContent(
            Node parentNode, String tagName, String textContent) {

        List<Element> tempElements = extractAllElements(parentNode.getChildNodes());

        List<Element> elements = new ArrayList<>(1);

        for (Element child : tempElements) {
            if (child.getTagName().equals(tagName) && child.getTextContent().equals(textContent)) {
                elements.add(child);
            }
        }

        return elements;
    }

    /**
     * Gets all child elements (direct children, children's children, etc) with the specified tag name and text content.
     *
     * @param parentNode  The parent node to get the child elements from.
     * @param tagName     The tag name to look for.
     * @param textContent The text content the element must have.
     * @return The child elements with the specified tag name and text content.
     */
    public static List<Element> getAllChildElementsWithTagNameAndTextContent(
            Node parentNode, String tagName, String textContent) {

        List<Element> children = getChildElementsWithTagNameAndTextContent(parentNode, tagName, textContent);

        List<Element> subChildren = new ArrayList<>(1);
        for (Element child : extractAllElements(parentNode.getChildNodes())) {
            subChildren.addAll(getAllChildElementsWithTagNameAndTextContent(child, tagName, textContent));
        }
        children.addAll(subChildren);

        return children;
    }

    /**
     * Gets the parent node of the specified child.
     *
     * @param childNode The child node who's parent will be returned.
     * @return The parent node if there is one and null otherwise.
     */
    public static Node getParentNode(Node childNode) {
        return childNode.getParentNode();
    }

}
