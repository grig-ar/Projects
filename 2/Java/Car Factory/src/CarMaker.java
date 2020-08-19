public class CarMaker extends ManagedThread implements DetailMaker {
    EngineStorage engineStorage;
    BodyStorage bodyStorage;
    AccessoryStorage accessoryStorage;
    CarStorage carStorage;
    private int amount;
    private int sleepTime;

    public CarMaker(EngineStorage engineStorage, BodyStorage bodyStorage, AccessoryStorage accessoryStorage, CarStorage carStorage, int sleepTime) {
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        this.sleepTime = sleepTime;
        this.amount = 0;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Detail makeDetail() {
        Detail car = new Car(amount++, engineStorage.getEngine(), bodyStorage.getBody(), accessoryStorage.getAccessory());
        carStorage.addCar((Car) car);
        return car;
    }

    @Override
    public void run() {
        try {
            while(this.keepRunning()) {
                Thread.sleep(this.sleepTime);
                this.makeDetail();
            }
        }
        catch (InterruptedException e) {
            System.out.printf("%s is interrupted", Thread.currentThread().getName());
        }
    }
}
