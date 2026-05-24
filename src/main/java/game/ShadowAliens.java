package game;

import java.util.ArrayList;
import java.util.Properties;

import bagel.AbstractGame;
import bagel.DrawOptions;
import bagel.Font;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

/**
 * Main game class that manages initialising the screens and game objects
 */
public class ShadowAliens extends AbstractGame {
    private static final int PLAYER_BOUND_WIDTH = 50;
    private static final int PLAYER_BOUND_HEIGHT = 50;

    private Player player;
    private PlayerState playerState;
    private ArrayList<Projectile> playerProjectiles = new ArrayList<>();
    private ArrayList<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    private ArrayList<Powerups> powerUps = new ArrayList<>();
    private Enemy[] enemy;
    
    // New components
    private final GameSpeed gameSpeed;
    private final InvincibleMode invincibleMode;
    private final GameResetter gameResetter;

    private ScreenManager screenManager;
    private StartScreen startScreen;
    private PauseScreen pauseScreen;
    private BattleScreen battleScreen;
    private EndScreen endScreen;

    private enum GameState {
        START,
        BATTLE,
        PAUSED,
        END
    }

    private GameState gameState;
    
    private final Font uiFont;
    private final DrawOptions textColor;
    private final String waveText;
    private final double waveX, waveY;
    private final String scoreText;
    private final double scoreX, scoreY;
    private int score = 0;

    private static int time = 0;
    private double accumulatedTime = 0;
    private boolean inWave = false;

    private int currentWave = 1;
    
    private static Properties gameProps;
    public static double screenWidth;
    public static double screenHeight;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        this.gameState = GameState.START;

        ShadowAliens.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        String[] bgColour = gameProps.getProperty("background.colour").split(",");
        Window.setClearColour(
            Double.parseDouble(bgColour[0]),
            Double.parseDouble(bgColour[1]),
            Double.parseDouble(bgColour[2])
        );

        String[] textColour = gameProps.getProperty("text.colour").split(",");
        this.textColor = new DrawOptions();
        textColor.setBlendColour(
            Double.parseDouble(textColour[0]),
            Double.parseDouble(textColour[1]),
            Double.parseDouble(textColour[2])
        );

        this.gameSpeed = new GameSpeed();
        this.invincibleMode = new InvincibleMode();
        this.playerState = new PlayerState();
        this.uiFont = new Font(
            gameProps.getProperty("text.font"),
            Integer.parseInt(gameProps.getProperty("text.size"))
        );
        
        this.waveText = gameProps.getProperty("wave.text");
        String[] wavePos = gameProps.getProperty("wave.pos").split(",");
        this.waveX = Double.parseDouble(wavePos[0]);
        this.waveY = Double.parseDouble(wavePos[1]);
        
        this.scoreText = gameProps.getProperty("score.text");
        String[] scorePos = gameProps.getProperty("score.pos").split(",");
        this.scoreX = Double.parseDouble(scorePos[0]);
        this.scoreY = Double.parseDouble(scorePos[1]);
        
        //Start Screen
        String fontFile = gameProps.getProperty("text.font");
        int fontSize = Integer.parseInt(gameProps.getProperty("text.size"));
        
        //Setup BattleScreen

        this.battleScreen = new BattleScreen(fontFile, textColor);
        battleScreen.setWaveCongfig(
            gameProps.getProperty("wave.text"),
            Integer.parseInt(gameProps.getProperty("text.size")),
            gameProps.getProperty("wave.pos").split(",")
        );
        battleScreen.setScoreConfig(
            0,
            gameProps.getProperty("score.text"),
            gameProps.getProperty("score.pos").split(",")
        );
        screenManager = new ScreenManager((int) screenWidth);
        screenManager.setBattleScreen(battleScreen);

        // Create and configure StartScreen
        this.startScreen = new StartScreen(fontFile, textColor);
        startScreen.setTitleConfig(
            gameProps.getProperty("start.title.text"),
            Integer.parseInt(gameProps.getProperty("start.title.size")),
            Double.parseDouble(gameProps.getProperty("start.title.posY"))
        );
        startScreen.setControlsConfig(
            gameProps.getProperty("start.instructionsList.text"),
            Double.parseDouble(gameProps.getProperty("start.instructionsList.startPosY")),
            Double.parseDouble(gameProps.getProperty("start.instructionsList.rowGap"))
        );
        startScreen.setFontSize(fontSize);
        screenManager.setStartScreen(startScreen);
        this.pauseScreen = new PauseScreen(fontFile, textColor);
        

