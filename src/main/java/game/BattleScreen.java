package game;
import bagel.DrawOptions;


public class BattleScreen extends Screen{
    private int posX;
    private int currentWave;

    private String scoreText;
    private int scoreX;
    private int scoreY;
    private int score;

    public BattleScreen(String fontFile, DrawOptions textColor){
        super(fontFile, textColor);
    }

    public void setWaveCongfig(String text, int size, String[] pos){
        super.text = text;
        super.size = size;
        this.posX = Integer.parseInt(pos[0]);
        super.posY = Integer.parseInt(pos[1]);
    }

    public void setCurrentWave(int wave){
        this.currentWave = wave;
    }
    public void setScoreConfig(int score, String text, String[] pos){
        this.score = score;
        this.scoreText = text;
        this.scoreX = Integer.parseInt(pos[0]);
        this.scoreY = Integer.parseInt(pos[1]);
    }

    @Override
    public void draw(double screenWidth) {
        setFontSize(size);
        // Draw wave
        font.drawString(text + currentWave, posX, posY, textColor);
        
        // Draw score
        font.drawString(scoreText + score, scoreX, scoreY, textColor);
    }

}
