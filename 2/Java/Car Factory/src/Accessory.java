public class Accessory implements Detail {
    int ID = -1;

    public Accessory(int ID) {
        this.ID = ID;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getStatus() {
        return "Accessory <" + Integer.toString(ID) + ">";
    }
}
