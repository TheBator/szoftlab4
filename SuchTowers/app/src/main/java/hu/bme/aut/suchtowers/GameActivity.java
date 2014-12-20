package hu.bme.aut.suchtowers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.InputStream;

import hu.bme.aut.suchtowers.model.Game;


public class GameActivity extends Activity {
    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        InputStream map = getResources().openRawResource(R.raw.loop);
        InputStream mission = getResources().openRawResource(R.raw.loop_epic);
        game = new Game(getBaseContext(), map, mission);
        GameView gv = (GameView)findViewById(R.id.game_view);
        gv.setGame(game);
        game.setObserver(gv);

        new Thread(new Runnable() {
            @Override
            public void run() {
                GameActivity.this.game.run();
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
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
