package util;

import model.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class PhotoSerializer {

    private static final Logger log = Logger.getLogger(PhotoSerializer.class.getName());

    private final String photoLibraryPath;

    public PhotoSerializer(String photoLibraryPath) throws IOException {
        this.photoLibraryPath = photoLibraryPath;
        createLibraryDirectory();
    }

    private void createLibraryDirectory() throws IOException {
        File photoLibraryDir = new File(photoLibraryPath);
        if (!photoLibraryDir.exists()) {
            photoLibraryDir.mkdir();
        }
        if (!photoLibraryDir.isDirectory()) {
            throw new IOException("This is not valid photo library directory path or directory could not be created: " + photoLibraryPath);
        }
    }

    public void savePhoto(Photo photo) {
        log.info("SAVE photo: " + photo.getId());
        try (FileOutputStream outputStream = new FileOutputStream(getPhotoPath(photo))) {
            outputStream.write(photo.getPhotoData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteLibraryContents() {
        File photoLibraryDirectory = new File(photoLibraryPath);
        for (String childFile : photoLibraryDirectory.list()) {
            File libraryFile = new File(photoLibraryPath, childFile);
            libraryFile.delete();
        }
    }

    private String getPhotoPath(Photo photo) {
        return Paths.get(photoLibraryPath, String.format("%s.%s", photo.getId().toString(), photo.getExtension())).toString();
    }
}
