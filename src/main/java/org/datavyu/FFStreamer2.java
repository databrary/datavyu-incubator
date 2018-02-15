package org.datavyu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class FFStreamer2 extends Application{

    public static void main (String args[]){ launch(args);}

    private static final File sourceFile = new File("/Users/reda/Databrary/datavyuByteDecoFFmpeg/src/main/java/DatavyuSampleVideo.mp4");

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FFMPEG Player");
        Group root = new Group();
        Scene scene = new Scene(root, 800,600);

        FFmpegPlayer fFmpegPlayer = new FFmpegPlayer(sourceFile);
        scene.setRoot(fFmpegPlayer);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public class FFmpegPlayer extends BorderPane{

        private Media media;
        private MediaPlayer mp;
        private MediaView mediaView;
        private final boolean repeat = false;
        private boolean stopRequested = false;
        private boolean atEndOfMedia = false;
        private Duration duration;
        private Slider timeSlider;
        private Label playTime;
        private Slider volumeSlider;
        private HBox mediaBar;

        public FFmpegPlayer(File sourceFile) {
            this.media = new Media(sourceFile.toURI().toString());
            this.mp = new MediaPlayer(media);
            setStyle("-fx-background-color: #bfc2c7;");
            mediaView = new MediaView(mp);


            Pane mvPane = new Pane() {
            };
            mvPane.getChildren().add(mediaView);
            mvPane.setStyle("-fx-background-color: black;");
            mediaView.fitHeightProperty().bind(mvPane.heightProperty());
            mediaView.fitWidthProperty().bind(mvPane.widthProperty());
            setCenter(mvPane);


            mediaBar = new HBox();
            mediaBar.setAlignment(Pos.CENTER);
            mediaBar.setPadding(new Insets(5, 10, 5, 10));
            BorderPane.setAlignment(mediaBar, Pos.CENTER);

            final Button playButton = new Button(">");
            final Button fastForward = new Button(">>");

            playButton.setOnAction(e -> {
                MediaPlayer.Status status = mp.getStatus();

                if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
                    // don't do anything in these states
                    return;
                }

                if (status == MediaPlayer.Status.PAUSED
                        || status == MediaPlayer.Status.READY
                        || status == MediaPlayer.Status.STOPPED) {
                    // rewind the movie if we're sitting at the end
                    if (atEndOfMedia) {
                        mp.seek(mp.getStartTime());
                        atEndOfMedia = false;
                    }
                    mp.play();
                } else {
                    mp.pause();
                }
            });

            fastForward.setOnAction(e -> {
                MediaPlayer.Status status = mp.getStatus();

                if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
                    // don't do anything in these states
                    return;
                }

                if (status == MediaPlayer.Status.PAUSED
                        || status == MediaPlayer.Status.READY
                        || status == MediaPlayer.Status.STOPPED) {
                    // rewind the movie if we're sitting at the end
                    if (atEndOfMedia) {
                        mp.seek(mp.getStartTime());
                        atEndOfMedia = false;
                    }
                    mp.setRate(2);
                } else {
                    mp.pause();
                }
            });

            mp.currentTimeProperty().addListener(ov -> updateValues());

            mp.setOnPlaying(() -> {
                if (stopRequested) {
                    mp.pause();
                    stopRequested = false;
                } else {
                    playButton.setText("||");
                }
            });

            mp.setOnPaused(() -> {
                System.out.println("onPaused");
                playButton.setText(">");
            });

            mp.setOnReady(() -> {
                duration = mp.getMedia().getDuration();
                updateValues();
            });

            mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
            mp.setOnEndOfMedia(() -> {
                if (!repeat) {
                    playButton.setText(">");
                    stopRequested = true;
                    atEndOfMedia = true;
                }
            });

            mediaBar.getChildren().add(playButton);
            mediaBar.getChildren().add(fastForward);
            // Add spacer
            Label spacer = new Label("   ");
            mediaBar.getChildren().add(spacer);

            // Add Time label
            Label timeLabel = new Label("Time: ");
            mediaBar.getChildren().add(timeLabel);

            // Add time slider
            timeSlider = new Slider();
            HBox.setHgrow(timeSlider, Priority.ALWAYS);
            timeSlider.setMinWidth(50);
            timeSlider.setMaxWidth(Double.MAX_VALUE);
            timeSlider.valueProperty().addListener(ov -> {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            });
            mediaBar.getChildren().add(timeSlider);

            // Add Play label
            playTime = new Label();
            playTime.setPrefWidth(130);
            playTime.setMinWidth(50);
            mediaBar.getChildren().add(playTime);

            // Add the volume label
            Label volumeLabel = new Label("Vol: ");
            mediaBar.getChildren().add(volumeLabel);

            // Add Volume slider
            volumeSlider = new Slider();
            volumeSlider.setPrefWidth(70);
            volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
            volumeSlider.setMinWidth(30);
            volumeSlider.valueProperty().addListener(ov -> {
                if (volumeSlider.isValueChanging()) {
                    mp.setVolume(volumeSlider.getValue() / 100.0);
                }
            });
            mediaBar.getChildren().add(volumeSlider);

            setBottom(mediaBar);
        }

        protected void updateValues() {
            if (playTime != null && timeSlider != null && volumeSlider != null) {
                Platform.runLater(() -> {
                    Duration currentTime = mp.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mp.getVolume()
                                * 100));
                    }
                });
            }
        }

        private String formatTime(Duration elapsed, Duration duration) {
            int intElapsed = (int) Math.floor(elapsed.toSeconds());
            int elapsedHours = intElapsed / (60 * 60);
            if (elapsedHours > 0) {
                intElapsed -= elapsedHours * 60 * 60;
            }
            int elapsedMinutes = intElapsed / 60;
            int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                    - elapsedMinutes * 60;

            if (duration.greaterThan(Duration.ZERO)) {
                int intDuration = (int) Math.floor(duration.toSeconds());
                int durationHours = intDuration / (60 * 60);
                if (durationHours > 0) {
                    intDuration -= durationHours * 60 * 60;
                }
                int durationMinutes = intDuration / 60;
                int durationSeconds = intDuration - durationHours * 60 * 60
                        - durationMinutes * 60;
                if (durationHours > 0) {
                    return String.format("%d:%02d:%02d/%d:%02d:%02d",
                            elapsedHours, elapsedMinutes, elapsedSeconds,
                            durationHours, durationMinutes, durationSeconds);
                } else {
                    return String.format("%02d:%02d/%02d:%02d",
                            elapsedMinutes, elapsedSeconds, durationMinutes,
                            durationSeconds);
                }
            } else {
                if (elapsedHours > 0) {
                    return String.format("%d:%02d:%02d", elapsedHours,
                            elapsedMinutes, elapsedSeconds);
                } else {
                    return String.format("%02d:%02d", elapsedMinutes,
                            elapsedSeconds);
                }
            }
        }

    }

}