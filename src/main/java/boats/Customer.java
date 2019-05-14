package boats;

import java.util.EnumSet;

public class Customer {

    private int id;
    private Lease lease;

    public Customer(int id, Lease lease){
        this.id = id;
        this.lease = lease;
    }

    public int getID(){
        return id;
    }

    public Lease getLease(){
        return lease;
    }

}
