package bulletapp.view.javafx.nodes.document.breadcrumbs;

import bulletapp.util.bulletpoint.BulletPointZoomHandler;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author David Hall
 */
class Breadcrumb implements INode, Initializable {

    @FXML
    private Label label;

    private final Node rootNode = new Button();

    private String id;

    Breadcrumb(String bulletPointId, String text) {
        id = bulletPointId;
        FXMLUtils.loadFXMLNode("/view/javafx/document/breadcrumbs/breadcrumb.fxml", this, rootNode);
        label.setText(text);
    }

    Breadcrumb(String bulletPointId) {
        this(bulletPointId, "Breadcrumb");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void setText(String text) {
        label.setText(text);
    }

    @Override
    public Node getNode() {
        return rootNode;
    }

    @FXML
    private void onAction(Event event) {
        BulletPointZoomHandler.getInstance().zoomToBulletPoint(id);
    }

}
