package bulletapp.view.javafx.nodes.choosers;

import bulletapp.util.selection.ISelectionListener;
import bulletapp.view.javafx.nodes.choosers.document.DocumentChooser;
import bulletapp.view.javafx.nodes.choosers.user.UserChooser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author David Hall
 */
public class UserDocumentSplitPane implements Initializable {

    @FXML
    private GridPane userChooser;

    @FXML
    private UserChooser userChooserController;

    @FXML
    private GridPane documentChooser;

    @FXML
    private SplitPane splitPane;

    @FXML
    private DocumentChooser documentChooserController;

    private final List<ISelectionListener<String>> userSelectionListeners;
    private final List<ISelectionListener<String>> documentSelectionListeners;

    public UserDocumentSplitPane() {
        userSelectionListeners = new ArrayList<>(1);
        documentSelectionListeners = new ArrayList<>(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userChooserController.addItemSelectionListener(userName -> {
            documentChooserController.updateDocumentList();
            userSelectionListeners.forEach(listener -> listener.onSelection(userName));
        });

        documentChooserController.addItemSelectionListener(documentName -> {
            documentSelectionListeners.forEach(listener -> listener.onSelection(documentName));
        });

    }

    public void addUserSelectionListener(ISelectionListener<String> userSelectionListener) {
        userSelectionListeners.add(userSelectionListener);
    }

    public void removeUserSelectionListener(ISelectionListener<String> userSelectionListener) {
        userSelectionListeners.remove(userSelectionListener);
    }

    public void addDocumentSelectionListener(ISelectionListener<String> documentSelectionListener) {
        documentSelectionListeners.add(documentSelectionListener);
    }

    public void removeDocumentSelectionListener(ISelectionListener<String> documentSelectionListener) {
        documentSelectionListeners.remove(documentSelectionListener);
    }

    public void scrollToTop() {
        userChooserController.scrollToTop();
        documentChooserController.scrollToTop();
    }

    public void reloadFromDisc() {
        userChooserController.loadLocalUsers();
        documentChooserController.updateDocumentList();
    }

}
