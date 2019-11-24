package bulletapp.view.javafx.nodes.choosers.bookmark;

import bulletapp.bullet.Bullet;
import bulletapp.controller.BulletController;
import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.util.selection.ISelectionListener;
import bulletapp.view.javafx.nodes.choosers.ListItem;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Hall
 */
class BookmarkListItem extends ListItem {

    @FXML
    private ImageView icon;

    private static final Image bookmarkIcon = new Image("/icons/bookmark/bookmark_icon.png");
    private static final Image removeBookmarkIcon = new Image("/icons/bookmark/bookmark_remove_icon.png");

    private final static BulletController bulletController = BulletController.getInstance();

    private final List<IRemoveRequestListener<BookmarkListItem>> removeRequestListeners = new ArrayList<>(1);
    private final List<ISelectionListener<BookmarkListItem>> selectionListeners = new ArrayList<>(1);

    @Getter
    private final String bulletPointId;

    BookmarkListItem(String bulletPointId) {
        super(Bullet.getInstance().getContentData(bulletPointId, 0).getValue().trim(),
                "/view/javafx/choosers/bookmark/bookmark_list_item.fxml", new GridPane());
        this.bulletPointId = bulletPointId;
    }

    void addRemoveRequestListener(IRemoveRequestListener<BookmarkListItem> removeRequestListener) {
        removeRequestListeners.add(removeRequestListener);
    }

    void removeRemoveRequestListener(IRemoveRequestListener<BookmarkListItem> removeRequestListener) {
        removeRequestListeners.remove(removeRequestListener);
    }

    void addSelectionListener(ISelectionListener<BookmarkListItem> selectionListener) {
        selectionListeners.add(selectionListener);
    }

    void removeSelectionListener(ISelectionListener<BookmarkListItem> selectionListener) {
        selectionListeners.remove(selectionListener);
    }

    @Override
    public void requestRemoval() {
        bulletController.setBulletPointBookmark(bulletPointId, false);
        removeRequestListeners.forEach(listener -> listener.onRemovalRequest(this));
    }

    @Override
    protected void onMouseEntered(Event event) {
        super.onMouseEntered(event);
        if (isRemovable()) {
            setIcon(removeBookmarkIcon);
        }
    }

    @Override
    protected void onMouseExited(Event event) {
        super.onMouseExited(event);
        icon.setImage(bookmarkIcon);
    }

    @Override
    protected void chooseButtonOnAction(Event event) {
        selectionListeners.forEach(listener -> listener.onSelection(this));
    }

}
