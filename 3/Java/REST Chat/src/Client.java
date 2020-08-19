import java.io.*;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Thread.sleep;

public class Client {

    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_COUNT = 10;
    private static final String DEFAULT_URL = "http://localhost:4554";
    private int token = -1;
    private boolean state = true;

    private void setState(boolean state) {this.state = state;}
    private boolean getState() {return this.state;}

    void exec(String[] operations, String url) throws IOException {
        switch(operations[0]) {
            case"/login":
                if (operations.length < 2) {
                    System.out.println("Incorrect operation");
                    return;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < operations.length; ++i) {
                    builder.append(operations[i]).append(" ");
                }
                execLogin(builder.toString().substring(0, builder.toString().length()-1), url);
                break;
            case"/logout":
                execLogout(url);
                break;
            case"/users":
                execUsers(url);
                break;
            case"/messages":
                int offset;
                int count;
                if (operations.length < 3) {
                    System.out.println("Incorrect operation");
                    return;
                }
                if (Integer.parseInt(operations[1]) < 0)
                    offset = DEFAULT_OFFSET;
                else
                    offset = Integer.parseInt(operations[1]);
                if (Integer.parseInt(operations[2]) < 0 || Integer.parseInt(operations[2]) > 100)
                    count = DEFAULT_COUNT;
                else
                    count = Integer.parseInt(operations[2]);
                execMessages(offset, count, url);
                break;
            default:
                System.out.println("Unknown operation");
                break;
        }
    }

    private void execLogin(String username, String url) throws IOException {
        URL serverURL = new URL(url + "/login");
        StringBuilder builder = new StringBuilder();
        HttpURLConnection urlConnection = (HttpURLConnection) serverURL.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        JSONObject output = new JSONObject();
        output.put("username", username);
        try (OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream())) {
            writer.write(output.toString());
            writer.flush();
        }
        int code = urlConnection.getResponseCode();
        if (code == 401) {
            System.out.println("This username already taken!");
            urlConnection.disconnect();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        JSONObject response = new JSONObject(builder.toString());
        token = response.getInt("token");
        System.out.println("login successful");
        this.setState(true);
        System.out.println("my token: " + token);
        urlConnection.disconnect();
        execMessages(DEFAULT_OFFSET, DEFAULT_COUNT, url);
    }

    private void execLogout(String url) throws IOException {
        if (token == -1) {
            return;
        }
        URL serverURL = new URL(url + "/logout");
        HttpURLConnection urlConnection = (HttpURLConnection) serverURL.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", String.valueOf(token));
        token = -1;
        int code = urlConnection.getResponseCode();
        if (code != 200) {
            System.out.println("Error: " + code + " " + urlConnection.getResponseMessage());
            urlConnection.disconnect();
            return;
        }
        urlConnection.disconnect();
        Main.stop();
    }

    private void execUsers(String url) throws IOException {
        if (token == -1) {
            System.out.println("You are not authorized");
            return;
        }
        URL serverURL = new URL(url + "/users");
        StringBuilder builder = new StringBuilder();
        HttpURLConnection urlConnection = (HttpURLConnection) serverURL.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", String.valueOf(token));
        int code = urlConnection.getResponseCode();
        if (code != 200) {
            System.out.println("Error: " + code + " " + urlConnection.getResponseMessage());
            if (code == 403)
                System.out.println("You are not authorized");
            urlConnection.disconnect();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        JSONObject response = new JSONObject(builder.toString());
        JSONArray arr = response.getJSONArray("users");
        for (int i = 0; i < arr.length(); ++i) {
            JSONObject inner = arr.getJSONObject(i);
            System.out.println("id: <" + inner.get("id") + ">");
            System.out.println("username: <" + inner.get("username") + ">" );
            System.out.println("online: <" + inner.get("online") + ">\n");
        }
        urlConnection.disconnect();
    }

    private void execMessages(int offset, int count, String url) throws IOException {
        if (token == -1) {
            System.out.println("You are not authorized");
            return;
        }
        URL serverURL = new URL(url + "/messages?offset=" + offset + "&count=" + count);
        StringBuilder builder = new StringBuilder();
        HttpURLConnection urlConnection = (HttpURLConnection) serverURL.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", String.valueOf(token));
        int code = urlConnection.getResponseCode();
        if (code != 200) {
            System.out.println("Error: " + code + " " + urlConnection.getResponseMessage());
            urlConnection.disconnect();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        urlConnection.disconnect();
        JSONObject response = new JSONObject(builder.toString());
        if (response.toString().equals("{}")) {
            System.out.println("No new messages");
            urlConnection.disconnect();
            return;
        }
        JSONArray arr = response.getJSONArray("messages");
        for (int i = ((offset > arr.length()) ? arr.length() : offset); i < ((offset + count) > arr.length() ? arr.length() : (offset + count)); ++i) {
            JSONObject inner = arr.getJSONObject(i);
            System.out.println("id: <" + inner.get("id") + ">");
            System.out.println("message: <" + inner.get("message") + ">" );
            System.out.println("author: <" + inner.get("author") + ">\n");
        }
        urlConnection.disconnect();
    }

    void sendMessage(String msg, String url) throws IOException {
        if (token == -1) {
            System.out.println("You are not authorized");
            return;
        }
        URL serverURL = new URL(url + "/messages");
        HttpURLConnection urlConnection = (HttpURLConnection) serverURL.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", String.valueOf(token));
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setDoOutput(true);
        JSONObject output = new JSONObject();
        output.put("message", msg);
        try (OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream())) {
            writer.write(output.toString());
            //writer.flush();
        }
        int code = urlConnection.getResponseCode();
        if (code != 200) {
            System.out.println("Error: " + code + " " + urlConnection.getResponseMessage());
            urlConnection.disconnect();
            return;
        }
        urlConnection.disconnect();
    }

     void update() {
        Thread update = new Thread(() -> {
            while(Main.getState()) {
                if (this.getState()) {
                    try {
                        execUpdate(DEFAULT_URL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "update");
        update.start();
    }

    private void execUpdate(String url) throws IOException {
        if (token == -1) {
            //System.out.println("You are not authorized");
            return;
        }
        URL serverURL = new URL(url + "/upd");
        StringBuilder builder = new StringBuilder();
        HttpURLConnection urlConnection = (HttpURLConnection) serverURL.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", String.valueOf(token));
        int code = urlConnection.getResponseCode();
        if (code != 200) {
            this.setState(false);
            System.out.println("Error: " + code + " " + urlConnection.getResponseMessage());
            urlConnection.disconnect();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        urlConnection.disconnect();
        JSONObject response = new JSONObject(builder.toString());
        if (response.toString().equals("{}")) {
            //System.out.println("No new messages");
            urlConnection.disconnect();
            return;
        }
        JSONArray arr = response.getJSONArray("messages");
        for (int i = 0; i < arr.length(); ++i) {
            JSONObject inner = arr.getJSONObject(i);
            System.out.println("id: <" + inner.get("id") + ">");
            System.out.println("message: <" + inner.get("message") + ">" );
            System.out.println("author: <" + inner.get("author") + ">\n");
        }
        urlConnection.disconnect();
    }

}