        // Initialize components
        
        
        // Setup GameResetter
        this.gameResetter = new GameResetter();
        gameResetter.setPlayerConfig(
            gameProps.getProperty("player.image"),
            gameProps.getProperty("playerLives.image"),
            screenWidth / 2,
            Double.parseDouble(gameProps.getProperty("player.posY")),
            Integer.parseInt(gameProps.getProperty("player.initialLives")),
            Integer.parseInt(gameProps.getProperty("player.speed")),
            Integer.parseInt(gameProps.getProperty("player.shootCooldown")),
            Integer.parseInt(gameProps.getProperty("player.hitInvincibilityTime"))
        );
        gameResetter.setEnemyConfig(
            gameProps.getProperty("explosion.large.image"),
            gameProps.getProperty("explosion.small.image"),
            Integer.parseInt(gameProps.getProperty("explosion.large.duration")),
            Integer.parseInt(gameProps.getProperty("explosion.small.duration")),
            gameProps.getProperty("enemyProjectile.image"),
            Integer.parseInt(gameProps.getProperty("enemyProjectile.movementSpeed")),
            Integer.parseInt(gameProps.getProperty("enemy.shooting.firingRate"))
        );

        gameResetter.setEnemyTypeConfig(
            gameProps.getProperty("enemy.regular.image"),
            gameProps.getProperty("enemy.strafing.image"),
            gameProps.getProperty("enemy.shooting.image")
        );

        gameResetter.setShieldConfig(
            gameProps.getProperty("powerup.shield.image"),
            Integer.parseInt(gameProps.getProperty("powerup.shield.movementSpeed")),
            Integer.parseInt(gameProps.getProperty("powerup.shield.duration"))
        );

        gameResetter.setLifeConfig(
            gameProps.getProperty("powerup.life.image"),
            Integer.parseInt(gameProps.getProperty("powerup.life.movementSpeed"))
        );

        gameResetter.setCooldownConfig(
            gameProps.getProperty("powerup.cooldown.image"),
            Integer.parseInt(gameProps.getProperty("powerup.cooldown.movementSpeed")),
            Integer.parseInt(gameProps.getProperty("powerup.cooldown.duration"))
        );

        gameResetter.setEngineConfig(
            gameProps.getProperty("powerup.engine.image"),
            Integer.parseInt(gameProps.getProperty("powerup.engine.movementSpeed")),
            Integer.parseInt(gameProps.getProperty("powerup.engine.duration"))
        );
        
        // Load enemies into GameResetter
        loadEnemiesAndPowerups();
        
        // Setup PauseScreen
        
        pauseScreen.setTitleConfig(
            gameProps.getProperty("pausedTitle.text"),
            Integer.parseInt(gameProps.getProperty("pausedTitle.size")),
            Double.parseDouble(gameProps.getProperty("pausedTitle.posY"))
        );
        pauseScreen.setControlsConfig(
            gameProps.getProperty("controlsList.text"),
            Double.parseDouble(gameProps.getProperty("controlsList.startPosY")),
            Double.parseDouble(gameProps.getProperty("controlsList.rowGap"))
        );
        pauseScreen.setTimescaleConfig(
            gameProps.getProperty("timescale.text"),
            gameProps.getProperty("timescale.pos")
        );
        pauseScreen.setFontSize(fontSize);
        screenManager.setPauseScreen(pauseScreen);
        
