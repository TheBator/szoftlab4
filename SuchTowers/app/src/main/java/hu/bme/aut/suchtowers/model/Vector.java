package hu.bme.aut.suchtowers.model;

/**
 * Egy koordináta pontot megvalósító osztály.
 *
 * @author Nusser Ádám
 */
public class Vector {
	public float x;
	public float y;

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector(Vector v) {
		x = v.x;
		y = v.y;
	}

	/**
	 * @param v A kapott vektort hozzáadja.
	 */
	public void Add(Vector v) {
		x += v.x;
		y += v.y;
	}

	/**
	 * @return Visszatér a két vektor közötti távolsággal.
	 */
	public float getDistance(Vector v) {
		return (float)Math.sqrt((x - v.x) * (x - v.x) + (y - v.y) * (y - v.y));
	}

	/**
	 * A kapott vektor felé elmozdul kapott távolsággal.
	 *
	 * @param distance A távolság
	 * @param v        A vektor ami felé mozdul
	 */
	public void MoveDistanceToVector(double distance, Vector v) {

		Vector vTemp = new Vector(v.x - x, v.y - y);

		double length = Math.sqrt(vTemp.x * vTemp.x + vTemp.y * vTemp.y);
		if (length < distance) {
			x = v.x;
			y = v.y;
		} else {
			vTemp.x /= length;
			vTemp.y /= length;

			vTemp.x *= distance;
			vTemp.y *= distance;

			this.Add(vTemp);
		}
	}

	/**
	 * @return igazzal tér vissza ha a vector epsilon sugarú körön belül van
	 */
	public boolean equals(Vector v, double epsilon) {
		return getDistance(v) <= epsilon;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector))
			return false;
		Vector v = (Vector) o;
		return (x == v.x) && (v.y == y);
	}
}