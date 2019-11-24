package bulletapp.view.javafx.nodes.choosers.user;

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
public class UserChooser extends ListChooser<UserListItem> implements IListAddable {

    private final UserListItem defaultUserListItem;
    private UserListItem selectedListItem;

    private final static BulletController bulletController = BulletController.getInstance();

    public UserChooser() {
        super();

        // Default user
        String defaultName = Bullet.getInstance().getDefaultUsername();
        defaultUserListItem = new UserListItem(defaultName);
        defaultUserListItem.addSelectionListener(this);
        defaultUserListItem.setRemovable(false);
        bulletController.addBulletUser(defaultName);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BulletPointEventBus.getInstance().addUserCreationListener((username) ->
                addUserListItem(username, true));
    }

    public void loadLocalUsers() {
        clearList();
        initDefaultUserListItem();
        bulletController.loadLocalUsers();

        Bullet.getInstance().getAllUsernames().forEach(username -> {
            if (!username.equals(Bullet.getInstance().getDefaultUsername())) {
                addUserListItem(username, true);
            }
        });

    }

    @Override
    public void addButtonOnAction(Event event) {
        NavigationHandler.navigateTo(NavigationTarget.MODAL_USERNAME);
    }

    private void initDefaultUserListItem() {
        addListItem(defaultUserListItem);
        selectListItem(defaultUserListItem);
    }

    private void addUserListItem(String username, boolean removable) {
        UserListItem userListItem = new UserListItem(username);
        userListItem.addSelectionListener(this);
        userListItem.addRemoveRequestListener(this);
        userListItem.setRemovable(removable);
        addListItem(userListItem);
    }

    public String getSelectedUserName() {
        return selectedListItem.getItemName();
    }

    @Override
    public void onSelection(UserListItem selected) {

        if (selected.isSelected()) {
            listItems().forEachRemaining(item -> {
                if (item != selected) {
                    item.setSelected(false);
                }
            });

            selectedListItem = selected;
            notifyItemSelectionListeners(selectedListItem.getItemName());

        }

    }

    private void selectListItem(UserListItem listItem) {
        selectedListItem = listItem;
        selectedListItem.setSelected(true);
        notifyItemSelectionListeners(selectedListItem.getItemName());
    }

    @Override
    public void onRemovalRequest(UserListItem userListItem) {

        removeListItem(userListItem);

        // Select default user list item if there are no other users
        if (getNumListItems() == 1 || userListItem.isSelected()) {
            userListItem.setSelected(false);
            selectListItem(defaultUserListItem);
        }

    }

}
