package hu.bme.aut.suchtowers.view;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.R;
import hu.bme.aut.suchtowers.model.Enemy;
import hu.bme.aut.suchtowers.model.EnemyType;
import hu.bme.aut.suchtowers.model.Game;

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

		if (e.getEnemyType() == EnemyType.human)
            img = BitmapFactory.decodeResource(r, R.drawable.human);
		else if (e.getEnemyType() == EnemyType.dwarf)
            img = BitmapFactory.decodeResource(r, R.drawable.dwarf);
		else if (e.getEnemyType() == EnemyType.elf)
            img = BitmapFactory.decodeResource(r, R.drawable.elf);
		else if (e.getEnemyType() == EnemyType.hobbit)
            img = BitmapFactory.decodeResource(r, R.drawable.hobbit);
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
