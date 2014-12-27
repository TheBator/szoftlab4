package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.R;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.Tower;
import hu.bme.aut.suchtowers.model.TowerGem;
import hu.bme.aut.suchtowers.model.Vector;

/**
 * A tornyok kirajzolásáért felelős Drawable.
 */
public class GraphicTower extends GameDrawable {
	protected Tower t;
	private Bitmap gemImage;
    private Resources r;
    private Paint paint = new Paint();

	/**
	 * Konstruktor mely hozzárendel egy tower objektumot, és beállítja a háttérképeket.
	 */
	public GraphicTower(Tower t, Resources r) {
		this.t = t;
        this.r = r;
		z_index = 2;
        Vector siz = Game.toMouseCoords(new Vector(0.67f, 0.67f));
		img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.tower), (int)siz.x, (int)siz.y, false);
	}

	/**
	 * Kirajzolja átszámított koordinátákkal a tornyot, 
	 * majd ha van rajta varázskő, akkor azt is rárajzolja.
	 * Végül varázskőtől függően rajzol egy olyan színű 
	 * és olyan sugarú kört a torony köré, , amilyen színü a varázskő,
	 * és amekkora a torony hatótávolsága.
	 */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(img, Game.toMouseCoords(t.getPosition()).x - img.getWidth() / 2f,
                Game.toMouseCoords(t.getPosition()).y - img.getHeight() / 2f, paint);

        if (gemImage != null) {
            canvas.drawBitmap(gemImage,
                    Game.toMouseCoords(t.getPosition()).x - img.getWidth() / 2 + img.getWidth() / 2,
                    Game.toMouseCoords(t.getPosition()).y - img.getHeight() / 2, paint);
            //gemImage.draw(canvas);
        }

        int range = (int) Game.toMouseCoords(new Vector(t.getRange(), 0)).x;

        int color = Color.argb(100, 160, 160, 160);

        if (t.getGem() == TowerGem.red)
            color = Color.argb(100, 255, 0, 0);

        if (t.getGem() == TowerGem.green)
            color = Color.argb(100, 0, 255, 0);

        if (t.getGem() == TowerGem.blue)
            color = Color.argb(100, 0, 0, 255);

        drawRangeCircle(canvas, color, (int) Game.toMouseCoords(t.getPosition()).x, (int) Game.toMouseCoords(t.getPosition()).y, range);
    }

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof GraphicTower && ((GraphicTower) other).t.equals(this.t);
	}

	/**
	 * Beállítja a megfelelő kirajzolandó képet, mikor a tornyon lévő varázskő megváltozik.
	 */
	public void setGem() {
        Vector siz = Game.toMouseCoords(new Vector(0.67f, 0.67f));
        if (t.getGem() != null) {
            if (t.getGem() == TowerGem.red)
                gemImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(r, R.drawable.red_gem), (int)(siz.x/2), (int)(siz.y/2), false);
            else if (t.getGem() == TowerGem.green)
                gemImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(r, R.drawable.green_gem), (int)(siz.x/2), (int)(siz.y/2), false);
            else if (t.getGem() == TowerGem.blue)
                gemImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(r, R.drawable.blue_gem), (int)(siz.x/2), (int)(siz.y/2), false);
        }
	}
}
