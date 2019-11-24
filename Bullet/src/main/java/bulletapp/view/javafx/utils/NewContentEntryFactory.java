package bulletapp.view.javafx.utils;

import bulletapp.content.ContentType;
import bulletapp.view.javafx.scenes.workspace.Workspace;
import javafx.scene.control.MenuItem;

public class NewContentEntryFactory {

    public static MenuItem createMenuItem(ContentType contentType) {
        switch (contentType) {
            case VIDEO:
                return new MenuItem("Video");
            case IMAGE:
                return new MenuItem("Image");
            case DIAGRAM:
                return new MenuItem("Diagram");
            case PDF:
                return new MenuItem("PDF");
            case LATEX:
                return new MenuItem("Math");
            case CODE:
                return new MenuItem("Code");
            case TABLE:
                return new MenuItem("Table");
        }

        return null;
    }


}
