package bulletapp.view.javafx.nodes.document;

import bulletapp.bullet.Bullet;
import bulletapp.content.ContentType;
import bulletapp.controller.BulletController;
import bulletapp.util.Id;
import bulletapp.util.removable.IRemovable;
import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.IBulletPointZoomListener;
import bulletapp.view.javafx.nodes.comment.CommentModalView;
import bulletapp.view.javafx.nodes.comment.CommentNode;
import bulletapp.view.javafx.nodes.content.ContentNode;
import bulletapp.view.javafx.nodes.content.ContentNodeFactory;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import bulletapp.view.syntax.SyntaxType;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author David Hall, Carl Holmberg
 */
class BulletPointNode implements INode, IRemovable, Initializable {

    @FXML
    private HBox contentsHBox;

    @FXML
    private VBox childrenVBox;

    @FXML
    private Button foldingButton;

    @FXML
    private ImageView foldingIcon;

    @FXML
    private Button zoomButton;

    @FXML
    private Button bookmarkButton;

    @FXML
    private ImageView bookmarkImageView;

    @FXML
    private Button commentButton;

    @FXML
    private Tooltip commentToolTip;

    @FXML
    private ImageView zoomImageView;

    @FXML
    private MenuButton addContentsButton;

    private final static Image emptyPointIcon = new Image("/icons/bulletpoint/icon_bullet_point_empty.png");
    private final static Image filledPointIcon = new Image("/icons/bulletpoint/icon_bullet_point_filled.png");

    private final static Image bookmarkIcon = new Image("/icons/bookmark/bookmark_icon.png");
    private final static Image removeBookmarkIcon = new Image("/icons/bookmark/bookmark_remove_icon.png");

    private final GridPane rootPane = new GridPane();

    private Id<String> id;

    private final List<BulletPointNode> childBulletPoints = new ArrayList<>(1);
    private final List<ContentNode> contentNodes = new ArrayList<>(1);
    private final List<CommentNode> commentNodes = new ArrayList<>(1);

    private final List<IRemoveRequestListener<BulletPointNode>> removeRequestListeners = new ArrayList<>(1);
    private final List<IBulletPointZoomListener> zoomListeners = new ArrayList<>(1);

    private final CommentModalView commentModalView = CommentModalView.getInstance();
    private final static BulletController bulletController = BulletController.getInstance();

    private boolean isAddContentButtonHoveredOn;

