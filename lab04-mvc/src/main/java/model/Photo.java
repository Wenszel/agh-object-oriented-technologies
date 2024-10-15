package model;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;


public class Photo {

    private String name;

    private final Image photoData;

    public Photo(String extension, byte[] photoData) {
        this.photoData = new Image(new ByteArrayInputStream(photoData));
        this.name = UUID.randomUUID().toString() + "." + extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getPhotoData() {
        return photoData;
    }
}
