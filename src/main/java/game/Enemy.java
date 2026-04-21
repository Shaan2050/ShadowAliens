package game;

import bagel.Image;

public class Enemy{
    private final Image enemyImage;
    private final int spawnTime;
    private final int speed;
    private int x;
    private int y = 0;
    private boolean isSpawned = false;
    private boolean arrival = true;

    public Enemy(String image, int Time, int speed, int x) {
        this.enemyImage = new Image(image);
        this.spawnTime = Time;
        this.speed = speed;
        this.x = x;
    }

    public void update(int frameCount){
        if(frameCount >= spawnTime){
            isSpawned = true;
        }
        if(isSpawned & arrival){
            y += speed;
        }
    }

    public void despawned() {
        arrival = false;
        y = -1;
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public void drawEnemy(){
        if(isSpawned & arrival){
            enemyImage.draw(x, y);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}