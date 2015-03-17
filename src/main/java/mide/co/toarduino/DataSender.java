package mide.co.toarduino;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Olumide on 3/7/2015.
 */
public class DataSender extends Thread{
    //Object that converts to binary, and adds the header
    private short[] idle;
    private short[] clock;
    private AudioTrack audioTrack;
    private int numSamples;
    Context context;

    public DataSender(Context context){
        this.context = context;
        numSamples = (int)(Generator.sampleRate * Generator.period);
        idle = new short[numSamples];
        clock = new short[numSamples];


        for(int i = 0; i < numSamples; i++){
            if(i < numSamples/2) {
                clock[i] = Generator.squarifyDown(i, numSamples/2);
            }
            else{
                clock[i] = Generator.squarifyUp(i, numSamples / 2);
            }
        }
    }

    public void run(){
//        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
//                Generator.sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
//                AudioFormat.ENCODING_PCM_16BIT, clock.length/2,
//                AudioTrack.MODE_STATIC);
//        audioTrack.write(clock, 0, clock.length);
//        audioTrack.setLoopPoints(0, clock.length/4, -1);
//        audioTrack.setNotificationMarkerPosition (clock.length/4);
//
//        audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
//            @Override
//            public void onPeriodicNotification(AudioTrack track) {
//                Log.v("Audio", "periodic marker reached");
//            }
//
//            @Override
//            public void onMarkerReached(AudioTrack track) {
//                Log.v("Audio", "marker reached");
//            }
//        });
//        audioTrack.play();
    }

    public void send(short[] data){
        short[] stereo = monoToStereo(data);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                Generator.sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, stereo.length/2,
                AudioTrack.MODE_STATIC);
        audioTrack.write(stereo, 0, stereo.length);
        System.out.println("Notification: " + audioTrack.setNotificationMarkerPosition(stereo.length/8));

        audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onPeriodicNotification(AudioTrack track) {
                //Do Nothing
            }

            @Override
            public void onMarkerReached(AudioTrack track) {
                Log.v("Audio", "marker reached");
                Toast.makeText(context, "Audio finished", Toast.LENGTH_SHORT).show();
            }
        });
        audioTrack.play();
    }

    private short[] monoToStereo(short[] monoShorts){
        short[] stereo = new short[monoShorts.length * 2];
        for(int i = 0; i < monoShorts.length; i++){
            stereo[2 * i] = (clock[i%numSamples]);
            stereo[2 * i + 1] = monoShorts[i];
        }
        return stereo;
    }
}
