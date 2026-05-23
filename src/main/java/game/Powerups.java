package game;

public class Powerups extends NonPlayerEntity{
    private String type;
    private int duration;
    private boolean collected = false;

    public Powerups(String imagePath, int spawnTime, int speed, int duration, double posX, double posY) {
        super(imagePath, spawnTime, speed);
        this.duration = duration;
        super.x = posX;
        super.y = posY;
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
}
