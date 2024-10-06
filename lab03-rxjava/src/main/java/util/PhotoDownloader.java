package util;

import driver.DuckDuckGoDriver;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.observables.GroupedObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import model.PhotoSize;
import org.apache.tika.Tika;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static model.PhotoSize.*;

public class PhotoDownloader {

    private static final Logger log = Logger.getLogger(PhotoDownloader.class.getName());

    public Observable<Photo> getPhotoExamples() throws IOException {
        return Observable.fromIterable(getListOfUrls())
                .map(this::getPhoto);
    }

    private List<String> getListOfUrls() {
        return List.of(
                "https://i.ytimg.com/vi/7uxQjydfBOU/hqdefault.jpg",
                "http://digitalspyuk.cdnds.net/16/51/1280x640/landscape-1482419524-12382542-low-res-sherlock.jpg",
                "http://image.pbs.org/video-assets/pbs/masterpiece/132733/images/mezzanine_172.jpg",
                "https://classicmystery.files.wordpress.com/2016/04/miss-marple-2.jpg",
                "https://i.pinimg.com/736x/7c/14/c9/7c14c97839940a09f987fbadbd47eb89--detective-monk-adrian-monk.jpg");
    }
    public Observable<Photo> searchForPhotos(String searchQuery) {
        return Observable.create((observer) -> {
            DuckDuckGoDriver.searchForImages(searchQuery).forEach(url -> {
                try {
                    observer.onNext(getPhoto(url));
                } catch (IOException e) {
                    log.log(Level.WARNING, "could not download a photo", e);
                }
            });
            observer.onComplete();
        });
    }

    public Observable<Photo> searchForPhotos(List<String> searchQueries) {

        PhotoProcessor photoProcessor = new PhotoProcessor();

        return Observable.fromIterable(searchQueries)
                .flatMap(this::searchForPhotos)
                .compose(this::setSchedulerBasedOnPhotoSize)
                .compose(photoProcessor::processPhotos)
                .observeOn(Schedulers.io())
                .buffer(5, TimeUnit.SECONDS)
                .flatMap(Observable::fromIterable);
    }

    private Observable<Photo> setSchedulerBasedOnPhotoSize(@NonNull Observable<Photo> photos) {
        return photos
                .groupBy(PhotoSize::resolve)
                .flatMap(this::setSuitableScheduler);
    }

    private Observable<Photo> setSuitableScheduler(GroupedObservable<PhotoSize, Photo> group) {
        return switch (Objects.requireNonNull(group.getKey())) {
            case SMALL -> group.observeOn(Schedulers.io());
            case MEDIUM -> group.observeOn(Schedulers.io());
            case LARGE -> group.observeOn(Schedulers.computation());
        };
    }


    private Photo getPhoto(String photoUrl) throws IOException {
        log.info("Downloading... " + photoUrl);
        byte[] photoData = downloadPhoto(photoUrl);
        return createPhoto(photoData);
    }

    private Photo createPhoto(byte[] photoData) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(photoData);
        if (fileType.startsWith("image")) {
            return new Photo(LocalDate.now(), fileType.substring(fileType.indexOf("/") + 1), photoData);
        }
        throw new IOException("Unsupported media type: " + fileType);
    }


    private byte[] downloadPhoto(String url) throws IOException {
        URLConnection connection = openConnection(url);
        try (InputStream inputStream = connection.getInputStream()) {
            return readPhoto(inputStream);
        }
    }

    private byte[] readPhoto(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bytesRead;
        byte[] data = new byte[16384];

        while ((bytesRead= inputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
    
    private URLConnection openConnection(String url) throws IOException {
        URL photoUrl = new URL(url);
        URLConnection yc = photoUrl.openConnection();
        yc.setRequestProperty("User-Agent", DuckDuckGoDriver.USER_AGENT);
        return yc;
    }
}
