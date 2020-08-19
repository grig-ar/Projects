import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static final int TIMEOUT = 1000;
    private static boolean working = true;

    private static void printHelp() {
        System.out.println("REST chat v.0.0.1");
        System.out.println("Usage: serverURL");
    }

    public static boolean getState() {
        return working;
    }

    public static void stop() {
        working = false;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
        }
        String serverURL = args[0];
        Client client= new Client();
        client.update();
        try ( BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (working) {
                String op = in.readLine();
                if (op.charAt(0) == '/') {
                    //if (op.length() < 5)
                        //System.out.println("Unknown operation");
                    if (op.indexOf( '/', 1 ) == 1)
                        client.sendMessage(op, serverURL);
                    else {
                        String[] splitted = op.split("\\s");
                        client.exec(splitted, serverURL);
                    }
                }
                else
                    client.sendMessage(op, serverURL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

