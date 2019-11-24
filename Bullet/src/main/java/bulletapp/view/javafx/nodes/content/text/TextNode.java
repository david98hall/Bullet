package bulletapp.view.javafx.nodes.content.text;

import bulletapp.content.ContentType;
import bulletapp.util.tree.Tree;
import bulletapp.view.javafx.nodes.content.ContentNode;
import bulletapp.view.javafx.utils.BulletPointEventBus;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

/**
 * A simple text-editor node
 *
 * @author David Hall
 */
public class TextNode extends ContentNode {

    @FXML
    private TextArea textArea;

    private boolean enterWasPressed;

    public TextNode() {
        this("");
    }

    public TextNode(String data) {
        super(data, "/view/javafx/content/textcontent.fxml", new AnchorPane());
        initTextArea();
    }

    private void initTextArea() {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (enterWasPressed) {
                enterWasPressed = false;
                textArea.setText(oldValue);
            } else {
                super.setContentData(newValue);
            }
        });
        textArea.setOnKeyPressed(event -> {

            KeyCode keyCode = event.getCode();

            switch (keyCode) {

                case BACK_SPACE:
                    if (!textArea.getText().isEmpty()) return;

                case ENTER:
                    enterWasPressed = true;
                    break;

            }

            notifyKeyPressedListeners(keyCode);

        });
    }

    private void notifyKeyPressedListeners(KeyCode keyCode) {
        BulletPointEventBus.getInstance().notifyKeyPressedListeners(getContainerId(), keyCode);
    }

    @Override
    public void setContentData(String data) {
        super.setContentData(data);
        textArea.setText(data);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.TEXT;
    }

    @Override
    public String getData() {
        return textArea.getText();
    }

}
