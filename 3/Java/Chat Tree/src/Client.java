//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.SocketException;
//
///**
// *
// * @author SHUBHAM
// */
//public class Client {
//
//
//    public static void main(String[] args) throws SocketException, IOException {
//
//        BufferedReader clientRead =new BufferedReader(new InputStreamReader(System.in));
//
//        InetAddress IP = InetAddress.getByName("127.0.0.1");
//        //int c=5;
//        DatagramSocket clientSocket = new DatagramSocket();
//        while(true)    //true
//        {
//            byte[] sendbuffer = new byte[1024];
//            byte[] receivebuffer = new byte[1024];
//
//            System.out.print("\nClient: ");
//            String clientData = clientRead.readLine();
//            sendbuffer = clientData.getBytes();
//            DatagramPacket sendPacket =
//                    new DatagramPacket(sendbuffer, sendbuffer.length, IP, 9876);
//            clientSocket.send(sendPacket);
//            if(clientData.equalsIgnoreCase("bye"))
//            {
//                System.out.println("Connection ended by client");
//                break;
//            }
//            DatagramPacket receivePacket =
//                    new DatagramPacket(receivebuffer, receivebuffer.length);
//            clientSocket.receive(receivePacket);
//            String serverData = new String(receivePacket.getData());
//            System.out.print("\nServer: " + serverData);
//
//            //checking condition for equals with serverData which is my string
//            //c--;
//        }
//        clientSocket.close();
//    }
//
//}
/*---------------------------*/

import java.io.*;
import java.net.*;


//class MessageSender implements Runnable {
//    public final static int PORT = 7331;
//    private DatagramSocket sock;
//    private String hostname;
//    MessageSender(DatagramSocket s, String h) {
//        sock = s;
//        hostname = h;
//    }
//    private void sendMessage(String s) throws Exception {
//        byte buf[] = s.getBytes();
//        InetAddress address = InetAddress.getByName(hostname);
//        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
//        sock.send(packet);
//    }
//    public void run() {
//        boolean connected = false;
//        do {
//            try {
//                sendMessage("GREETINGS");
//                connected = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } while (!connected);
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            try {
//                while (!in.ready()) {
//                    Thread.sleep(100);
//                }
//                sendMessage(in.readLine());
//            } catch(Exception e) {
//                System.err.println(e);
//            }
//        }
//    }
//}
//class MessageReceiver implements Runnable {
//    DatagramSocket sock;
//    byte buf[];
//    MessageReceiver(DatagramSocket s) {
//        sock = s;
//        buf = new byte[1024];
//    }
//    public void run() {
//        while (true) {
//            try {
//                DatagramPacket packet = new DatagramPacket(buf, buf.length);
//                sock.receive(packet);
//                String received = new String(packet.getData(), 0, packet.getLength());
//                System.out.println(received);
//            } catch(Exception e) {
//                System.err.println(e);
//            }
//        }
//    }
//}
//public class Client {
//
//    public static void main(String args[]) throws Exception {
//        String host = null;
//        if (args.length < 1) {
//            System.out.println("Usage: java ChatClient <server_hostname>");
//            System.exit(0);
//        } else {
//            host = args[0];
//        }
//        DatagramSocket socket = new DatagramSocket();
//        MessageReceiver r = new MessageReceiver(socket);
//        MessageSender s = new MessageSender(socket, host);
//        Thread rt = new Thread(r);
//        Thread st = new Thread(s);
//        rt.start(); st.start();
//    }
//}