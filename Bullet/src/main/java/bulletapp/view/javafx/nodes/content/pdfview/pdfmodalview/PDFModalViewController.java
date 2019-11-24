package bulletapp.view.javafx.nodes.content.pdfview.pdfmodalview;

import bulletapp.view.enums.NavigationTarget;
import bulletapp.view.interfaces.IPrefSizeListener;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

class PDFModalViewController {

    private final PDFModalViewNode node;

    private String filePath;

    private final List<IPrefSizeListener> sizeListeners;

    PDFModalViewController(PDFModalViewNode node) {
        this.node = node;
        sizeListeners = new ArrayList<>(1);
    }

    void addPefSizeListener(IPrefSizeListener prefSizeListener) {
        sizeListeners.add(prefSizeListener);
    }

    private void loadPDF(String filePath) {
        String base64 = fileToBase64(new File(filePath));

        double width = getPDFWidth();
        double height = getPDFHeight();
        node.setPrefSize(width, height);
        OnPrefSizeChanged(width, height);

        node.loadPDF(base64);
    }

    private String fileToBase64(File file) {
        byte[] data = null;
        file = getContentFile();

        try {
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(data);

    }

    private String getFilePath() {
        return filePath;
    }

    private File getContentFile() {
        if (getFilePath() == null || getFilePath() == "") {
            return null;
        }
        return new File(getFilePath());
    }

    private double getPDFHeight() {
        try {
            PDDocument doc = PDDocument.load(getContentFile());
            double height = doc.getPage(0).getMediaBox().getHeight();
            doc.close();
            return height;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double getPDFWidth() {
        try {
            PDDocument doc = PDDocument.load(getContentFile());
            double width = doc.getPage(0).getMediaBox().getWidth();
            doc.close();
            return width;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    String getFileName() {
        if (getContentFile() == null) {
            return "PDF";
        }
        return getContentFile().getName();
    }

    void loadData(String data) {
        if (!dataEquals(data)) {
            filePath = data;
        }
        loadData();
    }

    private void loadData() {
        if (getFilePath() != null && !getFilePath().equals("")) {
            loadPDF(getFilePath());
        }
    }

    boolean dataEquals(String data) {
        return data.equals(filePath);
    }

    private void OnPrefSizeChanged(double width, double height) {
        sizeListeners.forEach(prefSizeListener -> prefSizeListener.OnPrefSizeUpdate(NavigationTarget.PDF_MODAL_VIEW, width, height));
    }


}
