package hu.bme.aut.suchtowers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.InputStream;

import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.ObstacleGem;
import hu.bme.aut.suchtowers.model.TowerGem;
import hu.bme.aut.suchtowers.model.Vector;


public class GameActivity extends Activity {
    private Game game;
    private Thread gameThread;

    private ActionImageButton btnTower;
    private ActionImageButton btnObstacle;
    private ActionImageButton btnRedGem;
    private ActionImageButton btnGreenGem;
    private ActionImageButton btnBlueGem;
    private ActionImageButton btnYellowGem;
    private ActionImageButton btnOrangeGem;

    private ActionImageButton activeButton;

    private TextView tvASD;
    private GameView gview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        InputStream map = getResources().openRawResource(R.raw.demo);
        InputStream mission = getResources().openRawResource(R.raw.demo_attack);
        game = new Game(getBaseContext(), map, mission);
        GameView gv = (GameView)findViewById(R.id.game_view);
        gv.setGame(game);
        game.setObserver(gv);

        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GameActivity.this.game.run();
            }
        });
        gameThread.start(); //TODO stop thread if user left activity

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        tvASD = (TextView)findViewById(R.id.tvASD);
        gview = (GameView)findViewById(R.id.game_view);

        initButtons();
    }

    private void initButtons() {
        btnTower = (ActionImageButton)findViewById(R.id.btnTower);
        btnObstacle = (ActionImageButton)findViewById(R.id.btnObstacle);
        btnRedGem = (ActionImageButton)findViewById(R.id.btnRedGem);
        btnGreenGem = (ActionImageButton)findViewById(R.id.btnGreenGem);
        btnBlueGem = (ActionImageButton)findViewById(R.id.btnBlueGem);
        btnYellowGem = (ActionImageButton)findViewById(R.id.btnYellowGem);
        btnOrangeGem = (ActionImageButton)findViewById(R.id.btnOrangeGem);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activeButton == view) {
                    activeButton.setBackgroundResource(android.R.drawable.btn_default);
                    activeButton = null;
                }
                else {
                    activateButton((ActionImageButton) view);
                }
            }
        };

        btnTower.setOnClickListener(listener);
        btnObstacle.setOnClickListener(listener);
        btnRedGem.setOnClickListener(listener);
        btnGreenGem.setOnClickListener(listener);
        btnBlueGem.setOnClickListener(listener);
        btnYellowGem.setOnClickListener(listener);
        btnOrangeGem.setOnClickListener(listener);

        setBtnDelegates();
    }

    private void setBtnDelegates() {
        btnTower.setDelegate(new MapClickDelegate() {
            @Override
            public void mapClicked(float x, float y) {
                game.buildTower(Game.toGameCoords(new Vector(x, y)));
            }
        });

        btnObstacle.setDelegate(new MapClickDelegate() {
            @Override
            public void mapClicked(float x, float y) {
                game.buildObstacle(Game.toGameCoords(new Vector(x, y)));
            }
        });

        btnRedGem.setDelegate(new MapClickDelegate() {
            @Override
            public void mapClicked(float x, float y) {
                game.addGem(Game.toGameCoords(new Vector(x, y)), TowerGem.red);
            }
        });

        btnGreenGem.setDelegate(new MapClickDelegate() {
            @Override
            public void mapClicked(float x, float y) {
                game.addGem(Game.toGameCoords(new Vector(x, y)), TowerGem.green);
            }
        });

        btnBlueGem.setDelegate(new MapClickDelegate() {
            @Override
            public void mapClicked(float x, float y) {
                game.addGem(Game.toGameCoords(new Vector(x, y)), TowerGem.blue);
            }
        });

        btnYellowGem.setDelegate(new MapClickDelegate() {
            @Override
            public void mapClicked(float x, float y) {
                game.addGem(Game.toGameCoords(new Vector(x, y)), ObstacleGem.yellow);
            }
        });

        btnOrangeGem.setDelegate(new MapClickDelegate() {
            @Override
            public void mapClicked(float x, float y) {
                game.addGem(Game.toGameCoords(new Vector(x, y)), ObstacleGem.orange);
            }
        });
    }

    private void activateButton(ActionImageButton btn) {
        if (activeButton != null) {
            activeButton.setBackgroundResource(android.R.drawable.btn_default);
        }
        activeButton = btn;
        int color = getResources().getColor(R.color.active_button);
        activeButton.setBackgroundColor(color);
        gview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (activeButton != null) {
                    activeButton.doAction(motionEvent.getX(), motionEvent.getY());

                    tvASD.setText("TOUCHED");
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}