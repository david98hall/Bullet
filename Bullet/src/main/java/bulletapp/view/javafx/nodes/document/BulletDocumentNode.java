package bulletapp.view.javafx.nodes.document;

import bulletapp.bullet.Bullet;
import bulletapp.content.ContentType;
import bulletapp.controller.BulletController;
import bulletapp.util.bulletpoint.BulletPointData;
import bulletapp.util.bulletpoint.BulletPointZoomHandler;
import bulletapp.util.selection.ISelectionListener;
import bulletapp.util.tree.Tree;
import bulletapp.view.interfaces.IBulletPointZoomListener;
import bulletapp.view.javafx.nodes.document.breadcrumbs.BreadcrumbPane;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.BulletPointEventBus;
import bulletapp.view.javafx.utils.FXMLUtils;
import bulletapp.view.javafx.utils.IBulletPointKeyListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author David Hall
 */
public class BulletDocumentNode implements INode, Initializable, IBulletPointZoomListener {

    private final static BulletController bulletController = BulletController.getInstance();

    @FXML
    private HBox breadcrumbs;

    @FXML
    private BreadcrumbPane breadcrumbsController;

    @FXML
    private VBox bulletPointVBox;

    @Getter
    private String documentName;

    private final Tree<BulletPointNode> bulletPointNodeTree;
    private Tree<BulletPointNode> zoomBulletPointNodeTree;

    private final GridPane rootPane = new GridPane();

    private IBulletPointKeyListener keyListener;
    private ISelectionListener<String> zoomListener;

    public BulletDocumentNode() {
        this("Untitled Document");
    }

    /**
     * Parses the Bullet document with the given name and displays it.
     *
     * @param documentName the name of the document
     */
    public BulletDocumentNode(String documentName) {
        this.documentName = documentName;
        initListeners();
        bulletPointNodeTree = parseBulletPointDataTree(Bullet.getInstance().getDocumentState(documentName).getValue());
        loadFXML();
    }

    private void initListeners() {

        keyListener = (bulletPointId, keyCode) -> {

            switch (keyCode) {

                case ENTER:
                    addBulletPointNodeBeneath(bulletPointId);
                    break;

                case BACK_SPACE:
                    removeBulletPointNode(bulletPointId);
                    break;

                case TAB:
                    moveBulletPointToSiblingAbove(bulletPointId);
                    break;

            }

        };

        BulletPointEventBus.getInstance().addKeyListener(keyListener);

        zoomListener = this::onZoomRequest;

        BulletPointZoomHandler.getInstance().addZoomListener(zoomListener);

    }

    public void dispose() {
        BulletPointEventBus.getInstance().removeKeyListener(keyListener);
        BulletPointZoomHandler.getInstance().removeZoomListener(zoomListener);
    }

    private void moveBulletPointToSiblingAbove(String bulletPointId) {

        Tree<BulletPointNode> tree = bulletPointNodeTree.getTreeById(bulletPointId);
        BulletPointNode bulletPointNode = tree.getData();

        String id = tree.getId();
        String parentId = Tree.getParentId(id);

        if (parentId != null) {

            Tree<BulletPointNode> parentTree = parentId.equals("root")
                    ? bulletPointNodeTree
                    : bulletPointNodeTree.getTreeById(parentId);

            int treeIndex = parentTree.indexOfChild(tree);

            if (treeIndex > 0) {

                Tree<BulletPointNode> siblingTree = parentTree.getChildTree(treeIndex - 1);
                bulletController.moveBulletPoint(bulletPointId, siblingTree.getId(), siblingTree.getNumChildren());
                siblingTree.addChild(tree);
                siblingTree.getData().addChild(bulletPointNode);

                // Expand previous sibling, now parent
                expandBulletPointNode(siblingTree);

            }

        }

    }

    private void expandBulletPointNode(Tree<BulletPointNode> bulletPointNodeTree) {
        if (!bulletPointNodeTree.getData().isExpanded() && bulletPointNodeTree.getNumChildren() > 0) {
            bulletPointNodeTree.getData().setExpanded(true);
        }
    }

