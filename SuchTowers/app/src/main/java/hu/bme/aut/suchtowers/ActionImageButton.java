package hu.bme.aut.suchtowers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Bátor on 2014.12.21..
 */
public class ActionImageButton extends ImageButton {
    private MapClickDelegate delegate;
    public ActionImageButton(Context context) {
        super(context);
    }

    public ActionImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDelegate(MapClickDelegate del) {
        delegate = del;
    }

    public void doAction(float x, float y) {
        delegate.mapClicked(x, y);
    }
}
