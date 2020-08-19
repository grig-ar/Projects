import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LogoutHandler implements HttpHandler {

    //{     "message": "hello world!" }
    //{     "username": "art" }
    //Content-Type
    //application/json
    //Authorization
    //private ArrayList<User> usersOnline;
    //public LogoutHandler(UserControl userControl) {
        //this.usersOnline = userControl.getUsersOnline();
    //}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = null;
        switch (exchange.getRequestMethod()) {
            case "POST":
                if (exchange.getRequestURI().getPath().length() - "/logout".length() < 2) {
                    List<String> requestAuth = exchange.getRequestHeaders().getOrDefault("Authorization", null);
                    if (requestAuth == null) {
                        response = "Forbidden";
                        exchange.sendResponseHeaders(403, response.length());
                        try (OutputStream outputStream = exchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                        break;
                    }
                    int token = Integer.parseInt(requestAuth.get(0));
                    boolean userExists = false;
                    for (int i = 0; i < 16; ++i) {
                        if (token == UserControl.getUsersOnline().get(i).getToken()) {
                            userExists = true;
                            UserControl.getUsersOnline().get(i).setConnectionState(false);
                            System.out.println(UserControl.getUsersOnline().get(i).getUsername() + " left");
                            UserControl.getUsersOnline().remove(i);
                            response = "bye!";
                            exchange.getResponseHeaders().set("Content-Type", "application/json");
                            exchange.sendResponseHeaders(200, response.length());
                            break;
                        }
                    }
//                    if (!userExists) {
                        response = "Bad Request";
                        exchange.sendResponseHeaders(400, response.length());
                        try (OutputStream outputStream = exchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                            break;
                        }

//                    } else {
//                        response = "Bad Request";
//                        exchange.sendResponseHeaders(400, response.length());
//                        try (OutputStream outputStream = exchange.getResponseBody()) {
//                            outputStream.write(response.getBytes());
//                        }
//                        return;
//                    }
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

