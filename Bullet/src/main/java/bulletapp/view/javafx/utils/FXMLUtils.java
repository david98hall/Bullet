package bulletapp.view.javafx.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

/**
 * @author David Hall
 */
public final class FXMLUtils {

    public static Node loadFXMLNode(String fxmlURL, Object controller, Object root) {
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLUtils.class.getResource(fxmlURL));
        fxmlLoader.setRoot(root);
        fxmlLoader.setController(controller);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
