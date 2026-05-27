package game;

import java.util.ArrayList;

import bagel.Image;

public class Enemy extends NonPlayerEntity {
    public static Image explosionImage;
    private String projectileImage;
    private final String type;
    private boolean explosion = false;
    private int explosionStart;
    public static int explosionDuration;
    private int projectileSpeed;
    private boolean wasSpawned = false;


     // For strafing enemies
    private double xDirection = 0; // -1 for left, 1 for right
    private boolean hasInitializedDirection = false;
    
    // For shooting enemies
    private int lastShotTime = -1;
    private static int shootingFireRate = 0;
    private ArrayList<EnemyProjectile> projectiles = new ArrayList<>();

    private static String staticProjectileImage;
    private static int staticProjectileSpeed;

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

    public static void setShootingFireRate(int rate) {
        shootingFireRate = rate;
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

    public String getType() {
        return type;
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
    public void update(int frameCount) {
        if(frameCount >= spawnTime){
            isSpawned = true;
        }
        
        if(isSpawned && active){
            // Default downward movement
            y += speed;
            
            // Strafing movement
            if (type.equals("STRAFING")) {
                updateStrafing();
            }
            
            // Shooting behavior
            if (type.equals("SHOOTING")) {
                updateShooting(frameCount);
            }
        }
    }

    public void updateProjectiles(int time) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            EnemyProjectile p = projectiles.get(i);
            p.update();
            if (p.despawned()) {
                projectiles.remove(i);
            }
        }
    }

     private void updateStrafing() {
        // Initialize direction on first spawn
        if (!hasInitializedDirection && isSpawned) {
            hasInitializedDirection = true;
            // Determine which edge is closer
            double distToLeft = x;
            double distToRight = ShadowAliens.screenWidth - x;
            xDirection = (distToLeft < distToRight) ? -1 : 1;
        }
        
        // Move towards edge
        x += (speed * xDirection);
        
        // Check boundaries and bounce
        if (x <= 0) {
            x = 0;
            xDirection = 1; // Move towards right edge
        } else if (x >= ShadowAliens.screenWidth) {
            x = ShadowAliens.screenWidth;
            xDirection = -1; // Move towards left edge
        }
    }
    
    private void updateShooting(int time) {
        // First shot at arrivalTime + firingRate
        if (lastShotTime == -1 && time >= spawnTime + shootingFireRate) {
            lastShotTime = time;
            fireProjectile();
        } else if (lastShotTime != -1 && time >= lastShotTime + shootingFireRate) {
            lastShotTime = time;
            fireProjectile();
        }
    }
    

    public static void setProjectile(String imagePath, int speed) {
        staticProjectileImage = imagePath;
        staticProjectileSpeed = speed;
    }
    private void fireProjectile(){
        EnemyProjectile newProjectile = new EnemyProjectile(staticProjectileImage, x, y);
        newProjectile.setMovementSpeed(staticProjectileSpeed);
        projectiles.add(newProjectile);
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

    public void drawProjectiles() {
        for (EnemyProjectile p : projectiles) {
            p.draw();
        }
    }

    public ArrayList<EnemyProjectile> getProjectiles() {
        return new ArrayList<>(projectiles);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void clearProjectiles() {
        projectiles.clear();
    }

    public boolean wasSpawnedLastFrame() {
        return wasSpawned;
    }

}