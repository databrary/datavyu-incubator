package org.datavyu;



import org.bytedeco.javacv.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FFStreamer {


    private static final File sourceFile = new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/DatavyuSampleVideo.mp4");//PATH to the video

    private static volatile Thread playThread;

    public static void main(String[] args) {
        playThread = new Thread(() -> {
            try {
                CanvasFrame jFrame = new CanvasFrame(sourceFile.toString());
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(sourceFile);
                grabber.setFormat("mp4");

                grabber.start();

                Frame frame = null;

                //Read Audio
                AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
                soundLine.open(audioFormat);
                soundLine.start();

                ExecutorService executor = Executors.newSingleThreadExecutor();

                while (!Thread.interrupted() && grabber.grab() != null) {
                    frame = grabber.grab();
                    if (frame == null) {
                        break;
                    }

                    if (frame.image != null) {
                        jFrame.showImage(frame);
                    }else if (frame.samples != null){

                        ShortBuffer channelSamplesShortBuffer = (ShortBuffer) frame.samples[0];
                        channelSamplesShortBuffer.rewind();

                        ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesShortBuffer.capacity() * 2);

                        for (int i = 0; i < channelSamplesShortBuffer.capacity(); i++) {
                            short val = channelSamplesShortBuffer.get(i);
                            outBuffer.putShort(val);
                        }

                        try {
                            executor.submit(() -> {
                                soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
                                outBuffer.clear();
                            }).get();
                        } catch (InterruptedException interruptedException) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                }
                System.out.println("loop end with frame: " + frame);
                executor.shutdownNow();
                grabber.stop();
                grabber.release();
                jFrame.dispose();
            } catch (FrameGrabber.Exception ex) {
                System.out.println("exception: " + ex);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            System.out.println("end");
        });
        playThread.start();
    }
}