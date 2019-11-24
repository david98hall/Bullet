package bulletapp.view.syntax;

import org.fxmisc.richtext.model.StyleSpans;

import java.net.URL;
import java.util.Collection;

/**
 * @author David Hall
 */
public interface ISyntaxHighlight {

    StyleSpans<Collection<String>> getHighlightingFor(String text);

    URL getHighlightingStylesheetURL();

}
