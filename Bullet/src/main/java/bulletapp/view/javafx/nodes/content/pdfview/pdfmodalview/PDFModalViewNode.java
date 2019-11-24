package bulletapp.view.javafx.nodes.content.pdfview.pdfmodalview;

import bulletapp.view.interfaces.IPrefSizeListener;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class PDFModalViewNode implements Initializable, INode {

    private final PDFModalViewController controller;

    @FXML
    private final AnchorPane rootPane;

    @FXML
    private WebView webView;

    private static PDFModalViewNode instance;

    private PDFModalViewNode() {
        controller = new PDFModalViewController(this);
        rootPane = new AnchorPane();
        FXMLUtils.loadFXMLNode("/view/javafx/modal/panes/pdf_pane.fxml", this, rootPane);
    }

    public static PDFModalViewNode getInstance() {
        if (instance == null) {
            instance = new PDFModalViewNode();
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String url = getClass().getResource("/pdf/web/viewer.html").toExternalForm();
        webView.getEngine().setJavaScriptEnabled(true);
        webView.getEngine().load(url);

    }

    void loadPDF(String base64) {

        if (Worker.State.SUCCEEDED == webView.getEngine().getLoadWorker().getState()) {
            webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");

        } else {
            webView.getEngine().getLoadWorker().stateProperty().addListener((ChangeListener<? super Worker.State>) (ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");

                }
            });
        }
    }

    void setPrefSize(double width, double height) {
        rootPane.setPrefSize(width, height);
    }

    public String getTitle() {
        return controller.getFileName();
    }

    public void loadData(String data) {
        controller.loadData(data);
    }

    @Override
    public Node getNode() {
        return rootPane;
    }

    public void addPrefSizeListener(IPrefSizeListener prefSizeListener) {
        controller.addPefSizeListener(prefSizeListener);
    }
}
