package bulletapp.view.javafx.nodes.content.table.grid.cells;

import bulletapp.view.javafx.nodes.content.table.grid.cells.contentcell.NumberCellNode;
import bulletapp.view.javafx.nodes.content.table.grid.cells.contentcell.TextCellNode;
import bulletapp.view.javafx.nodes.content.table.grid.cells.labelcell.LabelCellNode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class CellFactory {

    public static final int cellSize = 30;

    public static TextCellNode createTextCell() {
        return new TextCellNode(getCellRoot());
    }

    public static LabelCellNode createColumnHeader() {
        return new LabelCellNode(getCellRoot());
    }

    public static LabelCellNode createRowHeader() {
        return new LabelCellNode(getSmallCellRoot());
    }


    public static NumberCellNode createNumberCell() {
        return new NumberCellNode(getCellRoot());
    }


    public static LabelCellNode createRowHeader(String data) {
        return new LabelCellNode(getSmallCellRoot(), data);
    }

    public static TextCellNode createTextCell(String data) {
        return new TextCellNode(getCellRoot(), data);
    }

    public static LabelCellNode createColumnHeader(String data) {
        return new LabelCellNode(getCellRoot(), data);
    }

    public static NumberCellNode createNumberCell(Number data) {
        return new NumberCellNode(getCellRoot(), data);
    }

    private static Pane getCellRoot() {
        Pane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(cellSize);
        anchorPane.setMinHeight(cellSize);
        anchorPane.setMaxHeight(cellSize);

        anchorPane.setMinWidth(cellSize * 3);
        anchorPane.setMinWidth(cellSize * 3);
        anchorPane.setMaxWidth(cellSize * 3);

        return anchorPane;
    }

    private static Pane getSmallCellRoot() {
        Pane anchorPane = getCellRoot();

        anchorPane.setPrefWidth(cellSize);
        anchorPane.setMinWidth(cellSize);
        anchorPane.setMaxWidth(cellSize);

        return anchorPane;
    }


}
