package game;
 
import java.util.ArrayList;
 
 
public class GameResetter {
    private String playerImage;
    private String playerLivesImage;
    private double screenWidth;
    private double playerPosY;
    private int playerInitialLives;
    private int playerSpeed;
    private int playerHitInvincibilityTime;
    
    private String regularEnemyImage;
    private String strafingEnemyImage;
    private String shootingEnemyImage;

    private String explosionLargeImage;
    private String explosionSmallImage;
    private int explosionLargeDuration;
    private int explosionSmallDuration;
    
    private String enemyProjectileImage;
    private int enemyProjectileSpeed;
    private int enemyShootingFireRate;

    // Powerup configurations
    private String shieldImage;
    private int shieldSpeed;
    private int shieldDuration;
    
    private String lifeImage;
    private int lifeSpeed;
    
    private String cooldownImage;
    private int cooldownSpeed;
    private int cooldownDuration;
    
    private String engineImage;
    private int engineSpeed;
    private int engineDuration;
 
    private final ArrayList<ArrayList<Integer>> enemyArrivalTimes = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> enemySpeeds = new ArrayList<>();
    private final ArrayList<ArrayList<String>> enemyTypes = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> enemyPositions = new ArrayList<>();
    
    private final ArrayList<ArrayList<Integer>> powerupArrivalTimes = new ArrayList<>();
    private final ArrayList<ArrayList<String>> powerupTypes = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> powerupPositions = new ArrayList<>();
    
    private int currentWave = 1;
    private int totalWaves = 0;
 
    public void setPlayerConfig(String image, String livesImage, double startX, 
                            double startY, int initialLives, int speed, 
                            int shootCooldown, int hitInvincibilityTime) {
        this.playerImage = image;
        this.playerLivesImage = livesImage;
        this.screenWidth = startX * 2;  // Since startX is screenWidth/2
        this.playerPosY = startY;
        this.playerInitialLives = initialLives;
        this.playerSpeed = speed;
        this.playerHitInvincibilityTime = hitInvincibilityTime;
    }

    public void setEnemyConfig(String explosionLarge, String explosionSmall, 
                               int largeDuration, int smallDuration,
                               String enemyProjectileImage, int enemyProjectileSpeed,
                               int enemyShootingFireRate) {
        
        this.explosionLargeImage = explosionLarge;
        this.explosionSmallImage = explosionSmall;
        this.explosionLargeDuration = largeDuration;
        this.explosionSmallDuration = smallDuration;
        this.enemyProjectileImage = enemyProjectileImage;
        this.enemyProjectileSpeed = enemyProjectileSpeed;
        this.enemyShootingFireRate = enemyShootingFireRate;
    }

    public void setEnemyTypeConfig(String regularEnemyImage, String strafingEnemyImage, String shootingEnemyImage) {
        this.regularEnemyImage = regularEnemyImage;
        this.strafingEnemyImage = strafingEnemyImage;
        this.shootingEnemyImage = shootingEnemyImage;
    }
 
    public void addWave(){
        enemyArrivalTimes.add(new ArrayList<>());
        enemySpeeds.add(new ArrayList<>());
        enemyTypes.add(new ArrayList<>());
        enemyPositions.add(new ArrayList<>());
        
        powerupArrivalTimes.add(new ArrayList<>());
        powerupTypes.add(new ArrayList<>());
        powerupPositions.add(new ArrayList<>());
        
        totalWaves++;
    }
 
     public void setShieldConfig(String image, int speed, int duration) {
        this.shieldImage = image;
        this.shieldSpeed = speed;
        this.shieldDuration = duration;
    }
    
    public void setLifeConfig(String image, int speed) {
        this.lifeImage = image;
        this.lifeSpeed = speed;
    }
    
    public void setCooldownConfig(String image, int speed, int duration) {
        this.cooldownImage = image;
        this.cooldownSpeed = speed;
        this.cooldownDuration = duration;
    }
    