        // Initialize game objects using GameResetter
        this.player = gameResetter.resetPlayer();
        this.enemy = gameResetter.resetEnemies(currentWave - 1);
        Powerups[] wavePowerups = gameResetter.resetPowerups(currentWave);
        for (Powerups p : wavePowerups) {
            powerUps.add(p);
        }
    }

    // End Screen setup
    private void endScreenSetup(boolean playerWon) {
        this.endScreen = new EndScreen(gameProps.getProperty("text.font"), textColor);
        this.endScreen.setStatus(playerWon);
        if(playerWon){
            String titleText = gameProps.getProperty("end.win.text") ;
            this.endScreen.setTitleConfig(
                titleText,
                Integer.parseInt(gameProps.getProperty("end.win.size")),
                Double.parseDouble(gameProps.getProperty("end.win.posY"))
            );
        }else{
            String titleText = gameProps.getProperty("end.lose.text");
            this.endScreen.setTitleConfig(
                titleText,
                Integer.parseInt(gameProps.getProperty("end.lose.size")),
                Double.parseDouble(gameProps.getProperty("end.lose.posY"))
            );
        }
            this.endScreen.setControlsConfig(
                gameProps.getProperty("end.instructionsList.text"),
                Double.parseDouble(gameProps.getProperty("end.instructionsList.startPosY")),
                Double.parseDouble(gameProps.getProperty("end.instructionsList.rowGap"))
            );
    
        this.endScreen.setFontSize(Integer.parseInt(gameProps.getProperty("font.size")));
        screenManager.setEndScreen(endScreen);
        screenManager.endGame();
    }
    
    private void loadEnemiesAndPowerups() {
        int wave = 1;
        
        while (true) {
            String firstEnemyTime = gameProps.getProperty("wave." + wave + ".enemy.0.arrivalTime");
            if(firstEnemyTime == null) break;
            gameResetter.addWave(); // Start with wave 1

            int index = 0;
            while(true){
                String time = gameProps.getProperty("wave." + wave + ".enemy." + index + ".arrivalTime");
                if(time == null) break;
                gameResetter.addEnemy(
                    wave - 1,
                    Integer.parseInt(time),
                    gameProps.getProperty("wave." + wave + ".enemy." + index + ".type"),
                    Integer.parseInt(gameProps.getProperty("wave." + wave + ".enemy." + index + ".movementSpeed")),
                    Integer.parseInt(gameProps.getProperty("wave." + wave + ".enemy." + index + ".posX"))
                );
                index++;
            }

            int powerupIndex = 0;
            while(true){
                String type = gameProps.getProperty("wave." + wave + ".powerup." + powerupIndex + ".type");
                if(type == null) break;
                
                gameResetter.addPowerup(
                    wave - 1,
                    Integer.parseInt(gameProps.getProperty("wave." + wave + ".powerup." + powerupIndex + ".arrivalTime")),
                    type,
                    Integer.parseInt(gameProps.getProperty("wave." + wave + ".powerup." + powerupIndex + ".posX"))
                );
                powerupIndex++;
            }
            wave++;
        }
    }
    
    private void resetGame() {
        time = 0;
        accumulatedTime = 0;
        score = 0;
        currentWave = 1;
        inWave = false;
        gameState = GameState.START;
        gameSpeed.reset();
        invincibleMode.reset();
        playerState.reset();
        
        // Clear old entities
        playerProjectiles.clear();
        enemyProjectiles.clear();
        powerUps.clear();
        
        // Reset player and enemies
        player = gameResetter.resetPlayer();
        enemy = gameResetter.resetEnemies(currentWave - 1);

        Powerups[] wavePowerups = gameResetter.resetPowerups(currentWave);
        for (Powerups p : wavePowerups) {
            powerUps.add(p);
        }

        battleScreen.setScoreConfig(score, gameProps.getProperty("score.text"),
                                   gameProps.getProperty("score.pos").split(","));
        battleScreen.setCurrentWave(currentWave);
    }

    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        // Handle global controls
        handleGlobalControls(input);
        
        /* if (player.getHealth() <= 0) {
            endScreenSetup(false);
            return;
        }

        if(allEnemiesAndProjectilesDestroyed()){
            if(inWave){
                
                if(currentWave > gameResetter.getTotalWaves()){
                    endScreenSetup(true);
                    return;
                }else{
                    currentWave++;
                    enemy = gameResetter.resetEnemies(currentWave - 1);
                    inWave = true;
                }
            }
            
        } */

        switch (gameState) {
            case START:
                updateStart(input);
                break;
                
            case BATTLE:
                updateBattle(input);
                break;
                
            case PAUSED:
                updatePaused(input);
                break;
                
            case END:
                updateEnd(input);
                break;
        }


        /* if (gameSpeed.isPaused()) {
            drawGameState();
            battleScreen.draw(screenWidth);
            pauseScreen.updateTimescale(gameSpeed.getTimeScale());
            pauseScreen.draw(screenWidth);
            return;
        }
        
        // Game update with timescale
        double multiplier = gameSpeed.getMultiplier();
        accumulatedTime += multiplier;
        
        while (accumulatedTime >= 1.0) {
            time++;
            updateGameLogic(input);
            accumulatedTime -= 1.0;
        }
        
        drawGameState();
        battleScreen.draw(screenWidth); */
    }

    private void updateStart(Input input) {
    
    screenManager.draw();
    
    if (input.wasPressed(Keys.SPACE)) {
        gameState = GameState.BATTLE;
        inWave = true;
        time = 0;
        accumulatedTime = 0;
    }
}

    private void updateBattle(Input input) {
        // Check lose condition
        if (player.getHealth() <= 0) {
            endScreenSetup(false);
            gameState = GameState.END;
            return;
        }
        
         // Check win condition (all waves completed)
        if (allEnemiesAndProjectilesDestroyed() && inWave && currentWave >= gameResetter.getTotalWaves()) {
            inWave = false;
            endScreenSetup(true);
            gameState = GameState.END;
            return;
        }
        
        // Check if wave is complete
        if (allEnemiesAndProjectilesDestroyed() && inWave) {
            inWave = false;
            score += Integer.parseInt(gameProps.getProperty("score.waveCompleted"));
            
            if (currentWave < gameResetter.getTotalWaves()) {
                currentWave++;
                enemy = gameResetter.resetEnemies(currentWave - 1);
                powerUps.clear();

                Powerups[] wavePowerups = gameResetter.resetPowerups(currentWave);
                for (Powerups p : wavePowerups) {
                    powerUps.add(p);
                }
                playerProjectiles.clear();
                enemyProjectiles.clear();
                battleScreen.setCurrentWave(currentWave);
                inWave = true;
            }
            return;
        }
        
        // Pause toggle
        if (input.wasPressed(Keys.ESCAPE)) {
            gameState = GameState.PAUSED;
            gameSpeed.togglePause();
            return;
        }
        
        // Speed controls
        if (input.wasPressed(Keys.G)) gameSpeed.increase();
        if (input.wasPressed(Keys.F)) gameSpeed.decrease();
        if (input.wasPressed(Keys.I)) invincibleMode.toggle();
        if (input.wasPressed(Keys.N)) handleNextWaveCommand();
        
        // Game logic with timescale
        double multiplier = gameSpeed.getMultiplier();
        accumulatedTime += multiplier;
        
        while (accumulatedTime >= 1.0) {
            time++;
            updateGameLogic(input);
            accumulatedTime -= 1.0;
        }
        
        // Draw battle
        drawGameState();
        battleScreen.setScoreConfig(score, gameProps.getProperty("score.text"),
                                   gameProps.getProperty("score.pos").split(","));
        battleScreen.draw(screenWidth);
    }

    private void updatePaused(Input input) {
        // Handle pause-specific controls
        if (input.wasPressed(Keys.ESCAPE)) {
            gameState = GameState.BATTLE;
            gameSpeed.togglePause();
            return;
        }
        
        // Speed controls work in pause
        if (input.wasPressed(Keys.G)) gameSpeed.increase();
        if (input.wasPressed(Keys.F)) gameSpeed.decrease();
        
        // Draw frozen game state
        drawGameState();
        battleScreen.setScoreConfig(score, gameProps.getProperty("score.text"),
                                   gameProps.getProperty("score.pos").split(","));
        battleScreen.draw(screenWidth);
        pauseScreen.updateTimescale(gameSpeed.getTimeScale());
        pauseScreen.draw(screenWidth);
    }
 
    private void updateEnd(Input input) {
        // Player can move but not shoot
        player.movement(input);
        
        // Check boundaries
        if(player.getX() < PLAYER_BOUND_WIDTH / 2){
            player.setX(PLAYER_BOUND_WIDTH / 2);
        } else if(player.getX() > screenWidth - PLAYER_BOUND_WIDTH / 2){
            player.setX(screenWidth - PLAYER_BOUND_WIDTH / 2);
        }
        
        player.playerDraw();
        
        // Restart game if space pressed
        if (input.wasPressed(Keys.SPACE)) {
            resetGame();
        }
        
        // Draw end screen
        screenManager.draw();
    }
    
    private void handleGlobalControls(Input input) {
        
        if (input.wasPressed(Keys.G)) {
            gameSpeed.increase();
        }
        if (input.wasPressed(Keys.F)) {
            gameSpeed.decrease();
        }
        if (input.wasPressed(Keys.I)) {
            invincibleMode.toggle();
        }
        if (input.wasPressed(Keys.R)) {
            resetGame();
            gameState = GameState.START;
        }
    }

    private void handleNextWaveCommand() {
        // Clear all on-screen entities
        playerProjectiles.clear();
        enemyProjectiles.clear();
        for (Enemy e : enemy) {
            if (e != null) {
                e.despawned();
            }
        }
        powerUps.clear();
        
        // Award wave cleared bonus
        score += Integer.parseInt(gameProps.getProperty("score.waveCompleted"));
        
        // Move to next wave
        if (currentWave < gameResetter.getTotalWaves()) {
            currentWave++;
            enemy = gameResetter.resetEnemies(currentWave - 1);

            Powerups[] wavePowerups = gameResetter.resetPowerups(currentWave);
            for (Powerups p : wavePowerups) {
                powerUps.add(p);
            }
            battleScreen.setCurrentWave(currentWave);
            inWave = true;
        } else {
            // Last wave completed - go to win screen
            endScreenSetup(true);
            gameState = GameState.END;
        }
    }
    
    private void updateGameLogic(Input input) {
        player.movement(input);

        // bounds for the player
        if(player.getX() < PLAYER_BOUND_WIDTH / 2){
            player.setX(PLAYER_BOUND_WIDTH / 2);
        } else if(player.getX() > screenWidth - PLAYER_BOUND_WIDTH / 2){
            player.setX(screenWidth - PLAYER_BOUND_WIDTH / 2);
        }
        playerState.clearPowerupIfExpired(time);
        
        // Update enemies
        for (Enemy e : enemy) {
            if (e == null) continue;
            
            if (e.isExploding()) {
                e.updateExplosion(time);
                e.draw();
            } else {
                e.update(time);
                if (e.isSpawned()) {
                    Collision playerEnemyCollision = new Collision(player, e);
                    if (playerEnemyCollision.checkCollision() && !playerState.isInvincible(time)) {
                        player.livesLost();
                        score -= Integer.parseInt(gameProps.getProperty("score.gotHit"));
                        score = Math.max(0, score);
                        playerState.startInvincibility(time, gameResetter.getPlayerHitInvincibilityTime());
                        e.triggerExplosion(time);
                    }
                }

                if (e.getType().equals("SHOOTING")) {
                    e.updateProjectiles(time);
                    // Add enemy projectiles to the main list
                    enemyProjectiles.addAll(e.getProjectiles());
                    e.clearProjectiles();  // Clear after transferring
                }
            }
        }
        
        // Update powerups
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            Powerups p = powerUps.get(i);
            p.update(time);
            
            if (p.getY() > screenHeight) {
                powerUps.remove(i);
                continue;
            }
            
            // Check player-powerup collision
            if (p.isSpawned() && !p.isCollected()) {
                Collision playerPowerupCollision = new Collision(player, p);
                if (playerPowerupCollision.checkCollision()) {
                    activatePowerup(p);
                    score += Integer.parseInt(gameProps.getProperty("score.gotPowerup"));
                    p.despawned();
                }
            }
        }

        

        // Shooting
        if (input.wasPressed(Keys.SPACE)) {
            int cooldown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));

            // Apply cooldown powerup reduction
            if (playerState.getActivePowerupType() != null && 
                playerState.getActivePowerupType().equals("cooldown")) {
                cooldown = cooldown / 3;
                if (cooldown < 1) cooldown = 1;
            }
            
            Projectile newProjectile = new Projectile(
                gameProps.getProperty("projectile.image"),
                player.getX(),
                player.getY(),
                Integer.parseInt(gameProps.getProperty("projectile.movementSpeed")),
                cooldown
            );
            
            if (newProjectile.checkCooldown(time)) {
                playerProjectiles.add(newProjectile);
            }
        }
        
        // Update player projectiles
        for (int i = playerProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = playerProjectiles.get(i);
            p.update();
            if (p.despawned()) {
                playerProjectiles.remove(i);
            }
        }
        
        // Update enemy projectiles
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = enemyProjectiles.get(i);
            p.update();
            if (p.despawned()) {
                enemyProjectiles.remove(i);
            }
        }

        for (int i = playerProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = playerProjectiles.get(i);
            
            for (Enemy e : enemy) {
                if (e == null || !e.isSpawned() || e.isExploding()) continue;
                
                Collision projectileEnemyCollision = new Collision(p, e);
                if (projectileEnemyCollision.checkCollision()) {
                    // Award points based on enemy type
                    String scoreKey = "score.destroyedEnemy." + e.getType().toLowerCase();
                    score += Integer.parseInt(gameProps.getProperty(scoreKey));
                    
                    e.triggerExplosion(time);
                    playerProjectiles.remove(i);
                    break;
                }
            }
        }

        // Enemy projectile-player collision
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            EnemyProjectile ep = enemyProjectiles.get(i);
            
            Collision playerEnemyProjecileCollision = new Collision(player, ep);
            if (playerEnemyProjecileCollision.checkCollision() && !playerState.isInvincible(time)) {
                player.livesLost();
                score -= Integer.parseInt(gameProps.getProperty("score.gotHit"));
                score = Math.max(0, score);
                playerState.startInvincibility(time, gameResetter.getPlayerHitInvincibilityTime());
                enemyProjectiles.remove(i);
            }
        }
        
        // Enemy projectile-player projectile collision
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            EnemyProjectile ep = enemyProjectiles.get(i);
            
            for (int j = playerProjectiles.size() - 1; j >= 0; j--) {
                Projectile p = playerProjectiles.get(j);
                
                Collision collision = new Collision(p, ep);
                if (collision.checkCollision()) {
                    score += Integer.parseInt(gameProps.getProperty("score.hitProjectile"));
                    enemyProjectiles.remove(i);
                    playerProjectiles.remove(j);
                    break;
                }
            }
        }

    }

    private void activatePowerup(Powerups powerup) {
        String type = powerup.getType();
        
        switch (type) {
            case "shield":
                playerState.setActivePowerup(powerup, time);
                break;
            case "life":
                player.addLife();
                break;
            case "cooldown":
                playerState.setActivePowerup(powerup, time);
                break;
            case "engine":
                playerState.setActivePowerup(powerup, time);
                break;
        }
    }
 
    private boolean allEnemiesAndProjectilesDestroyed() {
        // Check enemies
        for (Enemy e : enemy) {
            if (e != null && e.isSpawned() && !e.isExploding()) {
                return false;
            }
        }
        
        // Check enemy projectiles
        if (!enemyProjectiles.isEmpty()) {
            return false;
        }
        
        // Check powerups
        for (Powerups p : powerUps) {
            if (p.isSpawned() && !p.isCollected()) {
                return false;
            }
        }
        
        return true;
    }
    
    private void drawGameState() {
        player.playerDraw();
        player.playerLivesDraw(
            gameProps.getProperty("playerLives.startPosition"),
            Integer.parseInt(gameProps.getProperty("playerLives.gap"))
        );
        
         // Draw invincibility overlay if active
        if (playerState.isInvincible(time) || playerState.isDevInvincibilityActive()) {
            // Draw invincibility image overlay
            String invincibilityImage = gameProps.getProperty("invincibility.image");
            // You'll need to load and draw this image
        }
        
        // Draw enemies and their projectiles
        for (Enemy e : enemy) {
            if (e != null) {
                e.draw();
                e.drawProjectiles();
            }
        }
        
        // Draw player projectiles
        for (Projectile p : playerProjectiles) {
            p.draw();
        }
        
        // Draw enemy projectiles
        for (EnemyProjectile p : enemyProjectiles) {
            p.draw();
        }
        
        // Draw powerups
        for (Powerups p : powerUps) {
            p.draw();
        }
    }

    

    public static void main(String[] args) {
        String gameDataPath = System.getProperty("gameData");
    
        if (gameDataPath == null || gameDataPath.isEmpty()) {
            gameDataPath = "gameData.properties";
    }
        Properties gameProps = IOUtils.readPropertiesFile(gameDataPath);
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}