package bulletapp.view.javafx.nodes.content.video;

import bulletapp.content.ContentType;
import bulletapp.view.javafx.nodes.content.ContentNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

public class VideoPlayerNode extends ContentNode {

    private String url;

    @FXML
    private WebView webview;

    @FXML
    private TextField textField;

    public VideoPlayerNode(){
        super("/view/javafx/content/videoplayer/videoplayer.fxml",new AnchorPane());


    }

    public VideoPlayerNode(String data){
        this();
        setContentData(data);

    }

    private String getEmbeddedYouTubeUrl(String url){
        try {
            return   url.replace("/watch?v=","/embed/");
        }catch (Exception e){
            return url;
        }

    }


    @Override
    public String getData() {
        return url;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.VIDEO;
    }

    private void loadYoutubeURL(){
        webview.getEngine().load(getEmbeddedYouTubeUrl(url));
    }

    @FXML
    void onLoadButtonActionEvent(ActionEvent event) {
        setContentData(textField.getText());
    }

    @Override
    public void setContentData(String data){
        super.setContentData(data);
        textField.setText(data);
        url=data;
        loadYoutubeURL();

    }
}
