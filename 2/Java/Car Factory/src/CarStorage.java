import java.util.LinkedList;
import java.util.Random;

public class CarStorage {
    private int capacity;
    private LinkedList<Car> queue;
    private Random random = new Random();

    public CarStorage(int capacity) {
        random.setSeed(System.currentTimeMillis());
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void addCar(Car car) {
        boolean keepGoing = true;
        while (keepGoing) {
            if (this.queue.size() < capacity) {
                this.queue.add(car);
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

    public synchronized Car getCar() {
        Car car;
        while (true) {
            if (this.queue.size() > 0) {
                car = this.queue.remove(0);
                this.notifyAll();
                return car;
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
