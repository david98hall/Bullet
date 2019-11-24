package bulletapp.view.javafx.nodes.choosers;

import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.util.selection.ISelectionListener;
import bulletapp.view.javafx.nodes.interfaces.INode;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author David Hall
 */
public abstract class ListChooser<T extends INode> implements Initializable, ISelectionListener<T>, IRemoveRequestListener<T> {

    @FXML
    private ScrollPane listScrollPane;

    @FXML
    private VBox listVBox;

    private final List<T> listItems;

    private final List<ISelectionListener<String>> itemSelectionListeners;

    public ListChooser() {
        super();
        listItems = new ArrayList<>(1);
        itemSelectionListeners = new ArrayList<>(1);
    }

    protected void addListItem(T t) {
        listItems.add(t);
        listVBox.getChildren().add(t.getNode());
    }

    protected void removeListItem(T item) {
        listItems.remove(item);
        listVBox.getChildren().remove(item.getNode());
    }

    protected void clearList() {
        listVBox.getChildren().clear();
        listItems.clear();
    }

    protected Iterator<T> listItems() {
        return listItems.iterator();
    }

    public int getNumListItems() {
        return listItems.size();
    }

    public void addItemSelectionListener(ISelectionListener<String> itemSelectionListener) {
        itemSelectionListeners.add(itemSelectionListener);
    }

    public void removeItemSelectionListener(ISelectionListener<String> itemSelectionListener) {
        itemSelectionListeners.remove(itemSelectionListener);
    }

    protected void notifyItemSelectionListeners(String listItemName) {
        itemSelectionListeners.forEach(listener -> listener.onSelection(listItemName));
    }

    public void scrollToTop() {
        listScrollPane.setVvalue(0);
    }

    public void scrollToBottom() {
        listScrollPane.setVvalue(listScrollPane.getVmax());
    }

}
