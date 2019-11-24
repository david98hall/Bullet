package bulletapp.view.javafx.nodes.choosers.document;

import bulletapp.bullet.Bullet;
import bulletapp.controller.BulletController;
import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.javafx.nodes.choosers.IListAddable;
import bulletapp.view.javafx.nodes.choosers.ListChooser;
import bulletapp.view.javafx.utils.BulletPointEventBus;
import javafx.event.Event;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author David Hall
 */
public class DocumentChooser extends ListChooser<DocumentListItem> implements IListAddable {

    private final static BulletController bulletController = BulletController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BulletPointEventBus.getInstance().addDocumentCreationListener(this::addDocumentListItem);
    }

    public void updateDocumentList() {
        clearList();
        bulletController.loadBulletDocumentFiles();
        Bullet.getInstance().getCurrentUserDocumentNames().forEach(this::addDocumentListItem);
    }

    @Override
    public void addButtonOnAction(Event event) {
        NavigationHandler.navigateTo(NavigationTarget.MODAL_DOCUMENT_NAME);
    }

    private void addDocumentListItem(String documentName) {
        DocumentListItem documentListItem = new DocumentListItem(documentName);
        documentListItem.addRemoveRequestListener(this);
        documentListItem.addSelectionListener(this);
        addListItem(documentListItem);
    }

    @Override
    public void onSelection(DocumentListItem selected) {
        notifyItemSelectionListeners(selected.getItemName());
    }

    @Override
    public void onRemovalRequest(DocumentListItem documentListItem) {
        removeListItem(documentListItem);
    }

}
