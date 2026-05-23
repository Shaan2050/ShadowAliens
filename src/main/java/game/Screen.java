package game;


import bagel.DrawOptions;
import bagel.Font;

public abstract class Screen {
    protected Font font;
    protected String fontFile;
    protected DrawOptions textColor;
    protected String text;
    protected double posY;
    protected int size;
    protected double instructionsStartPos;
    protected double instructionsRowGap;
    protected String[] instructionsText;

    public Screen(String fontFile, DrawOptions textColor){
        this.fontFile = fontFile;
        this.font = new Font(fontFile, 10);
        this.textColor = textColor;
    }

    public void setTitleConfig(String text, int size, double posY) {
        this.text = text;
        this.posY = posY;
        this.size = size;
    }

    public void setControlsConfig(String instructionsList, double startPos, double rowGap) {
        this.instructionsText = instructionsList.split(",");
        this.instructionsStartPos = startPos;
        this.instructionsRowGap = rowGap;
    }
    
    protected void setFontSize(int size) {
        this.font = new Font(fontFile, size);
    }

    protected void drawCenteredText(String text, double posY, double screenWidth) {
        double textWidth = font.getWidth(text);
        double posX = (screenWidth - textWidth) / 2;
        font.drawString(text, posX, posY, textColor);
    }

    protected void drawControlsList(){
        for (int i = 0; i < instructionsText.length; i++) {
            drawCenteredText(instructionsText[i], instructionsStartPos + (i * instructionsRowGap), font.getWidth(instructionsText[i]));
        }
    }

    protected void drawTitle(double screenWidth) {
        setFontSize(size);
        drawCenteredText(text, posY, screenWidth);
    }
    
    public abstract void draw(double value);
}
