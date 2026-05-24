package game;

public class Powerups extends NonPlayerEntity{
    private String type;
    private int duration;
    private int activationTime = -1;
    private boolean collected = false;

    public Powerups(String imagePath, int spawnTime, int speed, String type, int duration, double posX) {
        super(imagePath, spawnTime, speed);
        this.type = type.toLowerCase();
        this.duration = duration;
        super.x = posX;
        super.y = -50; // Start above screen
    }

    @Override
    public void despawned() {
        collected = true;
        active = false;
        y=-100;
    }
    @Override
    public void draw() {
        if(isSpawned && !collected && active) {
            image.drawFromTopLeft(x, y);
        }
    }

    public String getType() {
        return type;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void activate(int currentTime) {
        activationTime = currentTime;
    }
    
    public boolean isExpired(int currentTime) {
        if (activationTime == -1) return false;
        return currentTime >= activationTime + duration;
    }
    
    public int getActivationTime() {
        return activationTime;
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public int getRemainingDuration(int currentTime) {
        if (activationTime == -1) return 0;
        int remaining = duration - (currentTime - activationTime);
        return Math.max(0, remaining);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}
