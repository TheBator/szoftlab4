package hu.bme.aut.suchtowers.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Az ellenséget ütemezését megvalósító osztály.
 *
 * @author Nusser Ádám
 * @author Szabó Antal
 */
public class Mission implements Serializable {

	private List<Spawn> spawnList;

	/**
	 * A kapott útvonalról betölti a Mission-t.
	 */
    public Mission(InputStream xmlFile, Map map) throws Exception {
        //File xmlFile = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.parse(xmlFile);
        d.getDocumentElement().normalize();

        Element mission = (Element) d.getElementsByTagName("mission").item(0);

        spawnList = new ArrayList<Spawn>();

        HashMap<String, EnemyType> str2type = new HashMap<String, EnemyType>();
        str2type.put("elf", EnemyType.elf);
        str2type.put("dwarf", EnemyType.dwarf);
        str2type.put("hobbit", EnemyType.hobbit);
        str2type.put("human", EnemyType.human);

        NodeList ens = mission.getElementsByTagName("enemy");
        for (int i = 0; i < ens.getLength(); ++i) {
            Element en = (Element) ens.item(i);
            EnemyType type = str2type.get(en.getElementsByTagName("type").item(0).getTextContent());
            int wpid = Integer.parseInt(en.getElementsByTagName("waypointID").item(0).getTextContent());
            float time = Float.parseFloat(en.getElementsByTagName("time").item(0).getTextContent());
            spawnList.add(new Spawn(new Enemy(type, map.getWaypointByID(wpid)), time));
        }
    }

	/**
	 * Lekéri a következő ellenséget..
	 *
	 * @return Visszatér a listában tárolt következő ellenséggel, vagy null értékkel.
	 */
	public Enemy getNextEnemy() {
		if (!spawnList.isEmpty()) {
			Spawn nextSpawn = spawnList.get(0);
			nextSpawn.timeToSpawn -= 1.0 / Game.FPS;
			if (nextSpawn.timeToSpawn <= 0) {
				spawnList.remove(0);
				return nextSpawn.enemy;
			}
		}
		return null;
	}

	/**
	 * @return Van-e még ellenség
	 */
	public boolean hasEnemy() {
		return !spawnList.isEmpty();
	}

}
