public class Message {

    private int id;
    private String message;
    private int authorID;
    public Message(int id, String message, int authorID){
        this.id = id;
        this.message = message;
        this.authorID = authorID;
    }
    int getId() {return this.id;}
    String getMessage() {return this.message;}
    int getAuthor() {return this.authorID;}
}
