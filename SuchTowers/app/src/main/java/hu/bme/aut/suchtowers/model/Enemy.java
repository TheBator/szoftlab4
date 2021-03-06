package hu.bme.aut.suchtowers.model;

import java.io.Serializable;

/**
 * Egy ellenség megvalósítása.
 *
 */
public class Enemy implements Serializable {
	private EnemyType type;
	private double health;
	private Vector position;
	private Waypoint targetWaypoint;
	private Waypoint nextWaypoint = null;
	private double slowingFactor = 1;

	/**
	 * Létrehoz egy új ellenséget.
	 *
	 * @param type  az ellenség típusa
	 * @param start a kezdő waypoint
	 */
	public Enemy(EnemyType type, Waypoint start) {
		this.type = type;
		position = new Vector(start.getPosition());
		targetWaypoint = start.getNextWaypoint();
		health = type.getHealth();
	}

	/* Változás: nincs Cloneable interfész, helyette copy konstruktor */
	public Enemy(Enemy en) {
		type = en.type;
		health = en.health;
		position = new Vector(en.position);
		targetWaypoint = en.targetWaypoint;
		slowingFactor = en.slowingFactor;
	}

	/**
	 * Mozgatja az ellenséget a célja felé.
	 *
	 * @return nyert-e az ellenség
	 */
	public boolean move() {
		if (targetWaypoint == null) {
			return true;
		}
		Vector wPos = targetWaypoint.getPosition();
		double speed = type.getSpeed() * slowingFactor;

		position.MoveDistanceToVector(speed / Game.FPS, wPos);

		if (position.equals(wPos)) { // ez így jó, nem kell epszilon, mert a movethispointaspecifiedistancetowardsagivenvectorplease fgv fentebb direktbe' adja át, ha már elég közel van
			if (nextWaypoint == null) {
				targetWaypoint = targetWaypoint.getNextWaypoint();
			} else {
				targetWaypoint = nextWaypoint;
				nextWaypoint = null;
			}
		}
		return false;
	}

	/**
	 * Sebzi az ellenséget.
	 *
	 * @param amount a sebzés mértéke
	 */
	public void damage(double amount) {
		health -= amount;
	}

	/**
	 * @return az ellenség végső céljától való távolsága
	 */
	public double getDistance() {
		if (targetWaypoint == null) {
			return 0;
		} else {
			return targetWaypoint.getDistance() + position.getDistance(targetWaypoint.getPosition());
		}
	}

	/**
	 * @return az ellenség típusa
	 */
	public EnemyType getEnemyType() {
		return type;
	}

	/**
	 * @return az ellenség pozíciója
	 */
	public Vector getPosition() {
		return position;
	}

	public boolean isAlive() {
		return health > 0;
	}

	/**
	 * Beállítja az ellenség sebességét egy szorzó segítségével.
	 *
	 * @param slowingFactor sebesség szorzó
	 */
	public void setSlowingFactor(double slowingFactor) {
		this.slowingFactor = slowingFactor;
	}

	public double getSlowingFactor() {
		return slowingFactor;
	}

	/**
	 * Megsebzi az ellenfelet, majd kettévágja
	 *
	 * @return az új ellenség
	 */
	public Enemy split(double dmg) {
		if (isAlive()) {
			this.damage(dmg);
			Enemy rtn = new Enemy(this);
			rtn.health *= 0.5;

			final int moveNum = 10;
			for (int i = 0; i < moveNum; ++i)
				rtn.move();

			return rtn;
		}

		return null;
	}
}
