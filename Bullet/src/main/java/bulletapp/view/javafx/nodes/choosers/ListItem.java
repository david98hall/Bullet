package bulletapp.view.javafx.nodes.choosers;

import bulletapp.util.removable.IRemovable;
import bulletapp.util.selection.ISelectionListener;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author David Hall
 */
public abstract class ListItem implements IRemovable, INode {

    @FXML
    private ImageView icon;

    @FXML
    private Label nameLabel;

    private final Node rootNode;

    @Getter
    private boolean isRemovable = true;

    public ListItem(String name, String fxmlFilePath, Node rootNode) {
        super();
        this.rootNode = rootNode;
        FXMLUtils.loadFXMLNode(fxmlFilePath, this, rootNode);
        nameLabel.setText(name);
    }

    protected void setItemName(String name) {
        nameLabel.setText(name);
    }

    public String getItemName() {
        return nameLabel.getText();
    }

    @Override
    public Node getNode() {
        return rootNode;
    }

    @FXML
    protected void onMouseEntered(Event event) {
        highlight(true);
    }

    @FXML
    protected void onMouseExited(Event event) {
        highlight(false);
    }

    @FXML
    protected void removeButtonOnAction(Event event) {
        if (isRemovable) {
            requestRemoval();
        } else {
            chooseButtonOnAction(event);
        }
    }

    @FXML
    protected abstract void chooseButtonOnAction(Event event);

    protected void highlight(boolean b) {
        if (b) {
            rootNode.setStyle("-fx-background-color: #d8d8d8");
        } else {
            rootNode.setStyle("-fx-background-color: white");
        }
    }

    public void setRemovable(boolean removable) {
        isRemovable = removable;
    }

    protected void setIcon(Image icon) {
        this.icon.setImage(icon);
    }

}
