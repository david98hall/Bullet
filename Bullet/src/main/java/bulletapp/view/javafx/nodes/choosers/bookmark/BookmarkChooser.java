package bulletapp.view.javafx.nodes.choosers.bookmark;

import bulletapp.bullet.Bullet;
import bulletapp.util.bulletpoint.BulletPointZoomHandler;
import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.javafx.nodes.choosers.ListChooser;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author David Hall
 */
public class BookmarkChooser extends ListChooser<BookmarkListItem> {

    public void updateList() {
        clearList();
        Bullet.getInstance().getBookmarkedIds().forEach(this::addBookmarkListItem);
    }

    private void addBookmarkListItem(String bookmarkedId) {
        BookmarkListItem bookmarkListItem = new BookmarkListItem(bookmarkedId);
        bookmarkListItem.addRemoveRequestListener(this);
        bookmarkListItem.addSelectionListener(this);
        addListItem(bookmarkListItem);
    }

    @Override
    public void onRemovalRequest(BookmarkListItem bookmarkListItem) {
        removeListItem(bookmarkListItem);
    }

    @Override
    public void onSelection(BookmarkListItem selected) {
        notifyItemSelectionListeners(selected.getItemName());
        BulletPointZoomHandler.getInstance().zoomToBulletPoint(selected.getBulletPointId());
        NavigationHandler.navigateTo(NavigationTarget.WORKSPACE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
