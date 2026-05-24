package game;
 
/**
 * Manages the player's active state including powerups and invincibility effects
 */
public class PlayerState {
    private Powerups activePowerup = null;
    private int invincibilityEndTime = -1;
    private boolean devInvincibilityActive = false;
    
    public void setActivePowerup(Powerups powerup, int currentTime) {
        this.activePowerup = powerup;
        if (powerup != null) {
            powerup.activate(currentTime);
        }
    }
    
    public Powerups getActivePowerup() {
        return activePowerup;
    }
    
    public void clearActivePowerup() {
        activePowerup = null;
    }
    
    public String getActivePowerupType() {
        if (activePowerup == null) return null;
        return activePowerup.getType();
    }
    
    public boolean hasPowerup() {
        return activePowerup != null;
    }
    
    public void clearPowerupIfExpired(int currentTime) {
        if (activePowerup != null && activePowerup.isExpired(currentTime)) {
            clearActivePowerup();
        }
    }
    
    // Invincibility management (from hitting enemies)
    public void startInvincibility(int currentTime, int duration) {
        this.invincibilityEndTime = currentTime + duration;
    }
    
    public boolean isInvincible(int currentTime) {
        return (currentTime < invincibilityEndTime || 
                (activePowerup != null && activePowerup.getType().equals("shield") && 
                 !activePowerup.isExpired(currentTime))) ||
                devInvincibilityActive;
    }
    
    public void endInvincibility() {
        this.invincibilityEndTime = -1;
    }
    
    // Dev mode invincibility
    public void toggleDevInvincibility() {
        devInvincibilityActive = !devInvincibilityActive;
    }
    
    public boolean isDevInvincibilityActive() {
        return devInvincibilityActive;
    }
    
    public void setDevInvincibility(boolean active) {
        devInvincibilityActive = active;
    }
    
    public void reset() {
        activePowerup = null;
        invincibilityEndTime = -1;
        devInvincibilityActive = false;
    }
}
