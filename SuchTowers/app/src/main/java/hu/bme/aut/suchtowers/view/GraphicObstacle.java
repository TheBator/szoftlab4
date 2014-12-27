package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.R;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.Obstacle;
import hu.bme.aut.suchtowers.model.ObstacleGem;
import hu.bme.aut.suchtowers.model.Vector;

/**
 * Az akadályok kirajzolásáért felelős Drawable.
 */
public class GraphicObstacle extends GameDrawable {
	private Bitmap gemImage;
	private Obstacle o;
    private Resources r;
    private Paint p = new Paint();

	/**
	 * Konstruktor mely hozzárendel egy obstacle objektumot, és beállítja a háttérképeket.
	 */
	public GraphicObstacle(Obstacle o, Resources r) {
		this.o = o;
        this.r = r;
		z_index = 1;
		img = BitmapFactory.decodeResource(r, R.drawable.obstacle);

        Vector siz = Game.toMouseCoords(new Vector(0.67f, 0.67f));
        img = Bitmap.createScaledBitmap(img, (int)siz.x, (int)siz.y, false);
	}

	/**
	 * Kirajzolja átszámított koordinátákkal az akadályt, 
	 * majd ha van rajta varázskő, akkor azt is rárajzolja.
	 * Végül varázskőtől függően rajzol egy olyan színű 
	 * és olyan sugarú kört a torony köré, amilyen színü a varázskő,
	 * és amekkora az akadály hatótávolsága.
	 */
    @Override
    public void draw(Canvas canvas) {
		/*g.drawImage(img, (int) Game.toMouseCoords(o.getPosition()).x - img.getWidth(null) / 2,
				(int) Game.toMouseCoords(o.getPosition()).y - img.getHeight(null) / 2, null);*/
        canvas.drawBitmap(img, Game.toMouseCoords(o.getPosition()).x - img.getWidth() / 2,
                Game.toMouseCoords(o.getPosition()).y - img.getHeight() / 2, p);


        /* TODO: convert to android */
        if (gemImage != null) {
            canvas.drawBitmap(gemImage,
                    Game.toMouseCoords(o.getPosition()).x - img.getWidth() / 2 + img.getWidth() / 2,
                    Game.toMouseCoords(o.getPosition()).y - img.getHeight() / 2, p);
        }


        int range = (int) Game.toMouseCoords(new Vector(o.getRange(), 0)).x;

        int color = Color.argb(100, 160, 160, 160);

        if (o.getGem() == ObstacleGem.yellow)
            color = Color.argb(100, 160, 160, 0);

        if (o.getGem() == ObstacleGem.orange)
            color = Color.argb(100, 80, 0, 128);

        drawRangeCircle(canvas, color, (int) Game.toMouseCoords(o.getPosition()).x, (int) Game.toMouseCoords(o.getPosition()).y, range);
    }

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof GraphicObstacle && ((GraphicObstacle) other).o.equals(this.o);
	}

	/**
	 * Beállítja az akadály varázskövének megfelelően a varázskő képét.
	 */
	public void setGem() {
        Vector siz = Game.toMouseCoords(new Vector(0.67f, 0.67f));
		if (o.getGem() != null) {
			if (o.getGem() == ObstacleGem.orange)
				gemImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(r, R.drawable.orange_gem), (int)siz.x / 2, (int)siz.y / 2, false);
			else if (o.getGem() == ObstacleGem.yellow)
                gemImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(r, R.drawable.yellow_gem), (int)siz.x / 2, (int)siz.y / 2, false);
        }
	}

}
