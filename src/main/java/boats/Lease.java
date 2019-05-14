package boats;

public final class Lease {

	private Boat boat;
	private LeaseState state = LeaseState.WAITING;

	public Boat getBoat() {
		return boat;
	}

	public LeaseState getState() {
		return state;
	}

	public void setBoat(Boat b) {
		if (b == null) {
			throw new IllegalArgumentException("Boat must not be null.");
		}

		if (state != LeaseState.WAITING) {
			throw new IllegalStateException("State must be WAITING!");
		}

		state = LeaseState.ACTIVE;
		boat = b;
	}

	public void resetBoat() {
		if (state != LeaseState.ACTIVE)
			throw new IllegalStateException("State must be ACTIVE!");

		state = LeaseState.COMPLETED;
		boat = null;
	}

}
