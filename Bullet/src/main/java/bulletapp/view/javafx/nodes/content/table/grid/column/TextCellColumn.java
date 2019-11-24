package bulletapp.view.javafx.nodes.content.table.grid.column;


import bulletapp.view.javafx.nodes.content.table.grid.cells.CellFactory;
import bulletapp.view.javafx.nodes.content.table.grid.interfaces.ICell;

import java.util.List;

public class TextCellColumn extends CellColumn<String, String> {

    public TextCellColumn() {
        super();
    }

    public TextCellColumn(List<String> data) {
        super(data);

    }

    @Override
    protected ICell<String, String> createCell() {
        return CellFactory.createTextCell();
    }

    @Override
    protected ICell<String, String> createCell(String data) {
        return CellFactory.createTextCell(data);
    }


}
