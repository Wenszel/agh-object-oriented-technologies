package util;

import io.reactivex.rxjava3.core.Observable;
import model.Photo;
import model.PhotoSize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class PhotoProcessor {
    public static final int MINIATURE_WIDTH = 300;
    public static final int MINIATURE_HEIGHT = 200;
    private static final Logger log = Logger.getLogger(PhotoProcessor.class.getName());

    public Boolean isPhotoValid(Photo photo) {
        return PhotoSize.resolve(photo) != PhotoSize.SMALL;
    }

    public Photo convertToMiniature(Photo photo) {
        log.info("...Converting photo... : " + photo.getPhotoData().length);
        return resize(photo, MINIATURE_WIDTH, MINIATURE_HEIGHT);
    }

    private Photo resize(Photo photo, int scaledWidth, int scaledHeight) {
        BufferedImage inputImage = loadPhoto(photo);
        if (inputImage == null) return photo;
        Dimension scaledDimension = rescaleKeepingRatio(inputImage, scaledWidth, scaledHeight);
        BufferedImage resizedImage = applyNewDimensions(scaledDimension, inputImage);
        return createResizedPhoto(photo, resizedImage);
    }

    private BufferedImage loadPhoto(Photo photo) {
        try (InputStream inputStream = new ByteArrayInputStream(photo.getPhotoData())) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            log.warning("Could not load photo: " + photo.getId());
            return null;
        }
    }

    private static BufferedImage applyNewDimensions(Dimension scaledDimension, BufferedImage inputImage) {
        BufferedImage outputImage = new BufferedImage(scaledDimension.width,
                scaledDimension.height, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledDimension.width, scaledDimension.height, null);
        g2d.dispose();
        return outputImage;
    }

    private Photo createResizedPhoto(Photo photo, BufferedImage outputImage) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(outputImage, photo.getExtension(), outputStream);
            return new Photo(photo.getDownloadedDate(), photo.getExtension(), outputStream.toByteArray());
        } catch (IOException e) {
            log.warning("Could not save photo: " + photo.getId());
            return photo;
        }
    }

    public Observable<Photo> processPhotos(Observable<Photo> photos) {
        return photos
                .filter(this::isPhotoValid)
                .map(this::convertToMiniature);
    }

    private Dimension rescaleKeepingRatio(BufferedImage inputImage, int preferredWidth, int preferredHeight) {
        double widthRatio = inputImage.getWidth() / (double) preferredWidth;
        double heightRatio = inputImage.getHeight() / (double) preferredHeight;

        int finalWidth = preferredWidth;
        int finalHeight = preferredHeight;
        if (widthRatio > heightRatio) {
            finalHeight = (int) (inputImage.getHeight() / heightRatio);
        } else {
            finalWidth = (int) (inputImage.getWidth() / widthRatio);
        }
        return new Dimension(finalWidth, finalHeight);
    }
}
