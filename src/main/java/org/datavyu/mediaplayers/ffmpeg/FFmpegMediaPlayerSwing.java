package org.datavyu.mediaplayers.ffmpeg;

import javafx.stage.Stage;
import javafx.util.Duration;
import org.bytedeco.javacv.*;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import java.io.File;


/**
 * This is an FFmpeg Media Playerd displayed in a Swing Frame
 */
public class FFmpegMediaPlayerSwing implements StreamViewer {
    private final Identifier identifier;
    private DatavyuMedia media;
    private CanvasFrame jFrame;
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;

    //TODO: add a frame grabber and an audio grabber and a MovieStreamer to sync the two streams
    // FFmpeg Media Player will talk to the MovieStreamer

    public FFmpegMediaPlayerSwing(Identifier identifier, DatavyuMedia media){
        this.identifier = identifier;
        this.media = media;
        jFrame = new CanvasFrame(new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/DatavyuSampleVideo.mp4").toString());
        try {
            grabber = FFmpegFrameGrabber.createDefault(new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/DatavyuSampleVideo.mp4"));
            grabber.start();
//            recorder = new FFmpegFrameRecorder("DatavyuSampleVideo.avi", grabber.getImageWidth(), grabber.getImageHeight());
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public void play() {
        try {
            if(grabber.grab() != null){
                jFrame.showImage(grabber.grab());
//                recorder.record(grabber.grab());
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
//        catch (FrameRecorder.Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {
        try {
            grabber.stop();
            grabber.release();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
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
