package bulletapp.view.javafx.nodes.content.code;

import bulletapp.view.syntax.ISyntaxHighlight;
import bulletapp.view.syntax.SyntaxType;

/**
 * @author David Hall
 */
final class SyntaxHighlightFactoryFX {

    static ISyntaxHighlight createSyntaxHighlight(SyntaxType type) {

        switch (type) {

            case JAVA:
                return new JavaHighlightFX();

        }

        return null;
    }

}
