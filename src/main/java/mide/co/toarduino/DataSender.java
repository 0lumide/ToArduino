package mide.co.toarduino;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Olumide on 3/7/2015.
 */
public class DataSender extends Thread{
    //Object that converts to binary, and adds the header
    private byte[] start;
    private byte[] stop;
    private byte[] clock;
    private AudioTrack audioTrack;

    public DataSender(){
        int numSamples = (int)(Generator.sampleRate * Generator.period);
        start = new byte[2*numSamples];
        stop = new byte[2*numSamples];
        clock = new byte[2*numSamples];

        int stopId = 0;
        int clockId = 0;
        int startId = 0;
        for(int i = 0; i < numSamples; i++){
            if((2*i / numSamples) == 0){
                clock[clockId++] = (byte)((-Math.pow((-1+(4*i/numSamples)), 20) + 1) * -128);
                clock[clockId++] = (byte)((-Math.pow((-1+(4*i/numSamples)), 20) + 1) * -128);
            }
            else{
                clock[clockId++] = (byte)((Math.pow((-1+((4*i - 2*numSamples)/numSamples)), 20) - 1) * 128);
                clock[clockId++] = (byte)((Math.pow((-1+((4*i - 2*numSamples)/numSamples)), 20) - 1) * 128);
            }
            start[startId++] = (byte) (Math.sin(50*i) * 127);
            start[startId++] = (byte) -128;
        }
    }

    public void run(){
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                Generator.sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_8BIT, clock.length/2,
                AudioTrack.MODE_STATIC);
        audioTrack.write(clock, 0, clock.length);
        audioTrack.setLoopPoints(0, clock.length/4, -1);
        audioTrack.setNotificationMarkerPosition (clock.length/4);

        audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onPeriodicNotification(AudioTrack track) {
                Log.v("Audio", "periodic marker reached");
            }

            @Override
            public void onMarkerReached(AudioTrack track) {
                Log.v("Audio", "marker reasched");
            }
        });
        audioTrack.play();
    }

    public void send(byte[] bytes){
        byte[] stereo = monoToStereo(bytes);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                Generator.sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_8BIT, stereo.length/2,
                AudioTrack.MODE_STATIC);
        audioTrack.write(stereo, 0, stereo.length);
        System.out.println(stereo.length);
        System.out.println(audioTrack.setLoopPoints(0, stereo.length/4, -1));
        audioTrack.setNotificationMarkerPosition (stereo.length/4);
        audioTrack.write(stereo, 0, stereo.length);
        audioTrack.play();
    }

    private byte[] monoToStereo(byte[] monoBytes){
        byte[] stereo = new byte[monoBytes.length * 2];
        for(int i = 0; i < monoBytes.length; i++){
            stereo[2 * i] = monoBytes[i];
            stereo[2 * i + 1] = (byte) ((i/(Generator.sampleRate * Generator.period * 0.5))%2);
        }
        return stereo;
    }


}
