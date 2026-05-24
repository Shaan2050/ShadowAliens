package game;

import bagel.util.Point;
import bagel.util.Rectangle;

public class Collision{
    private Rectangle rect1;
    private final Rectangle rect2;

    public Collision(Player player, Enemy enemy){
        this.rect1 = new Rectangle(new Point(player.getX(), player.getY()), 50, 50);
        this.rect2 = new Rectangle(new Point(enemy.getX(), enemy.getY()), 50, 50);
    }

    public Collision(Projectile projectile, Enemy enemy){
        this.rect1 = new Rectangle(new Point(projectile.getX(), projectile.getY()), 30, 30);
        this.rect2 = new Rectangle(new Point(enemy.getX(), enemy.getY()), 50, 50);
    }

    // Constructor for Player-EnemyProjectile collision
    public Collision(Player player, EnemyProjectile enemyProjectile) {
        this.rect1 = new Rectangle(new Point(player.getX(), player.getY()), 50, 50);
        this.rect2 = new Rectangle(new Point(enemyProjectile.getX(), enemyProjectile.getY()), 30, 30);
    }
    
    // Constructor for Projectile-EnemyProjectile collision
    public Collision(Projectile projectile, EnemyProjectile enemyProjectile) {
        this.rect1 = new Rectangle(new Point(projectile.getX(), projectile.getY()), 30, 30);
        this.rect2 = new Rectangle(new Point(enemyProjectile.getX(), enemyProjectile.getY()), 30, 30);
    }
    
    // Constructor for Player-Powerup collision
    public Collision(Player player, Powerups powerup) {
        this.rect1 = new Rectangle(new Point(player.getX(), player.getY()), 50, 50);
        this.rect2 = new Rectangle(new Point(powerup.getX(), powerup.getY()), 50, 50);
    }

    public boolean checkCollision(){
        return rect1.intersects(rect2);
    }

    
}