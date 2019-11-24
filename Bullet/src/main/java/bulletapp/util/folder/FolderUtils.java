package bulletapp.util.folder;

import java.io.File;

/**
 * @author David Hall, Carl Holmberg
 */
public final class FolderUtils {

    public static Folder createFolder(Folder parentFolder, String folderName) {
        File folderFile = new File(parentFolder.getFolderFile(), folderName);
        makeDirectory(folderFile);
        return new Folder(folderFile);
    }

    public static Folder createFolderInUserHome(String folderName) {
        return createFolder(new Folder(System.getProperty("user.home")), folderName);
    }

    private static void makeDirectory(File file) {
        boolean folderExists = file.exists() && file.isDirectory();
        if (!folderExists) {
            file.mkdir();
        }
    }

}
