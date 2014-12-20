package hu.bme.aut.suchtowers.model;

/**
 * A varázskövek közös, absztrakt ősosztálya.
 */
public abstract class Gem {

	protected float range;
	protected int cost = 400;

	/**
	 * Megadja, hogy mennyivel szorzódik a hatótávolság ezen varázskő hatására.
	 *
	 * @return A hatótávolság együtthatója.
	 */
	public float getRangeMultiplier() {
		return range;
	}

	public int getCost() {
		return cost;
	}
}
