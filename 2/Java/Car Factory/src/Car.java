public class Car implements Detail {
    private Engine engine;
    private Body body;
    private Accessory accessory;
    private int ID = -1;
    static int amount = 0;

    public Car(int ID, Engine engine, Body body, Accessory accessory) {
        this.ID = ID;
        this.engine = engine;
        this.body = body;
        this.accessory = accessory;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getStatus() {
        return  "Car <" + Integer.toString(ID) + ">\n" +
                "Engine <" + Integer.toString(this.engine.getID()) + ">\n" +
                "Body <" + Integer.toString(this.body.getID()) + ">\n" +
                "Accessory <" + Integer.toString(this.accessory.getID()) + ">";
    }
}
