package hu.bme.aut.suchtowers.model;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Ez az osztály fogja össze a többi osztályt.
 * A felhasználótól érkező parancsokat, eseményeket kezeli.
 * Tárolja a játék különböző elemeit:
 * Ellenségeket, tornyokat, akadályokat, lövedékeket.
 *
 * @author Tallér Bátor
 * @author Szabó Antal
 * @author Török Attila
 */
public class Game implements Serializable {
	public static final int FPS = 30;
	private Map map = null;
	private Mission mission = null;
	private final List<Enemy> enemies = new ArrayList<Enemy>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	private final List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private final List<Tower> towers = new ArrayList<Tower>();
	private int magic = 4200;
	private long tick = 0;

	private transient GameObserver view;

	/**
	 * A játék lehetséges állapotai
	 */
	enum State {
		RUNNING, PAUSED, WIN, LOSE, STOPPED
	}

	volatile State gameState = State.RUNNING;

	/**
	 * A játék konstruktora, betölt egy pályát és egy missziót
	 *
	 * @param mapStream     a map fájl neve
	 * @param missionStream a mission fájl neve
	 */
	public Game(Context context, InputStream mapStream, InputStream missionStream) {
		try {
			map = new Map(mapStream);
			mission = new Mission(missionStream, map);

			Fog.setFog(false);
			Tower.comeatmebro = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void pause() {
        gameState = State.PAUSED;
    }

    public void cont() {
        if (gameState == State.PAUSED || gameState == State.STOPPED)
            gameState = State.RUNNING;
    }

    public void stop() {
        gameState = State.STOPPED;
    }

    public void setObserver(GameObserver ob) {
        view = ob;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public Map getMap() {
        return map;
    }

    /***
     * Eggyel lépteti a játékot.
     * @return Hamissal tér vissza ha befejeződött a játék
     */
    public boolean runOne() {
        if (gameState != State.PAUSED) {
            step();
            setFog();
            ++tick;
        }

        view.drawAll();

        if (gameState != State.PAUSED && !mission.hasEnemy() && enemies.isEmpty()) {
            gameState = State.WIN;

        }

        if (gameState == State.LOSE) {
            view.gameLost();

            return false;
        } else if (gameState == State.WIN) {
            view.gameWon();

            return false;
        }

        return true;
    }

	private double gaus = new Random().nextGaussian();

	private void setFog() {
		long time = tick / FPS;

		double secs;
		if (Fog.isSet()) {
			secs = gaus * 5.0 + 10.0;
			/* Valamiért néha a secs pont 0 lett és ezért div by 0 exceptiont dobott */
			while ((int) secs == 0) {
				gaus = new Random().nextGaussian();
				secs = gaus * 5.0 + 10.0;
			}
		} else {
			secs = gaus * 13.0 + 40;
			while ((int) secs == 0) {
				gaus = new Random().nextGaussian();
				secs = gaus * 13.0 + 40.0;
			}
		}
		if (time != 0 && time % (int) secs == 0) {
			Fog.toggle();
			tick = 0;
			gaus = new Random().nextGaussian();
		}
	}

    public int getMagic() {
        return magic;
    }

	/**
	 * A játék logikáját egy lépéssel előrébb viszi.
	 * (Az ellenségek itt haladnak, a tornyok itt lőnek, a lövedékek ott repülnek, stb.)
	 */
	private void step() {
		slowEnemies();
		moveEnemies();

		Enemy enemy = mission.getNextEnemy();

		if (enemy != null) {
			addEnemy(enemy);
		}

		moveProjectiles();
		towersFire();
	}

	/**
	 * A lövedékek mozgatása. Ha az ellenség meghalt akkor kitörli
	 */
	private void moveProjectiles() {
		Iterator<Projectile> i = projectiles.iterator();
		while (i.hasNext()) {
			Projectile p = i.next();
			if (p.step()) {
				i.remove();
				view.projectileExploded(p);
				if (!p.getTarget().isAlive() && enemies.contains(p.target)) {
					removeEnemy(p.getTarget());
				}
			}
		}
	}

	/**
	 * Kitöröl egy ellenséget.
	 */
	private void removeEnemy(Enemy en) {
		magic += en.getEnemyType().magic;
		view.magicChanged(magic);
		enemies.remove(en);
		view.enemyDied(en);
	}

	/**
	 * A tornyok támadását intéző metódus.
	 */
	private void towersFire() {
		synchronized (towers) {
			for (Tower t : towers) {
				Projectile p = t.attack(enemies, this);
				if (p != null) {
					projectiles.add(p);
					view.projectileAdded(p);
				}
			}
		}
	}

	/**
	 * Az ellenségek mozgását intéző metódus.
	 */
	private boolean moveEnemies() {
		synchronized (enemies) {
			for (Enemy e : enemies) {
				if (e.move()) {
					gameState = State.LOSE;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Az ellenségek lassítását beállító metódus.
	 */
	private void slowEnemies() {
		synchronized (enemies) {
			synchronized (obstacles) {
				for (Enemy e : enemies) {
					e.setSlowingFactor(1);
					for (Obstacle o : obstacles) {
						if (e.getPosition().equals(o.getPosition(), o.getRange()))
							e.setSlowingFactor(o.getSlowingFactor(e) * e.getSlowingFactor());
					}
				}
			}
		}
	}

	/**
	 * Egy torony megerősítése egy varázskővel.
	 *
	 * @param pos A pozíció, ahol az erősítendő épület található.
	 * @param gem A varázskő, amellyel az épület erősítendő.
	 */
	public void addGem(Vector pos, TowerGem gem) {
		Tower t = getCollidingTower(pos);

		if (t != null && magic >= gem.cost) {
			magic -= gem.cost;
			view.magicChanged(magic);
			t.setGem(gem);
			view.towerEnchanted(t);
		}
	}

	/**
	 * Egy akadály megerősítése egy varázskővel.
	 *
	 * @param pos A pozíció, ahol az erősítendő épület található.
	 * @param gem A varázskő, amellyel az épület erősítendő.
	 */
	public void addGem(Vector pos, ObstacleGem gem) {
		Obstacle o = getCollidingObstacle(pos);

		if (o != null && magic >= gem.cost) {
			magic -= gem.cost;
			view.magicChanged(magic);
			o.setGem(gem);
			view.obstacleEnchanted(o);
		}
	}

	/**
	 * Egy ellenség hozzáadása a játéktérhez.
	 *
	 * @param en A hozzáadandó ellenség.
	 */
	public void addEnemy(Enemy en) {
		if (en != null) {
			synchronized (enemies) {
				enemies.add(en);
			}
			view.enemyAdded(en);
		}
	}

	/**
	 * A megadott helyre épít egy akadályt
	 * Ellenőrzi, hogy a megadott helyre a pályán lehet-e akadályt építeni,
	 * illetve hogy nem ütközik-e már meglévő akadállyal.
	 *
	 * @param pos az akadály koordinátái
	 * @return Épített-e oda akadályt
	 */
	public boolean buildObstacle(Vector pos) {
        Vector d = map.canBuildObstacle(pos);
		if (d != null && !collidesWithObstacle(pos, Obstacle.range) && coordInRange(pos) && magic >= Obstacle.cost) {
			pos.add(d);
            Obstacle o = new Obstacle(pos);
			synchronized (obstacles) {
				obstacles.add(o);
			}
			view.obstacleAdded(o);
			magic -= Obstacle.cost;
			view.magicChanged(magic);

			return true;
		}

		return false;
	}

    public boolean coordInRange(Vector p) {
        return p.x > 0 && p.x < 16 && p.y > 0 && p.y < 9;
    }

	/**
	 * A megadott helyre épít egy tornyot
	 * Ellenőrzi, hogy a megadott helyre a pályán lehet-e tornyot építeni,
	 * illetve hogy nem ütközik-e már meglévő toronnyal.
	 *
	 * @param pos a torony koordinátái
	 * @return Épített-e oda tornyot
	 */
	public boolean buildTower(Vector pos) {
		if (map.canBuildTower(pos) && !collidesWithTower(pos) && coordInRange(pos) && magic >= Tower.cost) {
			Tower t = new Tower(pos);
			synchronized (towers) {
				towers.add(t);
			}
			view.towerAdded(t);
			magic -= Tower.cost;
			view.magicChanged(magic);

			return true;
		}

		return false;
	}

	/**
	 * @return visszadja, hogy az adott kör ütközik-e egy akadállyal
	 */
	public boolean collidesWithObstacle(Vector pos, double radius) {
		synchronized (obstacles) {
			for (Obstacle o : obstacles) {
				if (o.doesCollideWithCircle(pos, radius))
					return true;
			}
		}

		return false;
	}

	/**
	 * @return visszaadja, hogy az adott pont ütközik-e egy toronnyal
	 */
	public boolean collidesWithTower(Vector pos) {
		synchronized (towers) {
			for (Tower t : towers) {
				if (t.doesCollide(pos))
					return true;
			}
		}

		return false;
	}

	/**
	 * @return Az akadály, amellyel az adott pont ütközik. Ha egyikkel sem, akkor null.
	 */
	public Obstacle getCollidingObstacle(Vector pos) {
		synchronized (obstacles) {
			for (Obstacle o : obstacles) {
				if (o.doesCollide(pos))
					return o;
			}
		}

		return null;
	}

	/**
	 * @return A torony, amellyel az adott pont ütközik. Ha egyikkel sem, akkor null.
	 */
	public Tower getCollidingTower(Vector pos) {
		synchronized (towers) {
			for (Tower t : towers) {
				if (t.doesCollide(pos))
					return t;
			}
		}

		return null;
	}

	/**
	 * Megmutatja, hogy 1 játékbeli méter hány pixel a képernyőn
	 */
	static private int pix = 10;

    private static float width, height;
    public static void updateViewDimensions(int width, int height) {
        float nw, nh;

        nw = width;
        nh = 9 * (width / 16.0f);
        if (nh > height) {
            nh = height;
            nw = 16 * (height / 9.0f);
        }

        Game.height = nh;
        Game.width = nw;

        Log.d("new_height", nh + "");
        Log.d("new_width", nw + "");
    }

	/**
	 * @param v egy Vector, ami fizikai koordinátákat tartalmaz
	 * @return v Vector áttranszformálva játékbeli koordinákba
	 */
	static public Vector toGameCoords(Vector v) {
		return new Vector(v.x / width * 16.0f, v.y / height * 9.0f);
	}

    /**
     * Azért, hogy kevesebb new legyen
     * @param x x fizikai koordináta
     * @param y y fizikai koordináta
     * @return a koordináták áttranszformálva játékbeli koordinákba
     */
    static public Vector toGameCoords(float x, float y) {
        return new Vector(x / width * 16.0f, y / height * 9.0f);
    }

	/**
	 * @param v egy Vector, ami játékbeli koordinátákat tartalmaz
	 * @return v Vector áttranszformálva fizikai koordinátákba
	 */
	static public Vector toMouseCoords(Vector v) {
		return new Vector((v.x / 16.0f) * width, (v.y / 9.0f) * height);
	}

    /**
     * Azért, hogy kevesebb new legyen
     * @param x x játékbeli koordináta
     * @param y y játékbeli koordniáta
     * @return a koordináták áttranszformálva fizikai koordinátákba
     */
    static public Vector toMouseCoords(float x, float y) {
        return new Vector((x / 16.0f) * width, (y / 9.0f) * height);
    }
}
