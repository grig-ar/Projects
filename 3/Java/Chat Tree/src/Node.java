import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;


class Node {
    //private static final String NODE_UUID = UUID.randomUUID().toString();
    private static final Object SYNC = new Object();
    private static final int RETRIES = 5;
    private static final int BUFFER = 4096;
    private static final int TIMEOUT = 1000;
    private static final int MIN_LOSS_CHANCE = 0;
    private static final int MAX_LOSS_CHANCE = 100;
    private static final int INPUT_TIMEOUT = 100;
    private static final int SAVED_MESSAGES = 10;
    private static final int PARENT_PORT_MISSING = -1;
    private static final int SIMPLE_MSG = 7;
    private static final int ACK_MSG = 8;

    private String nodeName = null;
    private int packetLossChance;
    private int port;

    private byte[] protocol = null;
    private DatagramSocket nodeSocket = null;
    private LinkedHashSet<String> receivedMessages;
    private ConcurrentHashMap<String, Integer> unconfirmed; //address:port, hash
    private ArrayList<InetAddress> clientAddresses;
    private ArrayList<Integer> clientPorts;
    private HashSet<String> existingClients;

    private static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    private static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

//    private void broadcast(byte[] message) {
//        for (int i = 0; i < clientAddresses.size(); ++i) {
//            InetAddress clientAddr = clientAddresses.get(i);
//            int clientPort = clientPorts.get(i);
//            DatagramPacket packet = new DatagramPacket(message, message.length, clientAddr, clientPort);
//            try {
//                nodeSocket.send(packet);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void reSend(byte[] message, String from) {
        Thread t3 = new Thread(() -> {
            int tries = 0;
            //while (!unconfirmed.isEmpty()) {
            for (int i = 0; i < clientAddresses.size(); ++i) {
                InetAddress clientAddr = clientAddresses.get(i);
                int clientPort = clientPorts.get(i);
                while (tries < RETRIES) {
                    if (!((clientAddr.toString() + ':' + String.valueOf(clientPort)).equals(from)) && unconfirmed.containsKey(clientAddr.toString() + ':' + String.valueOf(clientPort))) {
                        DatagramPacket packet = new DatagramPacket(message, message.length, clientAddr, clientPort);
                        try {
                            nodeSocket.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            sleep(TIMEOUT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!unconfirmed.containsKey(clientAddr.toString() + ':' + String.valueOf(clientPort)))
                            break;
                        else
                            tries++;
                    } else
                        break;
                }
                if (tries == RETRIES) {
                    existingClients.remove(clientAddr.toString() + ':' + String.valueOf(clientPort));
                    clientAddresses.remove(i);
                    clientPorts.remove(i);
                    System.out.println(clientAddr.toString() + ':' + String.valueOf(clientPort) + " DISCONNECTED");
                }
                tries = 0;
            }
            //}
        }, "t3");
        t3.start();
    }


