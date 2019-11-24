package bulletapp.view.javafx.scenes.workspace;

import bulletapp.controller.BulletController;
import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.javafx.nodes.document.BulletDocumentNode;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Workspace implements Initializable {

    // TODO Add BulletDocumentNode, Toolbar, etc (add what's essential first!)

    @FXML
    private GridPane rootPane;

    @FXML
    private VBox documentVBox;

    private final BulletController bulletController = BulletController.getInstance();

    private BulletDocumentNode currentDocumentNode;

    public Workspace() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void viewCurrentDocument() {
        documentVBox.getChildren().clear();
        if (currentDocumentNode != null) {
            currentDocumentNode.dispose();
        }
        currentDocumentNode = new BulletDocumentNode(bulletController.getCurrentDocumentName());
        documentVBox.getChildren().add(currentDocumentNode.getNode());
    }

    @FXML
    public void saveCurrentDocument() {
        bulletController.saveCurrentDocument();
    }

    @FXML
    private void bookmarkButtonOnAction(Event event) {
        NavigationHandler.navigateTo(NavigationTarget.MODAL_BOOKMARKS);
    }

    @FXML
    public void homeButtonOnAction() {
        NavigationHandler.navigateTo(NavigationTarget.START_SCREEN);
    }

}
