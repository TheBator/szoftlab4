package hu.bme.aut.suchtowers;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import hu.bme.aut.suchtowers.model.Enemy;
import hu.bme.aut.suchtowers.model.Fog;
import hu.bme.aut.suchtowers.model.Game;
import hu.bme.aut.suchtowers.model.Obstacle;
import hu.bme.aut.suchtowers.model.ObstacleGem;
import hu.bme.aut.suchtowers.model.Projectile;
import hu.bme.aut.suchtowers.model.Tower;
import hu.bme.aut.suchtowers.model.TowerGem;
import hu.bme.aut.suchtowers.model.Vector;
import hu.bme.aut.suchtowers.view.GraphicFog;
import hu.bme.aut.suchtowers.view.GraphicMap;

public class GameActivity extends Activity implements SensorEventListener {
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

    private GameView gview;
    private Timer t = new Timer();
    private Sensor accSensor;
    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gview = (GameView) findViewById(R.id.game_view);
        if (savedInstanceState == null) {
            Intent it = getIntent();
            Bundle b = it.getExtras();
            int mapId = b.getInt("MAP_ID");
            int missionId = b.getInt("MISSION_ID");

            InputStream map = getResources().openRawResource(mapId);
            InputStream mission = getResources().openRawResource(missionId);
            startNew(map, mission);
        }
        else {
            continueFromSavedState((Game)savedInstanceState.getSerializable("game"));
            Game g = (Game)savedInstanceState.getSerializable("game");
            game = g;
        }

        game.setObserver(gview);

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!game.runOne())
                    this.cancel();
            }
        }, 0, 1000 / Game.FPS);


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fog.setFogStateChangedListener(new Fog.FogStateChangedListener() {
            @Override
            public void stateChanged(boolean isSet) {
                if (isSet)
                    sm.registerListener(GameActivity.this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
                else
                    sm.unregisterListener(GameActivity.this);
            }
        });
    }

    private void startNew(InputStream map, InputStream mission) {
        game = new Game(getBaseContext(), map, mission);
        gview.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);
        gview.setGame(game, this);

        gview.setInit(new Runnable() {
            @Override
            public void run() {
                gview.addDrawable(new GraphicMap(game.getMap(), getResources(), gview));
                gview.addDrawable(new GraphicFog(gview.getContext()));
            }
        });
    }

    private void continueFromSavedState(final Game game) {
        this.game = game;
        gview.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);
        gview.setGame(game, this);
        gview.setInit(new Runnable() {
            @Override
            public void run() {
                for (Enemy e : game.getEnemies()) {
                    gview.enemyAdded(e);
                }
                for (Tower t : game.getTowers()) {
                    gview.towerAdded(t);
                }
                for (Obstacle o : game.getObstacles()) {
                    gview.obstacleAdded(o);
                }
                for (Projectile p : game.getProjectiles()) {
                    gview.projectileAdded(p);
                }

                gview.addDrawable(new GraphicMap(game.getMap(), getResources(), gview));
                gview.addDrawable(new GraphicFog(gview.getContext()));
            }
        });

        game.cont();
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
                }
                return true;
            }
        });
    }

    public void gameEnded() {
        //TODO
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.stop();
        gameThread = null;
        t.cancel();
        t.purge();
        sm.unregisterListener(this);
        Fog.removeListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("game", game);
    }

    private float ox, oy, oz;
    private long dt;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.equals(accSensor)) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            dt = 1;
            float dx, dy, dz;

            dx = Math.abs((ox - x)/dt);
            dy = Math.abs((oy - y)/dt);
            dz = Math.abs((oz - z)/dt);
            ox = x;
            oy = y;
            oz = z;
            if (Math.sqrt(dx * dx + dy * dy + dz * dz) > 45) {
                Log.d("AccSensor", "Fog set false");
                Fog.setFog(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
