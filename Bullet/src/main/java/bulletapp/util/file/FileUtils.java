package bulletapp.util.file;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author Carl Holmberg
 */

public class FileUtils {

    public static File createSubDirectory(String directory, String subDirName) {
        File dir = createDirectory(directory + "/" + subDirName);
        return dir;
    }

    public static File createSubDirectory(File parent, String subDirName) throws IOException {
        File subDir = new File(parent.getCanonicalPath() + "/" + subDirName);
        subDir.mkdir();
        return subDir;
    }

    static String[] getSubDirectories(String directory) {
        File file = new File(directory);

        return file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public static File createDirectory(String path) {
        File dir = new File(path);
        boolean success = dir.mkdirs();
        return dir;
    }

    public static File changeExtension(File f, String newExtension) {
        int i = f.getName().lastIndexOf('.');
        String name = f.getName().substring(0, i);
        f.renameTo(new File(f.getParent() + "/" + name + "." + newExtension));
        return f;
        //return new File(f.getParent() + "/" + name + "." + newExtension);
    }

    public static String getExtension(File f) {
        int i = f.getName().lastIndexOf('.');
        return f.getName().substring(i);
    }

    public static String stripExtension(String path) {
        File file = new File(path);
        return path.replace(getExtension(file), "");
    }

    public static boolean isValidFile(String path) {
        File file = new File(path);

        return file.exists() && !file.isDirectory();
    }

    public static File getDirectory(File file) {
        if (file.exists() && file.isDirectory()) return file;
        else return file.getParentFile();
    }

    public static String nameWithoutExtension(File f) {
        int i = f.getName().lastIndexOf('.');
        return f.getName().substring(0, i);
    }

    // Todo: Throw exception up the stack
    public static void overwriteFile(File file, String data) {
        try {
            Writer writer = new FileWriter(file.getCanonicalFile());
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            // Todo: Handle exception
            e.printStackTrace();
        }
    }

    public static void copyFile(File source, File destination) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(destination).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileMatchesOneExtension(File file, String... extensions) {

        String fileExtension = FileUtils.getExtension(file);

        for (String extension : extensions) {
            if (fileExtension.equals(extension)) {
                return true;
            }
        }

        return false;
    }

    public static String getParentDirectoryPath(String filePath) {
        return getParentDirectory(new File(filePath)).getAbsolutePath();
    }

    public static File getParentDirectory(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            return file.getParentFile();
        }
        return null;
    }

    public static File getParentDirectory(String filePath) {
        return getParentDirectory(new File(filePath));
    }

}
