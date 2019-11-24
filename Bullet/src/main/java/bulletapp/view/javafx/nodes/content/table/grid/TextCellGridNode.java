package bulletapp.view.javafx.nodes.content.table.grid;

import bulletapp.view.javafx.nodes.content.table.grid.column.CellColumn;
import bulletapp.view.javafx.nodes.content.table.grid.column.CellColumnFactory;

import java.util.List;

 class TextCellGridNode extends CellGridNode<String, String> {

    TextCellGridNode() {
        super();
    }

    TextCellGridNode(List<List<String>> data, List<String> header) {
        super(data, header);
    }

    TextCellGridNode(List<List<String>> data) {
        super(data);
    }

    @Override
    protected CellColumn<String, String> createCellColumn() {
        return CellColumnFactory.createTextCellColumn();
    }

    @Override
    protected CellColumn<String, String> createCellColumn(List data) {
        return CellColumnFactory.createTextCellColumn(data);
    }
}
