package game;

import bagel.Image;

public class Enemy{
    private final Image enemyImage;
    private final int spawnTime;
    private final int speed;
    private int x;
    private int y = 0;

    public Enemy(String image, int Time, int speed, int x) {
        this.enemyImage = new Image(image);
        this.spawnTime = Time;
        this.speed = speed;
        this.x = x;
    }

    public void drawEnemy(){
        enemyImage.draw(x, y);
        y += speed; 
    }
    
}