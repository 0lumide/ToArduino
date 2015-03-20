package mide.co.toarduino;

import android.media.AudioTrack;
import java.nio.ByteBuffer;

/**
 * Created by Olumide on 3/7/2015.
 * header information: A byte letting the arduino know what kind of data
 */
public class Generator {
    public static double period = 0.005;
    public static int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioTrack.MODE_STATIC);
    final static int MAX_SHORT = 32767;

    //basically make every bit last as long as as a period
    private static short[] encode(byte[] data){
        int numSamples = (int)(sampleRate * period);
        //8 for the number of bits in a byte
        short[] encoded = new short[(int)(data.length * 8 * numSamples)];
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            for(int s = 7; s >= 0; s--) {
                short bit = (short)((data[i] >> s) & 1);
                for (int j = 0; j < numSamples; j++) {
                    if(bit == 1){
                        encoded[count] = squarifyUp(j, numSamples);
                    }
                    else{
                        encoded[count] = squarifyDown(j, numSamples);
                    }
                    count++;
                }
            }
        }
        return encoded;
    }

    public static short squarifyUp(int sampleNum, int numSamples){
        return (short)(MAX_SHORT);
    }

    public static short squarifyDown(int sampleNum, int numSamples){
        return (short)-(MAX_SHORT);
    }

    public static short[] make(int value){
        byte[] withHeader = ByteBuffer.allocate(5).put((byte)'i').putFloat(value).array();
        return encode(withHeader);
    }

    public static short[] make(float value){
        byte[] withHeader = ByteBuffer.allocate(5).put((byte)'f').putFloat(value).array();
        return encode(withHeader);
    }

    public static short[] make(double value){
        byte[] withHeader = ByteBuffer.allocate(9).put((byte)'d').putDouble(value).array();
        return encode(withHeader);
    }

    public static short[] make(String value) throws StringTooLargeException{
        if (value.length() > 255){
            String message = String.format("String to be sent is %d characters greater than the 255 limit", (value.length() - 255));
            //Throw too large string exception
            throw new StringTooLargeException(message);
        }
        //1 for data type 1 for string length, hence max 256 then two per char
        int size = 2 + (value.length() * 2);
        ByteBuffer header = ByteBuffer.allocate(size).put((byte)value.length()).put((byte)'S');
        for(int i = 0; i < value.length(); i++){
            header.putChar(value.charAt(i));
        }
        return encode(header.array());
    }

    public static short[] make(char value){
        byte[] withHeader = ByteBuffer.allocate(3).put((byte)'c').putChar(value).array();
        return encode(withHeader);
    }
}
