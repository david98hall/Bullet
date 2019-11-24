package bulletapp.view.javafx.nodes.content.table.grid.column;

import bulletapp.view.javafx.nodes.content.table.grid.cells.CellFactory;
import bulletapp.view.javafx.nodes.content.table.grid.interfaces.ICell;

import java.util.List;

public class NumberCellColumn extends CellColumn<Number, String> {


    NumberCellColumn() {
        super();
    }

    NumberCellColumn(List<Number> data) {
        super(data);

    }

    @Override
    protected ICell<Number, String> createCell() {
        return CellFactory.createNumberCell();
    }

    @Override
    protected ICell<Number, String> createCell(Number data) {
        return CellFactory.createNumberCell(data);
    }


}
