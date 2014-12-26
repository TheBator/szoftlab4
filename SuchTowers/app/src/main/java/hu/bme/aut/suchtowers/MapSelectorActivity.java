package hu.bme.aut.suchtowers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapSelectorActivity extends Activity {
    private static List<GameData> maps;

    static {
        maps = getMaps();
    }

    private static List<GameData> getMaps() {
        List<GameData> rtn = new ArrayList<GameData>();
        Map<String, Integer> m = new HashMap<String, Integer>();
        Map<String, Integer> missions = new HashMap<String, Integer>();
        Field[] fields=R.raw.class.getFields();
        for(int i = 0; i < fields.length; i++){
            String name = fields[i].getName();
            if (name.startsWith("map")) {
                try {
                    m.put(name.split("_")[1], fields[i].getInt(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            else if (name.startsWith("mission")) {
                try {
                    String n = name.split("_")[1];
                    missions.put(n, fields[i].getInt(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Map.Entry<String, Integer> e : m.entrySet()) {
            GameData d = new GameData();
            d.name = e.getKey();
            d.map = e.getValue();
            d.mission = missions.get(e.getKey());

            rtn.add(d);
        }

        return rtn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selector);

        ListView lv = (ListView)findViewById(R.id.mapList);
        lv.setAdapter(new MapListAdapter(this, R.layout.row, maps));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_selector, menu);
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
