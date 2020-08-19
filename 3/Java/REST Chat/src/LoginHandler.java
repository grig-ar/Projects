import com.sun.net.httpserver.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginHandler implements HttpHandler {

    private ArrayList<User> usersOnline;
    private AtomicInteger currentID = new AtomicInteger(0);

//    public LoginHandler(UserControl userControl) {
//        usersOnline = UserControl.getUsersOnline();
//    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //new UserControl();
        ArrayList<User> usersOnline = UserControl.getUsersOnline();
        String response = null;
        int token;
        switch (exchange.getRequestMethod()) {
            case "POST":
                if (exchange.getRequestURI().getPath().length() - "/login".length() < 2) {
                    String input = new String(exchange.getRequestBody().readAllBytes());
                    JSONObject jsonObject = new JSONObject(input);
                    String username = jsonObject.getString("username");
                    boolean newUser = true;
                    for (int i = 0; i < usersOnline.size(); ++i) {
                        if (username.equals(usersOnline.get(i).getUsername())) {
                            newUser = false;
                            break;
                        }
                    }
                    if (newUser) {
                        Random random = new Random();
                        token = random.nextInt();
                        UserControl.addUser(new User(currentID.incrementAndGet(), username, true, token));
                    }
                    else {
                        exchange.sendResponseHeaders(401, response.length());
                        exchange.getResponseHeaders().set("WWW-Authenticate", "Token realm='Username is already in use'");
                        break;
                    }
//                    response = "    \"id\":" + currentID +",\n" +
//                            "    \"username\": \" " + username + "\",\n" +
//                            "    \"online\": true,\n" +
//                            "    \"token\": \"<" + token + ">\"";
                    JSONObject output = new JSONObject();
                    output.put("id", currentID);
                    output.put("username", username);
                    output.put("online", "true");
                    output.put("token", token);
                    response = output.toString();
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseHeaders().set("Content-Type:", "application/json");
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
