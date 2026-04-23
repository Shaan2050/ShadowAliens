package game;

public class InvincibleMode {
    private boolean active = false;
    
    public void toggle() {
        active = !active;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void reset() {
        active = false;
    }
}