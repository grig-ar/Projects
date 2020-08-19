public class Body implements Detail {
    int ID = -1;

    public Body(int ID) {
        this.ID = ID;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getStatus() {
        return "Body <" + Integer.toString(ID) + ">";
    }
}
