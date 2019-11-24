package bulletapp.view.javafx.nodes.content.pdfview;

import bulletapp.content.ContentType;
import bulletapp.view.javafx.nodes.content.ContentNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PDFViewNode extends ContentNode {

    private final PDFViewController controller;

    @FXML
    private Label PDFLabel;

    @FXML
    private Button chooseButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button openButton;

    public PDFViewNode() {
        super("/view/javafx/content/pdfviewer.fxml", new AnchorPane());
        controller = new PDFViewController(this);
    }

    public PDFViewNode(String data) {
        this();
        controller.loadPdf(data);
    }

    @FXML
    void viewButtonOnAction(ActionEvent event) {
        controller.viewButtonOnAction();
    }

    @FXML
    void chooseButtonOnAction(ActionEvent event) {
        controller.chooseButtonOnAction();
    }

    @FXML
    void openButtonOnAction(ActionEvent event) {
        controller.openButtonOnAction();
    }


    void updateTitle(String title) {
        PDFLabel.setText(title);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.PDF;
    }


    @Override
    public String getData() {
        return controller.getData();
    }

}
