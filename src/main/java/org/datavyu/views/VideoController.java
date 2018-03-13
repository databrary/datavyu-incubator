package org.datavyu.views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
        this.primaryStage.show();
    }

    private void controllerkeyPadInit(GridPane pane){
        Button addVideoButton = new Button("Add Video");// Add media Button
        Label mediaTime =  new Label("00:00:00:000");
        Button playButton = new Button("Play");// Play Media button
        Button stopButton = new Button("Stop");// Stop Media Button
        Button pauseButton = new Button("Pause");// Pause Media Button
        Button fButton = new Button(">>");// Shuttle Forward Button
        Button bButton = new Button("<<");// Shuttle backward Button
        Button jogFButton = new Button("Jog >");// Jog Forward Button
        Button jogBButton = new Button("< Jog");// Jog Backward Button
        Button onsetButton = new Button("Set onset");// Set Cell onset Button
        Button offsetButton = new Button("Set offSet");// Set Cell offset Button
        Button pointCellbutton = new Button("Point Cell");// Point a Cell button
        Button hideTrack = new Button("Hide Track");// Hide Track Button
        Button backButton = new Button("Back");// Back Button
        Button findButton = new Button("Find"); // Find Button
        Button newCellButton = new Button("New \n Cell"); // New Cell Button
        Button newCellPrevOffsetbutton = new Button("New Cell Set Prev offset");// New Cell Button with previous offset

        //mediaTime.textProperty().bind(); // To be bind to the clock TImer

        addVideoButton.setOnAction(event -> {
            //Open a Media
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open A Video");
            File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
            if(selectedFile != null){

                this.stremvViewer = new JfxMediaPlayer(Identifier.generateIdentifier(), new jfxMedia(selectedFile)).getStreamViewer();
            }
        });
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
        });
        bButton.setOnAction(event -> {
            // Shuttle Backward Media
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

        pane.add(mediaTime,2,0);
        pane.add(addVideoButton,1,1);
        pane.add(pointCellbutton,2,1);
        pane.add(hideTrack,3,1);
        pane.add(onsetButton,1,2);
        pane.add(playButton,2,2);
        pane.add(offsetButton,3,2);
        pane.add(backButton,4,2);
        pane.add(bButton,1,3);
        pane.add(stopButton,2,3);
        pane.add(fButton,3,3);
        pane.add(findButton,4,3);
        pane.add(jogBButton,1,4);
        pane.add(pauseButton,2,4);
        pane.add(jogFButton,3,4);
        pane.add(newCellButton,4,4,1,2);
        pane.add(newCellPrevOffsetbutton,1,5,2,1);
    }


}
