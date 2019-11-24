package bulletapp.view.javafx.nodes.content.table.grid;

import java.util.List;

public class CellGridFactory {

    public static TextCellGridNode createTextCellGridNode() {
        return new TextCellGridNode();
    }

    public static NumberCellGridNode createNumberCellGridNode() {
        return new NumberCellGridNode();
    }

    public static TextCellGridNode createTextCellGridNode(List<List<String>> data, List<String> header) {
        return new TextCellGridNode(data, header);
    }

    public static NumberCellGridNode createNumberCellGridNode(List<List<Number>> data, List<String> header) {
        return new NumberCellGridNode(data, header);
    }

    public static TextCellGridNode createTextCellGridNode(List<List<String>> data) {
        return new TextCellGridNode(data);
    }

    public static NumberCellGridNode createNumberCellGridNode(List<List<Number>> data) {
        return new NumberCellGridNode(data);
    }

    public static CustomCellGridNode createCustomCellGridNode(){
        return new CustomCellGridNode();
    }

}
