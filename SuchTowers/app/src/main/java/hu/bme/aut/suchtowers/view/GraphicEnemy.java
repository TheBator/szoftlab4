package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.R;
import hu.bme.aut.suchtowers.model.Enemy;
import hu.bme.aut.suchtowers.model.EnemyType;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.Vector;

/**
 * Az ellenségek kirajzolásáért felelős Drawable.
 */
public class GraphicEnemy extends GameDrawable {

	protected Enemy e;

	/**
	 * Konstruktor mely hozzárendel egy enemy objektumot, és beállítja a háttérképeket,
	 * az ellenség típusától függően.
	 */
	public GraphicEnemy(Enemy e, Resources r) {
		this.e = e;
		z_index = 3;
        Vector siz = Game.toMouseCoords(0.33f, 0.33f);

		if (e.getEnemyType() == EnemyType.human)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.human), (int)siz.x, (int)siz.y, false);
		else if (e.getEnemyType() == EnemyType.dwarf)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.dwarf), (int)siz.x, (int)siz.y, false);
		else if (e.getEnemyType() == EnemyType.elf)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.elf), (int)siz.x, (int)siz.y, false);
		else if (e.getEnemyType() == EnemyType.hobbit)
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.hobbit), (int)siz.x, (int)siz.y, false);
	}

	/**
	 * Kirajzolja az általa reprezentált ellenséget.
	 */
	@Override
	public void draw(Canvas c) {
        c.drawBitmap(img,
                (int) Game.toMouseCoords(e.getPosition()).x - img.getWidth() / 2,
                (int) Game.toMouseCoords(e.getPosition()).y - img.getHeight() / 2,
                new Paint());
	}

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof GraphicEnemy && ((GraphicEnemy) other).e.equals(this.e);
	}

}
