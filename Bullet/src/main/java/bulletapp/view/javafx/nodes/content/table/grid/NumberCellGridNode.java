package bulletapp.view.javafx.nodes.content.table.grid;

import bulletapp.view.javafx.nodes.content.table.grid.column.CellColumn;
import bulletapp.view.javafx.nodes.content.table.grid.column.CellColumnFactory;

import java.util.List;

 class NumberCellGridNode extends CellGridNode<Number, String> {

    NumberCellGridNode() {
        super();
    }

    NumberCellGridNode(List<List<Number>> data, List<String> header) {
        super(data, header);
    }

    NumberCellGridNode(List<List<Number>> data) {
        super(data);
    }

    @Override
    protected CellColumn<Number, String> createCellColumn() {
        return CellColumnFactory.createNumberCellColumn();
    }

    @Override
    protected CellColumn<Number, String> createCellColumn(List<Number> data) {
        return CellColumnFactory.createNumberCellColumn(data);
    }

}
