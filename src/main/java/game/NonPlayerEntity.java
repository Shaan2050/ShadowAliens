package game;
import bagel.Image;

public abstract class NonPlayerEntity {
    protected Image image;
    protected int spawnTime;
    protected int speed;
    protected double x;
    protected double y;
    protected boolean isSpawned;
    protected boolean active;

    public NonPlayerEntity(String imagePath, int spawnTime, int speed) {
        this.image = new Image(imagePath);
        this.spawnTime = spawnTime;
        this.speed = speed;
        this.isSpawned = false;
        this.active = true;
    }


    public abstract void despawned();
    public abstract void draw();

    public boolean isSpawned() {
        return isSpawned && active;
    }

    public void update(int frameCount) {
        if(frameCount >= spawnTime){
            isSpawned = true;
        }
        if(isSpawned && active){
            y += speed;
        }
    }
}
