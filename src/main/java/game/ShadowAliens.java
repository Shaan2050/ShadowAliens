package game;

import java.util.Properties;

import bagel.AbstractGame;
import bagel.Input;


/**
 * Main game class that manages initialising the screens and game objects
 */
public class ShadowAliens extends AbstractGame {
    private Player player;
    private Enemy[] enemy = new Enemy[10];
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

        this.player = new Player(gameProps.getProperty("player.image"),
                screenWidth / 2,
                Double.parseDouble(gameProps.getProperty("player.posY")),
                Integer.parseInt(gameProps.getProperty("player.initialLives")),
                Integer.parseInt(gameProps.getProperty("player.speed")));

        for(int i = 0; i < 10; i++){
            this.enemy[i] = new Enemy(gameProps.getProperty("enemy.image"),
            Integer.parseInt(gameProps.getProperty("enemy." + i + ".arrivalTime")),
            Integer.parseInt(gameProps.getProperty("enemy." + i + ".movementSpeed")),
            Integer.parseInt(gameProps.getProperty("enemy." + i + ".posX")));
        }
    }


    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        player.movement(input);
        player.playerLivesDraw(gameProps.getProperty("playerLives.image"),
                               gameProps.getProperty("playerLives.startPosition"),
                               Integer.parseInt(gameProps.getProperty("playerLives.gap")));

        for(Enemy e : enemy) {
            e.drawEnemy();
        }
        player.playerDraw();
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(("gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
