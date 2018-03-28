package org.datavyu.views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.datavyu.madias.javafx.jfxMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.mediaplayers.javafx.JfxMediaPlayer;
import org.datavyu.util.Identifier;

import java.io.File;

public class VideoController extends Application{

    private StreamViewer stremvViewer;
    Stage primaryStage;
    Scene controllerScene;
    private Rate currentRate = Rate.defaultRate();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initVideoControllerScene();

        this.controllerScene.getStylesheets().add("DatavyuView.css");
        this.primaryStage.setScene(controllerScene);
        this.primaryStage.setTitle("Data Viewer Controller");
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }

    private void initVideoControllerScene() {

        GridPane controllerkeyPad = new GridPane();
        controllerkeyPadInit(controllerkeyPad);

        //TODO: starts with a simple slider just to control the stream
        VBox mixerContoller = new VBox();
        mixerContollerInit(mixerContoller);

        HBox videoControllerVbox = new HBox(controllerkeyPad,mixerContoller);

        this.controllerScene = new Scene(videoControllerVbox);
    }

    public void setRate(Rate newRate) { this.currentRate = newRate; }

    public Rate getRate() { return this.currentRate; }

    private void mixerContollerInit(VBox mixerContoller) {

        Label mainClockLabel = new Label("Main Clock: ");
        Label mainClockTimeLabel =  new Label("00:00:00:000");
        Slider mainClockSlider =  new Slider();
        HBox mainClockHBox = new HBox(mainClockLabel, mainClockTimeLabel, mainClockSlider);

        Label streamClockLabel = new Label("Stream Clock: ");
        Label streamClockTimeLabel =  new Label("00:00:00:000");
        Slider streamSlider =  new Slider();
        HBox streamHBox = new HBox(streamClockLabel, streamClockTimeLabel, streamSlider);

        mixerContoller.getChildren().addAll(mainClockHBox,streamHBox);

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
            //TODO: add a File Chooser Filter
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open A Video");
            File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
            if (selectedFile != null) {
                //Start with JavaFX until I inject the Plugin Manager
                this.stremvViewer = new JfxMediaPlayer(Identifier.generateIdentifier(), jfxMedia.getMedia(selectedFile));
            }
        });

        //TODO: Check if we have any stream opened before we trigger an action
        playButton.setOnAction(event -> {
            // Play Media
            stremvViewer.setRate(Rate.defaultRate());
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
            System.out.println("Current Rate: " + currentRate + " Next Rate: " + currentRate.next());
            stremvViewer.shuttle(currentRate.next());
            setRate(currentRate.next());//TODO: Find a better way to perssist the Rate of the VideoController
        });
        bButton.setOnAction(event -> {
            // Shuttle Backward Media
            System.out.println("Current Rate: " + currentRate.getValue() + " Next Rate: " + currentRate.previous());
            stremvViewer.shuttle(currentRate.previous());
            setRate(currentRate.previous());
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


    //TODO: ADD Backward playback
    public enum Rate {
        X1D32((float) 0.03125) {
            @Override
            public Rate next() {
                return X1D16;
            }

            @Override
            public Rate previous() {
                return X1D32;
            }
        },
        X1D16((float) 0.0625) {
            @Override
            public Rate next() {
                return X1D8;
            }

            @Override
            public Rate previous() {
                return X1D32;
            }
        },
        X1D8((float) 0.125) {
            @Override
            public Rate next() {
                return X1D4;
            }

            @Override
            public Rate previous() {
                return X1D16;
            }
        },
        X1D4((float) 0.25) {
            @Override
            public Rate next() {
                return X1D2;
            }

            @Override
            public Rate previous() {
                return X1D8;
            }
        },
        X1D2((float) 0.5) {
            @Override
            public Rate next() {
                return X1;
            }

            @Override
            public Rate previous() {
                return X1D4;
            }
        },
        X1(1) {
            @Override
            public Rate next() {
                return Rate.X2;
            }

            @Override
            public Rate previous() {
                return X1D2;
            }
        },
        X2(2) {
            @Override
            public Rate next() {
                return X4;
            }

            @Override
            public Rate previous() {
                return X1;
            }
        },
        X4(4) {
            @Override
            public Rate next() {
                return X8;
            }

            @Override
            public Rate previous() {
                return X2;
            }
        },
        X8(8) {
            @Override
            public Rate next() {
                return X16;
            }

            @Override
            public Rate previous() {
                return X4;
            }
        },
        X16(16) {
            @Override
            public Rate next() {
                return X32;
            }

            @Override
            public Rate previous() {
                return X8;
            }
        },
        X32(32) {
            @Override
            public Rate next() {
                return this;
            }

            @Override
            public Rate previous() {
                return X16;
            }
        };

        private final float rate;

        Rate(float rate) {
            this.rate = rate;
        }

        public static Rate defaultRate(){
            return X1;
        }
        public abstract Rate next();

        public abstract Rate previous();

        public float getValue(){
            return this.rate;
        }
    }

}
