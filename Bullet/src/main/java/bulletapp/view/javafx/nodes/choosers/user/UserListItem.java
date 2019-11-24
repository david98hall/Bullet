package bulletapp.view.javafx.nodes.choosers.user;

import bulletapp.controller.BulletController;
import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.util.selection.ISelectionListener;
import bulletapp.view.javafx.nodes.choosers.ListItem;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Hall
 */
class UserListItem extends ListItem {

    private static final Image userIcon = new Image("/icons/user/user_black_icon.png");
    private static final Image removeUserIcon = new Image("/icons/user/user_remove_black_icon.png");

    private final List<IRemoveRequestListener<UserListItem>> removeRequestListeners;
    private final List<ISelectionListener<UserListItem>> selectionListeners;

    @Getter
    private boolean isSelected;

    private final static BulletController bulletController = BulletController.getInstance();

    UserListItem(String username) {
        super(username, "/view/javafx/choosers/user/user_list_item.fxml", new GridPane());
        removeRequestListeners = new ArrayList<>(1);
        selectionListeners = new ArrayList<>(1);
    }

    void addRemoveRequestListener(IRemoveRequestListener<UserListItem> removeRequestListener) {
        removeRequestListeners.add(removeRequestListener);
    }

    void removeRemoveRequestListener(IRemoveRequestListener<UserListItem> removeRequestListener) {
        removeRequestListeners.remove(removeRequestListener);
    }

    void addSelectionListener(ISelectionListener<UserListItem> documentListener) {
        selectionListeners.add(documentListener);
    }

    void removeSelectionListener(ISelectionListener<UserListItem> documentListener) {
        selectionListeners.remove(documentListener);
    }

    @Override
    protected void onMouseEntered(Event event) {
        if (!isSelected)
            super.onMouseEntered(event);
        if (isRemovable())
            setIcon(removeUserIcon);
    }

    @Override
    protected void onMouseExited(Event event) {
        if (!isSelected)
            super.onMouseExited(event);
        if (isRemovable())
            setIcon(userIcon);
    }

    @Override
    public void requestRemoval() {
        removeRequestListeners.forEach(listener -> listener.onRemovalRequest(this));
        bulletController.removeUser(getItemName());
    }

    @Override
    protected void chooseButtonOnAction(Event event) {
        requestSelection();
    }

    private void requestSelection() {
        if (!isSelected) {
            setSelected(true);
            selectionListeners.forEach(listener -> listener.onSelection(this));
        }
    }

    void setSelected(boolean selected) {
        isSelected = selected;
        highlight(isSelected);
        bulletController.selectBulletUser(getItemName(), isSelected);
    }

    @Override
    public void setRemovable(boolean removable) {
        super.setRemovable(removable);
        if (!removable) {
            setIcon(userIcon);
        }
    }
}
