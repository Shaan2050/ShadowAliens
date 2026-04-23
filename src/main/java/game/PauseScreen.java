package game;

import bagel.Font;

public class PauseScreen {
    private Font titleFont;
    private String titleText;
    private double titleY;
    private String[] controls;
    private double controlsStartY;
    private double controlsRowGap;
    private String timescaleText;
    private double timescaleX;
    private double timescaleY;
    
    public PauseScreen() {}
    
    public void setTitleConfig(String fontFile, int size, String text, double y) {
        this.titleFont = new Font(fontFile, size);
        this.titleText = text;
        this.titleY = y;
    }
    
    public void setControlsConfig(String controlsList, double startY, double rowGap) {
        this.controls = controlsList.split(",");
        this.controlsStartY = startY;
        this.controlsRowGap = rowGap;
    }
    
    public void setTimescaleConfig(String text, String position) {
        this.timescaleText = text;
        String[] pos = position.split(",");
        this.timescaleX = Double.parseDouble(pos[0]);
        this.timescaleY = Double.parseDouble(pos[1]);
    }
    
    public void draw(double screenWidth, int timeScale) {
        double titleX = screenWidth / 2 - titleFont.getWidth(titleText) / 2;
        titleFont.drawString(titleText, titleX, titleY);
        
        for (int i = 0; i < controls.length; i++) {
            double x = screenWidth / 2 - titleFont.getWidth(controls[i]) / 2;
            titleFont.drawString(controls[i], x, controlsStartY + (i * controlsRowGap));
        }
        
        String tsText = timescaleText + " " + timeScale;
        titleFont.drawString(tsText, timescaleX, timescaleY);
    }
}