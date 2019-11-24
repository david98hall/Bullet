package bulletapp.view.javafx.nodes.content.code;

import bulletapp.view.syntax.ISyntaxHighlight;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author David Hall
 */
class JavaHighlightFX implements ISyntaxHighlight {

    private static final String[] keywords = new String[]{
            "abstract", "assert",
            "boolean", "break", "byte",
            "case", "catch", "char", "class", "continue",
            "default", "do", "double",
            "else", "enum", "extends",
            "false", "final", "finally", "float", "for",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long",
            "native", "new", "null",
            "package", "private", "protected", "public",
            "return",
            "short", "static", "strictfp", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "true", "try",
            "void", "volatile", "while",

    };

    private static final String keywordPattern = "\\b(" + String.join("|", keywords) + ")\\b";
    private static final String parenPattern = "\\(|\\)";
    private static final String bracePattern = "\\{|\\}";
    private static final String bracketPattern = "\\[|\\]";
    private static final String semicolonPattern = "\\;";
    private static final String stringPattern = "\"([^\"]|\\\")*\"";

    private final Pattern pattern = Pattern.compile("(?<KEYWORD>" + keywordPattern + ")"
            + "|(?<PAREN>" + parenPattern + ")"
            + "|(?<BRACE>" + bracePattern + ")"
            + "|(?<BRACKET>" + bracketPattern + ")"
            + "|(?<SEMICOLON>" + semicolonPattern + ")"
            + "|(?<STRING>" + stringPattern + ")"
    );

    @Override
    public StyleSpans<Collection<String>> getHighlightingFor(String text) {

        Matcher matcher = pattern.matcher(text);

        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        int endOfLastKeyword = 0;
        while (matcher.find()) {

            String styleClass = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("STRING") != null ? "string" : null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - endOfLastKeyword);

            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());

            endOfLastKeyword = matcher.end();

        }

        spansBuilder.add(Collections.emptyList(), text.length() - endOfLastKeyword);

        return spansBuilder.create();
    }

    @Override
    public URL getHighlightingStylesheetURL() {
        return getClass().getResource("/view/javafx/css/syntax/highlight/java-highlighting-light.css");
    }

}