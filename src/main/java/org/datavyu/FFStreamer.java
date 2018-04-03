package org.datavyu;



import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import javax.sound.sampled.*;
import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FFStreamer {


    private static final String sourceFile = "/Users/reda/Databrary/datavyuByteDecoFFmpeg/src/main/java/DatavyuSampleVideo.mp4";//PATH to the video

    private static volatile Thread playThread;

    public static void main(String[] args) {
        playThread = new Thread(() -> {
            try {
                CanvasFrame jFrame = new CanvasFrame(sourceFile);
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(sourceFile);
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

                    Thread.sleep(10);
                    System.out.println("frame grabbed at " + grabber.getTimestamp());
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("end");
        });
        playThread.start();
    }
}