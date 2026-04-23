package game;

import java.util.ArrayList;


public class GameResetter {
    private String playerImage;
    private String playerLivesImage;
    private double screenWidth;
    private double playerPosY;
    private int playerInitialLives;
    private int playerSpeed;
    private String enemyImage;
    private String explosionImage;
    private int explosionDuration;

    private final ArrayList<Integer> enemyArrivalTimes = new ArrayList<>();
    private final ArrayList<Integer> enemySpeeds = new ArrayList<>();
    private final ArrayList<Integer> enemyPositions = new ArrayList<>();

    public void setPlayerConfig(String image, String livesImage, double startX, 
                            double startY, int initialLives, int speed) {
    this.playerImage = image;
    this.playerLivesImage = livesImage;
    this.screenWidth = startX * 2;  // Since startX is screenWidth/2
    this.playerPosY = startY;
    this.playerInitialLives = initialLives;
    this.playerSpeed = speed;
    }

    public void setEnemyConfig(String image, String explosionImage, int explosionDuration) {
        this.enemyImage = image;
        this.explosionImage = explosionImage;
        this.explosionDuration = explosionDuration;
    }
    public Player resetPlayer() {
        return new Player(playerImage, playerLivesImage, screenWidth / 2, playerPosY, playerInitialLives, playerSpeed);
    }

    public Enemy[] resetEnemies() {
        int count = enemyArrivalTimes.size();
        Enemy[] enemies = new Enemy[count];
        
        for (int i = 0; i < count; i++) {
            enemies[i] = new Enemy(
                enemyImage,
                enemyArrivalTimes.get(i),
                enemySpeeds.get(i),
                enemyPositions.get(i)
            );
        }
        
        Enemy.setExplosionProperties(explosionImage, explosionDuration);
        return enemies;
    }
    
    public void clearEnemies() {
        enemyArrivalTimes.clear();
        enemySpeeds.clear();
        enemyPositions.clear();
    }
    
}