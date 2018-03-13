package org.datavyu.views;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.datavyu.madias.javafx.jfxMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.mediaplayers.javafx.JfxMediaPlayer;
import org.datavyu.plugins.javafx.JfxPlugin;
import org.datavyu.util.Identifier;

import javax.swing.text.html.ImageView;
import java.io.File;

public class VideoController extends Application{

    StreamViewer stremvViewer;
    Stage primaryStage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        GridPane controllerkeyPad = new GridPane();
        controllerkeyPadInit(controllerkeyPad);
        Scene controllerScene = new Scene(controllerkeyPad);
        controllerScene.getStylesheets().add("DatavyuView.css");
        this.primaryStage.setScene(controllerScene);
        this.primaryStage.setTitle("Data Viewer Controller");
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }

    private void controllerkeyPadInit(GridPane pane){
        Button addVideoButton = new Button("Add Video");// Add media Button
        Label mediaTime =  new Label("00:00:00:000");
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

        Label jumpBackLabel =  new Label("Jump Back By");
        TextField jumpBackText = new TextField("00:00:05:000");//Force the format
        VBox jumpBackBox = new VBox(1,jumpBackLabel,jumpBackText);
        jumpBackBox.getStyleClass().add("vbox");

        Label stepPerSecondLabel =  new Label("Steps Per Second");
        TextField stepPerSecondText = new TextField();//Force the format
        VBox stepPerSecondBox = new VBox(1,stepPerSecondLabel,stepPerSecondText);
        stepPerSecondBox.getStyleClass().add("vbox");

        Label onsetLabel =  new Label("onset");
        TextField onsetText = new TextField("00:00:00:000");//Force the format
        VBox onsetBox = new VBox(1,onsetLabel,onsetText);
        onsetBox.getStyleClass().add("vbox");

        Label offsetLabel =  new Label("onset");
        TextField offsetText = new TextField("00:00:00:000");//Force the format
        VBox offsetBox = new VBox(1,offsetLabel,offsetText);
        offsetBox.getStyleClass().add("vbox");


        //mediaTime.textProperty().bind(); // To be bind to the clock Timer

        addVideoButton.setOnAction(event -> {
            //Open a Media
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open A Video");
            File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
            if(selectedFile != null){
                //Start with JavaFX until I inject the Plugin Manager
                this.stremvViewer = new JfxMediaPlayer(Identifier.generateIdentifier(), new jfxMedia(selectedFile)).getStreamViewer();
            }
        });

        //TODO: Check if we have any stream opened before we trigger an action
        playButton.setOnAction(event -> {
            // Play Media
            stremvViewer.play();
        });
        pauseButton.setOnAction(event -> {
            // Pause Media
            stremvViewer.pause();
        });
        stopButton.setOnAction(event -> {
            // Stop Media
            stremvViewer.stop();
        });
        fButton.setOnAction(event -> {
            // Shuttle Forward Media
            stremvViewer.setRate(1);
        });
        bButton.setOnAction(event -> {
            // Shuttle Backward Media
            stremvViewer.setRate(-1);
        });
        jogFButton.setOnAction(event -> {
            // Jog Forward Media
        });
        jogBButton.setOnAction(event -> {
            // Jog Backward Media
        });
        onsetButton.setOnAction(event -> {
            // Set Cell onset
        });
        offsetButton.setOnAction(event -> {
            // set Cell Offset
        });
        pointCellbutton.setOnAction(event -> {

        });
        hideTrack.setOnAction(event -> {

        });
        backButton.setOnAction(event -> {

        });
        findButton.setOnAction(event -> {

        });
        newCellButton.setOnAction(event -> {

        });
        newCellPrevOffsetbutton.setOnAction(event -> {

        });

        pane.add(mediaTime,2,0,3,1);
        pane.add(addVideoButton,1,1);
        pane.add(pointCellbutton,2,2);
        pane.add(hideTrack,3,2);
        pane.add(onsetButton,1,3);
        pane.add(playButton,2,3);
        pane.add(offsetButton,3,3);
        pane.add(backButton,4,3);
        pane.add(jumpBackBox,5,3);
        pane.add(bButton,1,4);
        pane.add(stopButton,2,4);
        pane.add(fButton,3,4);
        pane.add(findButton,4,4);
        pane.add(stepPerSecondBox,5,4);
        pane.add(jogBButton,1,5);
        pane.add(pauseButton,2,5);
        pane.add(jogFButton,3,5);
        pane.add(newCellButton,4,5,1,3);
        pane.add(onsetBox,5,5);
        pane.add(newCellPrevOffsetbutton,1,6,2,1);
        pane.add(offsetBox,5,6);
    }


}
