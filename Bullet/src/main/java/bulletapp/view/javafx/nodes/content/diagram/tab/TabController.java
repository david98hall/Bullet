package bulletapp.view.javafx.nodes.content.diagram.tab;

import bulletapp.view.javafx.nodes.content.diagram.tab.interfaces.ITabListener;
import bulletapp.view.javafx.nodes.content.table.grid.CellGridFactory;
import bulletapp.view.javafx.nodes.content.table.grid.CustomCellGridNode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class TabController {

    private final TabNode view;

    protected final CustomCellGridNode gridNode;

    private final List<ITabListener> gridListeners;


    private TabController(TabNode diagramNode) {

        gridNode = CellGridFactory.createCustomCellGridNode();
        view = diagramNode;
        view.addTable(gridNode.getNode());
        gridListeners = new ArrayList<>();


    }

    protected TabController(List<List<String>> data, List<List<Number>> value, TabNode diagramNode) {
        this(diagramNode);
        addNewData(data, value);
    }


    private void addData(List<List<String>> stringData, List<List<Number>> numberData) {
        addRows();
        stringData.forEach(gridNode::addTextColumn);
        numberData.forEach(gridNode::addNumberColumn);
    }

    private void addRows() {
        gridNode.addRows(10);
    }

    void addNewData(List<List<String>> stringdata, List<List<Number>> numberdata) {
        gridNode.clear();
        addData(stringdata, numberdata);
    }

    void notifyListeners() {
        List<List<String>> matrix = getData();

        gridListeners.forEach(listener -> listener.onChange(matrix, view));

    }

    List<List<String>> getData() {
        return gridNode.gridToMatrix();
    }


    void addITabListener(ITabListener listener) {
        gridListeners.add(listener);
    }

    void removeITabListener(ITabListener listener) {
        gridListeners.remove(listener);
    }

    void clearITabListener() {
        gridListeners.clear();
    }


    private void setColor(Color color) {

        //todo
        // series.getChart().applyCss();

        // Node node= series.getNode();


        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        String style = "-fx-stroke: rgba(" + rgb + ", 1.0);" + " -fx-background-color: blue, white; ";
        //  node.setStyle(style);

    }

}
