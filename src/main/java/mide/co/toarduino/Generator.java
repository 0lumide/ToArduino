package mide.co.toarduino;

import android.media.AudioTrack;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Olumide on 3/7/2015.
 * This class prepends the header information: A byte letting the arduino know what kind of data
 */
public class Generator {
    public static double period = 0.005;
    public static int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioTrack.MODE_STATIC);
    final static int MAX_SHORT = 32767;

    //basically make every bit last as long as as half a period
    private static byte[] encode(byte[] data){
        byte[] encoded = new byte[(int)(data.length * 16 * sampleRate * period * 0.5)];
        int c = 0;
        for (int i = 0; i < data.length; i++) {
            for(int s = 7; s >= 0; s--) {
                short bit = (short)((data[i] >> s) & 1);
                for (int j = 0; j < sampleRate * period * 0.5; j++) {
                    if(bit == 0)
                        encoded[c++] = squarify(j, (int)(sampleRate * period), false);
                    else
                        encoded[c++] = squarify(j, (int)(sampleRate * period), true);
                    if((c % 2) == 1)
                        encoded[c++] = squarify(j, (int)(sampleRate * period), false);
                    else
                        encoded[c++] = squarify(j, (int)(sampleRate * period), true);
                }
            }
        }
        System.out.println("\nGenerator");
        System.out.println(c);
        System.out.println(encoded.length);
        System.out.println(Arrays.toString(encoded));
        return encoded;
    }
    private static byte squarify(int pos, int limit, boolean up){
        if(up)
            return (byte)((-Math.pow((-1+(2*pos/limit)), 20) + 1) * 128);
        else
            return (byte)((-Math.pow((-1+(2*pos/limit)), 20) + 1) * -128);
    }
    public static byte[] make(int value){
        byte[] withHeader = ByteBuffer.allocate(5).put((byte)'i').putFloat(value).array();
        return encode(withHeader);
    }

    public static byte[] make(float value){
        byte[] withHeader = ByteBuffer.allocate(5).put((byte)'f').putFloat(value).array();
        return encode(withHeader);
    }

    public static byte[] make(double value){
        byte[] withHeader = ByteBuffer.allocate(9).put((byte)'d').putDouble(value).array();
        return encode(withHeader);
    }

    public static byte[] make(String value){
        int size = 1 + (value.length() * 2);
        ByteBuffer header = ByteBuffer.allocate(size).put((byte)'S');
        for(int i = 0; i < value.length(); i++){
            header.putChar(value.charAt(i));
        }
        return encode(header.array());
    }

    public static byte[] make(char value){
        byte[] withHeader = ByteBuffer.allocate(3).put((byte)'c').putChar(value).array();
        return encode(withHeader);
    }
}
