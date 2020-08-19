public class Engine implements Detail {
    int ID = -1;

    public Engine(int ID) {
        this.ID = ID;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getStatus() {
        return "Engine <" + Integer.toString(ID) + ">";
    }
}
