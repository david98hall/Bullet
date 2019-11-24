package bulletapp.view.javafx.nodes.comment;

import bulletapp.controller.BulletController;
import bulletapp.util.Id;
import bulletapp.util.removable.IRemovable;
import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Isak Schwartz
 * This class is used to view a bulletpoint's comments
 */

public class CommentNode implements INode, Initializable, IRemovable {

    @FXML
    private final AnchorPane rootPane;

    @FXML
    private Text userText;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button deleteButton;

    private Id<String> containerBulletPointId;
    private Id<Integer> indexId;
    private final static BulletController bulletController = BulletController.getInstance();
    private String username;
    private String commentText;
    private final List<IRemoveRequestListener<CommentNode>> removeRequestListeners = new ArrayList<>(1);

    public CommentNode() {
        rootPane = new AnchorPane();
        FXMLUtils.loadFXMLNode("/view/javafx/content/comment.fxml", this, rootPane);
     }

    public void setData(String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
        userText.setText("Comment added by " + username);
        commentTextArea.setText(commentText);
    }

    public void onTextChanged(Event event) {
        commentText = commentTextArea.getText();
        updateModel(commentTextArea.getText());
    }

    private int getIndex() {
        return rootPane.getParent().getChildrenUnmodifiable().indexOf(rootPane);
    }

    @Override
    public Node getNode() {
        return rootPane;
    }

    /**
     * Set parent bullet point id and the id representing the index of this comment among its parents comments
     * @param containerBulletPointId the parent bullet point's id
     * @param indexId the index id
     */
    public void setIds(Id<String> containerBulletPointId, Id<Integer> indexId) {
        setContainerBulletPointId(containerBulletPointId);
        setIndexId(indexId);
    }

    public void setContainerBulletPointId(Id<String> containerBulletPointId) {
        this.containerBulletPointId = containerBulletPointId;
    }

    public void setIndexId(Id<Integer> indexId) {
        this.indexId = indexId;
    }

    /**
     * Updates the text and id in the model
     * @param commentText
     */
    public void updateModel(String commentText) {
        bulletController.updateComment(commentText,containerBulletPointId.getId(), indexId.getId());
    }

    public void addToModel() {
        bulletController.addComment("", containerBulletPointId.getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userText.setText(bulletController.getCurrentUsername() + ":");
        commentTextArea.requestFocus();

        /*
        commentTextArea.textProperty().addListener((obs, oldText, newText) -> {
            updateModel(newText);
        });
        */
    }

    public void focus() {
        commentTextArea.requestFocus();
    }

    public void deleteButtonOnAction(Event event) {
        bulletController.removeComment(containerBulletPointId.getId(), indexId.getId());
        requestRemoval();
    }

    public CommentNode copy() {
        CommentNode newCommentNode = new CommentNode();
        newCommentNode.setData(this.username, this.commentText);
        newCommentNode.setIds(containerBulletPointId, indexId);
        // bind text
        commentTextArea.textProperty().bind(newCommentNode.commentTextArea.textProperty());
        return newCommentNode;

    }

    @Override
    public void requestRemoval() {
        for (IRemoveRequestListener<CommentNode> listener : removeRequestListeners) {
            listener.onRemovalRequest(this);
        }
    }

    public void addRemoveRequestListener(IRemoveRequestListener<CommentNode> listener) {
        removeRequestListeners.add(listener);
    }

}
