package bulletapp.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Carl Holmberg
 */
public class CompressionUtils {
    public static void zipDirectory(File source, String targetPath) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetPath));
        addDirToZip(source, out);
        out.close();
    }

    private static void addDirToZip(File dir, ZipOutputStream out) throws IOException {
        File[] files = dir.listFiles();
        byte[] tmpBuf = new byte[1024];

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addDirToZip(file, out);
                    continue;
                }
                FileInputStream in = new FileInputStream(file.getAbsolutePath());
                out.putNextEntry(new ZipEntry(file.getPath()));
                int len;
                while ((len = in.read(tmpBuf)) > 0) {
                    out.write(tmpBuf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
    }

    static boolean isValid(final File file) {
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(file);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            // Todo: Handle exception
            }
        }
    }
}
