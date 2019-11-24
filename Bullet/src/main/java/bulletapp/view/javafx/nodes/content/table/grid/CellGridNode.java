package bulletapp.view.javafx.nodes.content.table.grid;

import bulletapp.view.javafx.nodes.content.table.grid.cells.CellFactory;
import bulletapp.view.javafx.nodes.content.table.grid.column.CellColumn;
import bulletapp.view.javafx.nodes.content.table.grid.interfaces.ICell;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class CellGridNode<A, T> implements  INode {

    private final Pane rootPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox cellPane;

    @FXML
    private VBox rowHeader;

    @FXML
    private HBox columnHeader;

    @FXML
    private ScrollPane columScroll;

    @FXML
    private ScrollPane rowscroll;

    private final List<ICell<String, String>> rowLabel;
    private final List<ICell<String, String>> headers;
    private final List<CellColumn<A, T>> grid; //todo add listener

    @Getter
    private int rows;

    @Getter
    private int columns;


    CellGridNode() {
        grid = new ArrayList<>();
        headers = new ArrayList<>();
        rowLabel = new ArrayList<>();
        rootPane = new AnchorPane();

        rows = 0;
        columns = 0;

        FXMLUtils.loadFXMLNode("/view/javafx/content/table/grid/grid.fxml", this, rootPane);

        bindScrollBarsVvalue(scrollPane, columScroll);
        bindScrollBarsHvalue(scrollPane, rowscroll);
        bindScrollBarsVvalue(columScroll, scrollPane);
        bindScrollBarsHvalue(rowscroll, scrollPane);

    }

    CellGridNode(List<List<A>> data, List<String> header) {
        this();
        dataToGrid(data, header);

    }

    CellGridNode(List<List<A>> data) {
        this();
        dataToGrid(data);

    }

    private void dataToGrid(List<List<A>> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            addColumn(matrix.get(i));
        }
    }

    private void dataToGrid(List<List<A>> matrix, List<String> header) {
        for (int i = 0; i < matrix.size(); i++) {
            addColumn(header.get(i), matrix.get(i));
        }
    }

    /**
     *  Retrieves all of the data from the columns.
     *
     *  @return the column data in a List
     */
    public List<List<T>> gridToMatrix() {
        return gridToMatrix(grid);
    }

    protected <B, C> List<List<C>> gridToMatrix(List<CellColumn<B, C>> columns) {
        List<List<C>> matrix = new ArrayList<>(columns.size());
        columns.forEach(column -> matrix.add(column.getValue()));

        return matrix;
    }

    /**
     *  Retrieves all of the column names.
     *
     *  @return The column names in a List
     */
    private List<String> headerToList() {
        List<String> headerList = new ArrayList<>(columns);
        headers.forEach(header -> headerList.add(header.getValue()));
        return headerList;
    }

    protected abstract CellColumn<A, T> createCellColumn();

    protected abstract CellColumn<A, T> createCellColumn(List<A> data);

    private void addColumnHeader(String title) {
        ICell<String, String> header = CellFactory.createColumnHeader(title);
        headers.add(header);
        columnHeader.getChildren().add(header.getNode());
    }

    private void addRowHeader(String title) {
        ICell header = CellFactory.createRowHeader(title);
        rowHeader.getChildren().add(header.getNode());
        rowLabel.add(header);
    }

    private void addRowHeaders(int num) {
        while (num-- > 0)
            addRowHeader(++rows + "");
    }

    private void addGridColumn(List data) {
        CellColumn<A, T> cellColumn = createCellColumn(data);
        addGridColumn(cellColumn);
    }

    private void addGridColumn() {
        addGridColumns(1);
    }

    private void addGridColumn(CellColumn<A, T> cellColumn) {

        grid.add(cellColumn);
        cellPane.getChildren().add(cellColumn.getNode());
        addRowsToColumn(cellColumn, rows-cellColumn.getRows());
    }

    /**
     *  Adds the specified amount of columns.
     *
     *  @param num number of columns to be added
     */
    private void addGridColumns(int num) {
        while (num-- > 0) {
            CellColumn<A, T> cellColumn = createCellColumn();
            addGridColumn(cellColumn);
        }
    }


    protected void addColumn(String title, CellColumn<A, T> column) {
        addColumnHeader(title);
        addGridColumn(column);
        columns++;
    }

    /**
     *  Adds a columns whit the specified name and data.
     *
     *  @param title the name to be used in the Column header
     *
     *  @param data data that the column will contain
     */
    public void addColumn(String title, List<A> data) {
        addColumnHeader(title);
        addGridColumn(data);
        columns++;
    }

    /**
     *  Adds a columns whit the specified name.
     *
     *  @param title the name to be used in the Column header
     *
     */
    public void addColumn(String title) {
        addColumnHeader(title);
        addGridColumn();
        columns++;
    }

    private void addRowsToColumn(CellColumn column, int rows) {

            column.addCells(rows);

    }

    /**
     *  Adds a columns containing the specified data.
     *
     *  @param data data that the column will contain
     */
    public void addColumn(List<A> data) {
        addColumn(getColumnName(columns), data);
    }

    public void addColumn() {
        addColumn(getColumnName(columns));
    }


    /**
     *  Adds the specified amount of columns
     *
     *  @param num number of columns to be added
     */
    public void addColumns(int num) {
        while (num-- > 0)
            addColumn(getColumnName(columns));
    }



    String getColumnName(int col) {
        int a = col / 26;
        int b = (col % 26);

        return Character.toString((char) (65 + b)) + " " + (a);
    }



    /**
     *  Appends a single row to all of the columns
     *
     */
    public void addRow() {
        addRows(1);
    }


    /**
     *  Appends the specified amount of rows to all of the columns
     *
     *  @param rows number of rows to be added
     */
    public void addRows(int rows) {
        grid.forEach(columns -> columns.addCells(rows));
        addRowHeaders(rows);
    }

    /**
     *  Removes the column at the specified position.
     *
     *  @param index the index of the column to be removed
     */
    public void removeColumn(int index) {
        if (index < columns && columns > 0) {
            cellPane.getChildren().remove(grid.get(index).getNode());
            grid.remove(index);
            columnHeader.getChildren().remove(headers.get(index).getNode());
            headers.remove(index);
            columns--;
        }
    }


    /**
     *  Removes the row at the specified position.
     *
     *  @param index the index of the row to be removed
     */
    public void removeRow(int index) {
        grid.forEach(column -> column.removeCell(index));
        rowHeader.getChildren().remove(index);
        rows--;
    }


    private void bindScrollBarsHvalue(ScrollPane observer, ScrollPane listener) {
        observer.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() != oldValue.doubleValue()) {
                listener.setHvalue(newValue.doubleValue());
            }
        });
    }

    private void bindScrollBarsVvalue(ScrollPane observer, ScrollPane listener) {
        observer.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() != oldValue.doubleValue()) {
                listener.setVvalue(newValue.doubleValue());

                // System.out.println(scrollPane.getHeight()-scrollPane.getViewportBounds().getHeight());
            }
        });
    }


    /**
     *  Removes all of the rows and columns.
     */
    public void clear() {
        clearRows();
        clearColumns();
    }

    /**
     *  Removes all of the rows.
     */
    public void clearRows() {

        int temp = getRows();
        while (temp-- > 0) {
            removeRow(0);
        }

        clearColumns();
    }

    /**
     *  Removes all of the columns
     */
    public void clearColumns() {

        int temp = getColumns();
        while (temp-- > 0) {
            removeColumn(0);
        }

    }

    protected <B> List<B> setToAllowedListSize(List<B> list) {
        if (list != null && list.size() > getRows()) {
            list = list.subList(0, rows);
        }
        return list;
    }


    @Override
    public Node getNode() {
        return rootPane;
    }
}
