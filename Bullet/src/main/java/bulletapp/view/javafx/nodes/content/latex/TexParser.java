package bulletapp.view.javafx.nodes.content.latex;

import java.awt.*;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import static bulletapp.view.javafx.utils.image.ImageUtils.ImageToBufferedImage;

/**
 * @author Carl Holmberg
 * Parses expressions in LaTeX syntax to images (swing or JavaFX)
 */
class TexParser implements IMathParser {
    private TeXFormula formula;
    private Image texImage;
    private javafx.scene.image.Image texFxImage;
    private BufferedImage bufferedTexImage;

    private int currentWidth = 10;

    TexParser(String formula) {
        setExpression(formula);
        parseExpression();
    }

    /**
     * Renders new images from the saved expression
     */
    private void parseExpression() {
        if (formula == null) return;

        texImage = formula.createBufferedImage(TeXConstants.STYLE_DISPLAY, currentWidth, new Color(0, 0, 0),  new Color(255, 255, 255) );
        bufferedTexImage = ImageToBufferedImage(texImage);
        texFxImage = SwingFXUtils.toFXImage(ImageToBufferedImage(texImage), new WritableImage(texImage.getWidth(null), texImage.getHeight(null)));

    }

    /**
     * Sets a new expression and parses the formula
     * @param expression The maths expression in LaTeX syntax
     */
    @Override
    public void setExpression(String expression) {
        if (formula == null) {
            formula = new TeXFormula(expression);
        }
        else {
            formula.setLaTeX(expression);
        }
        parseExpression();
    }

    @Override
    public BufferedImage getBufferedImage(int width) {
        if (width != currentWidth) {
            currentWidth = width;
            parseExpression();
        }

        return bufferedTexImage;
    }

    @Override
    public Image getImage(int width) {
        if (width != currentWidth) {
            currentWidth = width;
            parseExpression();
        }

        return texImage;
    }

    @Override
    public javafx.scene.image.Image getFxImage(int width) {
        if (width != currentWidth) {
            currentWidth = width;
            parseExpression();
        }

        return texFxImage;
    }
}
