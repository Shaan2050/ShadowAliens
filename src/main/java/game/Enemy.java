package game;

import bagel.Image;

public class Enemy{
    private final Image enemyImage;
    public static Image explosionImage;
    private final int spawnTime;
    private final int speed;
    private int x;
    private int y = 0;

    private boolean isSpawned = false;
    private boolean arrival = true;
    private boolean explosion = false;
    private int explosionStart;
    public static int explosionDuration;

    public Enemy(String image, int Time, int speed, int x) {
        this.enemyImage = new Image(image);
        this.spawnTime = Time;
        this.speed = speed;
        this.x = x;
    }

    public static void setExplosionProperties(String imagePath, int duration) {
        explosionImage = new Image(imagePath);
        explosionDuration = duration;
    }

    public void triggerExplosion(int time){
        explosion = true;
        explosionStart = time;
        isSpawned = false;
        arrival = false;
    }

    public boolean isExploding(){
        return explosion;
    }

    public void update(int frameCount){
        if(frameCount >= spawnTime){
            isSpawned = true;
        }
        if(isSpawned && arrival){
            y += speed;
        }
    }

    public void updateExplosion(int time){
        if(explosionStart + explosionDuration <= time){
            explosion = false;
            arrival = false;
        }
    }

    public void despawned() {
        arrival = false;
        y = -100;
    }

    public boolean isSpawned() {
        return isSpawned && arrival;
    }

    public void drawEnemy(){
        drawExplosion();
        if(isSpawned && arrival){
            enemyImage.draw(x, y);
        }
    }

    public void drawExplosion(){
        if(explosion){
            explosionImage.draw(x, y);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}