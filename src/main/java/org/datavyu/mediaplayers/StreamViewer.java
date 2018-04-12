package org.datavyu.mediaplayers;


import javafx.beans.property.LongProperty;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

/**
 * The StreamViewer represents the view of the media (Media Player)
 */
public interface StreamViewer {

    Slider getStreamTimeSlider();

    /**
     * A unique identifier for the StreamViewer
     *
     * @return identifier
     */

    Identifier getIdentifier();

    /**
     * Play the Stream
     */
    void play();

    /**
     * Pause the Stream, must not affect the rate
     */
    void pause();

    /**
     * Stop the Stream, must change the rate to 0x, and rewind to the beginning
     */
    void stop();

    /**
     * Seek to to a specific TimeStamp
     *
     * @param timeStamp Timestamp
     */
    void seekTime(long timeStamp);

    /**
     * Shuttle, The media player must
     * shuttle from -1/32X to 32X speed
     *
     * @param rate Rate Speed
     */
    void shuttle(Rate rate);

    /**
     * Jog by Time
     *
     * @param direction The direction -1 for backward and +1 for forward
     * @param time Time
     */
    void jog(int direction, Duration time);

    /**
     * Get the streamviewer current time
     *
     * @return
     */
    long getCurrentTime();

    /**
     * Get the Frame Per Second
     *
     * @return
     */
    float getFrameRate();

    /**
     * Set the Frame Per Second, could be used
     * to align multiple stream FPS in order to grab
     * frames and samples at the same interval of time
     *
     * @param fps
     */
    void setFrameRate(float fps);

    /**
     * Get the number of the frame displayed on the screen
     *
     * @return
     */
    int getCurrentFrame();

    LongProperty currentTimeProperty();

    /**
     * Get the current Rate (Playback Speed) of the StreamViewer
     *
     * @return
     */
    Rate getRate();

    /**
     * Set the current Rate (Playback Speed) of the StreamViewer
     *
     * @param rate Rate
     */
    void setRate(Rate rate); //Should check the state of the video controller

    /**
     * Get the Audio Volume.
     */
    int getVolume();

    /**
     * Set the Audio Volume.
     *
     * @param volume
     */
    void setVolume(int volume);

    /**
     * Show the StreamViewer
     */
    void visible();

    /**
     * Hide the StreamViewer
     */
    void hide();

    /**
     * Close and Destroy the StreamViewer
     */
    void close();

    /**
     *
     * @param time
     */
    void back(Duration time);

    long getDuration();
}
