import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class UpdateHandler implements HttpHandler {

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
        List<String> requestAuth = exchange.getRequestHeaders().getOrDefault("Authorization", null);
        if (requestAuth == null) {
            response = "Forbidden";
            exchange.sendResponseHeaders(403, response.length());
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(response.getBytes());
            }
            return;
        }
        int token = Integer.parseInt(requestAuth.get(0));
        if (!getUserByToken(token, UserControl.getUsersOnline()).isOnline())
            System.out.println("You are not authorized");
        switch (exchange.getRequestMethod()) {
            case "GET":
                User curUser = getUserByToken(token, UserControl.getUsersOnline());
                JSONObject output = new JSONObject();
                JSONArray messageArray = new JSONArray();
                //if (UserControl.getMessages().get(UserControl.getMessages().size()).getId() != curUser.getLastMsgID()) {
                if (UserControl.getMessages().size() != 0) {
                    for (int i = curUser.getLastMsgID(); i < UserControl.getMessages().size(); ++i) {
                        JSONObject item = new JSONObject();
                        item.put("id", UserControl.getMessages().get(i).getId());
                        item.put("message", UserControl.getMessages().get(i).getMessage());
                        item.put("author", UserControl.getMessages().get(i).getAuthor());
                        messageArray.put(item);
                    }
                    curUser.setLastMsgID(UserControl.getMessages().get(UserControl.getMessages().size()-1).getId());
                }
                //curUser.setLastMsgID(UserControl.getMessages().get(UserControl.getMessages().size()).getId());
                curUser.setLastAction(new Date().getTime());
                output.put("messages", messageArray);
                response = output.toString();
                exchange.getResponseHeaders().set( "Content-Type", "application/json" );
                exchange.sendResponseHeaders( 200, response.length() );
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
