package game;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Player{
    private Image playerImage;
    //private static Image lives = new Image();
    private String playerIcon;
    
    private int health;
    final private int speed;
    private double x;
    final private double y;

    public Player(String playerIcon, double x, double y, int health, int speed){
        this.playerIcon = playerIcon;
        this.x = x;
        this.y = y;
        
        this.health = health;
        this.speed = speed;

        this.playerImage = new Image(playerIcon);
    }

    public void movement(Input input){
        if (input.isDown(Keys.A)) {
            x -= speed;
        }
        if (input.isDown(Keys.D)) {
            x += speed;
        }
    }

    public void playerDraw(){
        playerImage.draw(x, y);
    }

    public void playerLivesDraw(String image, String position, int gap){
        int x = Integer.parseInt(position.split(",")[0]);
        int y = Integer.parseInt(position.split(",")[1]);
        for(int i = 0; i < health; i++){
            Image livesImage = new Image(image);
            livesImage.draw(x + (i * gap), y);
        }
    }
}