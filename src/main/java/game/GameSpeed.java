package game;

public class GameSpeed {
    public enum GameState {
        BATTLE, PAUSED
    }
    
    private GameState gameState;
    private int timeScale;
    
    public GameSpeed() {
        this.gameState = GameState.BATTLE;
        this.timeScale = 1;
    }
    
    public void increase() {
        timeScale++;
    }
    
    public void decrease() {
        if (timeScale > 1) {
            timeScale--;
        } else if (timeScale == 1) {
            timeScale = -1;
        } else {
            timeScale--;
        }
    }
    
    public double getMultiplier() {
        if (timeScale > 0) {
            return timeScale;
        } else {
            return 1.0 / (2 - timeScale);
        }
    }
    
    public int getTimeScale() {
        return timeScale;
    }
    
    public void togglePause() {
        gameState = (gameState == GameState.BATTLE) ? GameState.PAUSED : GameState.BATTLE;
    }
    
    public boolean isPaused() {
        return gameState == GameState.PAUSED;
    }
    
    public boolean isBattle() {
        return gameState == GameState.BATTLE;
    }
    
    public void setBattle() {
        gameState = GameState.BATTLE;
    }
    
    public void reset() {
        timeScale = 1;
        gameState = GameState.BATTLE;
    }
}