import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;


public class Server {
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;

    private ArrayList<User> usersOnline = new ArrayList<>(16);
    public static void main(String[] args) {
        if (Integer.parseInt(args[0]) < MIN_PORT || Integer.parseInt(args[0]) > MAX_PORT) {
            System.out.println("Port value must be in range [1024, 65535]");
            return;
        }
        int port = Integer.parseInt(args[0]);
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new UserControl();
        if (server != null) {
            server.createContext("/login", new LoginHandler());
            server.createContext("/logout", new LogoutHandler());
            server.createContext("/message", new MessagesHandler());
            server.createContext("/users", new UsersHandler());
            server.createContext("/upd", new UpdateHandler());
            server.setExecutor(null);
            server.start();
        }
        timeout();
    }

        synchronized private static void timeout() {
        Thread timeout = new Thread(() -> {
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (User user : UserControl.getUsersOnline()) {
                    Date date = new Date();
                    if (user.isOnline() && (date.getTime() - user.getLastAction() > 30000)) {
                        user.setConnectionState(false);
                        UserControl.addMessage(new Message(UserControl.getMSGID(), user.getUsername() + " left by timeout", user.getID()));
                        System.out.println(user.getUsername() + " left by timeout");
                        UserControl.getUsersOnline().remove(user);
                        break;
                    }
                }
            }
        }, "timeout"
        );
        timeout.start();
    }

}
