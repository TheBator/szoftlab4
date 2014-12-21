package hu.bme.aut.suchtowers.model;

import java.io.Serializable;

/**
 * Párokat tároló generikus osztály.
 *
 * @author Nusser Ádám
 */
public class Pair<K, V> implements Serializable {
	public K a;
	public V b;

	public Pair(K a, V b) {
		this.a = a;
		this.b = b;
	}
}
