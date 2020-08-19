import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Arrays;

public class Test {
    static int port = 4356;
    private static DatagramSocket nodeSocket;

    static {
        try {
            nodeSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    String aMessage = "my message";
    InetAddress aHost;

    {
        try {
            aHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }



    public void sender() {
        byte [] m = new byte[0];
        try {
            m = aMessage.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DatagramPacket request = new DatagramPacket(m,aMessage.length(),aHost,port);
        try {
            nodeSocket.send(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiver() {
        byte[] buffer = new byte[64];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try {
            nodeSocket.receive(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String string = null;
        try {
            string = new String(reply.getData(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("DATA RECEIVED: " + string);
        System.out.println(reply.getAddress() + ":" + reply.getPort());
    }

    public void go() {
        Thread t1 = new Thread(this::sender);
        Thread t2 = new Thread(this::receiver);
        t1.start(); t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nodeSocket.close();
    }

    public static void main(String [] args){
            Test test = new Test();
            test.go();
            //nodeSocket.close();
//        DatagramSocket aSocket = null;
//
//        try{
//            DatagramSocket datagramSocket = new DatagramSocket(4555);
//            aSocket= new DatagramSocket(4356);
//
//            System.out.println("1");
//
//
//            //int serverPort = 6789;
//            DatagramPacket request = new DatagramPacket(m,aMessage.length(),aHost,4555);
//            System.out.println("2");
//            aSocket.send(request);
//            System.out.println("3");
//            byte [] buffer = new byte[1000];
//            System.out.println("4");
//            DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
//            datagramSocket.receive(reply);
//            System.out.println("5");
//            System.out.println("DATA RECEIVED" + Arrays.toString(reply.getData()));
//            aSocket.close();
//            datagramSocket.close();

    }
}
