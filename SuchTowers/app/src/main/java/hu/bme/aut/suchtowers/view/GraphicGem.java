package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
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

		if (g == TowerGem.red)
			img = BitmapFactory.decodeResource(r, R.drawable.red_gem);
		else if (g == TowerGem.green)
			img = BitmapFactory.decodeResource(r, R.drawable.green_gem);
		else if (g == TowerGem.blue)
			img = BitmapFactory.decodeResource(r, R.drawable.blue_gem);
		else if (g == ObstacleGem.orange)
			img = BitmapFactory.decodeResource(r, R.drawable.orange_gem);
		else if (g == ObstacleGem.yellow)
			img = BitmapFactory.decodeResource(r, R.drawable.yellow_gem);

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
		/*g.drawImage(img, (int) mpos.x - img.getWidth(null) / 4,
				(int) mpos.y - img.getHeight(null) / 4,
				img.getWidth(null) / 2,
				img.getHeight(null) / 2,
				null);*/
	}

	public Vector getPosition() {
		return pos;
	}
}
