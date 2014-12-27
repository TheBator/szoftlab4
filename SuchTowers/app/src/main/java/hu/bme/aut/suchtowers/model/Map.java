package hu.bme.aut.suchtowers.model;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * A pályát megvalósító / leíró osztály.
 *
 */
public class Map implements Serializable {
	public static float roadRadius = 0.33f;

	private HashMap<Integer, Waypoint> waypoints;
    private int lastId;
	/**
	 * A kapott útvonalról betölti a Map-et
	 */
    public Map(InputStream xmlFile) throws Exception {
        //File xmlFile = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.parse(xmlFile);
        d.getDocumentElement().normalize();

        Element map = (Element) d.getElementsByTagName("map").item(0);

        waypoints = new HashMap<Integer, Waypoint>();

        NodeList wps = map.getElementsByTagName("waypoint");
        for (int i = 0; i < wps.getLength(); ++i) {
            Element wp = (Element) wps.item(i);
            int id = Integer.parseInt(wp.getElementsByTagName("id").item(0).getTextContent());
            Element coords = (Element) wp.getElementsByTagName("coords").item(0);
            float x = Float.parseFloat(coords.getElementsByTagName("x").item(0).getTextContent());
            float y = Float.parseFloat(coords.getElementsByTagName("y").item(0).getTextContent());
            waypoints.put(id, new Waypoint(new Vector(x, y)));

            if (lastId < id) {
                lastId = id;
            }
        }

        NodeList rts = map.getElementsByTagName("route");
        for (int i = 0; i < rts.getLength(); ++i) {
            Element rt = (Element) rts.item(i);
            int a = Integer.parseInt(rt.getElementsByTagName("a").item(0).getTextContent());
            int b = Integer.parseInt(rt.getElementsByTagName("b").item(0).getTextContent());
            float chance = Float.parseFloat(rt.getElementsByTagName("chance").item(0).getTextContent());
            waypoints.get(a).setNextWaypoint(waypoints.get(b), chance);
        }

        for (Waypoint wp : waypoints.values()) {
            wp.setDistance();
        }

        xmlFile.close();
    }

	/**
	 * Segédmetódus, amely megadja egy pontnak egy két végpontjával adott szakasztól való távolságát.
	 *
	 * @param s1 A szakasz egyik pontja.
	 * @param s2 A szakasz másik pontja.
	 * @param p  A pont, aminek a távolsága érdekes.
	 * @return A pontnak a szakasztól mért távolsága.
	 */
	private Vector segmentPointDistance(Vector s1, Vector s2, Vector p) {
		float px = s2.x - s1.x;
        float py = s2.y - s1.y;

        float lenSquared = px * px + py * py;

        float u = ((p.x - s1.x) * px + (p.y - s1.y) * py) / lenSquared;
		u = Math.max(Math.min(u, 1), 0);

        float dx = s1.x + u * px - p.x;
        float dy = s1.y + u * py - p.y;

		return new Vector(dx, dy);
	}

	private Vector isInRoadRange(Vector pos, double range) {
		for (Waypoint wpfrom : waypoints.values()) {
			for (Waypoint wpto : wpfrom.listNextWaypoints()) {
                Vector dist = segmentPointDistance(wpfrom.getPosition(), wpto.getPosition(), pos);
				if (dist.length() < range) {
					return dist;
				}
			}
		}
		return null;
	}

	/**
	 * @return Az úttól való távolsággal tér vissza ha lehet oda építeni akadályt, null egyébként
	 */
	public Vector canBuildObstacle(Vector position) {
		return isInRoadRange(position, roadRadius);
	}

	/**
	 * @return Lehet-e a vektor helyére Tower-t építeni.
	 */
	public boolean canBuildTower(Vector position) {
        Vector v = isInRoadRange(position, roadRadius + Tower.radius);
		return v == null;
	}

	/**
	 * Megkeres egy waypointt az azonosítója alapján.
	 *
	 * @param waypointID A keresett waypoint azonosítója.
	 * @return A keresett waypoint, vagy null, ha nincs találat.
	 */
	public Waypoint getWaypointByID(int waypointID) {
		return waypoints.get(waypointID);
	}

	public Collection<Waypoint> getWaypoints() {
		return waypoints.values();
	}

    public Waypoint getLastWaypoint() {
        return getWaypointByID(lastId);
    }
}
