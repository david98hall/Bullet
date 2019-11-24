package bulletapp.services;

import bulletapp.util.file.FileUtils;
import bulletapp.util.folder.Folder;

import java.io.File;

/**
 * @author David Hall
 */
public final class DocumentLoaderFactory {


    public static IDocumentLoader getFileDocumentParser(String filePath) throws IllegalArgumentException {

        IDocumentLoader parser = null;

        if (new File(filePath).isDirectory()) {
            Folder bulletDocumentFolder = new Folder(filePath);
            for (File file : bulletDocumentFolder.getLocalFiles()) {
                if (FileUtils.getExtension(file).equals(".xml")) parser = new XMLDocumentLoader(file.getAbsolutePath());
            }
        }

        if (filePath.endsWith(".xml")) {
            parser = new XMLDocumentLoader(filePath);
        }

        if (parser != null && parser.isDocumentCompatible()) {
            return parser;
        }

        throw new IllegalArgumentException("The file is not compatible with Bullet");
    }

}
