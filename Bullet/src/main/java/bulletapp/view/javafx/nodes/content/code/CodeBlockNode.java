package bulletapp.view.javafx.nodes.content.code;


import bulletapp.content.ContentType;
import bulletapp.view.javafx.nodes.content.ContentNode;
import bulletapp.view.syntax.ISyntaxHighlight;
import bulletapp.view.syntax.SyntaxType;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.function.IntFunction;

/**
 * @author David Hall
 */
public class CodeBlockNode extends ContentNode {

    @FXML
    private AnchorPane childPane;

    private CodeArea codeArea;
    private ISyntaxHighlight syntaxHighlight;

    @Getter
    private SyntaxType syntaxType;

    public CodeBlockNode() {
        super("/view/javafx/content/codeblock.fxml", new AnchorPane());
        initCodeArea();
    }

    public CodeBlockNode(String data) {
        this();
        setContentData(data);
    }

    private void initCodeArea() {

        codeArea = new CodeArea();

        childPane.getChildren().add(codeArea);
        AnchorPane.setTopAnchor(codeArea, 0d);
        AnchorPane.setBottomAnchor(codeArea, 0d);
        AnchorPane.setLeftAnchor(codeArea, 0d);
        AnchorPane.setRightAnchor(codeArea, 0d);

        // Paragraph numbering
        IntFunction<String> format = (digits -> " %" + digits + "d ");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea, format));

        codeArea.textProperty().addListener((observable, oldValue, newValue) -> {

            // Update the syntax highlight when the text is changed
            codeArea.setStyleSpans(0, syntaxHighlight.getHighlightingFor(newValue));
            super.setContentData(getData());

        });

        setSyntaxType(SyntaxType.JAVA);

    }

    private void updateLook() {
        codeArea.getStylesheets().clear();
        codeArea.getStylesheets().add(syntaxHighlight.getHighlightingStylesheetURL().toExternalForm());
    }

    public void setSyntaxType(SyntaxType syntaxType) {
        this.syntaxType = syntaxType;
        syntaxHighlight = SyntaxHighlightFactoryFX.createSyntaxHighlight(syntaxType);
        updateLook();
    }

    @Override
    public ContentType getContentType() {
        return ContentType.CODE;
    }

    @Override
    public void setContentData(String data) {
        super.setContentData(data);
        int indexOfFirstColon = data.indexOf(":");
        SyntaxType syntaxType = SyntaxType.valueOf(data.substring(0, indexOfFirstColon));
        setSyntaxType(syntaxType);
        if (data.length() > indexOfFirstColon) {
            String codeString = data.substring(indexOfFirstColon + 1);
            codeArea.replaceText(codeString);
            codeArea.setStyleSpans(0, syntaxHighlight.getHighlightingFor(codeString));
        }
    }

    @Override
    public String getData() {
        return syntaxType + ":" + codeArea.getText();
    }
}
