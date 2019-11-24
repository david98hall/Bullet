package bulletapp.view.javafx.nodes.content.table.grid.cells.contentcell;

import bulletapp.view.javafx.nodes.content.table.grid.interfaces.ICell;
import bulletapp.view.javafx.nodes.content.table.grid.interfaces.ValueListener;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

 abstract class FieldCellNode<T, A> implements ICell<T, A> {

    private final Pane rootPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    TextField textField;

    private final List<ValueListener<A>> listenerList;

    private T data;

    FieldCellNode(Pane root) {
        listenerList = new ArrayList<>(1);
        rootPane = root;

        FXMLUtils.loadFXMLNode("/view/javafx/content/table/grid/cell/cell.fxml", this, rootPane);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                updateData(stringToValue(textField.getText()));
            }
        });

        init();
    }

    FieldCellNode(Pane root, T data) {
        this(root);
        setValue(data);
    }

    protected abstract void init();

    public void addListener(ValueListener<A> listener) {
        listenerList.add(listener);
    }

    protected abstract A parseData(T data);

    @Override
    public A getValue() {
        return parseData(data);
    }

    @Override
    public void setValue(T newValue) {
        data = newValue;
        textField.setText(valueToString());
    }

    protected abstract T stringToValue(String newValue);

    protected abstract String valueToString();


    @Override
    public Node getNode() {
        return rootPane;
    }

    private void updateData(T newData) {
        A oldData = getValue();
        setValue(newData);
        callListeners(oldData, getValue());
    }

    private void callListeners(A oldData, A newData) {
        listenerList.forEach(listener -> listener.onUpdate(oldData, newData));
    }

}
