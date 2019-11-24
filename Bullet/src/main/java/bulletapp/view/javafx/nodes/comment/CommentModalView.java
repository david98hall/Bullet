package bulletapp.view.javafx.nodes.comment;

import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.IAddCommentRequestListener;
import bulletapp.view.interfaces.IPrefSizeListener;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CommentModalView implements INode, IRemoveRequestListener<CommentNode> {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox commentNodeVBox;

    @FXML
    private Button addButton;

    private static CommentModalView instance;

    private List<IPrefSizeListener> prefSizeListeners = new ArrayList<>(1);
    @Setter
    private IAddCommentRequestListener addCommentRequestListener;

    public CommentModalView() {
        FXMLUtils.loadFXMLNode("/view/javafx/modal/panes/comment_pane.fxml",this, rootPane);
    }

    public static CommentModalView getInstance() {
        if (instance == null) {
            instance = new CommentModalView();
        }
        return instance;
    }

    public void clearView() {
        commentNodeVBox.getChildren().clear();
        commentNodeVBox.getChildren().add(addButton);
    }

    public void addButtonAction(Event event) {
        addCommentRequestListener.onAddCommentRequest();
    }

    public void addCommentNode(CommentNode commentNode) {
        commentNode.addRemoveRequestListener(this);
        viewCommentNode(commentNode);
    }

    private void viewCommentNode(CommentNode commentNode) {
        // add before the add-comment-button
        int size = commentNodeVBox.getChildren().size();
        commentNodeVBox.getChildren().add(size-1,commentNode.getNode());

        //scroll to bottom
        onPrefSizeChanged(rootPane.getWidth(), rootPane.getHeight());
        rootPane.applyCss();
        rootPane.layout();
        scrollPane.setVvalue(scrollPane.getVmax());

        commentNode.focus();
    }

    public void addPrefSizeListener(IPrefSizeListener prefSizeListener) {
        prefSizeListeners.add(prefSizeListener);
    }

    private void onPrefSizeChanged(double width, double height) {
        prefSizeListeners.forEach(prefSizeListener -> prefSizeListener.OnPrefSizeUpdate(NavigationTarget.MODAL_COMMENTS, width, height));
    }

    @Override
    public Node getNode() {
        return rootPane;
    }


    @Override
    public void onRemovalRequest(CommentNode commentNode) {
        commentNodeVBox.getChildren().remove(commentNode.getNode());
        onPrefSizeChanged(rootPane.getWidth(), rootPane.getHeight());
    }
}