    private void removeBulletPointNode(String bulletPointId) {

        Tree<BulletPointNode> tree = bulletPointNodeTree.getTreeById(bulletPointId);

        String parentId = Tree.getParentId(bulletPointId);

        BulletPointNode bulletPointNode = tree.getData();

        if (!isZoomed() || !isIdEqualToZoomRootId(bulletPointId)) {

            boolean parentIsRoot = parentId.equals("root");
            boolean inRootWithSiblings = parentIsRoot && bulletPointNodeTree.getNumChildren() > 1;

            boolean hasLessThanTwoContents = bulletPointNode.getNumContents() < 2;

            boolean shouldRemoveFromVBox = hasLessThanTwoContents && inRootWithSiblings;
            boolean shouldRemoveContent = bulletPointNode.getNumContents() > 1;

            if (shouldRemoveContent) {
                int contentToBeRemovedIndex = bulletPointNode.getNumContents() - 2;
                bulletPointNode.removeContentNode(contentToBeRemovedIndex);
                bulletController.removeContent(bulletPointId, contentToBeRemovedIndex);
            } else if (shouldRemoveFromVBox) {
                bulletPointVBox.getChildren().remove(bulletPointNode.getNode());
                bulletPointNodeTree.removeChild(bulletPointId);
                bulletController.removeBulletPoint(bulletPointId);
            } else {
                bulletPointNode.requestRemoval();
                bulletPointNodeTree.removeChild(bulletPointId);
                bulletController.removeBulletPoint(bulletPointId);
            }

        }

    }

    private boolean isIdEqualToZoomRootId(String id) {
        return zoomBulletPointNodeTree.getId().equals(id);
    }

    private void addBulletPointNodeBeneath(String siblingBulletPointId) {

        Tree<BulletPointNode> child = new Tree<>(new BulletPointNode());
        child.getData().addZoomListener(this);
        child.getData().addContent(ContentType.TEXT, "");

        if (isZoomed() && isIdEqualToZoomRootId(siblingBulletPointId)) {

            zoomBulletPointNodeTree.addChild(child, 0);
            bulletController.addBulletPoint(zoomBulletPointNodeTree.getId(), 0);
            bulletController.addContent(child.getId(), "", ContentType.TEXT);
            zoomBulletPointNodeTree.getData().addChild(0, child.getData());

            expandBulletPointNode(zoomBulletPointNodeTree);

        } else {

            Tree<BulletPointNode> parent = siblingBulletPointId.equals("root")
                    ? null
                    : bulletPointNodeTree.getTreeById(Tree.getParentId(siblingBulletPointId));

            if (parent == null) { // Not in child BulletPointNode
                bulletPointNodeTree.addChildAfter(siblingBulletPointId, child);
                int childIndex = bulletPointNodeTree.indexOfChild(child);
                bulletController.addBulletPoint(childIndex);
                bulletController.addContent(child.getId(), "", ContentType.TEXT);
                bulletPointVBox.getChildren().add(childIndex, child.getData().getNode());

            } else { // In a child BulletPointNode
                String lastSiblingId = String.valueOf(Tree.getLastId(siblingBulletPointId));
                parent.addChildAfter(lastSiblingId, child);
                int childIndex = parent.indexOfChild(child);
                bulletController.addBulletPoint(parent.getId(), childIndex);
                bulletController.addContent(child.getId(), "", ContentType.TEXT);
                parent.getData().addChild(childIndex, child.getData());

            }

        }

        child.getData().setId(child::getId);

    }

    private boolean isZoomed() {
        return zoomBulletPointNodeTree != bulletPointNodeTree;
    }

