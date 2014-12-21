package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.R;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.Gem;
import hu.bme.aut.suchtowers.model.ObstacleGem;
import hu.bme.aut.suchtowers.model.TowerGem;
import hu.bme.aut.suchtowers.model.Vector;

/**
 * Varázskő kirajzolásáért felelős osztály.
 */
public class GraphicGem extends GameDrawable {
	public Vector pos;

	/**
	 * Beállítja a kapott varázskőtől függően a kirajzolandó képet.
	 */
	public GraphicGem(Gem g, Resources r) {
		z_index = 2;
        Vector siz = Game.toMouseCoords(new Vector(2, 2));

		if (g == TowerGem.red)
			img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.red_gem), (int)siz.x, (int)siz.y, false);
		else if (g == TowerGem.green)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.green_gem), (int)siz.x, (int)siz.y, false);
		else if (g == TowerGem.blue)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.blue_gem), (int)siz.x, (int)siz.y, false);
		else if (g == ObstacleGem.orange)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.orange_gem), (int)siz.x, (int)siz.y, false);
		else if (g == ObstacleGem.yellow)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.yellow_gem), (int)siz.x, (int)siz.y, false);

		pos = new Vector(-100, -100);
	}


	/**
	 * Kirajzolja a varázskövet a pozíciójától eltolva, hogy attól jobbra fel kerüljön.
	 */
	@Override
	public void draw(Canvas c) {
		Vector mpos = Game.toMouseCoords(pos);
        c.drawBitmap(img,
                     (int) mpos.x - img.getWidth() / 4,
                     (int) mpos.y - img.getHeight() / 4,
                     new Paint());
	}

	public Vector getPosition() {
		return pos;
	}
}
