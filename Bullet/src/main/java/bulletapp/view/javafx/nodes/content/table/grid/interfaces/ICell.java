package bulletapp.view.javafx.nodes.content.table.grid.interfaces;

import bulletapp.view.javafx.nodes.interfaces.INode;

public interface ICell<T, A> extends INode {
    A getValue();

    void setValue(T newValue);

}
