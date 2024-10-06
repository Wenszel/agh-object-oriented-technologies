package util;

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

    private static final Logger log = Logger.getLogger(PhotoProcessor.class.getName());

    public Boolean isPhotoValid(Photo photo) {
        return PhotoSize.resolve(photo) != PhotoSize.SMALL;
    }

    public Photo convertToMiniature(Photo photo) throws IOException {
        log.info("...Converting photo... : " + photo.getPhotoData().length);
        return resize(photo, 300, 200);
    }



    private Photo resize(Photo photo, int scaledWidth, int scaledHeight)
            throws IOException {

        try (InputStream inputStream = new ByteArrayInputStream(photo.getPhotoData())) {
            BufferedImage inputImage = ImageIO.read(inputStream);

            if (inputImage != null) {
                Dimension scaledDimension = rescaleKeepingRatio(inputImage, scaledWidth, scaledHeight);

                BufferedImage outputImage = new BufferedImage(scaledDimension.width,
                        scaledDimension.height, inputImage.getType());

                Graphics2D g2d = outputImage.createGraphics();
                g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
                g2d.dispose();

                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    ImageIO.write(outputImage, photo.getExtension(), outputStream);

                    return new Photo(photo.getDownloadedDate(), photo.getExtension(), outputStream.toByteArray());
                }
            }
        } catch(IOException e) {
            log.warning("Miniature could not be created for photo: " + photo.getId());
        }
        return photo;
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
