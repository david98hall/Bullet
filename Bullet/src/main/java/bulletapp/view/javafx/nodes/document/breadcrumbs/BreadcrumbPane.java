package bulletapp.view.javafx.nodes.document.breadcrumbs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author David Hall
 */
public class BreadcrumbPane implements Initializable {

    @FXML
    private HBox container;

    private final List<Breadcrumb> breadcrumbs;

    public BreadcrumbPane() {
        breadcrumbs = new ArrayList<>(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addBreadcrumb(String bulletPointId, String text) {
        addBreadcrumb(bulletPointId, text, container.getChildren().size());
    }

    public void addBreadcrumb(String bulletPointId, String text, int index) {
        Breadcrumb breadcrumb = new Breadcrumb(bulletPointId, text);
        breadcrumbs.add(index, breadcrumb);
        container.getChildren().add(breadcrumb.getNode());
    }

    public void removeBreadcrumb(int index) {
        breadcrumbs.remove(index);
        container.getChildren().remove(index);
    }

    public void changeBreadcrumbText(int index, String newText) {
        breadcrumbs.get(index).setText(newText);
    }

    public void clear() {
        container.getChildren().clear();
        breadcrumbs.clear();
    }

}
