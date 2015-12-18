package mide.co.toarduino;

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
        short[] shortValue = Generator.make(value);
        dataSender.send(shortValue);
    }

    public void sendFloat(float value){
        short[] shortValue = Generator.make(value);
        dataSender.send(shortValue);
    }

    public void sendDouble(double value){
        short[] shortValue = Generator.make(value);
        dataSender.send(shortValue);
    }

    public void sendString(String value) throws StringTooLargeException{
        short[] shortValue = Generator.make(value);
        dataSender.send(shortValue);
    }

    public void sendChar(double value){
        short[] shortValue = Generator.make(value);
        dataSender.send(shortValue);
    }
}
