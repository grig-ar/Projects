public class Dealer extends ManagedThread {
    private CarStorage carStorage;
    private int sleepTime;
    private int carsSold;

    Dealer(CarStorage carStorage, int sleepTime) {
        this.carStorage = carStorage;
        this.sleepTime = sleepTime;
    }

    public synchronized Car sell() {
        this.carsSold++;
        Car car = carStorage.getCar();
        System.out.printf("%s is sold %s", Thread.currentThread().getName(), car.getStatus() + "\n");
        return car;
    }

    public int getCarsSold() {
        return this.carsSold;
    }

    @Override
    public void run() {
        try {
            while (this.keepRunning()) {
                Thread.sleep(this.sleepTime);
                this.sell();
            }
        }
        catch (InterruptedException e) {
            System.out.printf("%s is interrupted", Thread.currentThread().getName());
        }
    }
}
