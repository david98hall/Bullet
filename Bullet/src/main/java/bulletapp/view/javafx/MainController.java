package bulletapp.view.javafx;

import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.INavigationListener;
import bulletapp.view.javafx.scenes.start.StartScreen;
import bulletapp.view.javafx.scenes.workspace.Workspace;
import bulletapp.view.javafx.utils.StackPaneUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, INavigationListener {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane modalPanel;

    @FXML
    private StartScreen startScreenController;

    @FXML
    private GridPane startScreen;

    @FXML
    private Workspace workspaceController;

    @FXML
    private GridPane workspace;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        NavigationHandler.addNavigationListener(this);

        startScreenController.addDocumentSelectionListener(documentName -> {
            workspaceController.viewCurrentDocument();
            NavigationHandler.navigateTo(NavigationTarget.WORKSPACE);
        });

        NavigationHandler.navigateTo(NavigationTarget.START_SCREEN);


    }

    @Override
    public void navigateTo(NavigationTarget navigationTarget) {

        switch (navigationTarget) {
            case START_SCREEN:
                StackPaneUtils.switchView(startScreen, stackPane);
                break;
            case PDF_MODAL_VIEW:
            case MODAL_BOOKMARKS:
            case MODAL_COMMENTS:
            case MODAL_DOCUMENT_NAME:
            case MODAL_USERNAME:
                StackPaneUtils.switchView(modalPanel, stackPane);
                break;
            case WORKSPACE:
                StackPaneUtils.switchView(workspace, stackPane);
                break;
        }

    }

}
