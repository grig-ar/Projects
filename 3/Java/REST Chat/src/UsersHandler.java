import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsersHandler implements HttpHandler {

    //private ArrayList<User> usersOnline;
    //public UsersHandler(UserControl userControl) {
        //this.usersOnline = userControl.getUsersOnline();
    //}

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
        String input = new String(exchange.getRequestBody().readAllBytes());
        JSONObject jsonObject = new JSONObject(input);
        String message = jsonObject.getString("message");
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
        User currentUser = getUserByToken(token, UserControl.getUsersOnline());
        currentUser.setLastAction(new Date().getTime());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (exchange.getRequestURI().getPath().length() - "/users".length() < 2) {
                    JSONObject output = new JSONObject();
                    JSONArray usersArray = new JSONArray();
                    for (User curUser : UserControl.getUsersOnline()) {
                        JSONObject item = new JSONObject();
                        item.put("id", curUser.getID());
                        item.put("username", curUser.getUsername());
                        item.put("online", curUser.isOnline());
                        usersArray.put(item);
                    }
                    output.put("users", usersArray);
                    response = output.toString();
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseHeaders().set("Content-Type:", "application/json");
                } else {
                    String[] args = exchange.getRequestURI().getPath().split("/");
                    if (args.length >= 4) {
                        response = "Bad Request";
                        exchange.sendResponseHeaders(400, response.length());
                        try (OutputStream outputStream = exchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    }
                    else {
                        int userID = Integer.parseInt(args[2]);
                        for (User curUser : UserControl.getUsersOnline()) {
                            if (userID == curUser.getID()) {
                                JSONObject output = new JSONObject();
                                output.put("id", curUser.getToken());
                                output.put("username", curUser.getUsername());
                                output.put("online", curUser.isOnline());
                                response = output.toString();
                                exchange.sendResponseHeaders(200, response.length());
                                exchange.getResponseHeaders().set("Content-Type:", "application/json");
                                break;
                            }
                        }
                        if (response == null) {
                            response = "Not Found";
                            exchange.sendResponseHeaders(404, response.length());
                            exchange.getResponseHeaders().set("Content-Type:", "application/json");
                            try (OutputStream outputStream = exchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                            return;
                        }
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
