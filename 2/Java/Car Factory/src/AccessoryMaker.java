public class AccessoryMaker extends ManagedThread implements DetailMaker {
    private AccessoryStorage accessoryStorage;
    private int sleepTime;
    private static int amount;

    AccessoryMaker() {
        this.accessoryStorage = null;
        this.sleepTime = 0;
        amount = 0;
    }

    public AccessoryMaker(AccessoryStorage accessoryStorage, int sleepTime) {
        this.accessoryStorage = accessoryStorage;
        this.sleepTime = sleepTime;
        amount = 0;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Detail makeDetail() {
        Detail accessory = new Accessory(amount++);
        this.accessoryStorage.addAccessory((Accessory) accessory);
        return accessory;
    }

    @Override
    public void run() {
        try {
            while (this.keepRunning()) {
                Thread.sleep(this.sleepTime);
                this.makeDetail();
            }
        }
        catch (InterruptedException e) {
            System.out.printf("%s is interrupted", Thread.currentThread().getName());
        }
    }
}
