package bulletapp.view.javafx.nodes.content.latex;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An interface for parsing math expressions to rendered images
 */
public interface IMathParser {
    void setExpression(String expression);

    BufferedImage getBufferedImage(int width);
    Image getImage(int width);
    javafx.scene.image.Image getFxImage(int width);

}
