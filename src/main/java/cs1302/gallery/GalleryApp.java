package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents an iTunes GalleryApp.
 */
public class GalleryApp extends Application {

    final Menu menu1 = new Menu("File");
    final Menu menu2 = new Menu("Help");

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        // initializes menu
        MenuBar mb = new MenuBar(menu1,menu2);
        MenuItem mi1 = new MenuItem("Exit");
        mi1.setOnAction(e -> Platform.exit());
        menu1.getItems().add(mi1);
        MenuItem mi2 = new MenuItem("About");
        final Stage about = new About();
        mi2.setOnAction(e -> about.show());
        menu2.getItems().add(mi2);

        // initializes Search Bar and Display
        VBox pane = new VBox();
        pane.setMinHeight(480);
        SearchDisplay sd = new SearchDisplay();
        pane.getChildren().addAll(mb,sd);
        VBox.setVgrow(sd,Priority.ALWAYS);


        Scene scene = new Scene(pane);
        stage.setMaxWidth(880);
        stage.setMaxHeight(660);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

} // GalleryApp
