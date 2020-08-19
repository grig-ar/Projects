import java.util.LinkedList;
import java.util.Random;

public class BodyStorage {
    private int capacity;
    private LinkedList<Body> queue;
    private Random random = new Random();

    public BodyStorage(int capacity) {
        random.setSeed(System.currentTimeMillis());
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void addBody(Body body) {
        while (true) {
            if (this.queue.size() < capacity) {
                this.queue.add(body);
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

    public synchronized Body getBody() {
        Body body;
        while (true) {
            if (this.queue.size() > 0) {
                body = this.queue.remove(0);
                this.notifyAll();
                return body;
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
