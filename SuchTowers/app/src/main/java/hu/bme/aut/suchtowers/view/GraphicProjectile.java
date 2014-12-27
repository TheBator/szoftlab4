package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.R;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.Projectile;
import hu.bme.aut.suchtowers.model.SplitterProjectile;
import hu.bme.aut.suchtowers.model.Vector;

/**
 * A lövedékek kirajzolásáért felelős Drawable.
 */
public class GraphicProjectile extends GameDrawable {
	protected Projectile p;
    private Paint paint = new Paint();

	/**
	 * Konstruktor mely hozzárendel egy projectile objektumot, és beállítja a kirajzolandó képet.
	 */
	public GraphicProjectile(Projectile p, Resources r) {
		this.p = p;
		z_index = 4;

		if (p instanceof SplitterProjectile)
			img = BitmapFactory.decodeResource(r, R.drawable.splitter_projectile);
		else
			img = BitmapFactory.decodeResource(r, R.drawable.projectile);

        Vector siz = Game.toMouseCoords(new Vector(0.33f, 0.33f));

        if (p instanceof SplitterProjectile)
            img = Bitmap.createScaledBitmap(img, (int)siz.x, (int)siz.y, false);
        else
            img = Bitmap.createScaledBitmap(img, (int) siz.x, (int) siz.y, false);
	}


	/**
	 * Kirajzolja a lövedéket az áttranszformált koordinátákra.
	 */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(img, Game.toMouseCoords(p.getPosition()).x - img.getWidth() / 2f, Game.toMouseCoords(p.getPosition()).y - img.getHeight() / 2f, paint);
    }

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof GraphicProjectile && ((GraphicProjectile) other).p.equals(this.p);
	}

}
