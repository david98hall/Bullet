package bulletapp.view.javafx.nodes.content.latex;

import bulletapp.content.ContentType;
import bulletapp.view.javafx.nodes.content.ContentNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * @author Carl Holmberg
 * Renders LaTeX math expressions and displays them as images
 */

public class LatexNode extends ContentNode {

    @FXML
    private AnchorPane viewerPane, editorPane;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView latexImage;

    @FXML
    private StackPane stackPane;

    @FXML
    private StackPane latexPane;

    @FXML
    private Button editButton, viewButton, saveButton;

    @FXML
    private TextField textEditor;

    private String texString;

    private IMathParser LatexParser;

    public LatexNode() {
        super("/view/javafx/content/latexblock.fxml", new AnchorPane());
    }

    public LatexNode(String data) {
        this();
        setContentData(data);
        LatexParser = new TexParser(texString);
        initLatexArea();
        initTextArea();
        toggleStackPane();

    }

    /**
     * Preloads the text editor with content loaded from file or set at creation
     */
    private void initTextArea() {
        textEditor.setText(texString);
    }

    /**
     * Loads an image from the parser and displays it in the ImageView
     */
    private void initLatexArea() {
        Image fxImage = LatexParser.getFxImage(100);
        latexImage.setImage(fxImage);
        int x = 0;
    }

    /**
     * Refreshes the expression in the parser with content from the texString variable and displays the new image
     */
    private void parseLatex() {
        LatexParser.setExpression(texString);
        initLatexArea();
    }

    @Override
    public ContentType getContentType() {
        return ContentType.LATEX;
    }

    @Override
    public void setContentData(String data) {
        super.setContentData(data);
        texString = data;
    }

    @Override
    public String getData() {
        return texString;
    }

    /**
     * Reads an expression from the text area and parses the new input
     */
    @FXML
    public void getNewExpression() {
        setContentData(textEditor.getText());
        parseLatex();
    }

    /**
     * Switches between edit mode and display mode
     */
    @FXML
    private void toggleStackPane() {
        stackPane.getChildren().get(1).toBack();
    }



}
