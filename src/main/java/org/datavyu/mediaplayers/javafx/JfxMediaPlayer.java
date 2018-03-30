package org.datavyu.mediaplayers.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

public class JfxMediaPlayer extends Stage implements StreamViewer{

    //TODO: sync the streamviewr rate with the video controller rate
    //Think about removing the rate setter and make the rate listen to the video controller rate

    private final MediaPlayer mp;
    private final Identifier identifier;
    private boolean init = false;
    private MediaView mv;
    private long duration = -1;
    private long lastSeekTime = -1;
    private Rate rate;

    public JfxMediaPlayer(Identifier identifier, DatavyuMedia media) {
        this.identifier = identifier;
        mp = new MediaPlayer(media.getMedia());
        mp.setOnReady(new Runnable() {
            @Override
            public void run() {
//                logger.info("Creating a new media view.");
                mv = new MediaView(mp);

                final DoubleProperty width = mv.fitWidthProperty();
                final DoubleProperty height = mv.fitHeightProperty();

                width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
                height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

                mv.setPreserveRatio(true);

                StackPane root = new StackPane();
                root.getChildren().add(mv);

                final Scene scene = new Scene(root, 960, 540);
                scene.setFill(Color.BLACK);

                setScene(scene);
                setTitle(mp.getMedia().getSource());
                show();

                init = true;
            }
        });
    }

    public StreamViewer getStreamViewer(){ return this; }

    @Override
    public Identifier getIdentifier() { return this.identifier; }

    @Override
    public void play() { mp.play(); }

    @Override
    public void pause() {
//        logger.info("Pausing at time: " + mp.getCurrentTime());
        mp.pause();
    }

    @Override
    public void stop() {
        mp.stop();
    }

    @Override
    public void seek(long time) {
        if (lastSeekTime != time) {
//            logger.info("Seeking to: " + time);
            double rate = 0;
            if (mp.getRate() != 0) {
                mp.pause();
                rate = mp.getRate();
            }
            // NOTE: JavaFX only seems to be able to setCurrentTime accurately in 2.2 when the rate != 0, so lets fake that here.
            mp.setRate(1);
            mp.seek(Duration.millis(time));
            mp.setRate(rate);
            lastSeekTime = time;
        }
    }

    @Override
    public void jog(int direction, Duration time) {

    }

    @Override
    public long getCurrentTime() {
        return (long) mp.getCurrentTime().toMillis();
    }

    @Override
    public float getFrameRate() {
        return (float) 30;
    }

    public long getDuration() {
        if (duration == -1) {
            duration = (long) mp.getTotalDuration().toMillis();
        }
        return duration;
    }

    @Override
    public Rate getRate() {
        return Rate.getRate((float) mp.getRate());
    }

    @Override
    public void setRate(Rate rate) {
        this.mp.setRate(rate.getValue());
    }

    @Override
    public void add(StreamViewer stream) {
        //ONLY for DatavyuStatus
    }

    @Override
    public void shuttle(Rate rate) {
        this.setRate(rate);
    }

    public double height() {
        return this.mp.getMedia().getHeight();
    }

    public boolean isPlaying() { return mp.getStatus() == MediaPlayer.Status.PLAYING; }

    public void setScale(double scale) {
        this.setHeight(mp.getMedia().getHeight() * scale);
        this.setWidth(mp.getMedia().getWidth() * scale);
    }

    public void setVisible(final boolean visible) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mv.setVisible(visible);
                if (!visible) {
                    mp.setMute(true);
                    hide();
                } else {
                    mp.setMute(false);
                    show();
                }
            }
        });

    }

    public void setVolume(double volume) {
        mp.setVolume(volume);
    }

    public boolean isInit() {
        return init;
    }

    public void closeAndDestroy() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                close();
            }
        });
    }
}