    BulletPointNode() {
        FXMLUtils.loadFXMLNode("/view/javafx/document/bulletpoint.fxml", this, rootPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showFoldingButton(false);
        setExpanded(false);
        showActionButtons(false);
        showContentMenu(false);
        initContentAddButton();
    }

    private void initContentAddButton() {
        List<MenuItem> menuEntries = new ArrayList<>();
        for (ContentType contentType : ContentType.values()) {
            MenuItem entry = createMenuItem(contentType);
            if (entry != null) {
                menuEntries.add(entry);
            }
        }
        menuEntries.sort(Comparator.comparing(MenuItem::getText));
        addContentsButton.getItems().clear();
        addContentsButton.getItems().addAll(menuEntries);
    }

    private void showFoldingButton(boolean show) {
        foldingButton.setDisable(!show);
        foldingButton.setVisible(show);
    }

    private void showActionButtons(boolean show) {
        boolean isBookmark = isBookmark();
        bookmarkButton.setDisable(!show && !isBookmark);
        bookmarkButton.setVisible(show || isBookmark);
        commentButton.setDisable(!show);
        commentButton.setVisible(show);
    }

    private void showContentMenu(boolean show) {
        addContentsButton.setDisable(!show);
        addContentsButton.setVisible(show);
    }

    private boolean isBookmark() {
        return id != null && Bullet.getInstance().isBookmark(getId());
    }

    void addZoomListener(IBulletPointZoomListener zoomListener) {
        zoomListeners.add(zoomListener);
    }

    void removeZoomListener(IBulletPointZoomListener zoomListener) {
        zoomListeners.remove(zoomListener);
    }

    void addRemoveRequestListener(IRemoveRequestListener<BulletPointNode> removeRequestListener) {
        removeRequestListeners.add(removeRequestListener);
    }

    void removeRemoveRequestListener(IRemoveRequestListener<BulletPointNode> removeRequestListener) {
        removeRequestListeners.remove(removeRequestListener);
    }

    void addContent(ContentType contentType, String contentData) {
        addContent(contentNodes.size(), contentType, contentData);
    }

    void addContent(int index, ContentType contentType, String contentData) {
        ContentNode contentNode = ContentNodeFactory.createContentNode(contentType, contentData);
        if (contentData != null) {
            contentsHBox.getChildren().add(index, contentNode.getNode());
            contentNodes.add(index, contentNode);
            if (id != null) {
                bulletController.addContent(id.getId(), index, contentData, contentType);
                contentNode.setIds(id, () -> contentsHBox.getChildren().indexOf(contentNode.getNode()));
            }
        }
    }

    void removeContentNode(int index) {
        contentsHBox.getChildren().remove(index);
        contentNodes.remove(index);
    }

    int getNumContents() {
        return contentNodes.size();
    }

    void addCommentNode(String username, String commentText, Boolean addToModel) {
        CommentNode commentNode = new CommentNode();
        commentNode.setData(username, commentText);
        commentNodes.add(commentNode);

        commentNode.addRemoveRequestListener((commentNode2) -> {
            commentNodes.remove(commentNode2);
            updateCommentButtonLook();
        });

        if (id != null) {
            commentNode.setIds(id, () -> commentNodes.indexOf(commentNode));
        }

        if (addToModel) {
            commentNode.addToModel();
        }

        updateCommentButtonLook();
    }

    boolean containsChild(BulletPointNode bulletPointNode) {
        return childBulletPoints.contains(bulletPointNode);
    }

    void addChild(BulletPointNode child) {
        addChild(childrenVBox.getChildren().size(), child);
    }

    void addChild(int index, BulletPointNode child) {

        childBulletPoints.add(index, child);

        childrenVBox.getChildren().add(index, child.getNode());
        if (id == null) {
            child.setId(() -> "" + childBulletPoints.indexOf(child));
        } else {
            child.setId(() -> id.getId() + ";" + childBulletPoints.indexOf(child));
        }

        child.addRemoveRequestListener(this::removeChild);

        zoomImageView.setImage(filledPointIcon);

    }

    void removeChild(int index) {
        childrenVBox.getChildren().remove(index);
        BulletPointNode bulletPointNode = childBulletPoints.remove(index);
        bulletPointNode.contentsHBox.getChildren().clear();
        bulletPointNode.contentNodes.clear();
        bulletPointNode.setId(() -> "root");
        if (childBulletPoints.isEmpty()) {
            zoomImageView.setImage(emptyPointIcon);
        }
    }

    void restoreChildren() {
        childrenVBox.getChildren().clear();
        childBulletPoints.forEach(child -> childrenVBox.getChildren().add(child.getNode()));
    }

    void removeChild(BulletPointNode bulletPointNode) {
        removeChild(childrenVBox.getChildren().indexOf(bulletPointNode.getNode()));
    }

    @FXML
    private void zoomButtonOnAction(Event event) {
        zoomListeners.forEach(listener -> listener.onZoomRequest(id.getId()));
    }

    @FXML
    private void foldingButtonOnAction(Event event) {
        setExpanded(!isExpanded());
    }

    @FXML
    private void onMouseEntered(Event event) {
        showFoldingButton(childBulletPoints.size() > 0);
        showActionButtons(true);
        showContentMenu(true);
    }

    @FXML
    private void onMouseExited(Event event) {
        if (!isAddContentButtonHoveredOn) {
            showFoldingButton(isExpanded());
            showActionButtons(false);
            showContentMenu(false);
        }
    }

    @FXML
    private void addContentOnMouseEntered(Event event) {
        isAddContentButtonHoveredOn = true;
    }

    @FXML
    private void addContentOnMouseExited(Event event) {
        isAddContentButtonHoveredOn = false;
    }

    @FXML
    private void bookmarkButtonOnAction(Event event) {
        boolean isBookmark = isBookmark();
        bulletController.setBulletPointBookmark(id.getId(), !isBookmark);
        bookmarkImageView.setImage(isBookmark ? bookmarkIcon : removeBookmarkIcon);
    }

    boolean isExpanded() {
        return rootPane.getChildren().contains(childrenVBox);
    }

    void setExpanded(boolean expanded) {

        if (expanded) {
            rootPane.add(childrenVBox, 2, 1);
        } else {
            rootPane.getChildren().remove(childrenVBox);
        }

        rotateFoldingIcon();

    }

    private void rotateFoldingIcon() {
        foldingIcon.setRotate(isExpanded() ? foldingIcon.getRotate() + 90 : foldingIcon.getRotate() - 90);
    }

    @Override
    public Node getNode() {
        return rootPane;
    }

    void setId(Id<String> id) {
        this.id = id;

        boolean isBookmark = isBookmark();
        bookmarkImageView.setImage(isBookmark ? removeBookmarkIcon : bookmarkIcon);
        bookmarkButton.setDisable(!isBookmark);
        bookmarkButton.setVisible(isBookmark);

        contentNodes.forEach(contentNode ->
                contentNode.setIds(
                        this.id,
                        () -> contentsHBox.getChildren().indexOf(contentNode.getNode())
                )
        );

        commentNodes.forEach(commentNode ->
                commentNode.setIds(
                        id,
                        () -> commentNodes.indexOf(commentNode)
                )
        );

    }

    void moveContent(int index, int newIndex) {

        contentsHBox.getChildren().remove(index);
        ContentNode contentNode = contentNodes.remove(index);

        contentsHBox.getChildren().add(newIndex, contentNode.getNode());
        contentNodes.add(newIndex, contentNode);

        bulletController.moveContent(id.getId(), index, newIndex);

    }

    void moveChildBulletPoint(int index, int newIndex) {

        childrenVBox.getChildren().remove(index);
        BulletPointNode bulletPointNode = childBulletPoints.remove(index);

        childrenVBox.getChildren().add(newIndex, bulletPointNode.getNode());
        childBulletPoints.add(newIndex, bulletPointNode);

        bulletController.moveBulletPoint(bulletPointNode.id.getId(), id.getId(), newIndex);

    }

    /**
     * Adds a comment in the model and in the view
     *
     * @param event
     */
    @FXML
    private void commentButtonOnAction(Event event) {

        // only add comment directly if there are no existing comments to see
        if (commentNodes.size() == 0) {
            addCommentNode(bulletController.getCurrentUsername(), "", true);
        }

        // view the modal
        commentModalView.clearView();
        commentNodes.forEach(commentNode -> commentModalView.addCommentNode(commentNode));
        NavigationHandler.navigateTo(NavigationTarget.MODAL_COMMENTS);

        commentModalView.setAddCommentRequestListener(() -> {
            addCommentNode(bulletController.getCurrentUsername(), "", true);
            commentModalView.addCommentNode(commentNodes.get(commentNodes.size() - 1));
        });
        commentNodes.get(commentNodes.size() - 1).focus();
    }

    @Override
    public void requestRemoval() {
        removeRequestListeners.forEach(removeRequestListener -> removeRequestListener.onRemovalRequest(this));
    }

    String getId() {
        return id.getId();
    }

    private MenuItem createMenuItem(ContentType contentType) {
        // Todo: Move this to a factory somehow. The problem is the hierarchy, apply setOnAction() lambda on submenus from external class?
        String description;
        String defaultValue;

        switch (contentType) {
            case CODE:
                Menu codeMenu = new Menu("Code");
                MenuItem item = new MenuItem("Java");
                item.setOnAction(event -> {
                    addContent(contentNodes.size() - 1, contentType, SyntaxType.JAVA + ":");
                });

                codeMenu.getItems().clear();
                codeMenu.getItems().add(item);

                return codeMenu;
            case TEXT:
                return null;
            case VIDEO:
                description = "Video";
                defaultValue = "";
                break;
            case IMAGE:
                description = "Image";
                defaultValue = "";
                break;
            case DIAGRAM:
                description = "Diagram";
                defaultValue = "";
                break;
            case PDF:
                description = "PDF";
                defaultValue = "";
                break;
            case LATEX:
                description = "Latex";
                defaultValue = "";
                break;
            case TABLE:
                description = "Table";
                defaultValue = "";
                break;
            default:
                description = "";
                defaultValue = "";
        }

        MenuItem menuItem = new MenuItem(description);
        String finalDefaultValue = defaultValue;
        menuItem.setOnAction(event -> {
            addContent(contentNodes.size() - 1, contentType, finalDefaultValue);
        });

        return menuItem;
    }

    private void updateCommentButtonLook() {
        String toolTipText = "See " + commentNodes.size() + " comments";
        String styleClass = "buttonBlue";
        commentButton.getStylesheets().add(getClass().getResource("/view/javafx/css/theme_light.css").toExternalForm());

        switch (commentNodes.size()) {
            case 0:
                toolTipText = "Add comment";
                commentButton.getStyleClass().clear();
                styleClass = "button";
                break;
            case 1:
                toolTipText = "See comment";
                break;
        }
        commentToolTip.setText(toolTipText);
        commentButton.getStyleClass().add(styleClass);
    }

}
