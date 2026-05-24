package game;

public class ScreenManager {
    private enum GameState {
        START,
        BATTLE,
        PAUSED,
        END
    }
    
    private GameState currentState;
    private BattleScreen battleScreen;
    private StartScreen startScreen;
    private EndScreen endScreen;
    private PauseScreen pauseScreen;
    private final int screenWidth;

    public ScreenManager(int screenWidth) {
        this.screenWidth = screenWidth;
        this.currentState = GameState.START;
    }

    public void setBattleScreen(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;
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
    
    public GameState getState() { return currentState; }
    
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
