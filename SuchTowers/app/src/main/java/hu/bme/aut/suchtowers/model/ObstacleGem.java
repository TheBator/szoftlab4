package hu.bme.aut.suchtowers.model;

/**
 * Egy akadályokra tehető varázskövet ír le.
 */
public class ObstacleGem extends Gem {
	public static final ObstacleGem yellow;
	public static final ObstacleGem orange;
	protected double speed;

	protected ObstacleGem(float range, double speed) {
		this.range = range;
		this.speed = speed;
		cost = 250;
	}

	static {
		yellow = new ObstacleGem(1.5f, 0.9);
		orange = new ObstacleGem(1.1f, 0.7);
	}

	/**
	 * Megadja, hogy egy akadály lassítási képesséet mennyivel módosítja ez a varázskő.
	 *
	 * @param enemyType Az ellenségtípus, amire vonatkozóan lekérdezzük a lassítás módosulásának mértékét.
	 * @return Az enemyType típusú ellenség lassulásának módosulása.
	 */
	public double getSpeedMultiplier(EnemyType enemyType) {
		return speed;
	}
}
