package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.R;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.Map;
import hu.bme.aut.suchtowers.model.Vector;
import hu.bme.aut.suchtowers.model.Waypoint;

/**
 * A pálya kirajzolásáért felelős Drawable.
 */
public class GraphicMap extends GameDrawable {

	private Map m;
	private Bitmap mountains;
    private int width = (int)Game.toMouseCoords(new Vector(2, 2)).x;
    private Paint paint = new Paint();

	/**
	 * Konstruktor mely hozzárendel egy map objektumot, és beállítja a háttérképeket.
	 */
	public GraphicMap(Map m, Resources r) {
		this.m = m;
		z_index = 0;

		img = BitmapFactory.decodeResource(r, R.drawable.background);
		mountains = BitmapFactory.decodeResource(r, R.drawable.saurontower);

        paint.setColor(Color.argb(255, 85, 34, 0));
        paint.setStrokeWidth(width * 2);
	}

	/**
	 * Kirajzolja a pálya hátterét, majd a Waypointokon végighaladva
	 * kirajzolja az utakat is.
	 */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(img, 0, 0, paint);

        for (Waypoint wp : m.getWaypoints()) {
            Vector start_pos = Game.toMouseCoords(wp.getPosition());
            for (Waypoint w : wp.listNextWaypoints()) {
                Vector end_pos = Game.toMouseCoords(w.getPosition());
                canvas.drawLine(start_pos.x, start_pos.y, end_pos.x, end_pos.y, paint);
            }
            canvas.drawCircle(start_pos.x, start_pos.y, width, paint);
        }
        canvas.drawBitmap(mountains, 0, 0, paint);
    }

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof GraphicMap && ((GraphicMap) other).m.equals(this.m);
	}

}
