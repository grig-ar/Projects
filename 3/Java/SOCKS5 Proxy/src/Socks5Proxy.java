import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.xbill.DNS.*;


class Socks5Proxy {

    //region Constants
    private static final byte VERSION_SOCKS5 = 0x05;
    private static final byte RESERVED_BYTE = 0x00;
    private static final byte NUMBER_OF_METHODS_SUPPORTED = 0x01;
    private static final byte METHOD_NO_AUTHENTICATION = 0x00;
    private static final byte COMMAND_ESTABLISH_TCP_IP_CONNECTION = 0x01;
    private static final byte ADDRESS_TYPE_IPV4 = 0x01;
    private static final byte ADDRESS_TYPE_DOMAIN_NAME = 0x03;
    private static final byte STATUS_SUCCEEDED = 0x00;
    private static final byte STATUS_GENERAL_FAILURE = 0x01;
    private static final String DNS_HOST = "8.8.8.8";
    private static final int DNS_PORT = 53;
    private static final int BUFFER_SIZE_COMMON = 8192;
    private static final int BUFFER_SIZE_GREETINGS = 2;
    private static final int BUFFER_SIZE_HEADER = 10;
    private static final int END_OF_STREAM = -1;
    //endregion

    //region Attachment
    class Attachment {

        SocketChannel client, remote;
        InetAddress address = null;
        boolean isConnected = false;
        boolean isGreetingsDone = false;
        int port = 0;
    }
    //endregion

    //region Private entities
    private int port;
    private HashMap<SocketChannel, Attachment> clientConnections = new HashMap<>();
    private HashMap<SocketChannel, Attachment> remoteConnections = new HashMap<>();
    private HashMap<Integer, Attachment> dnsResolvingConnections = new HashMap<>();
    //endregion

    //region Constructor
    Socks5Proxy(int port) {
        this.port = port;
    }
    //endregion

    //region Run server
    void runServer() throws IOException {
        try (
                ServerSocketChannel serverChannel = ServerSocketChannel.open();
                DatagramChannel dnsChannel = DatagramChannel.open();
                Selector selector = Selector.open()
        ) {
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            dnsChannel.configureBlocking(false);
            dnsChannel.connect(new InetSocketAddress(DNS_HOST, DNS_PORT));
            dnsChannel.register(selector, SelectionKey.OP_READ);
            startSelection(selector, serverChannel, dnsChannel);
        }
    }
    //endregion

