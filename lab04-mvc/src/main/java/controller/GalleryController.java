package controller;


import javafx.fxml.FXML;

import model.Gallery;
import model.Photo;

public class GalleryController {

    private Gallery galleryModel;

    @FXML
    public void initialize() {
        // TODO additional FX controls initialization
    }

    public void setModel(Gallery gallery) {
        this.galleryModel = gallery;
        bindSelectedPhoto(gallery.getPhotos().get(0));
    }

    private void bindSelectedPhoto(Photo selectedPhoto) {
        // TODO view <-> model bindings configuration
    }
}

