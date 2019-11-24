package bulletapp.view.javafx.scenes.modalpanel;

import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.INavigationListener;
import bulletapp.view.interfaces.IPrefSizeListener;
import bulletapp.view.javafx.nodes.choosers.bookmark.BookmarkChooser;
import bulletapp.view.javafx.nodes.comment.CommentModalView;
import bulletapp.view.javafx.nodes.content.pdfview.pdfmodalview.PDFModalViewNode;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.StackPaneUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ModalPanel implements Initializable, INavigationListener, IPrefSizeListener {

    @FXML
    private AnchorPane modalPane;

    @FXML
    private HBox headerHBox;

    @FXML
    private Label modalTitleLabel;

    @FXML
    private Button closeButton;

    @FXML
    private ImageView closeButtonImageView;

    @FXML
    private StackPane stackPane;

    @FXML
    private GridPane bookmarkChooser;

    @FXML
    private BookmarkChooser bookmarkChooserController;

    private PDFModalViewNode pdfModalView;
    private CommentModalView commentModalView;
    private NameChooserModalView nameChooserModalView;

    public ModalPanel() {
        NavigationHandler.addNavigationListener(this);
    }

    private void addPDFView() {
        pdfModalView = PDFModalViewNode.getInstance();
        pdfModalView.addPrefSizeListener(this);
        stackPane.getChildren().add(pdfModalView.getNode());
    }

    private void addCommentView() {
        commentModalView = CommentModalView.getInstance();
        commentModalView.addPrefSizeListener(this);
        stackPane.getChildren().add(commentModalView.getNode());
    }

    private void addNameChooserView() {
        nameChooserModalView = NameChooserModalView.getInstance();
        nameChooserModalView.addPrefSizeListener(this);
        stackPane.getChildren().add(nameChooserModalView.getNode());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addPDFView();
        addCommentView();
        addNameChooserView();
    }

    @Override
    public void navigateTo(NavigationTarget navigationTarget) {
        switch (navigationTarget) {
            case PDF_MODAL_VIEW:
                navigateTo(pdfModalView, pdfModalView.getTitle());
                break;
            case MODAL_BOOKMARKS:
                navigateTo(bookmarkChooser, "Bookmarks");
                bookmarkChooserController.updateList();
                break;
            case MODAL_COMMENTS:
                navigateTo(commentModalView, "Comments");
                break;
            case MODAL_DOCUMENT_NAME:
                navigateTo(nameChooserModalView, "Enter document name");
                break;
            case MODAL_USERNAME:
                navigateTo(nameChooserModalView, "Enter username");
                break;
        }
    }

    private Node getNode(NavigationTarget navigationTarget) {
        switch (navigationTarget) {
            case PDF_MODAL_VIEW:
                return pdfModalView.getNode();
            case MODAL_BOOKMARKS:
                return bookmarkChooser;
            case MODAL_COMMENTS:
                return commentModalView.getNode();
            case MODAL_DOCUMENT_NAME:
            case MODAL_USERNAME:
                return nameChooserModalView.getNode();
            default:
                return null;
        }
    }

    public void navigateTo(INode node, String title) {
        navigateTo(node.getNode(), title);
    }

    public void navigateTo(Node node, String title) {
        setSize(node);
        setStackPaneChildVisible(node);
        modalTitleLabel.setText(title);
        StackPaneUtils.switchView(node, stackPane);
    }

    @FXML
    void closeButtonMouseEntered(MouseEvent event) {
        // TODO Change button appearance
    }

    @FXML
    void closeButtonMouseExited(MouseEvent event) {
        // TODO Change button appearance
    }

    @FXML
    void closeButtonOnAction(Event event) {
        NavigationHandler.goBack();
    }

    @FXML
    void consumeEvent(MouseEvent event) {
        event.consume();
    }

    private void setStackPaneChildrenInvisible() {
        stackPane.getChildren().forEach(child -> child.setVisible(false));
    }

    private void setSize(Node node) {
        stackPane.setMaxSize(node.prefWidth(-1), node.prefHeight(-1));
        stackPane.setMinSize(node.prefWidth(-1), node.prefHeight(-1));
        stackPane.setPrefSize(node.prefWidth(-1), node.prefHeight(-1));
    }

    private void setStackPaneChildVisible(Node node) {
        setStackPaneChildrenInvisible();
        node.setDisable(false);
        node.setVisible(true);
    }

    public void SetCloseButtonImageView(Image image) {
        closeButtonImageView.setImage(image);
    }

    @Override
    public void OnPrefSizeUpdate(NavigationTarget target, double width, double height) {
        if (isInFront(getNode(target))) {
            navigateTo(target);
        }
    }

    private boolean isInFront(Node node) {
        return !node.isDisabled();
    }
}
