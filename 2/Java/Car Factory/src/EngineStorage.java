import java.util.LinkedList;
import java.util.Random;

public class EngineStorage {
    private int capacity;
    private LinkedList<Engine> queue;
    private Random random = new Random();

    public EngineStorage(int capacity) {
        random.setSeed(System.currentTimeMillis());
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void addEngine(Engine engine) {
        //boolean keepGoing = true;
        while (true) {
            if (this.queue.size() < capacity) {
                this.queue.add(engine);
                //keepGoing = false;
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

    public synchronized Engine getEngine() {
        Engine engine;
        while (true) {
            if (this.queue.size() > 0) {
                engine = this.queue.remove(0);
                this.notifyAll();
                return engine;
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
