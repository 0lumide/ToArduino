package mide.co.toarduino;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by Olumide on 3/7/2015.
 */
public class ToArduino {
    //The object that plays the sounds
    private DataSender dataSender;
    private Generator generator;
    Context context;

    public ToArduino(Context context){
        this.context = context;
        dataSender = new DataSender(context);
        dataSender.setName("ToArduino");
        dataSender.start();
    }

    public void sendInt(int value){
        short[] shortValue = Generator.make(value);
        dataSender.send(shortValue);
    }

    public void sendFloat(float value){
        short[] shortValue = Generator.make(value);
//        System.out.println(Arrays.toString(shortValue));
        dataSender.send(shortValue);
    }

    public void sendDouble(double value){
        short[] shortValue = Generator.make(value);
//        System.out.println(Arrays.toString(shortValue));
        dataSender.send(shortValue);
    }

    public void sendString(String value){
        short[] shortValue = Generator.make(value);
        dataSender.send(shortValue);
    }

    public void sendChar(double value){
        short[] shortValue = Generator.make(value);
//        System.out.println(Arrays.toString(shortValue));
        dataSender.send(shortValue);
    }
}
