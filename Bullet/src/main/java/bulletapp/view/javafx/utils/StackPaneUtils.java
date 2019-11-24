package bulletapp.view.javafx.utils;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * @author David Hall
 */
public final class StackPaneUtils {

    public static void switchView(Node view, StackPane stackPane) {
        stackPane.getChildren().forEach(node -> {
            if (node != view) {
                node.setDisable(true);
            }
        });
        view.setDisable(false);
        view.toFront();
    }

}
