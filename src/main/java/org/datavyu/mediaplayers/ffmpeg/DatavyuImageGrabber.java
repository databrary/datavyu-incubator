package org.datavyu.mediaplayers.ffmpeg;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.datavyu.madias.DatavyuMedia;


public class DatavyuImageGrabber {
    private final Java2DFrameConverter converter;
    private FFmpegFrameGrabber grabber;

    private DatavyuImageGrabber(DatavyuMedia media) throws FrameGrabber.Exception {
        grabber =FFmpegFrameGrabber.createDefault(media.getSourceFile());
        grabber.start();
        converter = new Java2DFrameConverter();
    }

    public static DatavyuImageGrabber createImageGrabber(DatavyuMedia media) throws FrameGrabber.Exception {
        return new DatavyuImageGrabber(media);
    }

    public Image grabImage() throws FrameGrabber.Exception {
        Frame grabbedImage = this.grabber.grabImage();
        return SwingFXUtils.toFXImage(this.converter.convert(grabbedImage), null);
    }

    public double getImageWidth() {
        return this.grabber.getImageWidth();
    }

    public double getImageHeight() {
        return this.grabber.getImageHeight();
    }

    public void setTimestamp(long timestamp) throws FrameGrabber.Exception {
        this.grabber.setTimestamp(timestamp);
    }

    public double getFrameRate() {
        return this.grabber.getFrameRate();
    }
}
