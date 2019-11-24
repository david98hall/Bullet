package bulletapp.view.javafx.nodes.content.table;

import bulletapp.content.ContentType;
import bulletapp.view.javafx.nodes.content.ContentNode;
import bulletapp.view.javafx.nodes.content.table.grid.CellGridFactory;
import bulletapp.view.javafx.nodes.content.table.grid.CellGridNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

public class TableNode extends ContentNode {

    @FXML
    private BorderPane gridArea;


    @FXML
    private Button saveButton;


    private CellGridNode<String,String> grid;

    public TableNode() {
        super("/view/javafx/content/table/table.fxml", new AnchorPane());

        grid = CellGridFactory.createTextCellGridNode();
        grid.addColumns(10);
        grid.addRows(11);
        gridArea.setCenter(grid.getNode());
        gridToString();

    }

    public TableNode(String data) {
        this();
        setContentData(data);
    }

    @Override
    public void setContentData(String data) {
        if(validData(data)){
        super.setContentData(data);
        loadData(data);
        }
    }

    private boolean validData(String data){
        if(data.contains("[") && data.contains("]")){
            return true;
        }
        return false;
    }

    @Override
    public String getData() {

        return gridToString();
    }

    private String gridToString() {
        List<List<String>> list = grid.gridToMatrix();
        StringBuilder builder = new StringBuilder();
        list.forEach(column -> {
            builder.append(column.toString());
        });


        return builder.toString();
    }

    private List<List<String>> stringToGrid(String data) {

        List<List<String>> list = new ArrayList<>();

        data=data+";";

        for (int i = 0;data.contains("[") ; i++) {

            list.add(new ArrayList<>());

            String sub = data.substring(data.indexOf("[" )+ 1, data.indexOf("]"))+ ",";

            while (sub.length()>1){
                String element=sub.substring(0,sub.indexOf(","));
                if(element.contains("null")){
                    element="";
                }
                list.get(i).add(element);

                sub=sub.substring(sub.indexOf(",")+1);
                sub=sub.trim();
            }

            data = data.substring(data.indexOf("]") + 1);
        }

        return list;
    }

    private void loadData(String data) {
       grid.clear();
       grid.addRows(11);
       stringToGrid(data).forEach(column->grid.addColumn(column));

    }

    private void save(){
        setContentData(gridToString());
    }


    @Override
    public ContentType getContentType() {
        return ContentType.TABLE;
    }


    @FXML
    void onSaveButtonActionEvent(ActionEvent event) {
        save();
    }


}