    private void addBulletPointsToTheirParents(Tree<BulletPointNode> bulletPointTree, BulletPointNode parent) {

        bulletPointTree.forEach(childTree -> {

            BulletPointNode childBulletPointNode = childTree.getData();

            if (parent != null) {
                parent.addChild(childBulletPointNode);
            }

            addBulletPointsToTheirParents(childTree, childBulletPointNode);

        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addBulletPointsToTheirParents(bulletPointNodeTree, null);
        showRootOfDocument();
        if (bulletPointNodeTree.isLeaf()) {
            addBulletPointNodeBeneath("root");
        }
    }

    private Tree<BulletPointNode> parseBulletPointDataTree(Tree<BulletPointData> bulletPointDataTree) {

        Tree<BulletPointNode> bulletPointNodeTree = bulletPointDataTree.convert(bulletPointData -> {

            BulletPointNode bulletPointNode = new BulletPointNode();

            bulletPointNode.addZoomListener(this);

            bulletPointData.getAllContentData().forEach(contentData ->
                    bulletPointNode.addContent(contentData.getKey(), contentData.getValue()));

            bulletPointData.getCommentData().forEach(commentData ->
                    bulletPointNode.addCommentNode(commentData.getKey(), commentData.getValue(), false));

            return bulletPointNode;
        });

        // Set BulletPointNode ids
        bulletPointNodeTree.getAllChildTrees().forEach(child -> child.getData().setId(child::getId));

        return bulletPointNodeTree;
    }

    public void moveBulletPoint(int index, int newIndex) {
        zoomBulletPointNodeTree.moveChild(index, newIndex);
        Node node = bulletPointVBox.getChildren().remove(index);
        bulletPointVBox.getChildren().add(newIndex, node);
    }

    private void loadFXML() {
        FXMLUtils.loadFXMLNode("/view/javafx/document/document.fxml", this, rootPane);
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Override
    public Node getNode() {
        return rootPane;
    }

    @Override
    public void onZoomRequest(String bulletPointId) {
        showBulletPointAsRoot(bulletPointId);
    }

    public void showRootOfDocument() {
        showBulletPointAsRoot("root");
    }

    private void showCurrentZoomLevel() {
        if (zoomBulletPointNodeTree.getId().equals("root")) {
            zoomBulletPointNodeTree.forEach(child -> bulletPointVBox
                    .getChildren()
                    .add(child.getData().getNode()));
        } else {
            bulletPointVBox.getChildren().add(zoomBulletPointNodeTree.getData().getNode());
        }
    }

    public void showBulletPointAsRoot(String bulletPointId) {

        bulletPointVBox.getChildren().clear();

        if (bulletPointId.equals("root")) {
            zoomBulletPointNodeTree = bulletPointNodeTree;
            showBreadcrumbs(false);
        } else {
            if (isZoomed()) {
                Tree<BulletPointNode> oldParent =
                        bulletPointNodeTree.getTreeById(Tree.getParentId(zoomBulletPointNodeTree.getId()));
                if (oldParent != null) {
                    oldParent.getData().restoreChildren();
                }
            }
            zoomBulletPointNodeTree = bulletPointNodeTree.getTreeById(bulletPointId);
            updateBreadcrumbs();
            if (!isShowingBreadcrumbs())
                showBreadcrumbs(true);
            expandBulletPointNode(zoomBulletPointNodeTree);
        }

        showCurrentZoomLevel();
    }

    private void updateBreadcrumbs() {
        breadcrumbsController.clear();
        breadcrumbsController.addBreadcrumb("root", "root");

        getParentIds(zoomBulletPointNodeTree).forEach(parentId -> {
            String firstContentData = Bullet.getInstance().getContentData(parentId, 0).getValue();
            if (firstContentData == null) firstContentData = "";
            breadcrumbsController.addBreadcrumb(parentId, firstContentData);
        });

    }

    private List<String> getParentIds(Tree<BulletPointNode> tree) {
        List<String> parentIds = new ArrayList<>(1);
        Tree<BulletPointNode> parentTree = bulletPointNodeTree.getTreeById(Tree.getParentId(tree.getId()));
        if (parentTree != null) {
            parentIds.add(parentTree.getId());
            parentIds.addAll(0, getParentIds(parentTree));
        }
        return parentIds;
    }

    private boolean isShowingBreadcrumbs() {
        return rootPane.getChildren().contains(breadcrumbs);
    }

    private void showBreadcrumbs(boolean show) {
        if (show) {
            rootPane.add(breadcrumbs, 0, 0);
        } else {
            rootPane.getChildren().remove(breadcrumbs);
        }
    }

}
