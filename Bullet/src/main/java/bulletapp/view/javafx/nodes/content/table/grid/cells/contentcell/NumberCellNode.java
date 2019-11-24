package bulletapp.view.javafx.nodes.content.table.grid.cells.contentcell;

import javafx.scene.layout.Pane;

public class NumberCellNode extends FieldCellNode<Number, String> {

    public NumberCellNode(Pane root) {
        super(root);
    }

    public NumberCellNode(Pane root, Number data) {
        super(root, data);
    }

    @Override
    protected void init() {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!(newValue.matches("\\d*") || newValue.contains("-") || newValue.contains("."))) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @Override
    protected String parseData(Number data) {
        try {
            return data + "";
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Number stringToValue(String newValue) {
        newValue = newValue.trim();
        try {
            return Double.parseDouble(newValue);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected String valueToString() {
        if (getValue() == null) {
            return "";
        }
        return getValue() + "";
    }

    public boolean containsValidNumbers() {
        //todo
        return false;
    }

}
