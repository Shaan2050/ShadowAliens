package game;

public class EnemyProjectile extends Projectile{
    private static int movementSpeed = 5;
    public EnemyProjectile(String image, double x, double y) {
        super(image, x, y, 1, 0);
    }

    @Override
    public boolean despawned() {
        // Projectile leaves the screen when it passes the bottom edge
        return super.y > ShadowAliens.screenHeight;
    }

    public static void setMovementSpeed(int speed){
        movementSpeed = speed;
    }

    @Override
    public void update() {
        super.y += movementSpeed; // Move downwards
    }
}