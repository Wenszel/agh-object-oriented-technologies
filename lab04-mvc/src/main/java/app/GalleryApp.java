package app;

import controller.GalleryController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import model.Gallery;
import util.PhotoDownloader;
import util.PhotoSerializer;

import java.io.IOException;

public class GalleryApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        var gallery = new Gallery();
        configureModel(gallery);
        try {
            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(GalleryApp.class.getClassLoader().getResource("view/galleryView.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            GalleryController controller = loader.getController();
            controller.setModel(gallery);

            // add layout to a scene and show them all
            configureStage(primaryStage, rootLayout);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    private void configureStage(Stage primaryStage, BorderPane rootLayout) {
        var scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gallery app");
        primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
        primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
    }

    private void configureModel(Gallery gallery) throws IOException {
        var photoSerializer = new PhotoSerializer("photos");
        photoSerializer.registerGallery(gallery);
        fillGallery(gallery);
    }

    private void fillGallery(Gallery gallery) {
        var photoDownloader = new PhotoDownloader();
        photoDownloader.getPhotoExamples()
                .subscribe(gallery::addPhoto);
    }
}
