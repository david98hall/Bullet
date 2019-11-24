package bulletapp.view.javafx.scenes.start;

import bulletapp.controller.BulletController;
import bulletapp.util.selection.ISelectionListener;
import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.INavigationListener;
import bulletapp.view.javafx.nodes.choosers.UserDocumentSplitPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author David Hall
 */
public class StartScreen implements Initializable, INavigationListener {

    @FXML
    private UserDocumentSplitPane userDocumentSplitPaneController;

    public StartScreen() {
        NavigationHandler.addNavigationListener(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * add a selection listener to this object
     *
     * @param documentSelectionListener
     */
    public void addDocumentSelectionListener(ISelectionListener<String> documentSelectionListener) {
        userDocumentSplitPaneController.addDocumentSelectionListener(documentSelectionListener);
    }

    @Override
    public void navigateTo(NavigationTarget navigationTarget) {
        if (navigationTarget.equals(NavigationTarget.START_SCREEN)) {
            BulletController.getInstance().resetBullet();
            userDocumentSplitPaneController.reloadFromDisc();
        }
    }
}
