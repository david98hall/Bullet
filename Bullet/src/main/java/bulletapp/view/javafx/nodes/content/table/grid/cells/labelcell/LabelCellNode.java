package bulletapp.view.javafx.nodes.content.table.grid.cells.labelcell;


import bulletapp.view.javafx.nodes.content.table.grid.interfaces.ICell;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class LabelCellNode implements ICell<String,String>, Initializable {

    private final Pane rootPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label label;


    private String data;

    public LabelCellNode(Pane pane) {
        this(pane, "E");
    }

    public LabelCellNode(Pane pane, String label) {
        rootPane = pane;

        data = label;
        FXMLUtils.loadFXMLNode("/view/javafx/content/table/grid/cell/headercell.fxml", this, rootPane);
    }

    @Override
    public void setValue(String newValue) {
        label.setText(newValue);
    }

    @Override
    public String getValue() {
        return label.getText();
    }

    @Override
    public Node getNode() {
        return rootPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.label.setText(data);
    }
}
