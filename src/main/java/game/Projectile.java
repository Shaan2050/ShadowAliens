package game;

import bagel.Image;

public class Projectile{
    private final Image projectileImage;
    //private final Image explosionImage;
    private final double x;
    private double y;
    private final int speed;
    private final int cooldown;
    private static int shootTime = -10;

    public Projectile(String image, double x, double y, int speed, int cooldown){
        this.projectileImage = new Image(image);
        //this.explosionImage = new Image(explosion);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.cooldown = cooldown;
    }
    
    public boolean checkCooldown(int time){
        boolean result =  time - shootTime >= cooldown;
        if (result){
        shootTime = time;
        }
        return result;
    }

    public boolean despawned(){
        return y < 0;
    }

    public void update(){
        y -= speed;
    }

    public void draw(){
        projectileImage.draw(x, y);
    }

   /*  public void explosion(){
        explosionImage.draw(x, y);
        
    } */

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}

