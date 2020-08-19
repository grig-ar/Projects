import java.util.LinkedList;
import java.util.Random;

public class AccessoryStorage {
    private int capacity;
    private LinkedList<Accessory> queue;
    private Random random = new Random();

    public AccessoryStorage(int capacity) {
        random.setSeed(System.currentTimeMillis());
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void addAccessory(Accessory accessory) {
        boolean keepGoing = true;
        while (keepGoing) {
            if (this.queue.size() < capacity) {
                this.queue.add(accessory);
                keepGoing = false;
                notifyAll();
                return;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Waining interrupted");
            }
        }
    }

    public synchronized Accessory getAccessory() {
        Accessory accessory;
        while (true) {
            if (this.queue.size() > 0) {
                accessory = this.queue.remove(0);
                this.notifyAll();
                return accessory;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Waining interrupted");
            }
        }
    }

    public int getSize() {
        return this.queue.size();
    }
}
