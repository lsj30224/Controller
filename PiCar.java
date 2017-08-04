/*
This Source Is Created By Seungjun Lee
I Don't allow Unauthorized Using.
Blog URL :  http://lsj30224.blog.me
*/
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.*;
import java.io.DataInputStream;
import java.io.*;
import java.net.*;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;


public class PiCar extends Thread
{


    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput PinOne = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "pin1",
                  PinState.LOW);
    final GpioPinDigitalOutput PinTwo = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "pin2",
                  PinState.LOW);

    private ServerSocket server;

    public PiCar(int port) throws IOException
    {
        server = new ServerSocket(port);
    }

    public void run()
    {
        Gpio.wiringPiSetup();
        SoftPwm.softPwmCreate(5, 0, 100);
        while(true)
        {
            try
            {
                //System.out.println("Waiting for client on port"+server.getLocalPort());
                Socket socket = server.accept();
                //System.out.println("Just Connected to "+server.getInetAddress());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                switch(dis.readUTF())
                {
                    case "STOP" :
                         PinOne.low();
                         PinTwo.low();
                         break;

                    case "GO" :
                         PinOne.high();
                         PinTwo.low();
                         break;

                    case "BACK" :
                         PinOne.low();
                         PinTwo.high();
                         break;

                    case "LTURN" :
                         System.out.println("Served");
                         SoftPwm.softPwmWrite(5, 13);
                         //try {Thread.sleep(250);} catch(InterruptedException e){e.getMessage();}
                         break;

                    case "RTURN" :
                         SoftPwm.softPwmWrite(5, 17);
                         //try {Thread.sleep(250);} catch(InterruptedException e){e.getMessage();}
                         break;

                    case "SNP" :
                         SoftPwm.softPwmWrite(5, 15);
                         //try {Thread.sleep(250);} catch(InterruptedException e){e.getMessage();}

                    default :
                         break;
                }

                socket.close();
            }
            catch(SocketTimeoutException s)
            {
                System.out.println("Time Out!");
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }





    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("This Program was made by Seungjun Lee");
        System.out.println("I Do Not Allow Unauthorized Use.");
        System.out.println("Blog URL : lsj30224.blog.me");

        final int PORT = 59807;
        try
        {
            new PiCar(PORT).start();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
