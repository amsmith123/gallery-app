package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.image.*;

/**
 * Creates an About Me stage for iTunes GalleryApp.
 */
public class About extends Stage {

    /**
     * Creates stage to display about me Info.
     */
    public About() {
        super();
        VBox root = new VBox(5);
        root.setPadding(new Insets(5,5,5,5));
        ImageView img = new ImageView(new Image("file:resources/HeadShot.png"));
        img.setFitWidth(150);
        img.setPreserveRatio(true);
        Text text = new Text("Arjun Smith\nams38663@uga.edu\nv1.0.1");
        text.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().addAll(img,text);

        Scene scene = new Scene(root);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("About Arjun Smith");
        this.setMaxHeight(640);
        this.setMaxWidth(480);
        this.setScene(scene);
        this.sizeToScene();
    } // start
} // about
