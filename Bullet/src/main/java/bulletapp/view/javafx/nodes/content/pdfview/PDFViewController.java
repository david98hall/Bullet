package bulletapp.view.javafx.nodes.content.pdfview;

import bulletapp.content.ContentType;
import bulletapp.controller.BulletController;
import bulletapp.view.NavigationHandler;
import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.javafx.nodes.content.pdfview.pdfmodalview.PDFModalViewNode;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;

class PDFViewController {

    private final PDFViewNode node;

    private final PDFModalViewNode modalNode;

    private final static BulletController bulletController = BulletController.getInstance();

    private String path;

    PDFViewController(PDFViewNode node) {
        this.node = node;
        modalNode = PDFModalViewNode.getInstance();
        path = "";
    }


    void viewButtonOnAction() {
        //todo: open modal panel
        NavigationHandler.navigateTo(NavigationTarget.PDF_MODAL_VIEW);
        modalNode.loadData(getData());


    }

    void chooseButtonOnAction() {
        File file = openFileChooser();
        if (file != null) {
            loadPDF(file);
        }
    }

    void openButtonOnAction() {
        Desktop desktop = Desktop.getDesktop();
        String path = getData();
        File file = new File(path);
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose file");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("pdf Files", "*.pdf"));

        return fileChooser.showOpenDialog(node.getNode().getScene().getWindow());

    }

    private void loadPDF(File file) {

        path = file.getPath();
        node.setContentData(file.getPath());
        node.updateTitle(file.getName());

    }

    void loadPdf(String data) {
        if (!data.equals(path)) {
            try {
                File file = new File(data);
                if (file.exists()) {
                    loadPDF(file);
                }
            } catch (Exception e) {
            }
        }
    }


    public ContentType getContentType() {
        return ContentType.PDF;
    }


    public String getData() {
        return path;
    }

}
