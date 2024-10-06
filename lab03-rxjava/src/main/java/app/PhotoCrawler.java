package app;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
        try {
            photoDownloader
                    .getPhotoExamples()
                    .subscribe(photoSerializer::savePhoto);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);
        }
    }

    public void downloadPhotosForQuery(String query) throws IOException, InterruptedException {
        photoDownloader.searchForPhotos(query)
                .subscribe(photoSerializer::savePhoto,
                        throwable -> {log.log(Level.SEVERE, "Błąd podczas pobierania zdjęć", throwable);},
                        () -> {System.out.println("Wszystkie zdjęcia pobrane.");}
                );
    }

    public void downloadPhotosForMultipleQueries(List<String> queries) {
        photoDownloader.searchForPhotos(queries)
                .subscribe(photoSerializer::savePhoto,
                        throwable -> {log.log(Level.SEVERE, "Błąd podczas pobierania zdjęć", throwable);},
                        () -> {System.out.println("Wszystkie zdjęcia pobrane.");}
                );
    }

//    public Observable<Photo> processPhotos(Observable<Photo> photos) {
//        return photos
//                .filter(photoProcessor::isPhotoValid)
//                .map(photoProcessor::convertToMiniature);
//    }
}

