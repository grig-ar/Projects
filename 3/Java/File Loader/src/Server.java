import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

class PrintSpeed extends TimerTask {
    long bytesReceived;
    public void run() {
        if (bytesReceived != 0) {
            System.out.println("Instant speed: " + bytesReceived / 3 + " bytes/sec");
            bytesReceived = 0;
        }

    }

}

public class Server {
    private static String filePath = System.getProperty("user.dir") + "\\uploads";
    private static ServerSocket serverSocket = null;

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

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

//    private int init(String[] args) throws IOException {
//        if (args.length < 1) {
//            System.out.println("Port is not specified");
//            return 1;
//        }
//        var port = Integer.parseInt(args[0]);
//        var serverSocket = new ServerSocket(port);
//        return 0;
//    }

    private static File createFile(String fileName) {
        String truePath = null;
        File f;
        var extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        var name = fileName.substring(0, fileName.lastIndexOf('.'));
        var counter = 1;
        truePath = filePath + "\\" + fileName;
        var path = Paths.get(truePath);
        while (Files.exists(path)) {
            fileName = name + "(" + counter + ")." + extension;
            truePath = filePath + "\\" + fileName;
            path = Paths.get(truePath);
            ++counter;
        }
        f = new File(truePath);
        try {
            f.getParentFile().mkdirs();
            f.createNewFile();

        } catch (IOException e) {
            System.out.println("Failed to create file " + fileName);
        }
        return f;
    }

    private static void workWithFiles(Socket socket) throws IOException {
        String fileName = null;

        File f = null;
        int count;
        int bytesReceived = 0;
        byte[] buffer = new byte[9001];
        byte[] protocol = new byte[4096 + 1 + 8 + 16];
        byte[] checkSum = new byte[16];
        try (var inputStream = socket.getInputStream()) {
            count = inputStream.read(protocol);
            byte[] tempBuffer = null;
            if (count > 4121 || count == 0) {
                System.out.println("Incorrect protocol!");
            }
            for (var i = 0; i < 4096; ++i) {
                if (protocol[i] == '*') {
                    try {
                        fileName = new String(Arrays.copyOfRange(protocol, 0, i), "UTF-8");
                        long fileSize = bytesToLong(Arrays.copyOfRange(protocol, i + 1, i + 9));
                        checkSum = Arrays.copyOfRange(protocol, i + 9, i + 25);
                        tempBuffer = Arrays.copyOfRange(protocol, i + 25, protocol.length);
                        bytesReceived += 4121 - i;
                        break;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            f = createFile(fileName);

            try (var fileOutputStream = new FileOutputStream(f)) {
                fileOutputStream.write(tempBuffer);
                var start = System.nanoTime();
                var timer = new Timer();
                var printSpeed = new PrintSpeed();
                printSpeed.bytesReceived = bytesReceived;
                timer.schedule(printSpeed, 0, 3000);
                while ((count = inputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    printSpeed.bytesReceived += count;
                    bytesReceived += count;
                }
                timer.cancel();
                timer.purge();
                var end = System.nanoTime();
                var sec = (end - start) / (double)1_000_000_000;
                NumberFormat formatter = new DecimalFormat("#0");
                System.out.println("Avg speed: " + formatter.format(bytesReceived / sec) + " bytes/sec");
            }
        }
        checkHash(checkSum, f);
    }

    private static void checkHash(byte[] checkSum, File f) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] actualCheckSum = null;
        try {
            actualCheckSum = getFileChecksum(md, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Arrays.equals(actualCheckSum, checkSum))
            System.out.println("Receiving completed successfully!");
        else
            System.out.println("Receiving failed!");
    }

    private static void working(Socket clientSocket) {
        var working = new Thread(() -> {
            try {
                workWithFiles(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "working");
        working.start();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Port is not specified");
            return;
        }
        var port = Integer.parseInt(args[0]);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println ("Waiting for request...");
            while (true) {
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (clientSocket != null) {
                    System.out.print(clientSocket.getInetAddress().toString());
                    System.out.println(" connected");
                }
                working(clientSocket);

            }
    }
}

