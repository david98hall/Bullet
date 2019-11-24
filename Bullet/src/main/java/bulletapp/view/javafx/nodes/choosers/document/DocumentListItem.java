package bulletapp.view.javafx.nodes.choosers.document;

import bulletapp.bullet.Bullet;
import bulletapp.controller.BulletController;
import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.util.selection.ISelectionListener;
import bulletapp.view.javafx.nodes.choosers.ListItem;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Hall
 */
class DocumentListItem extends ListItem {

    private static final Image documentIcon = new Image("/icons/document/document_black_icon.png");
    private static final Image removeDocumentIcon = new Image("/icons/document/document_black_remove_icon.png");

    private final List<IRemoveRequestListener<DocumentListItem>> removeRequestListeners;
    private final List<ISelectionListener<DocumentListItem>> selectionListeners;

    private static final BulletController bulletController = BulletController.getInstance();

    DocumentListItem(String documentName) {
        super(documentName, "/view/javafx/choosers/document/document_list_item.fxml", new GridPane());
        removeRequestListeners = new ArrayList<>(1);
        selectionListeners = new ArrayList<>(1);
    }

    void addRemoveRequestListener(IRemoveRequestListener<DocumentListItem> removeRequestListener) {
        removeRequestListeners.add(removeRequestListener);
    }

    void removeRemoveRequestListener(IRemoveRequestListener<DocumentListItem> removeRequestListener) {
        removeRequestListeners.remove(removeRequestListener);
    }

    void addSelectionListener(ISelectionListener<DocumentListItem> documentListener) {
        selectionListeners.add(documentListener);
    }

    void removeSelectionListener(ISelectionListener<DocumentListItem> documentListener) {
        selectionListeners.remove(documentListener);
    }

    @Override
    public void requestRemoval() {
        removeRequestListeners.forEach(listener -> listener.onRemovalRequest(this));
        bulletController.removeBulletDocument(Bullet.getInstance().getCurrentUserName(), getItemName());
    }

    @Override
    protected void onMouseEntered(Event event) {
        super.onMouseEntered(event);
        if (isRemovable()) {
            setIcon(removeDocumentIcon);
        }
    }

    @Override
    protected void onMouseExited(Event event) {
        super.onMouseExited(event);
        if (isRemovable()) {
            setIcon(documentIcon);
        }
    }

    @Override
    protected void chooseButtonOnAction(Event event) {
        bulletController.selectBulletDocument(getItemName());
        selectionListeners.forEach(listener -> listener.onSelection(this));
    }

    @Override
    public void setRemovable(boolean removable) {
        super.setRemovable(removable);
        if (!isRemovable()) {
            setIcon(documentIcon);
        }
    }

}
