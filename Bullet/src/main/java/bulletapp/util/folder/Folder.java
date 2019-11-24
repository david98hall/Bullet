package bulletapp.util.folder;

import bulletapp.util.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author David Hall, Carl Holmberg
 */
public class Folder {

    private final File folder;

    public Folder(String url) {
        folder = new File(url);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("The given URL does not lead to a directory.");
        }
    }

    public Folder(File directoryFile) {
        this(directoryFile.getAbsolutePath());
    }

    public Folder(Folder otherFolder) {
        this(otherFolder.folder);
    }

    public List<File> getLocalFiles() {
        List<File> files = new ArrayList<>();
        File[] fileArray = folder.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    public List<String> getLocalFileNames() {
        List<String> names = new ArrayList<>(1);
        File[] fileArray = folder.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                names.add(file.getName());
            }
        }
        return names;
    }

    public List<File> getAllFiles() {
        List<File> allFiles = new ArrayList<>(getLocalFiles());
        getLocalSubFolders().forEach(subFolder -> allFiles.addAll(subFolder.getAllFiles()));
        return allFiles;
    }

    public List<Folder> getLocalSubFolders() {
        List<Folder> subFolders = new ArrayList<>();
        File[] fileArray = folder.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                if (file.isDirectory()) {
                    subFolders.add(new Folder(file));
                }
            }
        }
        return subFolders;
    }

    public Folder getSubFolderByName(String name) {
        List<Folder> subFolders = getAllSubFolders();
        for (Folder folder : subFolders) {
            if (folder.getName().equals(name)) return folder;
        }

        return null;
    }

    public List<Folder> getAllSubFolders() {
        List<Folder> allSubFolders = new ArrayList<>();
        getLocalSubFolders().forEach(subFolder -> {
            allSubFolders.add(subFolder);
            allSubFolders.addAll(subFolder.getAllSubFolders());
        });
        return allSubFolders;
    }

    public File getFileByName(String fileName, boolean searchInAllSubFolders) {

        List<File> files = new ArrayList<>(1);

        if (searchInAllSubFolders) {
            files.addAll(getAllFiles());
        } else {
            files.addAll(getLocalFiles());
        }

        fileName = fileName.toLowerCase().trim();

        for (File file : files) {
            if (file.getName().toLowerCase().contains(fileName)) {
                return file;
            }
        }

        return null;
    }

    public File getFileByExactName(String fileName, boolean searchInAllSubFolders) {

        List<File> files = new ArrayList<>(1);

        if (searchInAllSubFolders) {
            files.addAll(getAllFiles());
        } else {
            files.addAll(getLocalFiles());
        }

        fileName = fileName.toLowerCase().trim();

        for (File file : files) {
            if (file.getName().toLowerCase().equals(fileName)) {
                return file;
            }
        }

        return null;
    }

    public List<File> getFilesByName(String fileName, boolean searchInAllSubFolders) {

        List<File> allFiles = new ArrayList<>(1);

        if (searchInAllSubFolders) {
            allFiles.addAll(getAllFiles());
        } else {
            allFiles.addAll(getLocalFiles());
        }

        fileName = fileName.toLowerCase().trim();

        List<File> filesByName = new ArrayList<>(1);

        for (File file : allFiles) {
            if (file.getName().toLowerCase().contains(fileName)) {
                filesByName.add(file);
            }
        }

        return filesByName;
    }

    public void addByMove(File file) throws IOException {
        addByCopy(file);
        file.delete();
    }

    public void addByCopy(File file) throws IOException {
        File destination = new File(folder, file.getName());
        FileUtils.copyFile(file, destination);
    }

    public void addByMove(File... files) throws IOException {
        for (File file : files) {
            addByMove(file);
        }
    }

    public void addByCopy(File... files) throws IOException {
        for (File file : files) {
            addByCopy(file);
        }
    }

    public boolean isEmpty() {
        return getLocalFiles().isEmpty();
    }

    public String getName() {
        return folder.getName();
    }

    public String getAbsolutePath() {
        return folder.getAbsolutePath();
    }

    public File getFolderFile() {
        return new File(getAbsolutePath());
    }

    public File createNewFileHandle(String fileName) {
        return new File(folder, fileName);
    }

    public void addSubDirectory(String fileName) throws IOException {
        File fileHandle = new File(folder, fileName);
        if (fileHandle.exists() && fileHandle.isDirectory()) {
            return;
        }

        boolean success = fileHandle.mkdir();

        if (!success) throw new IOException("Could not add subdirectory");
    }


    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder1 = (Folder) o;
        return Objects.equals(folder, folder1.folder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folder);
    }

    /**
     * Deletes this folder and all of its contents.
     */
    public void delete() {
        getLocalFiles().forEach(File::delete);
        getLocalSubFolders().forEach(Folder::delete);
        folder.delete();
    }
}
