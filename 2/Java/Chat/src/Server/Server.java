import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    private static final String CONNECTION_MESSAGE = "Jd2UgV6xlCmK9gWbX91dsIHXqNTPK8HP";
//    public static final String DISCONNECTION_MESSAGE = "9MLcTr7wnUQN86n3uknz4yjzW0NTUMDR";
    ArrayList<ObjectOutputStream> clientOutputStreams;
    ArrayList<String> usersOnline = new ArrayList<>();
    StringBuilder temp = new StringBuilder();
//    int k;
//    boolean dc;

    public class ClientHandler implements Runnable {
        ObjectInputStream in;
        Socket clientSocket;
        public ClientHandler(Socket socket) {
            try {
                clientSocket = socket;
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (Exception ex) {ex.printStackTrace();}
        }

        public void run() {
            Object o1 = null;
            Object o2 = null;
            try {
                while ((o1 = in.readObject()) != null) {
                    o2 = in.readObject();
                    if (o2.equals(CONNECTION_MESSAGE) && !usersOnline.contains((String) o1)) {
                        usersOnline.add((String) o1);
                        usersOnline.add("\n");
                        for (int i = 0; i < usersOnline.size() - 1; ++i) {
                            temp.append(usersOnline.get(i));
                        }
                        o1 = temp.toString();
                        System.out.println("Read username");
                    }
//                    if (o2.equals(DISCONNECTION_MESSAGE) && usersOnline.contains((String) o1)) {
//                        k = usersOnline.lastIndexOf((String) o1);
//                        dc = true;
////                        clientOutputStreams.remove(k);
//                        usersOnline.remove((String) o1);
//                        usersOnline.remove(k);
//                        for (int i = 0; i < usersOnline.size() - 1; ++i) {
//                            temp.append(usersOnline.get(i));
//                        }
//                        o1 = DISCONNECTION_MESSAGE;
//                        o2 = temp.toString();
//                        System.out.println("Remove username");
//                    }
                    else
                        System.out.println("Read two objects");
                    temp.setLength(0);
                    tellEveryone(o1, o2);
                }
            } catch (Exception ex) {System.out.println("Client disconnected");}
        }
    }

    public static void main (String[] args) {
        new Server().go();
    }

    public void go() {
        clientOutputStreams = new ArrayList<>();
        try {
            ServerSocket serverSock = new ServerSocket(4242);
            while (true) {
                Socket clientSocket = serverSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(out);
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("new client was connected");
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void tellEveryone(Object one, Object two) {
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(one);
                out.writeObject(two);
            } catch (Exception ex) {ex.printStackTrace();}
        }
//        clientOutputStreams.remove(k);
//        dc = false;
    }
}
