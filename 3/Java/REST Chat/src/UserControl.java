import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class UserControl {
    static private ArrayList<User> usersOnline = new ArrayList<>(16);
    static private AtomicInteger msgID = new AtomicInteger(0);
    static private ArrayList<Message> messages = new ArrayList<>(64);

    static ArrayList<Message> getMessages() {return messages;}
    static ArrayList<User> getUsersOnline() {return usersOnline;}
    static int getMSGID() {return msgID.incrementAndGet();}
    static void addUser(User user) {
        usersOnline.add(user);
    }
    static void addMessage(Message msg) {
        messages.add(msg);
    }
}
