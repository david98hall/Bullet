package bulletapp.view.javafx.nodes.content.table.grid;

import bulletapp.view.javafx.nodes.content.table.grid.column.CellColumn;
import bulletapp.view.javafx.nodes.content.table.grid.column.CellColumnFactory;

import java.util.List;

public class CustomCellGridNode extends CellGridNode<Object,String>  {

    public CustomCellGridNode(){
        super();
    }

    @Override
    protected CellColumn createCellColumn() {
        return CellColumnFactory.createTextCellColumn();
    }

    @Override
    protected CellColumn createCellColumn(List data) {
        setToAllowedListSize(data);
        return CellColumnFactory.createTextCellColumn(data);
    }

    private CellColumn createNumberCellColumn(){
        return CellColumnFactory.createNumberCellColumn();
    }

    private CellColumn createNumberCellColumn(List<Number> data){
        setToAllowedListSize(data);
        return CellColumnFactory.createNumberCellColumn(data);
    }

    public void addNumberColumn(String title){
        addColumn(title,createNumberCellColumn());
    }
    public void addNumberColumn(){
        addNumberColumn(getColumnName(getColumns()));
    }
    public void addNumberColumn(List<Number> data){
        addColumn(getColumnName(getColumns()),createNumberCellColumn(data));
    }
    public void addNumberColumn(String title ,List<Number> data){
        addColumn(title,createNumberCellColumn(data));
    }



    public void addTextColumn(String title){
        addColumn(title,createCellColumn());
    }
    public void addTextColumn(){
        addTextColumn(getColumnName(getColumns()));
    }
    public void addTextColumn(List<String> data){
        addColumn(getColumnName(getColumns()),createCellColumn(data));
    }
    public void addTextColumn(String title ,List<String> data){
        addColumn(title,createCellColumn(data));
    }



}
