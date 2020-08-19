import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        int engineStorageCapacity = 500;
        EngineStorage engineStorage = new EngineStorage(engineStorageCapacity);
        int bodyStorageCapacity = 250;
        BodyStorage bodyStorage = new BodyStorage(bodyStorageCapacity);
        int accessoryStorageCapacity = 125;
        AccessoryStorage accessoryStorage = new AccessoryStorage(accessoryStorageCapacity);
        int carStorageCapacity = 100;
        CarStorage carStorage = new CarStorage(carStorageCapacity);
        int detailSleepTime = 50;

        EngineMaker engineMaker = new EngineMaker(engineStorage, detailSleepTime);
	    BodyMaker bodyMaker = new BodyMaker(bodyStorage, detailSleepTime);
	    engineMaker.start();
	    bodyMaker.start();
        int accessoryMakers = 8;
        AccessoryMaker[] accessoryMaker = new AccessoryMaker[accessoryMakers];

	    for (int i = 0; i < accessoryMakers; ++i) {
	        accessoryMaker[i] = new AccessoryMaker(accessoryStorage, detailSleepTime);
	        accessoryMaker[i].start();
        }

        int carMakers = 5;
        CarMaker[] carMaker = new CarMaker[carMakers];
	    for (int i = 0; i < carMakers; ++i) {
            int carSleepTime = 500;
            carMaker[i] = new CarMaker(engineStorage, bodyStorage, accessoryStorage, carStorage, carSleepTime);
	        carMaker[i].start();
        }

        int dealers = 5;
        Dealer[] dealer = new Dealer[dealers];
	    for (int i = 0; i < dealers; ++i) {
            int dealerSleepTime = 2500;
            dealer[i] = new Dealer(carStorage, dealerSleepTime);
	        dealer[i].start();
        }
        JFrame frame = new JFrame("Factory monitor");
	    JLabel storage = new JLabel("Storage:");
	    JLabel made = new JLabel("Made:");
	    JLabel sold = new JLabel("Sold:");
	    JLabel amountOfStoredEngines = new JLabel("Engines: 0");
	    JLabel amountOfMadeEngines = new JLabel("Engines: 0");
        JLabel amountOfStoredBodies = new JLabel("Bodies: 0");
        JLabel amountOfMadeBodies = new JLabel("Bodies: 0");
        JLabel amountOfStoredAccessory = new JLabel("Accessory: 0");
        JLabel amountOfMadeAccessory = new JLabel("Accessory: 0");
        JLabel amountOfStoredCars = new JLabel("Cars: 0");
        JLabel amountOfMadeCars = new JLabel("Cars: 0");
        JLabel amountOfSoldCars = new JLabel("Cars: 0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setSize(400,250);

        Box leftBox = new Box(BoxLayout.Y_AXIS);
        leftBox.add(storage);
        leftBox.add(Box.createVerticalGlue());
        leftBox.add(Box.createRigidArea(new Dimension(0,30)));
        leftBox.add(amountOfStoredEngines);
        leftBox.add(Box.createRigidArea(new Dimension(0,30)));
        leftBox.add(amountOfStoredBodies);
        leftBox.add(Box.createRigidArea(new Dimension(0,30)));
        leftBox.add(amountOfStoredAccessory);
        leftBox.add(Box.createRigidArea(new Dimension(0,30)));
        leftBox.add(amountOfStoredCars);

        Box middleBox = new Box(BoxLayout.Y_AXIS);
        middleBox.add(made);
        middleBox.add(Box.createVerticalGlue());
        middleBox.add(Box.createRigidArea(new Dimension(0,30)));
        middleBox.add(amountOfMadeEngines);
        middleBox.add(Box.createRigidArea(new Dimension(0,30)));
        middleBox.add(amountOfMadeBodies);
        middleBox.add(Box.createRigidArea(new Dimension(0,30)));
        middleBox.add(amountOfMadeAccessory);
        middleBox.add(Box.createRigidArea(new Dimension(0,30)));
        middleBox.add(amountOfMadeCars);

        Box rightBox = new Box(BoxLayout.Y_AXIS);
        rightBox.add(sold);
        rightBox.add(Box.createVerticalGlue());
        rightBox.add(Box.createRigidArea(new Dimension(0,115)));
        rightBox.add(amountOfSoldCars);

        Box mainBox = new Box(BoxLayout.X_AXIS);
        mainBox.add(leftBox);
        mainBox.add(Box.createRigidArea(new Dimension(50,0)));
        mainBox.add(middleBox);
        mainBox.add(Box.createRigidArea(new Dimension(50,0)));
        mainBox.add(rightBox);

        frame.add(mainBox);
        Timer clock = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int made = 0;
                int sold = 0;
                amountOfStoredEngines.setText("Engines: " + Integer.toString(engineStorage.getSize()));
                amountOfStoredBodies.setText("Bodies: " + Integer.toString(bodyStorage.getSize()));
                amountOfStoredAccessory.setText("Accessory: " + Integer.toString(accessoryStorage.getSize()));
                amountOfStoredCars.setText("Cars: " + Integer.toString(carStorage.getSize()));
                made = engineMaker.getAmount();
                amountOfMadeEngines.setText("Engines: " + Integer.toString(made));
                made = bodyMaker.getAmount();
                amountOfMadeBodies.setText("Bodies: " + Integer.toString(made));
                made = 0;
                for (int i = 0; i < accessoryMakers; ++i) {
                    made += accessoryMaker[i].getAmount();
                }
                amountOfMadeAccessory.setText("Accessory: " + Integer.toString(made));
                made = 0;
                for (int i = 0; i < carMakers; ++i) {
                    made += carMaker[i].getAmount();
                }
                amountOfMadeCars.setText("Cars: " + Integer.toString(made));
                for (int i = 0; i < dealers; ++i) {
                    sold += dealer[i].getCarsSold();
                }
                amountOfSoldCars.setText("Cars: " + Integer.toString(sold));
            }
        });
        frame.setVisible(true);
        clock.start();
    }
}

