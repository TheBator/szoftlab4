package hu.bme.aut.suchtowers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MapSelectorActivity extends Activity {
    private List<GameData> maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selector);
        maps = getMaps();

        ListView lv = (ListView)findViewById(R.id.mapList);
        lv.setAdapter(new MapListAdapter(this, R.layout.row, maps));
    }

    private List<GameData> getMaps() {
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
            try {
                d.name = getMapName(e.getValue());
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (SAXException e1) {
                e1.printStackTrace();
            }
            d.map = e.getValue();
            d.mission = missions.get(e.getKey());

            rtn.add(d);
        }

        return rtn;
    }

    private String getMapName(int id) throws ParserConfigurationException, IOException, SAXException {
        String rtn = null;
        InputStream is = getResources().openRawResource(id);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.parse(is);
        d.getDocumentElement().normalize();

        Element map = (Element) d.getElementsByTagName("map").item(0);


        NodeList wps = map.getElementsByTagName("name");

        rtn = wps.item(0).getTextContent();
        is.close();

        return rtn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
