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
    private static int index = 0;
    private static int time = 0;
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
                gameProps.getProperty("playerLives.image"),
                screenWidth / 2,
                Double.parseDouble(gameProps.getProperty("player.posY")),
                Integer.parseInt(gameProps.getProperty("player.initialLives")),
                Integer.parseInt(gameProps.getProperty("player.speed")));

        while(true){
            String time = gameProps.getProperty("enemy." + index + ".arrivalTime");
            if(time == null){
                break;
            }
            this.enemy[index] = new Enemy(gameProps.getProperty("enemy.image"),
            Integer.parseInt(time),
            Integer.parseInt(gameProps.getProperty("enemy." + index + ".movementSpeed")),
            Integer.parseInt(gameProps.getProperty("enemy." + index + ".posX")));

            index++;
        }
    }


    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        time++;
        player.movement(input);
        player.playerLivesDraw(gameProps.getProperty("playerLives.startPosition"),
                               Integer.parseInt(gameProps.getProperty("playerLives.gap")));

      
        player.playerDraw();
        
        
        for(Enemy e : enemy) {
            Collision playerEnemyCollision = new Collision(player, e);
            e.update(time);
            e.drawEnemy();
            
            if(e.isSpawned() && playerEnemyCollision.checkCollision(player, e)){
                player.livesLost();
                e.despawned();

            }
        }
        
        
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(("gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
