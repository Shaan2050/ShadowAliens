package game;
import bagel.DrawOptions;

public class EndScreen extends Screen{
    private boolean status;

    public EndScreen(String fontFile, DrawOptions textColor) {
        super(fontFile, textColor);
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    @Override
    public void draw(double screenWidth) {
        drawTitle(screenWidth);
        drawControlsList();
    }
}
