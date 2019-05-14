package boats;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Queue;

public class BoatManager {

	private int customerID = 0;
	private int boatID = 0;
	private boolean hasBoatTypeA;
	private boolean hasBoatTypeB;

	private Queue<IDBoat> boatsA = new LinkedList<IDBoat>();
	private Queue<IDBoat> boatsB = new LinkedList<IDBoat>();
	private Queue<Customer> customerA = new LinkedList<Customer>();
	private Queue<Customer> customerB = new LinkedList<Customer>();
	private Queue<Customer> customerAB = new LinkedList<Customer>();


	public BoatManager(Boat[] boats) {
		for (Boat boat : boats){
			if (boat.getType() == BoatType.BOAT_TYPE_A){
				boatsA.add(new IDBoat(boatID,boat));
			}
			else {
				boatsB.add(new IDBoat(boatID,boat));
			}
			boatID++;
		}
		if (boatsA.size() > 0) hasBoatTypeA = true;
		if (boatsB.size() > 0) hasBoatTypeB = true;
	}

	public Lease requestBoat(EnumSet<BoatType> accepts) throws UnavailableBoatTypeException {
		if (!accepts.contains(BoatType.BOAT_TYPE_A) && !accepts.contains(BoatType.BOAT_TYPE_B)) throw new UnavailableBoatTypeException();

		Lease lease = new Lease();
		Customer customer = new Customer(customerID,lease);
		customerID++;

		if (accepts.contains(BoatType.BOAT_TYPE_A)){
			if (accepts.contains(BoatType.BOAT_TYPE_B)){
				customerAB.add(customer);
				BoatType aOrB = boatAorB();
				if (aOrB != null ) {
					giveBoat(aOrB);
				}
			}
			else {
				if (!hasBoatTypeA) throw new UnavailableBoatTypeException();
				customerA.add(customer);
				if (boatsA.size() > 0 ) {
					giveBoat(BoatType.BOAT_TYPE_A);
				}
			}
		}
		else {
			if (!hasBoatTypeB) throw new UnavailableBoatTypeException();
			customerB.add(customer);
			if (boatsB.size() > 0 ) {
				giveBoat(BoatType.BOAT_TYPE_B);
			}
		}

		return lease;
	}

	public void returnBoat(Lease l) {
		Boat boat = l.getBoat();

		BoatType type = boat.getType();

		if (type == BoatType.BOAT_TYPE_A){
			boatsA.add(new IDBoat(boatID,boat));
		} else {
			boatsB.add(new IDBoat(boatID,boat));
		}
		boatID++;

		giveBoat(type);


		l.resetBoat();
	}

	/* Adds a Boat to the longest-waiting fitting customer */
	private void giveBoat(BoatType type){
		if (type == BoatType.BOAT_TYPE_A){
			/* BOAT TYPE A */
			if (customerAB.size() > 0){
				if (customerA.size() > 0){
					/* Comparing IDs of customerA and customerAB */
					if (customerA.peek().getID() < customerAB.peek().getID()){
						customerA.poll().getLease().setBoat(boatsA.poll().getBoat());
					} else {
						customerAB.poll().getLease().setBoat(boatsA.poll().getBoat());

					}
				} else {
					customerAB.poll().getLease().setBoat(boatsA.poll().getBoat());
				}
			} else {
				if (customerA.size() > 0) {
					customerA.poll().getLease().setBoat(boatsA.poll().getBoat());
				}
			}
		}
		else {
			/* BOAT TYPE B */
			if (customerAB.size() > 0){
				if (customerB.size() > 0){
					/* Comparing IDs of customerB and customerAB */
					if (customerB.peek().getID() < customerAB.peek().getID()){
						customerB.poll().getLease().setBoat(boatsB.poll().getBoat());
					} else {
						customerAB.poll().getLease().setBoat(boatsB.poll().getBoat());

					}
				} else {
					customerAB.poll().getLease().setBoat(boatsB.poll().getBoat());
				}
			} else {
				if (customerB.size() > 0) {
					customerB.poll().getLease().setBoat(boatsB.poll().getBoat());
				}
			}
		}
	}

	/* returns BoatType A or B, depending on which Boat is the 'oldest' */
	private BoatType boatAorB(){
		if (boatsA.size() > 0 || boatsB.size() > 0){
			if (boatsA.size() > 0) {
				if (boatsB.size() > 0){
					if (boatsA.peek().getID() > boatsB.peek().getID()) return BoatType.BOAT_TYPE_B;
					else return BoatType.BOAT_TYPE_A;
				}
				else return BoatType.BOAT_TYPE_A;
			}
			else return BoatType.BOAT_TYPE_B;
		}
		return null;
	}
}
