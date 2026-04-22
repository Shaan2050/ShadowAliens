package game;

import java.util.ArrayList;
import java.util.Properties;

import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;

/**
 * Main game class that manages initialising the screens and game objects
 */
public class ShadowAliens extends AbstractGame {
    private Player player;
    private ArrayList<Projectile> projectile = new ArrayList<>();
    private Enemy[] enemy = new Enemy[10];

    private static boolean shoot;
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

        if (input.wasPressed(Keys.SPACE)) {
            Projectile newProjectile = new Projectile(gameProps.getProperty("projectile.image"),
                player.getX(),
                player.getY(),
                Integer.parseInt(gameProps.getProperty("projectile.movementSpeed")),
                Integer.parseInt(gameProps.getProperty("player.shootCooldown"))
                );

            if(newProjectile.checkCooldown(time)){
                projectile.add(newProjectile);
            }
        }
            for(int i = projectile.size() - 1; i >= 0; i--){
                Projectile p = projectile.get(i);
                p.update();
                if(p.despawned()){
                    projectile.remove(i);
                }
            }

            for(Projectile p : projectile){
                p.draw();
            }
        
        
        
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(("gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
