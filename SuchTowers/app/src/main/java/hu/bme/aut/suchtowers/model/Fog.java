package hu.bme.aut.suchtowers.model;

/**
 * A Fog osztály statikus metódusokkal biztosítja a köd ki- és bekacsolását,
 * illetve lekérdezését.
 *
 * @author Tallér Bátor
 */
public class Fog {
    public static interface FogStateChangedListener {
        public void stateChanged(boolean isSet);
    }
	private static boolean isSet = false;
    private static FogStateChangedListener listener;

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
        if (isSet != fog) {
            isSet = fog;
            if (listener != null) {
                listener.stateChanged(isSet);
            }
        }
	}

	public static void toggle() {
		isSet = !isSet;
        if (listener != null) {
            listener.stateChanged(isSet);
        }
	}

	public static boolean isSet() {
		return isSet;
	}

    public static void setFogStateChangedListener(FogStateChangedListener lst) {
        listener = lst;
    }

    public static void removeListener() {
        listener = null;
    }
}
