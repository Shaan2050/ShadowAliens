package game;

public class ScreenManager {
    private enum GameState {
        START,
        BATTLE,
        PAUSED,
        END
    }
    
    private GameState currentState;
    private StartScreen startScreen;
    private EndScreen endScreen;
    private PauseScreen pauseScreen;
    private final int screenWidth;

    public ScreenManager(int screenWidth) {
        this.screenWidth = screenWidth;
        this.currentState = GameState.START;
    }

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    public void setEndScreen(EndScreen endScreen) {
        this.endScreen = endScreen;
    }

    public void setPauseScreen(PauseScreen pauseScreen) {
        this.pauseScreen = pauseScreen;
    }
     public void setState(GameState state) { this.currentState = state; }
    public GameState getState() { return currentState; }
    public boolean isState(GameState state) { return currentState == state; }
    
    // Transitions
    public void startBattle() { currentState = GameState.BATTLE; }
    public void pause() { currentState = GameState.PAUSED; }
    public void unpause() { currentState = GameState.BATTLE; }
    public void endGame() { currentState = GameState.END; }
    
    // Drawing
    public void draw() {
        if (currentState == GameState.START) {
            startScreen.draw(screenWidth);
        } else if (currentState == GameState.PAUSED) {
            pauseScreen.draw(screenWidth);
        } else if (currentState == GameState.END) {
            endScreen.draw(screenWidth);
        }
    }
    
    public void setPauseTimeScale(int timeScale) {
        pauseScreen.updateTimescale(timeScale);
    }
    
    public void reset() {
        currentState = GameState.BATTLE;
    }
}
