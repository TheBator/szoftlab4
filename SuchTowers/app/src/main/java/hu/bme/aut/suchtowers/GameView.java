package hu.bme.aut.suchtowers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.bme.aut.suchtowers.model.Enemy;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.GameObserver;
import hu.bme.aut.suchtowers.model.Obstacle;
import hu.bme.aut.suchtowers.model.Projectile;
import hu.bme.aut.suchtowers.model.Tower;
import hu.bme.aut.suchtowers.view.GameDrawable;
import hu.bme.aut.suchtowers.view.GraphicEnemy;
import hu.bme.aut.suchtowers.view.GraphicMap;
import hu.bme.aut.suchtowers.view.GraphicObstacle;
import hu.bme.aut.suchtowers.view.GraphicProjectile;
import hu.bme.aut.suchtowers.view.GraphicTower;


/**
 * TODO: document your custom view class.
 */
public class GameView extends View implements GameObserver, Serializable {
    private List<GameDrawable> drawables = new ArrayList<GameDrawable>();
    private float density = getResources().getDisplayMetrics().density;
    private Game game;
    private int magic;
    private String msg = "";

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setGame(Game game) {
        this.game = game;
        drawables.add(new GraphicMap(game.getMap(), getResources(), this));
        magic = game.getMagic();
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            super.onDraw(canvas);

            synchronized (drawables) {
                for (GameDrawable d : drawables) {
                    d.draw(canvas);
                }
            }
        }
        else {
            Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.background);
            canvas.drawBitmap(bp, 0, 0, new Paint());
        }
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(40);
        p.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Magic: " + magic, getWidth() - 5, 40, p);
        p.setTextSize(80);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(msg, getWidth() / 2, (getHeight() - 80) / 2, p);
    }

    @Override
    public void drawAll() {
        postInvalidate();
    }

    @Override
    public void enemyAdded(Enemy e) {
        synchronized (drawables) {
            drawables.add(new GraphicEnemy(e, getResources()));
        }
    }

    @Override
    public void gameLost() {
        msg = getResources().getString(R.string.game_lost);
        drawAll();
    }

    @Override
    public void gameWon() {
        msg = getResources().getString(R.string.game_won);
        drawAll();
    }

    @Override
    public void projectileAdded(Projectile p) {
        synchronized (drawables) {
            drawables.add(new GraphicProjectile(p, getResources()));
        }
    }

    @Override
    public void projectileExploded(Projectile p) {
        synchronized (drawables) {
            drawables.remove(new GraphicProjectile(p, getResources()));
        }
    }

    @Override
    public void magicChanged(int amount) {
        magic = amount;
    }

    @Override
    public void enemyDied(Enemy e) {
        synchronized (drawables) {
            drawables.remove(new GraphicEnemy(e, getResources()));
        }
    }

    @Override
    public void towerEnchanted(Tower t) {
        synchronized (drawables) {
            GraphicTower gt = (GraphicTower) drawables.get(drawables.indexOf(new GraphicTower(t, getResources())));
            gt.setGem();
        }
    }

    /**
     * Hozzáad egy gem-et egy már a kirajzolandó listában lévő akadályhoz.
     */
    public void obstacleEnchanted(Obstacle o) {
        synchronized (drawables) {
            GraphicObstacle go = (GraphicObstacle) drawables.get(drawables.indexOf(new GraphicObstacle(o, getResources())));
            go.setGem();
        }
    }

    /**
     * Hozzáad egy tornyot a kirajzolandó objektumokhoz.
     */
    @Override
    public void towerAdded(Tower t) {
        synchronized (drawables) {
            drawables.add(new GraphicTower(t, getResources()));
            Collections.sort(drawables, Collections.reverseOrder());
        }
    }

    /**
     * Hozzáad egy akadályt a kirajzolandó objektumokhoz.
     */
    @Override
    public void obstacleAdded(Obstacle o) {
        synchronized (drawables) {
            drawables.add(new GraphicObstacle(o, getResources()));
            Collections.sort(drawables, Collections.reverseOrder());
        }
    }
}