    Node(String name, int chance, int p, String parentIP, int parentPort) {
        nodeName = name;
        packetLossChance = chance;
        port = p;
        clientAddresses = new ArrayList<>();
        clientPorts = new ArrayList<>();
        existingClients = new HashSet<>();
        receivedMessages = new LinkedHashSet<>(SAVED_MESSAGES);
        unconfirmed = new ConcurrentHashMap<>(SAVED_MESSAGES);
        try {
            nodeSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (parentIP != null && parentPort != PARENT_PORT_MISSING) {
            existingClients.add(parentIP + ":" + String.valueOf(parentPort));
            try {
                clientAddresses.add(InetAddress.getByName(parentIP));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            clientPorts.add(parentPort);
        }
    }

    private void sendMessage(String msg) {
        byte[] buffer = null;
        String messageUUID = UUID.randomUUID().toString();

        //if (messageType == SIMPLE_MSG) {
        byte[] msgType = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(SIMPLE_MSG).array();
        byte[] mshHash = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt((messageUUID + msg).hashCode()).array();
        protocol = concat(msgType, mshHash);
        //protocol = concat(protocol, nodeName.getBytes("UTF-8"));
        buffer = concat(protocol, msg.getBytes(StandardCharsets.UTF_8));
        //broadcast(buffer);
        for (int i = 0; i < clientAddresses.size(); ++i) {
            //unconfirmed.put(byteArrayToInt(mshHash), clientAddresses.get(i).toString() + ':' + clientPorts.get(i));
            unconfirmed.put(clientAddresses.get(i).toString() + ':' + clientPorts.get(i), byteArrayToInt(mshHash));
        }
        reSend(buffer, nodeSocket.getLocalAddress().toString() + ':' + String.valueOf(port));
        protocol = null;
        //}

//        if (messageType == ACK_MSG) {
//
//            try {
//                buffer = "delivery confirmed".getBytes("UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            //broadcast(buffer);
//            reSend(buffer, nodeSocket.getLocalAddress().toString() + ':' + String.valueOf(port));
//        }

    }

    private void sendACK(int hash, String to) {
        String[] params = to.split(":");

        InetAddress clientAddr = null;
        params[0] = params[0].substring(1);
        try {
            clientAddr = InetAddress.getByName(params[0]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int clientPort = Integer.parseInt(params[1]);
        byte[] message = concat(ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(ACK_MSG).array(),
                ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(hash).array());

        DatagramPacket packet = new DatagramPacket(message, message.length, clientAddr, clientPort);

        try {
            nodeSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void broadcast() {

    }

    private void messageSender() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                while (!in.ready()) {
                    sleep(INPUT_TIMEOUT);
                }
                sendMessage(in.readLine());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveMessage() {
        byte[] buf = new byte[BUFFER];
        while (true) {
            try {
                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    nodeSocket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (packetLossChance < ThreadLocalRandom.current().nextInt(MIN_LOSS_CHANCE, MAX_LOSS_CHANCE)) {
                    if (byteArrayToInt(Arrays.copyOfRange(buf, 0, 4)) == SIMPLE_MSG) {
                        int msgHash = byteArrayToInt(Arrays.copyOfRange(buf, 4, 8));
                        String uniqueMessage = new String(Arrays.copyOfRange(buf, 4, buf.length), StandardCharsets.UTF_8);
                        String outputMessage = new String(Arrays.copyOfRange(buf, 8, buf.length), StandardCharsets.UTF_8);
                        if (!receivedMessages.contains(uniqueMessage)) {
                            InetAddress receivedAddress = packet.getAddress();
                            int receivedPort = packet.getPort();
                            String id = receivedAddress.toString() + ":" + String.valueOf(receivedPort);
                            if (!existingClients.contains(id)) {
                                existingClients.add(id);
                                clientPorts.add(receivedPort);
                                clientAddresses.add(receivedAddress);
                            }
                            receivedMessages.add(uniqueMessage);
                            System.out.println(id + " : " + outputMessage);
                            sendACK(msgHash, id);
                            reSend(buf, id);
                        }
                    }

                    if (byteArrayToInt(Arrays.copyOfRange(buf, 0, 4)) == ACK_MSG) {
                        InetAddress receivedAddress = packet.getAddress();
                        int receivedPort = packet.getPort();
                        String glob_id = receivedAddress.toString() + ":" + String.valueOf(receivedPort);
                        Integer msgHash = byteArrayToInt(Arrays.copyOfRange(buf, 4, 8));
                        //for (int i = 0; i < 5; ++i) {
                        if (unconfirmed.containsValue(msgHash)) {
                            unconfirmed.remove(glob_id);
                            System.out.println("Delivery Confirmed!");
                        }
                        //}

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void go() {
        Thread t1 = new Thread(this::receiveMessage);
        Thread t2 = new Thread(this::messageSender);
        t2.start();
        t1.start();
    }
}