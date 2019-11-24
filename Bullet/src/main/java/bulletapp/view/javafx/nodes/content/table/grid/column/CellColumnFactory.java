package bulletapp.view.javafx.nodes.content.table.grid.column;

import java.util.List;

public class CellColumnFactory {

    public static NumberCellColumn createNumberCellColumn() {
        return new NumberCellColumn();
    }

    public static NumberCellColumn createNumberCellColumn(List<Number> data) {
        return new NumberCellColumn(data);
    }


    public static TextCellColumn createTextCellColumn() {
        return new TextCellColumn();
    }

    public static TextCellColumn createTextCellColumn(List<String> data) {
        return new TextCellColumn(data);
    }
}

