package bulletapp.view.javafx.scenes.modalpanel;

import bulletapp.controller.BulletController;
import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.INavigationListener;
import bulletapp.view.interfaces.IPrefSizeListener;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.BulletPointEventBus;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NameChooserModalView implements INode, INavigationListener {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button createButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private Text warningText;

    private static NameChooserModalView instance;
    private static NavigationTarget currentTarget;
    private final List<IPrefSizeListener> prefSizeListeners;
    private final BulletController bulletController;
    private final String allowedNameRegex = "^.*[0-9]+.*||.*[A-z]+.*$";

    public static NameChooserModalView getInstance() {
        if (instance == null)
            instance = new NameChooserModalView();
        return instance;
    }


    private NameChooserModalView() {
        FXMLUtils.loadFXMLNode("/view/javafx/modal/panes/name_chooser_pane.fxml", this, rootPane);
        prefSizeListeners = new ArrayList<>(1);
        bulletController = BulletController.getInstance();
        NavigationHandler.addNavigationListener(this::navigateTo);
    }

    public void onCreateAction(Event event) {
        String name = nameTextField.getText();

        // warn if name doesnt contain digits or letters
        if ( name.equals("") || !name.matches(allowedNameRegex) ) {
            warningText.setText("Please enter a name");
        }
        else { // create document
            int copyNumber = 0;
            String newName = name;

            while (nameExists(newName)) {
                // add a number if the name already is taken
                copyNumber++;
                newName = name + " " + copyNumber;
            }

            createModelObject(newName);
            NavigationHandler.navigateTo(NavigationTarget.START_SCREEN);
        }

    }



    public void addPrefSizeListener(IPrefSizeListener listener) {
        prefSizeListeners.add(listener);
    }

    private boolean nameExists(String name) {
        if (currentTarget == NavigationTarget.MODAL_DOCUMENT_NAME) {
            return bulletController.documentAlreadyExists(name, bulletController.getCurrentUsername());
        }
        else if (currentTarget == NavigationTarget.MODAL_USERNAME) {
            return bulletController.userAlreadyExists(name);
        }
        return false;
    }

    private void createModelObject(String name) {
        if (currentTarget == NavigationTarget.MODAL_DOCUMENT_NAME) {
            bulletController.createBulletDocument(name);
            BulletPointEventBus.getInstance().notifyDocumentCreationListeners(name);
            bulletController.saveDocument(name);
        }
        else if (currentTarget == NavigationTarget.MODAL_USERNAME) {
            bulletController.addBulletUser(name);
            BulletPointEventBus.getInstance().notifyUserCreationListeners(name);
        }

    }

    @Override
    public Node getNode() {
        return rootPane;
    }

    @Override
    public void navigateTo(NavigationTarget navigationTarget) {
        switch (navigationTarget) {
            case MODAL_DOCUMENT_NAME:
                createButton.setText("Create document");
                nameTextField.setPromptText("Enter document name");
                break;
            case MODAL_USERNAME:
                createButton.setText("Add user");
                nameTextField.setPromptText("Enter username");
                break;
        }
        currentTarget = navigationTarget;
        warningText.setText("");
        nameTextField.setText("");
    }
}
