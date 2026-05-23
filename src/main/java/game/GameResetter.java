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

    private final ArrayList<ArrayList<Integer>> enemyArrivalTimes = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> enemySpeeds = new ArrayList<>();
    private final ArrayList<ArrayList<String>> enemyTypes = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> enemyPositions = new ArrayList<>();

    private int currentWave = 1;
    private int totalWaves = 0;

    public void setPlayerConfig(String image, String livesImage, double startX, 
                            double startY, int initialLives, int speed) {
    this.playerImage = image;
    this.playerLivesImage = livesImage;
    this.screenWidth = startX * 2;  // Since startX is screenWidth/2
    this.playerPosY = startY;
    this.playerInitialLives = initialLives;
    this.playerSpeed = speed;
    }

    public void addWave(){
        enemyArrivalTimes.add(new ArrayList<>());
        enemySpeeds.add(new ArrayList<>());
        enemyTypes.add(new ArrayList<>());
        enemyPositions.add(new ArrayList<>());
        totalWaves++;
    }

    public void setEnemyConfig(String image, String explosionImage, int explosionDuration) {
        this.enemyImage = image;
        this.explosionImage = explosionImage;
        this.explosionDuration = explosionDuration;
    }

    public void addEnemy(int wave,int arrivalTime, String type, int speed, int position) {
        enemyArrivalTimes.get(wave).add(arrivalTime);
        enemyTypes.get(wave).add(type);
        enemySpeeds.get(wave).add(speed);
        enemyPositions.get(wave).add(position);
    }

    public Player resetPlayer() {
        return new Player(playerImage, playerLivesImage, screenWidth / 2, playerPosY, playerInitialLives, playerSpeed);
    }

    public Enemy[] resetEnemies(int wave) {
        int count = enemyArrivalTimes.get(wave).size();
        Enemy[] enemies = new Enemy[count];
        
        for (int i = 0; i < count; i++) {
            enemies[i] = new Enemy(
                enemyImage,
                enemyTypes.get(wave).get(i),
                enemyArrivalTimes.get(wave).get(i),
                enemySpeeds.get(wave).get(i),
                enemyPositions.get(wave).get(i)
            );
        }
        
        Enemy.setExplosionProperties(explosionImage, explosionDuration);
        return enemies;
    }
    
    public void clearEnemies() {
        enemyArrivalTimes.clear();
        enemySpeeds.clear();
        enemyPositions.clear();
        enemyTypes.clear();
    }
    
}