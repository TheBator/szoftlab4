package hu.bme.aut.suchtowers.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * A játéktérre kirajzolható objektumok közös absztrakt ősosztálya.
 */
public abstract class GameDrawable implements Comparable<GameDrawable> {
	/**
	 * Megadja a kirajzolási sorrendet
	 */
	protected int z_index;
	/**
	 * A kirajzolt kép
	 */
	protected Bitmap img;

	/**
	 * A kirajzolást végző absztrakt metódus, amelyet a leszármazottaknak felül kell definiálniuk.
	 *
	 * @param v A Graphics példány, amire a leszámazottnak rajzolnia kell.
	 */
	public abstract void draw(Canvas v);

	/**
	 * Két Drawable példányt hasonlít össze a z-indexük alapján.
	 */
	public int compareTo(GameDrawable other) {
		return other.z_index - this.z_index;
	}

    /**
     * Statikus segédmetódus az épületek hatósugarát jelző kör kirajzolásához.
     *
     * @param canvas A Graphics példány, amire rajzolunk.
     * @param color  A rajzolandó kör színe.
     * @param x      A kör középpontjának vízszintes koordinátája.
     * @param y      A kör középpontjának függőleges koordinátája.
     * @param radius A kör sugara.
     */
    public static void drawRangeCircle(Canvas canvas, int color, int x, int y, int radius) {
        Paint paint = new Paint();

        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

        color = Color.argb(80, Color.red(color), Color.green(color), Color.blue(color));
        paint.setColor(color);

        canvas.drawCircle(x, y, radius, paint);
        //g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        paint.setColor(Color.argb(70, 0, 0, 0));
        paint.setStrokeWidth(2);

        radius += 2;
        //g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
        canvas.drawCircle(x, y, radius, paint);
        radius -= 4;
        //g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
        canvas.drawCircle(x, y, radius, paint);
    }
}
