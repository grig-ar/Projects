public class EngineMaker extends ManagedThread implements DetailMaker  {
    private EngineStorage engineStorage;
    private int sleepTime;
    private static int amount;

    EngineMaker() {
        this.engineStorage = null;
        this.sleepTime = 0;
        amount = 0;
    }

    public EngineMaker(EngineStorage engineStorage, int sleepTime) {
        this.engineStorage = engineStorage;
        this.sleepTime = sleepTime;
        amount = 0;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Detail makeDetail() {
        Detail detail = new Engine(amount++);
        this.engineStorage.addEngine((Engine) detail);
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
