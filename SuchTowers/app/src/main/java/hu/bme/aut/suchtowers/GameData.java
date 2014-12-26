package hu.bme.aut.suchtowers;

/**
 * Created by Bátor on 2014.12.26..
 */
public class GameData  {
    public String name;
    public int map;
    public int mission;

    public GameData() {

    }

    public GameData(String name, int map, int mission) {
        this.name = name;
        this.map = map;
        this.mission = mission;
    }
}
