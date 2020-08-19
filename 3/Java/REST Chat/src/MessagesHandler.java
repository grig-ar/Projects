import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class MessagesHandler implements HttpHandler {
    //private ArrayList<User> usersOnline;
    //private ArrayList<Message> messages;
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_COUNT = 10;

//    public MessagesHandler(UserControl userControl) {
//        this.usersOnline = userControl.getUsersOnline();
//        this.messages = userControl.getMessages();
//    }

        public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public static Map<String, String> parseQueryString(String qs) {
        Map<String, String> result = new HashMap<>();
        if (qs == null)
            return result;

        int last = 0, next, l = qs.length();
        while (last < l) {
            next = qs.indexOf('&', last);
            if (next == -1)
                next = l;

            if (next > last) {
                int eqPos = qs.indexOf('=', last);
                try {
                    if (eqPos < 0 || eqPos > next)
                        result.put(URLDecoder.decode(qs.substring(last, next), "utf-8"), "");
                    else
                        result.put(URLDecoder.decode(qs.substring(last, eqPos), "utf-8"), URLDecoder.decode(qs.substring(eqPos + 1, next), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e); // will never happen, utf-8 support is mandatory for java
                }
            }
            last = next + 1;
        }
        return result;
    }

    private User getUserByToken(int token, ArrayList<User> users) {
        for (User user : users) {
            if (user.getToken() == token)
                return user;
        }
        return null;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = null;
        int offset = DEFAULT_OFFSET;
        int count = DEFAULT_COUNT;
        String input = new String(exchange.getRequestBody().readAllBytes());
//        JSONObject jsonObject = new JSONObject(input);
//        String message = jsonObject.getString("message");
        List<String> requestAuth = exchange.getRequestHeaders().getOrDefault( "Authorization", null );
        if (requestAuth == null) {
            response = "Forbidden";
            exchange.sendResponseHeaders(403, response.length());
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(response.getBytes());
            }
            return;
        }
        int token = Integer.parseInt(requestAuth.get(0));
        User curUser = getUserByToken(token, UserControl.getUsersOnline());
        switch (exchange.getRequestMethod()) {
            case "GET":
                Map<String, String> args = parseQueryString(exchange.getRequestURI().getQuery());

                if (args.containsKey("offset"))
                    offset = Integer.parseInt(args.get("offset"));
                if (args.containsKey("count"))
                    count = Integer.parseInt(args.get("count"));

                if (count < 0 || count > 100 || offset < 0) {
                    response = "Bad Request";
                    exchange.sendResponseHeaders(400, response.length());
                    try (OutputStream outputStream = exchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                    return;
                }
                JSONObject output = new JSONObject();
                JSONArray messageArray = new JSONArray();
                if (UserControl.getMessages().size() > 0) {
                    for (int i = (offset > UserControl.getMessages().size()) ? UserControl.getMessages().size() : offset; i < ((offset + count) > UserControl.getMessages().size() ? UserControl.getMessages().size() : (offset + count)); ++i) {
                        JSONObject item = new JSONObject();
                        item.put("id", UserControl.getMessages().get(i).getId());
                        item.put("message", UserControl.getMessages().get(i).getMessage());
                        item.put("author", UserControl.getMessages().get(i).getAuthor());
                        messageArray.put(item);
                        curUser.setLastMsgID(UserControl.getMessages().get(i).getId());
                    }
                    output.put("messages", messageArray);
                }
                response = output.toString();
                exchange.getResponseHeaders().set( "Content-Type", "application/json" );
                exchange.sendResponseHeaders( 200, response.length() );
                break;
            case "POST":
                if (exchange.getRequestURI().getPath().length() - "/messages".length() < 2) {
                    JSONObject jsonObject = new JSONObject(input);
                    String message = jsonObject.getString("message");
                    curUser.setLastAction(new Date().getTime());
                    JSONObject output1 = new JSONObject();
                    Message classMessage = new Message(UserControl.getMSGID(), message, curUser.getID());
                    output1.put("id", curUser.getID());
                    output1.put("message", message);
                    UserControl.addMessage(classMessage);
                    response = output1.toString();
                    exchange.sendResponseHeaders( 200, response.length() );
                    exchange.getResponseHeaders().set( "Content-Type", "application/json" );
                }
                else {
                    response = "Bad Request";
                    exchange.sendResponseHeaders(400, response.length());
                    try (OutputStream outputStream = exchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                }
                break;
            default:
                response = "Bad Request";
                exchange.sendResponseHeaders(400, response.length());
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(response.getBytes());
                }
                return;
        }
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }
}
