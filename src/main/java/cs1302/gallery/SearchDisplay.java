package cs1302.gallery;

import java.net.URL;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.geometry.Insets;
import java.io.InputStreamReader;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Creates a class that contains the search function of the {@code GalleryApp}.
 */
public class SearchDisplay extends VBox {

    private HBox searchContainer;
    private Button pauseButton;
    final HBox sq = new HBox(new Text("Search Query:"));
    private TextField searchBar;
    protected Button searchButton;
    private ImageView[] imgList;
    private TilePane tp;
    private JsonArray results;
    private int numResults;
    private URL url;
    private boolean paused;
    private int[] displayImgList;
    private double progress;
    private ProgressBar pb;
    final ErrorDisplay em = new ErrorDisplay();

    /**
     * Construcst a {@code SearchDisplay} Item.
     */
    public SearchDisplay() {
        super();
        searchContainer = new HBox(10);
        tp = new TilePane();
        tp.setPrefRows(4);
        tp.setPrefTileHeight(100);
        tp.setPrefTileWidth(100);
        this.setVgrow(tp, Priority.ALWAYS);

        // initializes TimeLine
        EventHandler<ActionEvent> timeHandler = event -> {
            if (!displayImgList.equals(null)) {
                updateDisplay();
            } // if
        };
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), timeHandler);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);

        //sets up search bar & buttons
        sq.setPadding(new Insets(5,0,0,0));
        searchContainer.setPadding(new Insets(0,10,0,10));
        pauseButton = new Button("Pause");
        pauseButton.setPrefWidth(80);
        pauseButton.setTextAlignment(TextAlignment.CENTER);
        paused = true;
        EventHandler<ActionEvent> pause = event -> {
            if (paused) {
                paused = false;
                pauseButton.setText("Pause");
                timeline.play();
            } else {
                paused = true;
                pauseButton.setText("Play");
                timeline.pause();
            } //if
        };
        pauseButton.setOnAction(pause);
        searchBar = new TextField("rock");
        searchButton = new Button("Update Images");
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        EventHandler<ActionEvent> handler = event -> onSearch();
        searchButton.setOnAction(handler);

        //Initialize Progress Bar
        progress = 0;
        pb = new ProgressBar(progress);

        HBox bottom = new HBox(10);
        bottom.getChildren().addAll(pb,new Text("Images Provided Courtesy of iTunes"));

        searchButton.fire();
        pauseButton.fire();
        // adds buttons and query field to hbox
        searchContainer.getChildren().addAll(pauseButton, sq, searchBar, searchButton);
        this.getChildren().addAll(searchContainer, tp, bottom);
    } // SearchDisplay

    /** Action upon search button press. */
    private void onSearch() {
        runNow(() -> {
            try {
                String sUrl = makeUrl(searchBar.getText());
                url = new URL(sUrl);
            } catch (Exception e) {
                System.out.println("Invalid URL");
            } // try
            readUrl();
            if (numResults >= 21) {
                fillImgList();
                Platform.runLater(() -> display());
            } else {
                Platform.runLater(() -> em.show());
            } // if
            progress = 0;
            pb.setProgress(0);
        });
    } // onSearch

    /**
     * From Threads Reading.
     *
     *     "Creates and immediately starts a new daemon thread that executes       "
     *     "{@code target.run()}. This method, which may be called from any thread,"
     *     "will return immediately its the caller.                                "
     * @param r the object whose {@code run} method is invoked when this thread is started
     */
    private static void runNow(Runnable r) {
        Thread thread = new Thread(r);
        thread.setPriority(2);
        thread.setDaemon(true);
        thread.start();
    } // runNow

    /**
     * Updates display when not paused.
     */
    private void updateDisplay() {
        try {
            int indexReplaced = (int)(Math.random() * 20);
            int replacedWith = (int)(Math.random() * numResults);
            boolean sentinel = true;
            while (sentinel) {
                boolean b = false;
                for (int i : displayImgList) {
                    if (i == replacedWith) {
                        b = true;
                    } // if
                } // for
                if (b) {
                    replacedWith = (int)(Math.random() * numResults);
                } else {
                    sentinel = false;
                } // if
            } //while
            displayImgList[indexReplaced] = replacedWith;
            tp.getChildren().set(indexReplaced, imgList[replacedWith]);
        } catch (NullPointerException npe) {
            return;
        } // try
    } // updateDisplay

    /**
     * Creates {@code JsonArray} of results for search query.
     */
    private void readUrl() {
        try {
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(reader);
            JsonObject root = je.getAsJsonObject();
            JsonArray res = root.getAsJsonArray("results");
            results = res;
            numResults = results.size();
        } catch (Exception e) {
            System.out.println("Invalid URL1");
            e.printStackTrace();
        } // try
    } // readUrl

    /**
     * Searches iTunes Library with given search query.
     */
    private void display() {
        tp.getChildren().clear();
        displayImgList = new int[20];
        for (int i = 0; i < 20; i ++) {
            tp.getChildren().add(imgList[i]);
            displayImgList[i] = i;
        } // for
    } // display


    /** Fills imgList with ImageObjects from {@code JsonArray}. */
    private void fillImgList() {
        int nonNull = 0;
        imgList = new ImageView[numResults];
        for (int i = 0; i < numResults; i ++) {
            JsonObject result = results.get(i).getAsJsonObject();
            if (!result.get("artworkUrl100").equals(null)) {
                imgList[i] = new ImageView(toImage(result));
                nonNull ++;
                progress += 0.05;
                pb.setProgress(progress);
            } // if
        } // for
        ImageView[] newArray = new ImageView[nonNull];
        int i = 0;
        for (ImageView img : imgList) {
            if (!img.equals(null)) {
                newArray[i] = img;
                i ++;
            } // if
        } // for
        imgList = newArray;
    } // fillImgList

    /**
     * Extracts image url from {@code JsonObject} and returns it as an {@code Image}.
     *
     * @param result the {@code JsonObject} from which the URL is taken
     * @return an {@code Image} object created from the URL
     */
    private Image toImage(JsonObject result) {
        JsonElement artworkUrl100 = result.get("artworkUrl100");
        Image image = new Image(artworkUrl100.getAsString());
        return image;
    } // toImage

    /**
     * Takes search query and constructs a string version of a url as defined in the iTunes API.
     *
     * @param query the term being searched
     * @return a String representation of the url to search.
     */
    private String makeUrl(String query) throws IllegalArgumentException {
        if (query.equals("")) {
            throw new IllegalArgumentException("Invalid Search Query");
        } // if
        String searchParam = "";
        char[] chars = query.toCharArray();
        char space = ' ';
        for (char c : chars) {
            if (c == space) {
                searchParam += "+";
            } else {
                searchParam += c;
            } // if
        } // for
        String sUrl = "https://itunes.apple.com/search?term=" + searchParam + "&media=music";
        return sUrl;
    } // makeUrl

} // SearchBar
