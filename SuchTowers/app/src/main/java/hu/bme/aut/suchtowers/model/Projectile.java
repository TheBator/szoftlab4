package hu.bme.aut.suchtowers.model;

import java.io.Serializable;

/**
 * Egy lövedék viselkedését megvalósító osztály.
 *
 * @author Nusser Ádám
 */
public class Projectile implements Serializable {

	protected double damage;
	protected Vector position;
	protected double speed;
	protected Enemy target;

	/**
	 * Létrehoz egy lövedéket.
	 *
	 * @param target a lövedék cél ellensége
	 * @param start  a lövedék kezdeti pozíciója
	 * @param damage a lövedék által kifejtett sebzés
	 * @param speed  a lövedék sebessége
	 */
	public Projectile(Enemy target, Vector start, double damage, double speed) {

		this.target = target;
		this.position = start;
		this.damage = damage;
		this.speed = speed;

	}

	/**
	 * Mozgatja a lövedéket a célja felé. Ha elérte, megsebzi.
	 *
	 * @return true, ha a lövedék elérte a célját, egyébként false
	 */
	public boolean step() {

		if (target == null)
			return true;

		position.MoveDistanceToVector(speed / Game.FPS, target.getPosition());

		if (position.equals(target.getPosition(), 0.1)) {
			target.damage(damage);
			return true;
		}
		return false;
	}

	/**
	 * @return a lövedék pozíciója
	 */
	public Vector getPosition() {
		return position;
	}

	public Enemy getTarget() {
		return target;
	}

}
