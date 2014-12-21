package hu.bme.aut.suchtowers.model;

import java.io.Serializable;

/**
 * A varázskövek közös, absztrakt ősosztálya.
 */
public abstract class Gem implements Serializable {

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
