package hu.bme.aut.suchtowers.model;

/**
 * A Fog osztály statikus metódusokkal biztosítja a köd ki- és bekacsolását,
 * illetve lekérdezését.
 *
 * @author Tallér Bátor
 */
public class Fog {
	private static boolean isSet = false;

	/**
	 * Attól függően, hogy be van-e kapcsolva a köd,
	 * visszaad egy látótávolság szorzót
	 */
	public static float getRangeMultiplier() {
		if (isSet)
			return 0.65f;

		return 1.0f;
	}

	public static void setFog(boolean fog) {
		isSet = fog;
	}

	public static void toggle() {
		isSet = !isSet;
	}

	public static boolean isSet() {
		return isSet;
	}
}
