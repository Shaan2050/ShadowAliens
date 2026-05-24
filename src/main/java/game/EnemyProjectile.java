package game;

public class EnemyProjectile extends Projectile{
    private static int movementSpeed = 5;
    public EnemyProjectile(String image, double x, double y) {
        super(image, x, y, 1, 0);
    }

    public boolean despawned(int screenHeight){
        return getY() > screenHeight; // Assuming screen height is 800
    }

    public static void setMovementSpeed(int speed){
        movementSpeed = speed;
    }

    @Override
    public void update() {
        super.y += movementSpeed; // Move downwards
    }
}