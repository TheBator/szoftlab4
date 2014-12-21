package hu.bme.aut.suchtowers.model;

import java.io.Serializable;

/**
 * Egy konkrét ellenség típusát és játékba lépésének idejét tárolja.
 *
 * @author Szabó Antal
 */
public class Spawn implements Serializable {
	public Enemy enemy;
	public double timeToSpawn;

	public Spawn(Enemy enemy, double tts) {
		this.enemy = enemy;
		this.timeToSpawn = tts;
	}
}
