import java.util.Date;

public class User {
    private int id;
    private int token;
    private String username;
    private boolean isOnline;
    private long lastAction;
    private int lastMsgID;
    User(int id, String username, Boolean isOnline, int token) {
        this.id = id;
        this.username = username;
        this.isOnline = isOnline;
        this.token = token;
        this.lastAction = new Date().getTime();
        this.lastMsgID = 0;
    }
    int getID() {return this.id;}
    String getUsername() {return this.username;}
    int getToken() {return this.token;}
    long getLastAction() {return this.lastAction;}
    int getLastMsgID() {return this.lastMsgID;}
    void setLastMsgID(int lastMsgID) {this.lastMsgID = lastMsgID;}
    void setLastAction(long lastAction) {this.lastAction = lastAction;}
    boolean isOnline() {return this.isOnline;}
    void setConnectionState(boolean state) {this.isOnline = state;}

}
