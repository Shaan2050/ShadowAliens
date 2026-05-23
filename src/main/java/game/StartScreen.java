package game;
import bagel.DrawOptions;

public class StartScreen extends Screen {
    public StartScreen(String fontFile, DrawOptions textColor) {
        super(fontFile, textColor);
    }

    @Override
    public void draw(double screenWidth) {
        drawTitle(screenWidth);
        drawControlsList();
    }
}
