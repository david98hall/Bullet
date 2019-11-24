package bulletapp.view.javafx.nodes.content.diagram.tab;

import bulletapp.util.removable.IRemovable;
import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.view.javafx.nodes.content.diagram.DiagramViews;
import bulletapp.view.javafx.nodes.content.diagram.interfaces.IDiagramNavigationListener;
import bulletapp.view.javafx.nodes.content.diagram.tab.interfaces.ITabListener;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

public class TabNode implements INode, IRemovable, Initializable, IDiagramNavigationListener {

    private final TabController controller;
    private final Pane root = new AnchorPane();
    private final List<IRemoveRequestListener<TabNode>> removeListener = new ArrayList<>(1);


    @FXML
    private TextField label;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private VBox tableAnchorPane;

    private final Tab tab;

    private AbstractMap.SimpleEntry<List<List<String>>, List<List<Number>>> simpleEntry;

    public TabNode(String title, Tab tab, List<List<String>> stringData, List<List<Number>> numberData) {
        loadFXML("/view/javafx/content/diagram/tab.fxml");
        controller = new TabController(stringData, numberData, this);
        this.tab = tab;

        tab.setText(title);

        label.setText(title);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        label.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                tab.setText(label.getText());
            }
        });
    }

    private void loadFXML(String FXMLPath) {
        FXMLUtils.loadFXMLNode(FXMLPath, this, root);
    }

    public void addRemoveListener(IRemoveRequestListener<TabNode> listener) {
        removeListener.add(listener);
    }

    public void addITabListener(ITabListener listener) {
        controller.addITabListener(listener);
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void requestRemoval() {
        removeListener.forEach(listener -> listener.onRemovalRequest(this));
        removeListener.clear();
        controller.clearITabListener();
    }

    public Color getColor() {
        return colorPicker.getValue();
    }


    @FXML
    void Remove(ActionEvent event) {
        requestRemoval();
    }

    public void setData(AbstractMap.SimpleEntry<List<List<String>>, List<List<Number>>> simpleEntry) {
        controller.addNewData(simpleEntry.getKey(), simpleEntry.getValue());
    }

    void addTable(Node node) {
        node.prefHeight(tableAnchorPane.getHeight());
        tableAnchorPane.getChildren().add(node);
    }

    public Tab getTab() {
        return tab;
    }

    @Override
    public void navigateTo(DiagramViews to) {
        if (to == DiagramViews.DIAGRAM_VIEW) {
            controller.notifyListeners();
        }
    }

    public String getName() {
        return label.getText();
    }

    public void setName(String name) {
        label.setText(name);
        tab.setText(name);
    }

    public List<List<String>> getData() {
        return controller.getData();
    }

    public void removeRemoveListener(IRemoveRequestListener<TabNode> listener) {
        removeListener.remove(listener);
    }

    public void removeITabListener(ITabListener listener) {
        controller.removeITabListener(listener);
    }
}