    public void setEngineConfig(String image, int speed, int duration) {
        this.engineImage = image;
        this.engineSpeed = speed;
        this.engineDuration = duration;
    }
 
    public void addEnemy(int wave, int arrivalTime, String type, int speed, int position) {
        enemyArrivalTimes.get(wave).add(arrivalTime);
        enemyTypes.get(wave).add(type);
        enemySpeeds.get(wave).add(speed);
        enemyPositions.get(wave).add(position);
    }
    
    public void addPowerup(int wave, int arrivalTime, String type, int position) {
        powerupArrivalTimes.get(wave).add(arrivalTime);
        powerupTypes.get(wave).add(type);
        powerupPositions.get(wave).add(position);
    }
 
    public Player resetPlayer() {
        return new Player(playerImage, playerLivesImage, screenWidth / 2, playerPosY, 
                         playerInitialLives, playerSpeed);
    }
 
    public Enemy[] resetEnemies(int wave) {
        int count = enemyArrivalTimes.get(wave).size();
        Enemy[] enemies = new Enemy[count];
        
        for (int i = 0; i < count; i++) {
            enemies[i] = new Enemy(
                getEnemyImage(enemyTypes.get(wave).get(i)),
                enemyTypes.get(wave).get(i),
                enemyArrivalTimes.get(wave).get(i),
                enemySpeeds.get(wave).get(i),
                enemyPositions.get(wave).get(i)
            );
        }
        
        /* Enemy.setExplosionProperties(explosionLargeImage, explosionLargeDuration);
        Enemy.setShootingFireRate(enemyShootingFireRate);
        EnemyProjectile.setMovementSpeed(enemyProjectileSpeed); */
        
        return enemies;
    }

    private String getEnemyImage(String type) {
        switch (type.toUpperCase()) {
            case "STRAFING":
                return strafingEnemyImage;
            case "SHOOTING":
                return shootingEnemyImage;
            default:
                return regularEnemyImage;
        }
    }
    
    public Powerups[] resetPowerups(int wave) {
        int waveIndex = wave - 1;
        
        if (waveIndex >= powerupTypes.size()) {
            return new Powerups[0];
        }
        
        int count = powerupTypes.get(waveIndex).size();
        Powerups[] powerups = new Powerups[count];
        
        for (int i = 0; i < count; i++) {
            String type = powerupTypes.get(waveIndex).get(i);
            int arrivalTime = powerupArrivalTimes.get(waveIndex).get(i);
            int position = powerupPositions.get(waveIndex).get(i);
            
            // Get the right config based on type
            String image = "";
            int speed = 0;
            int duration = 0;
            
            switch (type) {
                case "shield":
                    image = shieldImage;
                    speed = shieldSpeed;
                    duration = shieldDuration;
                    break;
                case "life":
                    image = lifeImage;
                    speed = lifeSpeed;
                    duration = 0;  // Life has no duration
                    break;
                case "cooldown":
                    image = cooldownImage;
                    speed = cooldownSpeed;
                    duration = cooldownDuration;
                    break;
                case "engine":
                    image = engineImage;
                    speed = engineSpeed;
                    duration = engineDuration;
                    break;
            }
            
            powerups[i] = new Powerups(image, arrivalTime, speed, type, duration, position);
        }
        
        return powerups;
    }
    
    public void clearEnemies() {
        enemyArrivalTimes.clear();
        enemySpeeds.clear();
        enemyPositions.clear();
        enemyTypes.clear();
    }
    
    public void clearPowerups() {
        powerupArrivalTimes.clear();
        powerupTypes.clear();
        powerupPositions.clear();
    }
 
    public int getTotalWaves() {
        return totalWaves;
    }
    
    public int getCurrentWave() {
        return currentWave;
    }
    
    public void nextWave() {
        currentWave++;
    }
    
    public void resetWaves() {
        currentWave = 1;
    }
    
    public int getPlayerHitInvincibilityTime() {
        return playerHitInvincibilityTime;
    }
}