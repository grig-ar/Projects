import java.io.IOException;


public class Main {

    //region Constants
    private static final int PORT_MIN = 1024;
    private static final int PORT_MAX = 65536;
    //endregion

    //region Main
    public static void main(String[] args) {
        int port;
        if (args.length < 1) {
            printHelp();
        } else {
            try {
                port = Integer.parseInt(args[0]);
                if (port > PORT_MAX || port < PORT_MIN)
                    throw new NumberFormatException();
                Socks5Proxy proxy = new Socks5Proxy(port);
                proxy.runServer();
            } catch (NumberFormatException e) {
                printHelp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Print help
    private static void printHelp() {
        System.out.println("SOCKS5 proxy v.0.0.5");
        System.out.println("Usage: java -jar SOCKS5.jar port");
        System.out.println("Port value must be in range (1024, 65536)");
    }
    //endregion

}
