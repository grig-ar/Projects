package nsu.g16203.grigorovich;

import java.awt.event.*;
import java.util.*;

public class Main {
    private static String mode;
    private static boolean endOfGame;
    private static boolean first;
    private static GameField gameField;
    private static View view;

    public static void main(String[] args) {
//        System.out.println("Working Directory = " +
//                System.getProperty("user.dir"));
        init();
        if (mode.equals("gui"))
            view.buildGUI((GameGUIField) gameField);
        else
            gameField.printField();
        if (mode.equals("text")) {
            while (!endOfGame) {
                String[] cmd;
                cmd = input();
                logic(cmd);
                first = false;
                gameField.printField();
            }
        }
    }

    private static void init() {
        Scanner sc = new Scanner(System.in);
        String temp;
        String[] commands;
        System.out.println("Enter the game mode(gui/text): ");
        mode = sc.nextLine();
        mode = mode.toLowerCase();
        while (!mode.equals("gui") && !mode.equals("text")) {
            mode = mode.toLowerCase();
            System.out.println("Incorrect mode. Try again");
            mode = sc.nextLine();
        }
        System.out.println("Enter the field size and mines amount in format: x y amount");
        temp = sc.nextLine();
        commands = temp.split(" ");
        int xTemp = Integer.parseInt(commands[0]);
        int yTemp = Integer.parseInt(commands[1]);
        int amount = Integer.parseInt(commands[2]);
        if(mode.equals("gui")) {
            gameField = new GameGUIField(xTemp, yTemp, amount);
            view = new View();
        }
        if(mode.equals("text"))
            gameField = new GameField(xTemp,yTemp,amount);
        endOfGame = false;
        first = true;
    }

    private static String[] input() {
        System.out.println("Enter the command[open][mark] and coordinates in format: cmd x y");
        Scanner sc = new Scanner(System.in);
        String temp;
        String[] commands;
        temp = sc.nextLine();
        commands = temp.split(" ");
        return commands;
    }
    private static void logic(String[] commands) {
        int res;
        MouseEvent e = null;
        int xTemp = Integer.parseInt(commands[1]);
        int yTemp = Integer.parseInt(commands[2]);
        res = gameField.receiveCommand(commands[0], xTemp, yTemp);
        if (first && res == -1) {
            //while(gameField.getCell(xTemp,yTemp).getState() == -2 || gameField.getCell(xTemp,yTemp).getState() == -1) {
                gameField.removeMine(xTemp,yTemp);
                gameField.addMine(xTemp,yTemp);
            //}
            res = 0;
        }
        endOfGame = endOfGame || (res == -1) || gameField.isAllOpened();
        if (endOfGame)
            gameField.showAll();
    }
}
