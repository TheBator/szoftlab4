package hu.bme.aut.suchtowers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import hu.bme.aut.suchtowers.model.Fog;

/**
 * A köd kirajzolásáért felelős Drawable.
 */
public class GraphicFog extends GameDrawable {
    private Paint p = new Paint();
    private int width, height;
	public GraphicFog(Context c) {
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

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
