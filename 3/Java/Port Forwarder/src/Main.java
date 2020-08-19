import java.io.IOException;


public class Main {
    private static final int MIN_PORT = 79;
    private static final int MAX_PORT = 65535;

    public static void main(String[] args) {
        int lPort;
        int rPort;
        String rHost;
        if (args.length == 3) {
            if (Integer.parseInt(args[0]) < MIN_PORT || Integer.parseInt(args[0]) > MAX_PORT) {
                System.out.println("Port value must be in range [79, 65535]");
                return;
            }
            lPort = Integer.parseInt(args[0]);

            rHost = args[1];

            if (Integer.parseInt(args[2]) < MIN_PORT || Integer.parseInt(args[2]) > MAX_PORT) {
                System.out.println("Port value must be in range [79, 65535]");
                return;
            }
            rPort = Integer.parseInt(args[2]);
        }
        else {
            printHelp();
            return;
        }
        PortForwarder portForwarder = new PortForwarder(lPort, rHost, rPort);
        try {
            portForwarder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void printHelp() {
        System.out.println("Port forwarder v.0.0.1");
        System.out.println("Usage: <lport> <rhost> <rport>");
    }
}
