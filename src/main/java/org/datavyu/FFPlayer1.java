package org.datavyu;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;

import java.io.*;

import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.swscale.*;

public class FFPlayer1 {

    public static void main(String arg[]) throws IOException {

        String sourceFileString = "DatavyuSampleVideo.mp4";

        AVFormatContext pFormatCtx = null; //stores information about the file format in the AVFormatContext structure

        AVCodecContext pCodecCtxOrig = null;
        AVCodecContext pCodecCtx = null;
        AVCodec pCodec = null;
        AVDictionary optionsDict = null;

        int videoStream = -1;
        int audioStream = -1;

        AVFrame pFrame = null;



        /**
         * This registers all available file formats
         * and codecs with the library so they will be
         * used automatically when a file with the corresponding
         * format/codec is opened.
         */
        av_register_all();

        /**
         * This function reads the file header and stores information
         * about the file format in the AVFormatContext structure we have
         * given it. The last three arguments are used to specify the file
         * format, buffer size, and format options, but by setting this
         * to NULL or 0, libavformat will auto-detect these.
         * This function only looks at the header, so next we need to check
         * out the stream information in the file
         */
        if(avformat_open_input(pFormatCtx, sourceFileString, null, null)!=0){
            System.exit(-1);
        }

        /**
         * Retrieve Stream information, this function populates
         * pFormatCtx->streams with the proper information.
         */
        if(avformat_find_stream_info(pFormatCtx, (PointerPointer) null) < 0){
            System.exit(-1);
        }

        /**
         * Dump information about file onto standard error,
         * Now pFormatCtx->streams is just an array of
         * pointers, of size pFormatCtx->nb_streams,
         */
        av_dump_format(pFormatCtx, 0, sourceFileString, 0);

        // Find the first video stream
        // Find the first audio sample(will hancle the audio after)

        for (int i = 0; i < pFormatCtx.nb_streams(); i++) {
            if (pFormatCtx.streams(i).codec().codec_type() == AVMEDIA_TYPE_VIDEO) {
                videoStream = i;
                break;
            }
            if (pFormatCtx.streams(i).codec().codec_type() == AVMEDIA_TYPE_AUDIO) {
                audioStream = i;
                break;
            }
        }

        if (videoStream == -1) {
            System.exit(-1); // Didn't find a video stream but we can check if we have Audio sample
        }


        /**
         * The stream's information about the codec is in what we
         * call the "codec context." This contains all the information
         * about the codec that the stream is using, and now we have
         * a pointer to it. But we still have to find the
         * actual codec and open it
         */
        // Get a pointer to the codec context for the video stream
        pCodecCtx = pFormatCtx.streams(videoStream).codec();


        // Find the decoder for the video stream
        pCodec = avcodec_find_decoder(pCodecCtx.codec_id());

        if (pCodec == null) {
            System.err.println("Unsupported codec!");
            System.exit(-1); // Codec not found
        }

        // Open codec
        if (avcodec_open2(pCodecCtx, pCodec, optionsDict) < 0) {
            System.exit(-1); // Could not open codec-
        }

        //Now we need a place to actually store the frame:
        //Allocate Video Frame
        pFrame = av_frame_alloc();

        /**
         * Since we're planning to output PPM files, which are
         * stored in 24-bit RGB, we're going to have to convert
         * our frame from its native format to RGB. ffmpeg will do
         * these conversions for us. For most projects
         * (including ours) we're going to want to convert our
         * initial frame to a specific format. Let's allocate
         * a frame for the converted frame now.
         */

        // Allocate an AVFrame structure
        AVFrame pFrameRGB = av_frame_alloc();
        if(pFrameRGB == null) {
            System.exit(-1);
        }

        /**
         * Even though we've allocated the frame, we still
         * need a place to put the raw data when we \
         * convert it. We use avpicture_get_size to get the
         * size we need, and allocate the space manually:
         */
        // Determine required buffer size and allocate buffer
        int numBytes = avpicture_get_size(AV_PIX_FMT_RGB24, pCodecCtx.width(), pCodecCtx.height());

        /**
         * av_malloc is ffmpeg's malloc that is just a simple
         * wrapper around malloc that makes sure the memory
         * addresses are aligned and such. It will not protect
         * you from memory leaks, double freeing, or other malloc problems.
         */
        BytePointer buffer = new BytePointer(av_malloc(numBytes));

        /**
         * Now we use avpicture_fill (av_image_fill_arrays) to associate the frame with our
         * newly allocated buffer. About the AVPicture cast: the
         * AVPicture struct is a subset of the AVFrame
         * struct - the beginning of the AVFrame struct is
         * identical to the AVPicture struct.
         */
        avpicture_fill(new AVPicture(pFrameRGB), buffer, AV_PIX_FMT_RGB24,
                pCodecCtx.width(), pCodecCtx.height());

        /**
         * What we're going to do is read through the entire video
         * stream by reading in the packet, decoding it into our
         * frame, and once our frame is complete, we will
         * convert and save it.
         */


        /**
         * The process, again, is simple: av_read_frame() reads in
         * a packet and stores it in the AVPacket struct. Note that
         * we've only allocated the packet structure - ffmpeg allocates
         * the internal data for us, which is pointed to by
         * packet.data. This is freed by the av_free_packet()
         * later. avcodec_decode_video() converts the
         * packet to a frame for us. However, we
         * might not have all the information we need for
         * a frame after decoding a packet, so avcodec_decode_video()
         * sets frameFinished for us when we have the
         * next frame. Finally, we use sws_scale() to
         * convert from the native format (pCodecCtx->pix_fmt) to
         * RGB. Remember that you can cast an AVFrame pointer
         * to an AVPicture pointer. Finally, we pass the frame
         * and height and width information to our SaveFrame function.
         */
        int i = 0;
        int[] frameFinished = new int[1];
        AVPacket packet =  new AVPacket();
        SwsContext sws_ctx = null;

        while (av_read_frame(pFormatCtx, packet) >= 0) {
            // Is this a packet from the video stream?
            if (packet.stream_index() == videoStream) {
                // Decode video frame (avcodec_send_packet())
                avcodec_decode_video2(pCodecCtx, pFrame, frameFinished, packet);
                int cpt = -1;
                // Did we get a video frame?
                if (frameFinished[0] != 0) {
                    // Convert the image from its native format to RGB
                    sws_scale(sws_ctx, pFrame.data(), pFrame.linesize(), 0,
                            pCodecCtx.height(), pFrameRGB.data(), pFrameRGB.linesize());
                    // Open file
                    OutputStream stream = new FileOutputStream("frame "+ cpt +".ppm");

                    // Write header
                    stream.write(("P6\n" + pCodecCtx.width() + " " + pCodecCtx.height() + "\n255\n").getBytes());

                    // Write pixel data
                    BytePointer data = pFrame.data(0);
                    byte[] bytes = new byte[pCodecCtx.width() * 3];
                    int l = pFrame.linesize(0);
                    for(int y = 0; y < pCodecCtx.height(); y++) {
                        data.position(y * l).get(bytes);
                        stream.write(bytes);
                    }

                    // Close file
                    stream.close();
                    cpt++;
                }
            }

            // Free the packet that was allocated by av_read_frame
            av_packet_unref(packet);
        }

        // Free the RGB image
        av_free(buffer);
        av_free(pFrameRGB);

        // Free the YUV frame
        av_free(pFrame);

        // Close the codec
        avcodec_close(pCodecCtx);

        // Close the video file
        avformat_close_input(pFormatCtx);

        System.exit(0);
    }

}
