package model;

import java.time.LocalDate;
import java.util.UUID;


public class Photo {

    private final UUID id = UUID.randomUUID();

    private final LocalDate downloadedDate;

    private final String extension;

    private final byte[] photoData;

    public Photo(LocalDate downloadedDate, String extension, byte[] photoData) {
        this.downloadedDate = downloadedDate;
        this.extension = extension;
        this.photoData = photoData;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDownloadedDate() {
        return downloadedDate;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getPhotoData() {
        return photoData;
    }
}
