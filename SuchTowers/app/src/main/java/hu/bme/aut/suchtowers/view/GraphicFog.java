package hu.bme.aut.suchtowers.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import hu.bme.aut.suchtowers.model.Fog;

/**
 * A köd kirajzolásáért felelős Drawable.
 */
public class GraphicFog extends GameDrawable {
    private Paint p = new Paint();
    private int width, height;
	public GraphicFog(View v) {

        width = v.getWidth();
        height = v.getHeight();

        z_index = 5;
        p.setColor(Color.argb(128, 255, 255, 255));
        p.setStyle(Paint.Style.FILL);
	}

	/**
	 * Kirajzolja a ködöt, mint egy teljes képernyős szürke átfedés.
	 */
	@Override
	public void draw(Canvas c) {
		if (Fog.isSet()) {
            c.drawRect(0, 0, width, height, p);
		}
	}
}
