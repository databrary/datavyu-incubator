package org.datavyu.views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Duration;
import org.datavyu.madias.javafx.jfxMedia;
import org.datavyu.mediaplayers.DatavyuStream;
import org.datavyu.mediaplayers.ffmpeg.FFmpegMediaPlayer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import java.io.File;

public class VideoController extends Application{

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize the DatavyuStream (MainStream)
        this.mainStream = DatavyuStream.createDatavyuStream();

        initVideoControllerScene();

        this.primaryStage.setScene(controllerScene);
        this.primaryStage.setTitle("Data Viewer Controller");
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }

    public void setRate(Rate newRate) { this.currentRate = newRate; }

    public Rate getRate() { return this.currentRate; }

    /**
     * //TODO: Be consistent with the time (Duration, long or double)
     * @param direction
     * @param time
     */
    private void jogStreams(int direction, Duration time) {
        //TODO: Need to implement the jog feature
        this.mainStream.jog(direction, time);
    }

    private void shuttleStreams(final int direction) {
        if(direction == +1){
            System.out.println("Actual Rate: " +currentRate+ " Next Rate: "+currentRate.next());
            this.currentRate = currentRate.next();
            this.mainStream.shuttle(currentRate);
        }
        if(direction == -1){
            System.out.println("Actual Rate: " +currentRate+ " Next Rate: "+currentRate.previous());
            this.currentRate = currentRate.previous();
            this.mainStream.shuttle(currentRate);
        }
    }

    private void pauseStreams() {
        //Keep the current state of the Rate
        this.mainStream.pause();
    }

    private void playStreams() {
        //Always Play at X1
        this.mainStream.play();
    }

    private void stopStreams(){
        //Set the rate to X0
        this.mainStream.stop();
    }

    private void backStream(final Duration time) {
        this.mainStream.back(time);
    }

    private void openStreams() {
        //TODO: add a File Chooser Filter
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open A Video");
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if (selectedFile != null) {
            //the MainStream represent the MainClock
            //TODO: add a new stream according to the selected plugin
//            this.mainStream.add(new JfxMediaPlayer(Identifier.generateIdentifier(), jfxMedia.getMedia(selectedFile)));
//            mainStream.add(new FFmpegMediaPlayerSwing(Identifier.generateIdentifier(), null));
            mainStream.add(new FFmpegMediaPlayer(Identifier.generateIdentifier(), jfxMedia.getMedia(selectedFile)));
        }
    }

    private void mixerContollerInit(VBox mixerContoller) {

        mixerContoller.setId("mixer-controller");

        Label startRegionLabel =  new Label("Start Region: ");
        startRegionTextField =  new TextField("00:00:00:000");
        HBox startRegionHBox = new HBox(startRegionLabel,startRegionTextField);

        Label endRegionLabel =  new Label("End Region: ");
        endRegionTextField =  new TextField("00:00:00:000");
        HBox endRegionHBox = new HBox(endRegionLabel,endRegionTextField);

        HBox regionHBox = new HBox(startRegionHBox,endRegionHBox);

        Label mainClockLabel = new Label("Main Clock: ");
        mainClockTimeLabel =  new Label("00:00:00:000");
        mainClockSlider =  new Slider();
        HBox mainClockHBox = new HBox(mainClockLabel, mainClockTimeLabel, mainClockSlider);
        mainClockHBox.setId("main-clock-hbox");

        Label streamClockLabel = new Label("Stream Clock: ");
        streamClockTimeLabel =  new Label("00:00:00:000");
        streamSlider =  new Slider();
        HBox streamHBox = new HBox(streamClockLabel, streamClockTimeLabel, streamSlider);
        streamHBox.setId("stream-clock-hbox");


        mixerContoller.getChildren().addAll(regionHBox, mainClockHBox,streamHBox);

    }

    private void controllerkeyPadInit(GridPane pane) {

        Button addVideoButton = new Button("Add Video");// Add media Button

        Label mediaTime = new Label("00:00:00:000");
        mediaTime.setId("mediatime-label");

        Button playButton = new Button();// Play Media button
        playButton.setId("play-button");

        Button stopButton = new Button();// Stop Media Button
        stopButton.setId("stop-button");

        Button pauseButton = new Button();// Pause Media Button
        pauseButton.setId("pause-button");

        Button fButton = new Button();// Shuttle Forward Button
        fButton.setId("shuttle-f-button");

        Button bButton = new Button();// Shuttle backward Button
        bButton.setId("shuttle-b-button");

        Button jogFButton = new Button();// Jog Forward Button
        jogFButton.setId("jog-f-button");

        Button jogBButton = new Button();// Jog Backward Button
        jogBButton.setId("jog-b-button");

        Button onsetButton = new Button();// Set Cell onset Button
        onsetButton.setId("onset-button");

        Button offsetButton = new Button();// Set Cell offset Button
        offsetButton.setId("offset-button");

        Button pointCellbutton = new Button();// Point a Cell button
        pointCellbutton.setId("point-cell-button");

        Button hideTrack = new Button();// Hide Track Button
        hideTrack.setId("hidetrack-button");

        Button backButton = new Button();// Back Button
        backButton.setId("back-button");

        Button findButton = new Button(); // Find Button
        findButton.setId("find-button");

        Button newCellButton = new Button(); // New Cell Button
        newCellButton.setId("newcell-button");

        Button newCellPrevOffsetbutton = new Button();// New Cell Button with previous offset
        newCellPrevOffsetbutton.setId("newcell-prevoffset-button");

        Label jumpBackLabel = new Label("Jump Back By");
        TextField jumpBackText = new TextField("00:00:05:000");//Force the format
        VBox jumpBackBox = new VBox(jumpBackLabel, jumpBackText);
        jumpBackBox.getStyleClass().add("vbox");

        Label stepPerSecondLabel = new Label("Steps Per Second");
        TextField stepPerSecondText = new TextField();//Force the format
        VBox stepPerSecondBox = new VBox(stepPerSecondLabel, stepPerSecondText);
        stepPerSecondBox.getStyleClass().add("vbox");

        Label onsetLabel = new Label("onset");
        TextField onsetText = new TextField("00:00:00:000");//Force the format
        VBox onsetBox = new VBox(onsetLabel, onsetText);
        onsetBox.getStyleClass().add("vbox");

        Label offsetLabel = new Label("onset");
        TextField offsetText = new TextField("00:00:00:000");//Force the format
        VBox offsetBox = new VBox(offsetLabel, offsetText);
        offsetBox.getStyleClass().add("vbox");

        //mediaTime.textProperty().bind(); // To be bind to the clock Timer

        addVideoButton.setOnAction(event -> {
            //Open a Media
            this.openStreams();
        });

        //TODO: Check if we have any stream opened before we trigger an action
        playButton.setOnAction(event -> {
            // Play Media
            this.playStreams();
        });
        pauseButton.setOnAction(event -> {
            // Pause Media
            this.pauseStreams();
        });
        stopButton.setOnAction(event -> {
            // Stop Media
            this.stopStreams();
        });
        fButton.setOnAction(event -> {
            // Shuttle Forward Media (+1)
            this.shuttleStreams(+1);
        });
        bButton.setOnAction(event -> {
            // Shuttle Backward Media (-1)
            this.shuttleStreams(-1);
        });
        jogFButton.setOnAction(event -> {
            // Jog Forward Media (+1)
            //TODO: Jog by frame and not time, and get the number of frame from the textfield
            this.jogStreams(+1, Duration.ONE);
        });
        jogBButton.setOnAction(event -> {
            // Jog Backward Media (-1)
            //TODO: Jog by frame and not time, and get the number of frame from the textfield
            this.jogStreams(-1, Duration.ONE);
        });
        onsetButton.setOnAction(event -> {
            // Set Cell onset
            //TODO: hook it to the Spreadsheet
        });
        offsetButton.setOnAction(event -> {
            // set Cell Offset
            //TODO: hook it to the Spreadsheet
        });
        pointCellbutton.setOnAction(event -> {

        });
        hideTrack.setOnAction(event -> {

        });
        backButton.setOnAction(event -> {
            //TODO: Back by time, and get the time from the textfield
            this.backStream(Duration.ONE);
        });
        findButton.setOnAction(event -> {
            //TODO: hook it to the Spreadsheet
        });
        newCellButton.setOnAction(event -> {
            //TODO: hook it to the Spreadsheet
        });
        newCellPrevOffsetbutton.setOnAction(event -> {
            //TODO: hook it to the Spreadsheet
        });

        pane.add(mediaTime, 2, 0, 3, 1);
        pane.add(addVideoButton, 1, 1);
        pane.add(pointCellbutton, 2, 2);
        pane.add(hideTrack, 3, 2);
        pane.add(onsetButton, 1, 3);
        pane.add(playButton, 2, 3);
        pane.add(offsetButton, 3, 3);
        pane.add(backButton, 4, 3);
        pane.add(jumpBackBox, 5, 3);
        pane.add(bButton, 1, 4);
        pane.add(stopButton, 2, 4);
        pane.add(fButton, 3, 4);
        pane.add(findButton, 4, 4);
        pane.add(stepPerSecondBox, 5, 4);
        pane.add(jogBButton, 1, 5);
        pane.add(pauseButton, 2, 5);
        pane.add(jogFButton, 3, 5);
        pane.add(newCellButton, 4, 5, 1, 3);
        pane.add(onsetBox, 5, 5);
        pane.add(newCellPrevOffsetbutton, 1, 6, 2, 1);
        pane.add(offsetBox, 5, 6);
    }

    private void initVideoControllerScene() {
        primaryStage.setOnCloseRequest(event -> Platform.exit());

        GridPane controllerkeyPad = new GridPane();
        controllerkeyPadInit(controllerkeyPad);

        //TODO: start with a simple slider just to control the stream
        VBox mixerContoller = new VBox();
        mixerContollerInit(mixerContoller);

        HBox videoControllerVbox = new HBox(controllerkeyPad,mixerContoller);

        this.controllerScene = new Scene(videoControllerVbox);

        this.controllerScene.getStylesheets().add("DatavyuView.css");
    }

    private DatavyuStream mainStream;
    private Stage primaryStage;
    private Scene controllerScene;
    private TextField startRegionTextField;
    private TextField endRegionTextField;
    private Slider mainClockSlider;
    private Slider streamSlider;
    private Label mainClockTimeLabel;
    private Label streamClockTimeLabel;
    private Rate currentRate = Rate.playRate();
}
