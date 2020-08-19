import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Client {
    private static OutputStream outputStream = null;
    private static byte[] protocol = null;
    private static byte[] buffer = new byte[9001];
    private static int port;
    private static String filePath = null;
    private static String servAddr = null;

    static byte[] getFileChecksum(MessageDigest digest, File file) throws IOException {
        var fileInputStream = new FileInputStream(file);
        var bytes = new byte[9001];
        var bytesCount = 0;

        while ((bytesCount = fileInputStream.read(bytes)) != -1) {
            digest.update(bytes, 0, bytesCount);
        }
        fileInputStream.close();
        var byteArray = digest.digest();

        return byteArray;
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    private int init(String[] args) {
        if (args.length < 1) {
            System.out.println("File is not specified");
        }
        if (args.length < 2) {
            System.out.println("Address is not specified");
        }
        if (args.length < 3) {
            System.out.println("Port is not specified");
            return 1;
        }
        filePath = args[0];
        servAddr = args[1];
        port = Integer.parseInt(args[2]);
        return 0;
    }

    private void sendFile() throws IOException {
        var file = new File(filePath);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        var checkSum = getFileChecksum(md, file);
        var stringBuilder = new StringBuilder();
        for (var i = 0; i < checkSum.length; ++i) {
            stringBuilder.append(Integer.toString((checkSum[i] & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println(stringBuilder.toString());
        var fileName = file.getName();
        long size = file.length();
        byte[] sizeToBytes = longToBytes(size);
        try {
            protocol = (fileName + "*").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] result = Arrays.copyOf(protocol, protocol.length + sizeToBytes.length);
        System.arraycopy(sizeToBytes, 0, result, protocol.length, sizeToBytes.length);
        protocol = result;
        try (var socket = new Socket(servAddr, port)) {
            outputStream = socket.getOutputStream();
            outputStream.write(protocol);
            outputStream.write(checkSum);
            try (var fileInputStream = new FileInputStream(file)) {
                int count;
                while ((count = fileInputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, count);

            }
        }
        System.out.println("Send complete");
        outputStream.close();
    }

    public static void main(String[] args) {
        int err;
        var client = new Client();
        err = client.init(args);
        if (err != 0) {
            return;
        }
        try {
            client.sendFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
