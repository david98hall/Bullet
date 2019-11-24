package bulletapp.util.tree;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author David Hall
 */
public class TreeTest {

    @Test
    public void addChildTest() {

        Tree<String> rootTree = new Tree<>("ROOT");

        assertEquals(0, rootTree.getNumChildren());

        Tree<String> tree = new Tree<>("Child 1");

        assertEquals("root", tree.getId());

        rootTree.addChild(tree);

        assertEquals("0", tree.getId());

        Tree<String> tree1 = new Tree<>("Child 2");
        Tree<String> tree2 = new Tree<>("Child 3");

        rootTree.addChild(tree1, 0);

        assertEquals("1", tree.getId());

        rootTree.addChildAfter(tree1.getId(), tree2);

        assertEquals("2", tree.getId());
        assertEquals("0", tree1.getId());
        assertEquals("1", tree2.getId());

        assertEquals(rootTree.getChildTree(0), tree1);
        assertEquals(rootTree.getChildTree(1), tree2);
        assertEquals(rootTree.getChildTree(2), tree);

        Tree<String> tree3 = new Tree<>("Child 4");
        rootTree.addChildBefore(tree.getId(), tree3);

        assertEquals("3", tree.getId());
        assertEquals("0", tree1.getId());
        assertEquals("1", tree2.getId());
        assertEquals("2", tree3.getId());

        assertEquals(4, rootTree.getNumChildren());

    }

    @Test
    public void removeChildTest() {

        Tree<String> rootTree = new Tree<>("ROOT");

        rootTree.addChild(new Tree<>("Child 1"));

        assertEquals(1, rootTree.getNumChildren());

        rootTree.removeChild(rootTree.getChildTree(0));

        assertEquals(0, rootTree.getNumChildren());

    }

    @Test
    public void moveChildTest() {

        Tree<String> rootTree = new Tree<>();

        Tree<String> tree = new Tree<>();

        rootTree.addChild(tree);

        assertEquals("0", tree.getId());

        Tree<String> tree1 = new Tree<>();

        rootTree.addChild(tree1, 0);

        assertEquals("1", tree.getId());
        assertEquals("0", tree1.getId());

        rootTree.moveChild(1, 0);

        assertEquals(2, rootTree.getNumChildren());

        assertEquals("0", tree.getId());
        assertEquals("1", tree1.getId());

    }

    @Test
    public void getChildByIdTest() {

        Tree<String> rootTree = new Tree<>("ROOT");
        Tree<String> childTree = new Tree<>("Child 0");
        rootTree.addChild(childTree);

        assertEquals(childTree, rootTree.getTreeById("0"));

        Tree<String> subChild0 = new Tree<>("Sub Child 0");
        Tree<String> subChild1 = new Tree<>("Sub Child 1");

        childTree.addChild(subChild0);
        childTree.addChild(subChild1);

        assertEquals(subChild1, rootTree.getTreeById("0;1"));
        assertEquals(subChild1, childTree.getTreeById("1"));

        Tree<String> subSubChild = new Tree<>("Sub Sub Child 0");

        subChild0.addChild(subSubChild);

        assertEquals(subSubChild, rootTree.getTreeById("0;0;0"));
        assertEquals(subSubChild, childTree.getTreeById("0;0"));
        assertEquals(subSubChild, subChild0.getTreeById("0"));

        assertNull(rootTree.getTreeById("1"));

    }

    @Test
    public void getIdTest() {

        Tree<String> rootTree = new Tree<>();
        Tree<String> childTree = new Tree<>();
        rootTree.addChild(childTree);

        assertEquals("root", rootTree.getId());
        assertEquals("0", childTree.getId());

        Tree<String> subChild0 = new Tree<>();
        Tree<String> subChild1 = new Tree<>();

        assertEquals("root", subChild0.getId());
        assertEquals("root", subChild1.getId());

        childTree.addChild(subChild0);
        childTree.addChild(subChild1);

        assertEquals("0;0", subChild0.getId());
        assertEquals("0;1", subChild1.getId());

        Tree<String> subSubChild = new Tree<>();

        subChild1.addChild(subSubChild);

        assertEquals("0;1;0", subSubChild.getId());

    }

    @Test
    public void isLeafTest() {

        Tree<String> rootTree = new Tree<>("ROOT");
        Tree<String> childTree = new Tree<>("Child 0");
        rootTree.addChild(childTree);

        assertFalse(rootTree.isLeaf());
        assertTrue(childTree.isLeaf());

    }

    @Test
    public void requestRemovalTest() {

        Tree<String> rootTree = new Tree<>("ROOT");
        Tree<String> childTree = new Tree<>("Child 0");

        rootTree.addChild(childTree);

        assertEquals(1, rootTree.getNumChildren());

        childTree.requestRemoval();

        assertEquals(0, rootTree.getNumChildren());

    }

    @Test
    public void toStringTest() {

        Tree<String> rootTree = new Tree<>("ROOT");
        Tree<String> childTree = new Tree<>("Child 0");
        rootTree.addChild(childTree);

        Tree<String> subChild0 = new Tree<>("Sub Child 0");
        Tree<String> subChild1 = new Tree<>("Sub Child 1");

        childTree.addChild(subChild0);
        childTree.addChild(subChild1);

        Tree<String> subSubChild = new Tree<>("Sub Sub Child 0");
        Tree<String> subSubChild1 = new Tree<>("Sub Sub Child 0");

        subChild0.addChild(subSubChild);
        subChild1.addChild(subSubChild1);

        assertEquals("root\n0\n0;0\n0;0;0\n0;1\n0;1;0\n", rootTree.toString());

    }

    @Test
    public void getParentIdTest() {
        assertEquals("1;3;6", Tree.getParentId("1;3;6;22"));
        assertEquals("root", Tree.getParentId("0"));
        assertEquals("0", Tree.getParentId("0;1"));
        assertNull(Tree.getParentId("root"));
    }

    @Test
    public void traverseTest() {

        Tree<Integer> rootTree = new Tree<>(1);

        rootTree.addChild(new Tree<>(2));
        rootTree.getTreeById("0").addChild(new Tree<>(3));
        rootTree.getTreeById("0").addChild(new Tree<>(4));

        rootTree.addChild(new Tree<>(5));
        rootTree.getTreeById("1").addChild(new Tree<>(6));
        rootTree.getTreeById("1").addChild(new Tree<>(7));

        rootTree.getTreeById("1;1").addChild(new Tree<>(8));

        StringBuilder stringBuilder = new StringBuilder();
        rootTree.traverse().forEach(stringBuilder::append);

        assertEquals("12345678", stringBuilder.toString());

    }

    @Test
    public void convertTest() {

        Tree<Integer> tree = new Tree<>(1);
        tree.addChild(new Tree<>(2));
        tree.addChild(new Tree<>(3));
        tree.addChild(new Tree<>(4));

        Tree<Integer> subTree = new Tree<>(5);
        subTree.addChild(new Tree<>(51));
        tree.addChild(subTree);

        Tree<String> stringTree = tree.convert(String::valueOf);

        assertEquals(4, stringTree.getNumChildren());

        assertEquals("1", stringTree.getData());
        assertEquals("2", stringTree.getTreeById("0").getData());
        assertEquals("3", stringTree.getTreeById("1").getData());
        assertEquals("4", stringTree.getTreeById("2").getData());
        assertEquals("5", stringTree.getTreeById("3").getData());
        assertEquals("51", stringTree.getTreeById("3;0").getData());

    }

}
