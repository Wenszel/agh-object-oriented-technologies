package model;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import javafx.beans.property.*;
import javafx.scene.image.Image;


public class Photo {
    private StringProperty name;
    private final Property<Image> photoData;

    public Photo(String extension, byte[] photoData) {
        Image image = new Image(new ByteArrayInputStream(photoData));
        this.photoData = new SimpleObjectProperty<>(image);

        String fileName = UUID.randomUUID() + "." + extension;
        this.name = new SimpleStringProperty(fileName);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public Image getPhotoData() {
        return photoData.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Property<Image> photoDataProperty() {
        return photoData;
    }
}
