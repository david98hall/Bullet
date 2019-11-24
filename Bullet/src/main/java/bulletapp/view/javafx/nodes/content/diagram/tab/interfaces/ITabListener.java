package bulletapp.view.javafx.nodes.content.diagram.tab.interfaces;

import bulletapp.view.javafx.nodes.content.diagram.tab.TabNode;
import javafx.scene.control.Tab;

import java.util.List;

public interface ITabListener {
    void onChange(List<List<String>> table,TabNode tab);
}
