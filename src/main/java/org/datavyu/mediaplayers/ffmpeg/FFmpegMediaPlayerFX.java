package org.datavyu.mediaplayers.ffmpeg;


import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacv.*;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import java.io.File;

import static org.bytedeco.javacpp.avutil.AV_LOG_WARNING;

public class FFmpegMediaPlayerFX extends Stage implements StreamViewer {

    private final Identifier identifier;
    private DatavyuMedia media;
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
    private Thread playThread;
    private Java2DFrameConverter converter;
    private ImageView imageView;
    private Image image;

    //TODO: add a frame grabber and an audio grabber and a MovieStreamer to sync the two streams
    // FFmpeg Media Player will talk to the MovieStreamer

    //TODO: could use memoryStorage
    //CvMemStorage storage = CvMemStorage.create();

    public FFmpegMediaPlayerFX(Identifier identifier, DatavyuMedia media){
        this.identifier = identifier;
        this.media = media;
        converter = new Java2DFrameConverter();
        try {
            grabber = FFmpegFrameGrabber.createDefault(new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/DatavyuSampleVideo.mp4"));
//            grabber = FFmpegFrameGrabber.createDefault(new File(media.getSource()));
            grabber.setFrameRate(30.00);
            grabber.start();
            avutil.av_log_set_level(AV_LOG_WARNING);
//            recorder = new FFmpegFrameRecorder("DatavyuSampleVideo.avi", grabber.getImageWidth(), grabber.getImageHeight());
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        StackPane root = new StackPane();
        imageView = new ImageView();

        root.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());

        Scene scene = new Scene(root, grabber.getImageWidth(), grabber.getImageHeight());

        this.setTitle("Video: "+ media.getSource());
        this.setScene(scene);
        this.show();

    }

    @Override
    public Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public void play() {
        playThread = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    if (this.grabber.grab() != null) {
                        Frame frame = this.grabber.grab();
                        if (frame.image != null) {
                            this.image = SwingFXUtils.toFXImage(this.converter.convert(frame), null);
                            Platform.runLater(() -> {
                                this.imageView.setImage(image);
                            });
                        }
                    }
                }
            }catch(FrameGrabber.Exception e){
                e.printStackTrace();
            }
        });
        playThread.start();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {
        playThread.interrupt();
    }

    @Override
    public void seek(long time) {

    }

    @Override
    public void shuttle(Rate rate) {

    }

    @Override
    public void jog(int direction, Duration time) {

    }

    @Override
    public long getCurrentTime() {
        return 0;
    }

    @Override
    public float getFrameRate() {
        return 0;
    }

    @Override
    public Rate getRate() {
        return null;
    }

    @Override
    public void setRate(Rate rate) {

    }

    @Override
    public void add(StreamViewer stream) {

    }
}
