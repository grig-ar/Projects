import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

class PortForwarder {
    private static final int BUFFER_SIZE = 8192;

    private int lPort;
    private String rHost;
    private int rPort;
    private Map<SocketChannel, SocketChannel> clients = new HashMap<>();

    PortForwarder(int aLPort, String aRHost, int aRPort) {
        lPort = aLPort;
        rHost = aRHost;
        rPort = aRPort;
    }

    void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(lPort));
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }

                    if (key.isConnectable())
                        ((SocketChannel) key.channel()).finishConnect();

                    if (key.isReadable()) {
                        doForwarding(buffer, key);
                    }
                }
                iter.remove();
            }
        }
    }

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(rHost, rPort));
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
            clients.put(client, channel);
        }
    }

    private void doForwarding(ByteBuffer buffer, SelectionKey key) throws IOException {
        int bytes;
        SocketChannel client = (SocketChannel) key.channel();
        SocketChannel destination;
        if (clients.containsKey(client))
            destination = clients.get(client);
        else
            destination = getKeyByValue(clients, client);

        if (destination != null) {
            if (destination.isConnected()) {
                bytes = client.read(buffer);
                if (bytes == -1) {
                    //System.out.println("No data available anymore. Closing stream.");
                    if (clients.containsKey(client)) {
                        clients.remove(client);
                        client.close();
                        destination.close();
                    }
                    else
                        clients.remove(destination);
                }
                if (bytes > 0) {
                    buffer.flip();
                    destination.write(buffer);
                }
                buffer.flip();
                buffer.clear();
            }
        }
    }

    private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