    //region Selection process
    private void startSelection(Selector selector, ServerSocketChannel serverChannel, DatagramChannel dnsChannel) throws IOException {
        while (selector.select() > -1) {
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            for (SelectionKey key : selectedKeys) {
                if (key.isValid()) {
                    try {
                        if (key.isAcceptable()) {
                            accept(key, selector, serverChannel);
                        } else if (key.isConnectable()) {
                            ((SocketChannel) key.channel()).finishConnect();
                        } else if (key.isReadable()) {
                            read(key, selector, dnsChannel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //endregion

    //region Accepting connection
    private void accept(SelectionKey key, Selector selector, ServerSocketChannel serverChannel) throws IOException {
        SocketChannel newChannel = serverChannel.accept();
        if (newChannel != null) {
            Attachment attachment = new Attachment();
            attachment.client = newChannel;
            attachment.client.configureBlocking(false);
            clientConnections.put(newChannel, attachment);
            key.attach(attachment);
            newChannel.register(selector, SelectionKey.OP_READ);
        }
    }
    //endregion

    //region Finish connection
    private void connect(SelectionKey key, Attachment attachment, Selector selector) throws IOException {
        ArrayList<Attachment> onRemove = new ArrayList<>();
        try {
            attachment.remote = SocketChannel.open(new InetSocketAddress(attachment.address, attachment.port));
        } catch (SocketException e) {
            onRemove.add(attachment);
            return;
        }
        if (!attachment.client.isConnected() || !attachment.remote.isConnected()) {
            onRemove.add(attachment);
            return;
        }
        ByteBuffer temp = ByteBuffer.allocate(BUFFER_SIZE_HEADER);
        byte connectionStatus = attachment.remote.isConnected() ? STATUS_SUCCEEDED : STATUS_GENERAL_FAILURE;
        temp.put(VERSION_SOCKS5).put(connectionStatus).put(RESERVED_BYTE).put(ADDRESS_TYPE_IPV4).put(InetAddress.getLocalHost().getAddress()).putShort((short) attachment.port);
        attachment.client.write(ByteBuffer.wrap(temp.array(), 0, BUFFER_SIZE_HEADER));
        temp.clear();
        attachment.remote.configureBlocking(false);
        attachment.remote.register(selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
        attachment.isConnected = true;
        remoteConnections.put(attachment.remote, attachment);
        removeConnections(onRemove);
    }
    //endregion

    //region Choose reading method
    private void read(SelectionKey key, Selector selector, DatagramChannel dnsChannel) throws IOException {
        Attachment attachment;
        ArrayList<Attachment> onRemove = new ArrayList<>();
        if (key.channel() instanceof SocketChannel) {
            SocketChannel client = (SocketChannel) key.channel();
            attachment = clientConnections.getOrDefault(client, null);
            if (attachment == null) {
                attachment = remoteConnections.getOrDefault(client, null);
                if (attachment != null) {
                    readData(key, attachment);
                }
            } else {
                readHeader(key, attachment, dnsChannel, selector);
            }

        } else if (key.channel().equals(dnsChannel)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE_COMMON);
            int length = dnsChannel.read(byteBuffer);
            if (length > 0) {
                Message message = new Message(byteBuffer.array());
                Record[] records = message.getSectionArray(1);
                for (Record record : records) {
                    if (record instanceof ARecord) {
                        ARecord aRecord = (ARecord) record;
                        int id = message.getHeader().getID();
                        attachment = dnsResolvingConnections.get(id);
                        attachment.address = aRecord.getAddress();
                        connect(key, attachment, selector);
                        if (!attachment.client.isConnected()) {
                            onRemove.add(attachment);
                        }
                        dnsResolvingConnections.remove(id);
                        break;
                    }
                }
                byteBuffer.clear();
            }
        }
        removeConnections(onRemove);
    }
    //endregion

    //region Reading header
    private void readHeader(SelectionKey key, Attachment attachment, DatagramChannel dnsChannel, Selector selector) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE_COMMON);

        int length = -1;
        if (attachment.client.isConnected()) {
            try {
                length = attachment.client.read(byteBuffer);
            } catch (IOException e) {
                attachment.client.close();
                byteBuffer.clear();
                return;
            }
        }
        byteBuffer.flip();
        byte[] headerArray = byteBuffer.array();
        byteBuffer.flip();
        if (!attachment.isGreetingsDone) {
            if (length > 0) {
                if ((headerArray[0] != VERSION_SOCKS5 && headerArray[1] != NUMBER_OF_METHODS_SUPPORTED && headerArray[2] != METHOD_NO_AUTHENTICATION)) {
                    throw new IllegalStateException("Bad Request");
                }
                ByteBuffer temp = ByteBuffer.allocate(BUFFER_SIZE_GREETINGS);
                temp.put(VERSION_SOCKS5).put(METHOD_NO_AUTHENTICATION);
                attachment.client.write(ByteBuffer.wrap(temp.array(), 0, BUFFER_SIZE_GREETINGS));
                attachment.isGreetingsDone = true;
            } else if (length == END_OF_STREAM) {
                attachment.client.close();
            }
            byteBuffer.clear();
        } else {
            if (!attachment.isConnected) {
                if (length > 0) {
                    if ((headerArray[0] != VERSION_SOCKS5 && headerArray[1] != COMMAND_ESTABLISH_TCP_IP_CONNECTION && headerArray[2] != RESERVED_BYTE)) {
                        throw new IllegalStateException("Bad Request");
                    }
                    byte addressType = headerArray[3];
                    if (addressType != ADDRESS_TYPE_IPV4 && addressType != ADDRESS_TYPE_DOMAIN_NAME)
                        throw new IllegalStateException("Bad Request");
                    if (addressType == ADDRESS_TYPE_IPV4) {
                        byte[] addr = Arrays.copyOfRange(headerArray, 4, 8);
                        attachment.port = getPortValue(headerArray, 8);
                        attachment.address = InetAddress.getByAddress(addr);
                        connect(key, attachment, selector);
                    } else {
                        byte len = headerArray[4];
                        String address = new String(Arrays.copyOfRange(headerArray, 5, 5 + len), StandardCharsets.UTF_8);
                        attachment.port = getPortValue(headerArray, 5 + len);
                        Name name = Name.fromString(address, Name.root);
                        Record record = Record.newRecord(name, Type.A, DClass.IN);
                        Message message = Message.newQuery(record);
                        dnsChannel.write(ByteBuffer.wrap(message.toWire()));
                        dnsResolvingConnections.put(message.getHeader().getID(), attachment);
                    }
                } else if (length == END_OF_STREAM) {
                    attachment.client.close();
                }
            } else {
                if (attachment.client.isConnected()) {
                    if (length > 0) {
                        length = attachment.remote.write(ByteBuffer.wrap(byteBuffer.array(), 0, length));
                    } else if (length == END_OF_STREAM) {
                        attachment.client.close();
                        attachment.remote.close();
                    }
                }
            }
            byteBuffer.clear();
        }
    }
    //endregion

    //region Reading data
    private void readData(SelectionKey key, Attachment attachment) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE_COMMON);
        ArrayList<Attachment> onRemove = new ArrayList<>();
        if (attachment.remote.isConnected()) {
            try {
                int length = attachment.remote.read(byteBuffer);
                if (length > 0) {
                    length = attachment.client.write(ByteBuffer.wrap(byteBuffer.array(), 0, length));
                } else if (length == END_OF_STREAM) {
                    onRemove.add(attachment);
                }
            } catch (IOException e) {
                onRemove.add(attachment);
            }
        }
        removeConnections(onRemove);
        byteBuffer.clear();
    }
    //endregion

    //region Remove connections
    private void removeConnections(ArrayList<Attachment> attachments) throws IOException {
        for (Attachment attachment : attachments) {
            if (attachment.client.isConnected())
                attachment.client.close();
            if (attachment.remote != null && attachment.remote.isConnected())
                attachment.remote.close();
            clientConnections.remove(attachment.client);
            remoteConnections.remove(attachment.remote);
        }
    }
    //endregion

    //region Helper methods
    private int getPortValue(byte[] header, int position) {
        return (((0xFF & header[position]) << 8) + (0xFF & header[position + 1]));
    }
    //endregion

}
