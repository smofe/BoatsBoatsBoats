package boats;

public class IDBoat {

    private int ID;
    private Boat boat;

    public IDBoat(int ID, Boat boat){
        this.boat = boat;
        this.ID = ID;
    }

    public int getID(){
        return ID;
    }

    public Boat getBoat(){
        return boat;
    }

}
