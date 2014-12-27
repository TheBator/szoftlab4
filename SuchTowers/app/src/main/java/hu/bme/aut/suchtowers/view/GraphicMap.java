package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

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
    private float width = Game.toMouseCoords(Map.roadRadius, Map.roadRadius).x;
    private Paint paint = new Paint();
    private Vector last;

	/**
	 * Konstruktor mely hozzárendel egy map objektumot, és beállítja a háttérképeket.
	 */
	public GraphicMap(Map m, Resources r, View v) {
		this.m = m;
		z_index = 0;

		img = BitmapFactory.decodeResource(r, R.drawable.background);
		mountains = BitmapFactory.decodeResource(r, R.drawable.sauron);
        Vector s = Game.toMouseCoords(new Vector(mountains.getWidth() / 16f * 2, mountains.getHeight() / 9f * 2));
        mountains = Bitmap.createScaledBitmap(mountains, mountains.getWidth() / 2, mountains.getHeight() / 2, false);

        Vector siz = Game.toMouseCoords(new Vector(80, 60)); //TODO
        paint.setColor(Color.argb(255, 85, 34, 0));
        paint.setStrokeWidth(width * 2);

        last = Game.toMouseCoords(m.getLastWaypoint().getPosition());
        last.x -= mountains.getWidth() / 2;
        last.y -= mountains.getHeight();
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
        canvas.drawBitmap(mountains, last.x, last.y, paint);
    }

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof GraphicMap && ((GraphicMap) other).m.equals(this.m);
	}

}
