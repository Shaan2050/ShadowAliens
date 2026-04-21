package game;

import bagel.util.Point;
import bagel.util.Rectangle;

public class Collision{
    private Rectangle playerRect;
    private Rectangle enemyRect;

    public Collision(Player player, Enemy enemy){
        this.playerRect = new Rectangle(new Point(player.getX(), player.getY()), 50, 50);
        this.enemyRect = new Rectangle(new Point(enemy.getX(), enemy.getY()), 50, 50);
    }

    public boolean checkCollision(Player player, Enemy enemy){
        return playerRect.intersects(enemyRect);
    }
}