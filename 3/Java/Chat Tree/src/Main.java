import java.util.regex.Pattern;

public class Main {
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;
    private static final int MIN_LOSS_CHANCE = 0;
    private static final int MAX_LOSS_CHANCE = 100;
    private static final Pattern IPADDRESSPATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private static boolean validate(final String ip) {
        return (IPADDRESSPATTERN.matcher(ip).matches() || ip.toLowerCase().equals("localhost"));
    }

    public static void main(String[] args) {
        String nodeName;
        int packetLossChance;
        int nodePort;
        String parentNodeIPAddress = null;
        int parentNodePort = -1;

        if (args.length == 5) {
            nodeName = args[0];

            if (Integer.parseInt(args[1]) < MIN_LOSS_CHANCE || Integer.parseInt(args[1]) > MAX_LOSS_CHANCE) {
                System.out.println("Packet loss chance must be in range [0, 100]");
                return;
            }
            packetLossChance = Integer.parseInt(args[1]);

            if (Integer.parseInt(args[2]) < MIN_PORT || Integer.parseInt(args[2]) > MAX_PORT) {
                System.out.println("Port value must be in range [1024, 65535]");
                return;
            }
            nodePort = Integer.parseInt(args[2]);

            if (!validate(args[3])) {
                System.out.println("Incorrect parent IP address");
                return;
            }
            parentNodeIPAddress = args[3];

            if (Integer.parseInt(args[4]) < MIN_PORT || Integer.parseInt(args[4]) > MAX_PORT) {
                System.out.println("Parent port value must be in range [1024, 65535]");
                return;
            }
            parentNodePort = Integer.parseInt(args[4]);
        } else if (args.length == 3) {
            nodeName = args[0];

            if (Integer.parseInt(args[1]) < MIN_LOSS_CHANCE || Integer.parseInt(args[1]) > MAX_LOSS_CHANCE) {
                System.out.println("Packet loss chance must be in range [0, 99]");
                return;
            }
            packetLossChance = Integer.parseInt(args[1]);

            if (Integer.parseInt(args[2]) < MIN_PORT || Integer.parseInt(args[2]) > MAX_PORT) {
                System.out.println("Port value must be in range [1024, 65535]");
                return;
            }
            nodePort = Integer.parseInt(args[2]);
        } else {
            printHelp();
            return;
        }

        Node node = new Node(nodeName, packetLossChance, nodePort, parentNodeIPAddress, parentNodePort);
        try {
            node.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Chat tree v.0.0.2");
        System.out.println("Usage: nodeName packetLossChance port [parentNodeIPAddress parentNodePort]");
    }
}
