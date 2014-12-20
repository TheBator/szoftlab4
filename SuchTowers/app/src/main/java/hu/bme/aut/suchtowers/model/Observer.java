package hu.bme.aut.suchtowers.model;

/**
 * Created by Bátor on 2014.12.20..
 */
public interface Observer {
    public void drawAll();
    public void enemyAdded(Enemy e);
    public void gameLost();
    public void gameWon();
    public void projectileAdded(Projectile p);
    public void projectileExploded(Projectile p);
    public void magicChanged(int amount);
    public void enemyDied(Enemy e);
    public void towerEnchanted(Tower t);
    public void obstacleEnchanted(Obstacle o);
    public void obstacleAdded(Obstacle o);
    public void towerAdded(Tower t);
}
