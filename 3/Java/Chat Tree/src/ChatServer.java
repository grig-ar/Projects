import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread {
    public int port2 = 0;
    private final static int BUFFER = 1024;

    private DatagramSocket socket;
    private ArrayList<InetAddress> clientAddresses;
    private ArrayList<Integer> clientPorts;
    private HashSet<String> existingClients;

    public ChatServer(DatagramSocket sock, int p) throws IOException {
        socket = sock;
        port2 = p;
        clientAddresses = new ArrayList<>();
        clientPorts = new ArrayList<>();
        existingClients = new HashSet<>();
    }

    public void run() {
        byte[] buf = new byte[BUFFER];
        while (true) {
            try {
                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String content = new String(buf);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                String id = clientAddress.toString() + ":" + clientPort;
                if (!existingClients.contains(id) /*&& clientAddress.toString().equals("/127.0.0.1") && clientPort != port2*/) {
                    existingClients.add(id);
                    clientPorts.add(clientPort);
                    clientAddresses.add(clientAddress);
                }

                System.out.println(id + " : " + content);
                byte[] data = (id + " : " + content).getBytes();
                for (int i = 1; i < clientAddresses.size(); i++) {
                    InetAddress cl = clientAddresses.get(i);
                    int cp = clientPorts.get(i);
                    packet = new DatagramPacket(data, data.length, cl, cp);
                    socket.send(packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String args[]) throws Exception {
//        ChatServer s = new ChatServer();
//        s.start();
//    }
}
