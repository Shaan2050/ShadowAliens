package game;

import bagel.DrawOptions;

public class PauseScreen extends Screen {
    private double timeScale;
    private String timescaleText;
    private double timescaleX;
    private double timescaleY;
    
    public PauseScreen(String fontFile, DrawOptions textColor) {
        super(fontFile, textColor);
    }
    
    public void setTimescaleConfig(String text, String position) {
        this.timescaleText = text;
        String[] pos = position.split(",");
        this.timescaleX = Double.parseDouble(pos[0]);
        this.timescaleY = Double.parseDouble(pos[1]);
    }
    
    public void updateTimescale(double timeScale) {
        this.timeScale = timeScale;
    }

    @Override
    public void draw(double screenWidth) {
        drawTitle(screenWidth);
        drawControlsList();
        font.drawString(timescaleText + timeScale, timescaleX, timescaleY, textColor);
    }
}