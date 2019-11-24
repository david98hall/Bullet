package bulletapp.util.tree;

import bulletapp.util.converter.IConverter;
import bulletapp.util.removable.IRemovable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tree can be compared to the data structure called "Rose Tree".
 * A Tree can contain an infinite number of child Trees which in turn work the same as their parent.
 * Each Tree in an instance has a unique ID which can be used to locate child Trees quickly.
 * A Tree also contains data of a given type.
 *
 * @param <T> The Tree's data type
 * @author David Hall
 */
public class Tree<T> implements IRemovable, Iterable<Tree<T>> {

    @Setter
    @Getter
    private T data;

    private final List<Tree<T>> childTrees = new ArrayList<>(1);

    private static final String idSplitRegex = ";";
    private static final String rootId = "root";

    private Tree<T> parent;

    public Tree() {
    }

    /**
     * Sets the data
     *
     * @param data The data to set
     */
    public Tree(T data) {
        setData(data);
    }

    /**
     * Adds a child Tree to this instance
     *
     * @param child The child to add
     */
    public void addChild(Tree<T> child) {
        addChild(child, childTrees.size());
    }

    /**
     * @param child what tree to add
     * @param index where to add
     */
    public void addChild(Tree<T> child, int index) {

        if (child == parent) {
            throw new IllegalArgumentException("It is not possible to add a parent to its child!");
        }

        if (!childTrees.contains(child)) {

            child.requestRemoval();

            int numChildren = getNumChildren();

            childTrees.add(index, child);

            if (getNumChildren() > numChildren) {
                child.parent = this;
            }

        }

    }

    public void addChildBefore(String siblingId, Tree<T> child) {
        Tree<T> siblingTree = getTreeById(siblingId);
        addChild(child, childTrees.indexOf(siblingTree));
    }

    public void addChildAfter(String siblingId, Tree<T> child) {
        Tree<T> siblingTree = getTreeById(siblingId);
        addChild(child, childTrees.indexOf(siblingTree) + 1);
    }

    public void moveChild(int childIndex, int newIndex) {
        moveChild(childTrees.get(childIndex), newIndex);
    }

    public void moveChild(Tree<T> childTree, int newIndex) {

        if (newIndex >= 0 && childTrees.indexOf(childTree) != newIndex) {

            removeChild(childTree);

            addChild(childTree, newIndex);

        }

    }

    /**
     * Removes a child Tree from this instance
     *
     * @param child The child to remove
     */
    public void removeChild(Tree<T> child) {
        if (childTrees.remove(child)) {
            child.parent = null;
        }
    }

    /**
     * Removes a child Tree from this instance
     *
     * @param childId The id of the child to remove
     */
    public void removeChild(String childId) {
        Tree<T> child = getTreeById(childId);
        if (child != null) {
            child.requestRemoval();
        }
    }

    /**
     * Removes the child Tree at the given index and returns it
     *
     * @param index The index where to remove the child Tree
     */
    public void removeChild(int index) {
        removeChild(childTrees.get(index));
    }

    /**
     * Returns the child Tree at the specified index
     *
     * @param index The index of the child Tree to return
     * @return The child Tree at the specified index
     */
    public Tree<T> getChildTree(int index) {
        return childTrees.get(index);
    }

    /**
     * Returns the child Tree with the specified ID
     *
     * @param id The ID of the child Tree to return
     * @return The child Tree with the specified ID
     */
    public Tree<T> getTreeById(String id) {

        if (id.equals(rootId)) {
            return null;
        }

        Tree<T> prevTree = this;
        for (String idPartString : id.split(idSplitRegex)) {

            int idPart = Integer.parseInt(idPartString);

            if (0 <= idPart && idPart < prevTree.childTrees.size()) {

                prevTree = prevTree.childTrees.get(idPart);

            } else {
                return null;
            }

        }

        return prevTree;
    }

    /**
     * Gets the ID of this Tree
     *
     * @return This Tree's ID
     */
    public String getId() {

        if (parent == null)
            return rootId;

        int index = parent.childTrees.indexOf(this);

        if (parent.getId().equals(rootId)) {
            return String.valueOf(index);
        }

        return parent.getId() + idSplitRegex + index;
    }

    /**
     * Returns if this Tree is a leaf
     *
     * @return true if this Tree has no child Trees
     */
    public boolean isLeaf() {
        return childTrees.isEmpty();
    }

    public int getNumChildren() {
        return childTrees.size();
    }

    public int indexOfChild(Tree<T> child) {
        return childTrees.indexOf(child);
    }

    /**
     * Traverses this Tree and returns a List containing all data that is not null.
     *
     * @return A List of all data in this Tree and in all of its children and sub-children.
     */
    public List<T> traverse() {

        List<T> dataList = new ArrayList<>(1);

        if (data != null)
            dataList.add(data);

        childTrees.forEach(childTree -> dataList.addAll(childTree.traverse()));

        return dataList;
    }

    public List<Tree<T>> getAllChildTrees() {
        List<Tree<T>> children = new ArrayList<>(getNumChildren());
        childTrees.forEach(child -> {
            children.add(child);
            children.addAll(child.getAllChildTrees());
        });
        return children;
    }

    /**
     * Converts a tree with one data type to another tree with the exact same structure, but with a different data type.
     *
     * @param dataConverter A recipe for how each data object should be converted
     * @param <C>           The conversion's data type.
     * @return The conversion result.
     */
    public <C> Tree<C> convert(IConverter<T, C> dataConverter) {

        Tree<C> conversion = new Tree<>();

        if (data != null) {
            conversion.setData(dataConverter.convert(data));
        }

        childTrees.forEach(child -> conversion.addChild(child.convert(dataConverter)));

        return conversion;
    }

    @Override
    public void requestRemoval() {
        if (parent != null) {
            parent.removeChild(this);
        }
    }

    @Override
    public Iterator<Tree<T>> iterator() {
        return childTrees.iterator();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getId()).append("\n");
        childTrees.forEach(childTree -> stringBuilder.append(childTree.toString()));

        return stringBuilder.toString();
    }

    public static String getParentId(String id) {

        if (id.equals(rootId)) {
            return null;
        }

        if (id.split(idSplitRegex).length == 1) {
            return rootId;
        }

        return id.substring(0, id.lastIndexOf(idSplitRegex));
    }

    public static int getLastId(String id) {
        return Integer.parseInt(id.substring(id.lastIndexOf(idSplitRegex) + 1));
    }

}
