package bulletapp.view.javafx.nodes.content.table.grid.column;

import bulletapp.view.javafx.nodes.content.table.grid.interfaces.ICell;
import bulletapp.view.javafx.nodes.interfaces.INode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class CellColumn<A, T> implements INode {

    private final List<ICell<A, T>> cells;

    private final List<T> values;

    private final VBox vBox;

    CellColumn() {
        cells = new ArrayList<>();
        vBox = new VBox();
        values=new ArrayList<>();
    }

    CellColumn(List<A> data) {
        this();
        addCell(data);
    }

    protected abstract ICell<A, T> createCell();

    protected abstract ICell<A, T> createCell(A data);

    private void addCell() {
        addCell(createCell());
    }

    public void addCells(int num) {
        while (num-- > 0)
            addCell();
    }

    protected void addCell(ICell<A, T> cell) {
        cells.add(cell);
        vBox.getChildren().add(cell.getNode());

    }

    private void addCell(A data) {
        addCell(createCell(data));
    }

    private void addCell(List<A> list) {
        list.forEach(this::addCell);
    }


    /**
     *  Removes the ICell at the specified position.
     *
     *  @param index the index of the ICell to be removed
     */
    public void removeCell(int index) {
        if(index<cells.size()&&cells.size()>0) {
            removeCell(cells.get(index));
        }
    }

    /**
     *  Removes the specified cell
     *
     */
    public void removeCell(ICell<A, T> cell) {
        cells.remove(cell);
        removeVBoxChild(cell.getNode());
    }

    private void removeVBoxChild(Node node) {
        vBox.getChildren().remove(node);
    }

    public T getValue(int index) {
        return cells.get(index).getValue();
    }

    /**
     *  Retrieves a List of all the values the column contains.
     *
     */
    public List<T> getValue() {
        List<T> list = new ArrayList<>(cells.size());
        cells.forEach(cell -> list.add(cell.getValue()));
        return list;
    }
    /**
     *  Retrieves amount of rows the column contains.
     *
     */
    public int getRows(){
        return cells.size();
    }

    @Override
    public Node getNode() {
        return vBox;
    }
}
