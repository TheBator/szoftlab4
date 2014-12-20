package hu.bme.aut.suchtowers.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import hu.bme.aut.suchtowers.model.Fog;

/**
 * A köd kirajzolásáért felelős Drawable.
 */
public class GraphicFog extends GameDrawable {
    private Paint p = new Paint();
	public GraphicFog() {
        z_index = 5;
        p.setColor(Color.argb(128, 255, 255, 255));
	}

	/**
	 * Kirajzolja a ködöt, mint egy teljes képernyős szürke átfedés.
	 */
	@Override
	public void draw(Canvas c) {
		if (Fog.isSet()) {
            c.drawRect(0, 0, 800, 600, p);
			//c.setColor(new Color(255, 255, 255, 128));
			//c.fillRect(0, 0, 800, 600);
		}
	}
}
