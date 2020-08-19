public class BodyMaker extends ManagedThread implements DetailMaker  {
    private BodyStorage bodyStorage;
    private int sleepTime;
    private static int amount;

    BodyMaker() {
        this.bodyStorage = null;
        this.sleepTime = 0;
        amount = 0;
    }

    public BodyMaker(BodyStorage bodyStorage, int sleepTime) {
        this.bodyStorage = bodyStorage;
        this.sleepTime = sleepTime;
        amount = 0;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Detail makeDetail() {
        Detail detail = new Body(amount++);
        this.bodyStorage.addBody((Body) detail);
        return detail;
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
