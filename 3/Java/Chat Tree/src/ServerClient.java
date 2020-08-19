//import java.io.*;
//import java.net.*;
//import java.util.*;
//
//class MessageSender implements Runnable {
//    private final static int PORT = 7331;
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
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
//class MessageReceiver implements Runnable {
//    private DatagramSocket sock;
//    private byte buf[];
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
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
//public class ServerClient extends Thread {
//    private final static int PORT = 7331;
//    private final static int BUFFER = 1024;
//
//    private DatagramSocket socket;
//    private ArrayList<InetAddress> clientAddresses;
//    private ArrayList<Integer> clientPorts;
//    private HashSet<String> existingClients;
//
//    public ServerClient(int port) throws IOException {
//        socket = new DatagramSocket(port);
//        clientAddresses = new ArrayList<>();
//        clientPorts = new ArrayList<>();
//        existingClients = new HashSet<>();
//    }
//
//    public void run() {
//        byte[] buf = new byte[BUFFER];
//        while (true) {
//            try {
//                Arrays.fill(buf, (byte)0);
//                DatagramPacket packet = new DatagramPacket(buf, buf.length);
//                socket.receive(packet);
//
//                String content = new String(buf);
//
//                InetAddress clientAddress = packet.getAddress();
//                int clientPort = packet.getPort();
//
//                String id = clientAddress.toString() + ":" + clientPort;
//                if (!existingClients.contains(id)) {
//                    existingClients.add(id);
//                    clientPorts.add(clientPort);
//                    clientAddresses.add(clientAddress);
//                }
//
//                System.out.println(id + " : " + content);
//                byte[] data = (id + " : " +  content).getBytes("UTF-8");
//                for (int i = 0; i < clientAddresses.size(); ++i) {
//                    InetAddress cl = clientAddresses.get(i);
//                    int cp = clientPorts.get(i);
//                    packet = new DatagramPacket(data, data.length, cl, cp);
//                    socket.send(packet);
//                }
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String args[]) {
//        ServerClient s = null;
//        String addr = null;
//        int port;
//        if (args.length < 2) {
//            port = PORT;
//            addr = "localhost";
//        }
//        else {
//            addr = args[0];
//            port = Integer.parseInt(args[1]);
//        }
//        try {
//            s = new ServerClient(port);
//            DatagramSocket socket = new DatagramSocket();
//            MessageReceiver receiver = new MessageReceiver(socket);
//            MessageSender sender = new MessageSender(socket, addr);
//            Thread rt = new Thread(receiver);
//            Thread st = new Thread(sender);
//            rt.start();
//            st.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (s != null) {
//            s.start();
//        }
//    }
//}
