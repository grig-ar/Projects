import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    static String userName;
    private static final String CONNECTION_MESSAGE = "Jd2UgV6xlCmK9gWbX91dsIHXqNTPK8HP";
//    public static final String DISCONNECTION_MESSAGE = "9MLcTr7wnUQN86n3uknz4yjzW0NTUMDR";
    ArrayList<String> usersOnline = new ArrayList<>();
    JTextArea incoming;
    JTextArea usersList;
    JTextField outgoing;
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket sock;
    WindowEvent wev;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Client client = new Client();
        System.out.println("Username:");
        userName = sc.nextLine();
        client.go();
    }

    public void go() {
        JFrame frame = new JFrame("Chat Client");
        JPanel mainPanel = new JPanel();
        Box areaBox = new Box(BoxLayout.LINE_AXIS);
        incoming = new JTextArea(15,25);
        usersList = new JTextArea(5,10);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        usersList.setEditable(false);
        usersList.append("Users online:\n");
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        JButton disconnectButton = new JButton("Disconnect");
        sendButton.addActionListener(new SendButtonListener());
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnectButton.setText("in future version");
//                try {
//                    out.writeObject(userName);
//                    out.writeObject(DISCONNECTION_MESSAGE);
//                } catch (Exception ex) {System.out.println("Sorry. Could not send it to the server");}
//                System.exit(0);
            }
        });
        areaBox.add(Box.createHorizontalGlue());
        areaBox.add(usersList);
        areaBox.add(Box.createRigidArea(new Dimension(10,0)));
        areaBox.add(qScroller);
        mainPanel.add(areaBox);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        mainPanel.add(disconnectButton);
        setUpNetworking();
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
        try {
            out.writeObject(userName);
            out.writeObject(CONNECTION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(450,350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 4242);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            System.out.println("Networking established");
        } catch (IOException e) { System.out.println("Can't connect to the server"); }
    }

    public class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                out.writeObject(userName);
                out.writeObject(": " + outgoing.getText());
            } catch (Exception ex) {System.out.println("Sorry. Could not send it to the server");}
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class IncomingReader implements Runnable {
        StringBuilder temp = new StringBuilder();
        Object obj = null;
        boolean first = true;
        boolean connect = false;
//        boolean dc = false;
        int counter = 0;
        public void run() {
            try {
                while ((obj = in.readObject()) != null) {
                    System.out.println("Got an object from server");
                    System.out.println(obj.getClass());
                    String nameToShow = (String) obj;
//                    if (dc) {
//                        usersList.setText("Users online:\n");
//                        usersList.append(nameToShow);
//                        dc = false;
//                        if(!usersList.getText().contains(userName))
//                            System.exit(0);
//                        continue;
//                    }
//                    if (nameToShow.equals(DISCONNECTION_MESSAGE)) {
//                        dc = true;
//                        continue;
//                    }
                    String[] elem = nameToShow.split("\n");
                    if (first) {
                        for (int i = 0; i < elem.length; ++i) {
//                            if (elem[elem.length - 1].contains(DISCONNECTION_MESSAGE)) {
//                                String[] disc = elem[elem.length - 1].split(DISCONNECTION_MESSAGE);
//                                String removing = disc[0];
//                                String textField = usersList.getText();
//                                String[] newText = textField.split("\n");
//                                usersList.setText("");
//                                for (int j = 0; j < newText.length; ++j) {
//                                    if (newText[j].equals(removing))
//                                        newText[j] = "";
//                                    usersList.append(newText[j]);
//                                    usersList.append("\n");
//                                }
//                                ++counter;
//                            }

                            if (!usersOnline.contains(elem[i])) {
                                usersOnline.add(elem[i]);
                                usersList.append(elem[i]);
                                usersList.append("\n");
                            }
                        }
                    }
                    if(first) {
                        temp.append(nameToShow);
                        first = false;
                    }
                    else {
                        temp.append(nameToShow).append("\n");
                        if (nameToShow.equals(CONNECTION_MESSAGE))
                            connect = true;
                        first = true;
                    }
                    if(!connect && first)
                        incoming.append(temp.toString());
                    else
                        connect = false;
                    ++counter;
                    if (counter == 2) {
                        temp.setLength(0);
                        counter = 0;
                    }
                }
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }
}
