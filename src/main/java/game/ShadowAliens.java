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
    private Player player;
    private ArrayList<Projectile> projectile = new ArrayList<>();
    private Enemy[] enemy;
    
    // New components
    private GameSpeed gameSpeed;
    private InvincibleMode invincibleMode;
    private GameResetter gameResetter;
    private PauseScreen pauseScreen;
    
    private final Font uiFont;
    private DrawOptions textColor;
    private final String waveText;
    private final double waveX, waveY;
    private  String scoreText;
    private double scoreX, scoreY;
    private int score = 0;

    private static int time = 0;
    private double accumulatedTime = 0;
    
    private static Properties gameProps;
    public static double screenWidth;
    public static double screenHeight;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

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
        
        // Initialize components
        this.gameSpeed = new GameSpeed();
        this.invincibleMode = new InvincibleMode();
        
        // Setup GameResetter
        this.gameResetter = new GameResetter();
        gameResetter.setPlayerConfig(
            gameProps.getProperty("player.image"),
            gameProps.getProperty("playerLives.image"),
            screenWidth / 2,
            Double.parseDouble(gameProps.getProperty("player.posY")),
            Integer.parseInt(gameProps.getProperty("player.initialLives")),
            Integer.parseInt(gameProps.getProperty("player.speed"))
        );
        gameResetter.setEnemyConfig(
            gameProps.getProperty("enemy.image"),
            gameProps.getProperty("explosion.image"),
            Integer.parseInt(gameProps.getProperty("explosion.duration"))
        );
        
        // Load enemies into GameResetter
        loadEnemies();
        
        // Setup PauseScreen
        this.pauseScreen = new PauseScreen();
        pauseScreen.setTitleConfig(
            gameProps.getProperty("text.font"),
            Integer.parseInt(gameProps.getProperty("pausedTitle.size")),
            gameProps.getProperty("pausedTitle.text"),
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
        
        // Initialize game objects using GameResetter
        this.player = gameResetter.resetPlayer();
        this.enemy = gameResetter.resetEnemies();
    }
    
    private void loadEnemies() {
        int index = 0;
        while (true) {
            String time = gameProps.getProperty("enemy." + index + ".arrivalTime");
            if (time == null) break;
            
            gameResetter.addEnemy(
                Integer.parseInt(time),
                Integer.parseInt(gameProps.getProperty("enemy." + index + ".movementSpeed")),
                Integer.parseInt(gameProps.getProperty("enemy." + index + ".posX"))
            );
            index++;
        }
    }
    
    private void resetGame() {
        time = 0;
        accumulatedTime = 0;
        gameSpeed.reset();
        invincibleMode.reset();
        player = gameResetter.resetPlayer();
        projectile.clear();
        enemy = gameResetter.resetEnemies();
    }

    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        // Handle global controls
        handleGlobalControls(input);
        
        if (player.getHealth() <= 0) {
            Window.close();
            return;
        }

        if (gameSpeed.isPaused()) {
            drawGameState();
            drawUI();
            pauseScreen.draw(screenWidth, gameSpeed.getTimeScale());
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
        drawUI();
    }
    
    private void handleGlobalControls(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            gameSpeed.togglePause();
        }
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
            gameSpeed.setBattle();
        }
    }
    
    private void updateGameLogic(Input input) {
        player.movement(input);
        player.playerLivesDraw(gameProps.getProperty("playerLives.startPosition"),
                               Integer.parseInt(gameProps.getProperty("playerLives.gap")));
        player.playerDraw();
        
        // Update enemies
        for (Enemy e : enemy) {
            if (e == null) continue;
            
            if (e.isExploding()) {
                e.updateExplosion(time);
                e.drawEnemy();
            } else {
                Collision playerEnemyCollision = new Collision(player, e);
                e.update(time);
                e.drawEnemy();
                
                if (e.isSpawned() && playerEnemyCollision.checkCollision(player, e)) {
                    if (!invincibleMode.isActive()) {
                        player.livesLost();
                    }
                    e.despawned();
                }
            }
        }
        
        // Shooting
        if (input.wasPressed(Keys.SPACE)) {
            Projectile newProjectile = new Projectile(
                gameProps.getProperty("projectile.image"),
                player.getX(),
                player.getY(),
                Integer.parseInt(gameProps.getProperty("projectile.movementSpeed")),
                Integer.parseInt(gameProps.getProperty("player.shootCooldown"))
            );
            
            if (newProjectile.checkCooldown(time)) {
                projectile.add(newProjectile);
            }
        }
        
        // Update projectiles
        for (int i = projectile.size() - 1; i >= 0; i--) {
            Projectile p = projectile.get(i);
            p.update();
            if (p.despawned()) {
                projectile.remove(i);
            }
        }
        
        // Projectile-Enemy collision
        for (int i = projectile.size() - 1; i >= 0; i--) {
            Projectile p = projectile.get(i);
            
            for (Enemy e : enemy) {
                if (e == null || !e.isSpawned() || e.isExploding()) continue;
                
                Collision projectileEnemyCollision = new Collision(p, e);
                
                if (projectileEnemyCollision.checkCollision(p, e)) {
                    score++;
                    e.triggerExplosion(time);
                    projectile.remove(i);
                    break;
                }
            }
        }
    }
    
    private void drawGameState() {
        player.playerDraw();
        player.playerLivesDraw(
            gameProps.getProperty("playerLives.startPosition"),
            Integer.parseInt(gameProps.getProperty("playerLives.gap"))
        );
        
        for (Enemy e : enemy) {
            if (e != null) e.drawEnemy();
        }
        
        for (Projectile p : projectile) {
            p.draw();
        }
    }

    private void drawUI() {
        // Draw wave
        uiFont.drawString(waveText + " 1", waveX, waveY, textColor);
        
        // Draw score
        uiFont.drawString(scoreText + " " + score, scoreX, scoreY, textColor);
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