package mide.co.toarduino;

import java.util.Arrays;

/**
 * Created by Olumide on 3/7/2015.
 */
public class ToArduino {
    //The object that plays the sounds
    private DataSender dataSender;
    private Generator generator;

    public ToArduino(){
        dataSender = new DataSender();
        dataSender.setName("ToArduino");
        dataSender.start();
    }

    public void sendInt(int value){
        byte[] byteValue = Generator.make(value);
        dataSender.send(byteValue);
    }

    public void sendFloat(float value){
        byte[] byteValue = Generator.make(value);
        System.out.println(Arrays.toString(byteValue));
        dataSender.send(byteValue);
    }

    public void sendDouble(double value){
        byte[] byteValue = Generator.make(value);
        System.out.println(Arrays.toString(byteValue));
        dataSender.send(byteValue);
    }

    public void sendString(String value){
        byte[] byteValue = Generator.make(value);
        dataSender.send(byteValue);
    }

    public void sendChar(double value){
        byte[] byteValue = Generator.make(value);
        System.out.println(Arrays.toString(byteValue));
        dataSender.send(byteValue);
    }
}
