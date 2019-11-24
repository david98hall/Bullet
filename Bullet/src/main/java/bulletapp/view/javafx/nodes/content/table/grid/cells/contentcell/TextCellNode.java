package bulletapp.view.javafx.nodes.content.table.grid.cells.contentcell;

import javafx.scene.layout.Pane;

public class TextCellNode extends FieldCellNode<String, String> {
    public TextCellNode(Pane root) {
        super(root);
    }

    public TextCellNode(Pane root, String data) {
        super(root, data);
    }

    @Override
    protected void init() {

    }

    @Override
    protected String parseData(String data) {
        return data;
    }

    @Override
    protected String stringToValue(String newValue) {
        return newValue;
    }

    @Override
    protected String valueToString() {
        return getValue();
    }
}
