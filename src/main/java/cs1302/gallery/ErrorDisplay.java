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
 * Creates an Error Message for invalid search queries.
 */
public class ErrorDisplay extends Stage {

    VBox root;
    Text text;

    /**
     * Creates stage to display Error Message.
     */
    public ErrorDisplay() {
        super();
        root = new VBox(5);
        root.setPadding(new Insets(15,15,15,15));
        text = new Text("Error: Insufficient Search Results Found");
        text.setTextAlignment(TextAlignment.CENTER);
        /*closeButton = new Button("Close");
          closeButton.setOnAction(e -> this.close());*/
        root.getChildren().addAll(text);

        Scene scene = new Scene(root);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Error");
        this.setMaxHeight(640);
        this.setMaxWidth(480);
        this.setScene(scene);
        this.sizeToScene();
    } // start
} // about
