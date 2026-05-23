package game;

import bagel.Image;

public class Enemy extends NonPlayerEntity {
    public static Image explosionImage;
    private final String type;
    private boolean explosion = false;
    private int explosionStart;
    public static int explosionDuration;

    public Enemy(String image, String type, int Time, int speed, int x) {
        super(image, Time, speed);
        this.type = type.toUpperCase();
        super.x = x;
        super.y = 0;
    }

    public static void setExplosionProperties(String imagePath, int duration) {
        explosionImage = new Image(imagePath);
        explosionDuration = duration;
    }

    public void triggerExplosion(int time){
        explosion = true;
        explosionStart = time;
        isSpawned = false;
        active = false;
    }

    public boolean isExploding(){
        return explosion;
    }

    

    public void updateExplosion(int time){
        if(explosionStart + explosionDuration <= time){
            explosion = false;
            active = false;
        }
    }

    @Override
    public void despawned() {
        active = false;
        y = -100;
    }

    public boolean isSpawned() {
        return isSpawned && active;
    }

    @Override
    public void draw(){
        drawExplosion();
        if(isSpawned && active){
            image.drawFromTopLeft(x, y);
        }
    }

    public void drawExplosion(){
        if(explosion){
            explosionImage.draw(x, y);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}