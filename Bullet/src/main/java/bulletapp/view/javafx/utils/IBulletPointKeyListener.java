package bulletapp.view.javafx.utils;

import javafx.scene.input.KeyCode;

/**
 * @author David Hall
 */
public interface IBulletPointKeyListener {

    void onKeyPressed(String bulletPointId, KeyCode keyCode);

}
