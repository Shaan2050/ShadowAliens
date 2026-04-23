package game;

import bagel.util.Point;
import bagel.util.Rectangle;

public class Collision{
    private Rectangle playerRect;
    private final Rectangle enemyRect;
    private Rectangle projectileRect;

    public Collision(Player player, Enemy enemy){
        this.playerRect = new Rectangle(new Point(player.getX(), player.getY()), 50, 50);
        this.enemyRect = new Rectangle(new Point(enemy.getX(), enemy.getY()), 50, 50);
    }

    public Collision(Projectile projectile, Enemy enemy){
        this.projectileRect = new Rectangle(new Point(projectile.getX(), projectile.getY()), 30, 30);
        this.enemyRect = new Rectangle(new Point(enemy.getX(), enemy.getY()), 50, 50);
    }

    public boolean checkCollision(Player player, Enemy enemy){
        return playerRect.intersects(enemyRect);
    }

    public boolean checkCollision(Projectile projectile, Enemy enemy){
        return projectileRect.intersects(enemyRect);
    }
}