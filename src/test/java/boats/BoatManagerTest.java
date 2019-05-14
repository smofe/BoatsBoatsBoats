package boats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

public class BoatManagerTest {

	
	private static final EnumSet<BoatType> ACCEPTS_TYPE_A = EnumSet.of(BoatType.BOAT_TYPE_A);
	private static final EnumSet<BoatType> ACCEPTS_TYPE_B = EnumSet.of(BoatType.BOAT_TYPE_B);
	private static final EnumSet<BoatType> ACCEPTS_TYPE_A_AND_B = EnumSet.of(BoatType.BOAT_TYPE_A,
			BoatType.BOAT_TYPE_B);

	Boat boat1A = new Boat(BoatType.BOAT_TYPE_A);
	Boat boat2A = new Boat(BoatType.BOAT_TYPE_A);
	Boat boat3A = new Boat(BoatType.BOAT_TYPE_A);

	Boat boat1B = new Boat(BoatType.BOAT_TYPE_B);

	@Test
	public void testBoatManagerSimple() throws UnavailableBoatTypeException {
		BoatManager manager = new BoatManager(new Boat[] { boat1A, boat1B });

		Lease l = manager.requestBoat(ACCEPTS_TYPE_A);
		assertEquals(boat1A, l.getBoat());
		Lease l2 = manager.requestBoat(ACCEPTS_TYPE_B);
		assertEquals(boat1B, l2.getBoat());

		manager.returnBoat(l2);

		assertNull(l2.getBoat());

		Lease l3 = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);

		assertEquals(boat1B, l3.getBoat());
	}

	@Test
	public void testBoatManagerOldestFirst() throws UnavailableBoatTypeException {
		BoatManager manager = new BoatManager(new Boat[] { boat1A, boat1B });

		Lease l = manager.requestBoat(ACCEPTS_TYPE_A);
		assertEquals(boat1A, l.getBoat());
		Lease l2 = manager.requestBoat(ACCEPTS_TYPE_B);
		assertEquals(boat1B, l2.getBoat());

		manager.returnBoat(l2);
		manager.returnBoat(l);

		assertNull(l.getBoat());
		assertNull(l2.getBoat());

		Lease l3 = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertEquals(boat1B, l3.getBoat());

		Lease l4 = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertEquals(boat1A, l4.getBoat());

		Lease l5 = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertNull(l5.getBoat());
	}

	@Test
	public void testBoatManagerWaiting() throws UnavailableBoatTypeException {
		BoatManager manager = new BoatManager(new Boat[] { boat1A, boat1B });

		Lease l = manager.requestBoat(ACCEPTS_TYPE_A);
		assertEquals(boat1A, l.getBoat());

		Lease l2 = manager.requestBoat(ACCEPTS_TYPE_B);
		assertEquals(boat1B, l2.getBoat());

		Lease l3 = manager.requestBoat(ACCEPTS_TYPE_B);
		assertNull(l3.getBoat());

		Lease l4 = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertNull(l4.getBoat());

		Lease l5 = manager.requestBoat(ACCEPTS_TYPE_B);
		assertNull(l5.getBoat());

		manager.returnBoat(l2);
		assertNull(l2.getBoat());
		assertEquals(boat1B, l3.getBoat());

		manager.returnBoat(l);
		assertNull(l.getBoat());
		assertEquals(boat1A, l4.getBoat());

		manager.returnBoat(l3);
		assertNull(l3.getBoat());
		assertEquals(boat1B, l5.getBoat());

		assertNull(l.getBoat());

	}

	@Test
	public void testBoatManagerWaiting2() throws UnavailableBoatTypeException {
		BoatManager manager = new BoatManager(new Boat[] { boat1A, boat1B });

		Lease l = manager.requestBoat(ACCEPTS_TYPE_A);
		assertEquals(boat1A, l.getBoat());

		Lease l2 = manager.requestBoat(ACCEPTS_TYPE_B);
		assertEquals(boat1B, l2.getBoat());

		Lease l3 = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertNull(l3.getBoat());

		manager.returnBoat(l2);
		assertNull(l2.getBoat());
		assertEquals(boat1B, l3.getBoat());

		manager.returnBoat(l);
		assertNull(l.getBoat());
		manager.returnBoat(l3);

		Lease l4 = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertEquals(boat1A, l4.getBoat());

	}

	@Test
	public void testRequestUnavailableBoat() throws UnavailableBoatTypeException {
		BoatManager manager = new BoatManager(new Boat[] { boat1A });

		assertThrows(UnavailableBoatTypeException.class, () -> {
			manager.requestBoat(ACCEPTS_TYPE_B);
		});

		assertThrows(UnavailableBoatTypeException.class, () -> {
			manager.requestBoat(EnumSet.noneOf(BoatType.class));
		});

		Lease l = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertEquals(boat1A, l.getBoat());
	}

	@Test
	public void testMultipleBoats() throws UnavailableBoatTypeException {
		BoatManager manager = new BoatManager(new Boat[] { boat1A, boat2A, boat3A });

		Lease l = manager.requestBoat(ACCEPTS_TYPE_A_AND_B);
		assertEquals(boat1A, l.getBoat());
		manager.returnBoat(l);

		Lease l2 = manager.requestBoat(ACCEPTS_TYPE_A);
		assertEquals(boat2A, l2.getBoat());

		Lease l3 = manager.requestBoat(ACCEPTS_TYPE_A);
		assertEquals(boat3A, l3.getBoat());

		Lease l4 = manager.requestBoat(ACCEPTS_TYPE_A);
		assertEquals(boat1A, l4.getBoat());
	}
}
