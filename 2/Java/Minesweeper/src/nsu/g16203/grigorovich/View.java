package nsu.g16203.grigorovich;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

class View {
    private GameGUIField f;
    private ArrayList<JButton> buttonsList;
    BufferedImage[] view_images = new BufferedImage[14];
    private JFrame frame;
    private JPanel mainPanel;
    private Controller controller = new Controller();
    public void buildGUI(GameGUIField gameField) {
        f = gameField;
        File temp = new File(".");
        String s = temp.getAbsolutePath();
//        int curDir = s.lastIndexOf("\\");
//        String current = s.substring(curDir, s.length());
//        s = s.replace(current, "00");
//        curDir = s.lastIndexOf("\\");
//        current = s.substring(curDir, s.length());
//        s = s.replace(current, "");
//        System.out.println(s);
        try {
            for (int i = 0; i < 14; ++i) {
                File file = new File(s + "\\build\\classes\\nsu\\g16203\\grigorovich\\" + i + ".png");
                FileInputStream fis = new FileInputStream(file);
                view_images[i] = ImageIO.read(fis);
            }
        } catch (IOException e) { e.printStackTrace(); }
        f.images = view_images;
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonsList = new ArrayList<JButton>();
        frame.getContentPane().add(background);
        GridLayout grid = new GridLayout(f.xCoord, f.yCoord);
        grid.setVgap(0);
        grid.setHgap(0);
        mainPanel = new JPanel(grid);
        frame.setResizable(false);
        background.add(BorderLayout.CENTER, mainPanel);
        final int[] amount = {f.mines};
        Box labelBox = new Box(BoxLayout.Y_AXIS);
        JLabel minesInfo = new JLabel("Mines left: " + amount[0]);
        JLabel watch = new JLabel("Time: ");
        Timer clock = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.clock++;
                watch.setText("Time: " + f.clock);
            }
        });
        labelBox.add(minesInfo);
        labelBox.add(watch);
        background.add(BorderLayout.SOUTH, labelBox);
        for (int i = 0; i < f.xCoord*f.yCoord; i++) {
            JButton c = new JButton();
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Object a = e.getSource();
                    a = (JButton) a;
                    int x = ((JButton) a).getY()/16;
                    int y = ((JButton) a).getX()/16;
//                    System.out.println(x);
//                    System.out.println(y);
                    if(f.first)
                        clock.start();
                    int result = f.GUIfield[x][y].receiveCommand(e);
                    if (f.first && result == -2)  {
                        //while(f.getCell(x,y).getState() == -2 || f.getCell(x,y).getState() == -1) {
                        f.removeMine(x, y);
                        f.addMine(x, y);
                        f.GUIfield[x][y].isHidden = false;
                        //}
                        result = 0;
                    }
                    f.first = false;
                    switch (result) {
                        case -2:
                            clock.stop();
                            f.showAll();
                            f.isGame = false;
                            f.printField();
                            infoBox("You lose.");
                            break;
                        case 0:
                            if (f.getMinesEnv(x, y) == 0) {
                                for (int k = -1; k < 2; ++k) {
                                    for (int t = -1; t < 2; ++t) {
                                        if ((k + x >= 0) && (k + x < f.xCoord) && (t + y >= 0) && (t + y < f.yCoord)) {
                                            MouseEvent mouseEvent = new MouseEvent(buttonsList.get((k+x)*f.yCoord + (t+y)), MouseEvent.MOUSE_CLICKED, 0, InputEvent.BUTTON1_DOWN_MASK, 8, 8, 1, false );
                                            mouseClicked(mouseEvent);
                                        }
                                    }
                                }
                            }
                            f.printField();
                            break;
                        case -1: {
                            ++amount[0];
                            minesInfo.setText("Mines left: " + amount[0]);
                            break;
                        }
                        case 1: {
                            --amount[0];
                            minesInfo.setText("Mines left: " + amount[0]);
                            break;
                        }
                        default:
                            break;
                    }
                    if (controller.isVictory(f) && f.isGame) {
                        clock.stop();
                        infoBox("You win!");
                        f.isGame = false;
                    }
                    f.printField();
                }
            });
            JButton newGame = new JButton("New Game");
            newGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reset(controller.startNew(f));
                    clock.stop();
                    f.buttonsList = buttonsList;
                    amount[0] = f.mines;
                    minesInfo.setText("Mines left: " + amount[0]);
                    watch.setText("Time: ");
                    f.printField();
                }
            });
            Box buttonBox = new Box(BoxLayout.X_AXIS);
            buttonBox.add(newGame);
            background.add(BorderLayout.NORTH, buttonBox);
            c.setPreferredSize(new Dimension(16,16));
            c.setIcon(new ImageIcon(view_images[9]));
            c.setBorderPainted(false);
            buttonsList.add(c);
            mainPanel.add(c);
        }
        frame.setBounds(512, 256, 16*f.xCoord, 16*f.yCoord);
        frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        f.buttonsList = buttonsList;
    }
    public void reset(GameGUIField field) {
        f = field;
    }
    public static void infoBox(String infoMessage) {
        JOptionPane.showMessageDialog(null, infoMessage, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
