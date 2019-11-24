package bulletapp.view.javafx.nodes.content;

import bulletapp.content.ContentType;
import bulletapp.view.javafx.nodes.content.video.VideoPlayerNode;
import bulletapp.view.javafx.nodes.content.code.CodeBlockNode;
import bulletapp.view.javafx.nodes.content.diagram.DiagramNode;
import bulletapp.view.javafx.nodes.content.latex.LatexNode;
import bulletapp.view.javafx.nodes.content.pdfview.PDFViewNode;
import bulletapp.view.javafx.nodes.content.table.TableNode;
import bulletapp.view.javafx.nodes.content.text.TextNode;

/**
 * Creates ContentNodes.
 *
 * @author David Hall
 */
public final class ContentNodeFactory {

    /**
     * Creates a new ContentNode of a specified type and sets the initial content data.
     *
     * @param contentType The type of content.
     * @param data        The content data.
     * @return The created ContentNode.
     */
    public static ContentNode createContentNode(ContentType contentType, String data) {

        switch (contentType) {

            case DIAGRAM:
                return new DiagramNode(data);

            case CODE:
                return new CodeBlockNode(data);

            case LATEX:
                return new LatexNode(data);

            case PDF:
                return new PDFViewNode(data);

            case TEXT:
                return new TextNode(data);

            case VIDEO:
                return new VideoPlayerNode(data);

            case TABLE:
                return new TableNode(data);

            // TODO Add more cases

        }

        return null;
    }

    /**
     * Creates a new ContentNode of a specified type.
     *
     * @param contentType The type of content.
     * @return The created ContentNode.
     */
    public static ContentNode createContentNode(ContentType contentType) {
        return createContentNode(contentType, "");
    }

}
