package bulletapp.view.interfaces;

import bulletapp.content.ContentType;

/**
 * @author David Hall
 */
public interface IContentView {

    ContentType getContentType();

    void setContentData(String data);

}
