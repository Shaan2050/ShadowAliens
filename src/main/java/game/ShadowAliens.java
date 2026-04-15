package game;

import java.util.Properties;

import bagel.AbstractGame;
import bagel.Input;


/**
 * Main game class that manages initialising the screens and game objects
 */
public class ShadowAliens extends AbstractGame {
    private Player player;
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
    }


    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        player.movement(input);
        player.draw();
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(("gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
