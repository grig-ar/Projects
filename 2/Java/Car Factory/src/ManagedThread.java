public class ManagedThread extends Thread {
    int desiredState = ThreadStates.RUNNING;

    public int getDesiredState() {return desiredState;}

    public void mstop () {
        synchronized (this) {
            desiredState = ThreadStates.STOP;
            notifyAll();
        }
    }

    public void msuspend() {
        synchronized (this) {
            if (desiredState!=ThreadStates.STOP) {
                desiredState = ThreadStates.SLEEP;
            }
        }
    }

    public void mresume () {
        synchronized (this) {
            if (desiredState != ThreadStates.STOP)	{
                desiredState = ThreadStates.RUNNING;
                notifyAll();
            }
        }
    }

    protected boolean keepRunning() {
        synchronized (this) {
            if (desiredState == ThreadStates.RUNNING)
                return true;
            else {
                while (true) {
                    if (desiredState == ThreadStates.STOP) {
                        System.out.println(Thread.currentThread().getName()+ " stopped");
                        return false;
                    }
                    else if (desiredState == ThreadStates.SLEEP)
                        try {
                            System.out.println(Thread.currentThread().getName()+ " fell asleep");
                            this.wait();
                        } catch (Exception ex) {
                            System.err.println(Thread.currentThread().getName()+" interrupted");
                            return false;
                        }
                    else if (desiredState == ThreadStates.RUNNING) {
                        System.out.println(Thread.currentThread().getName()+" resumed");
                        return true;
                    }
                }
            }
        }
    }
}
