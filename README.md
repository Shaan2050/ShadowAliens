# SWEN20003 Semester 1, 2026
# Project 1
# Shadow Aliens

## Running Instructions
**NOTE:** This project was completed and tested in Visual Studio Code hence, there are also instructions for running it there.
### IntelliJ IDEA
1. Open project folder in IntelliJ
2. IntelliJ will automatically detect Maven and download dependencies
3. Navigate to `src/main/java/game/ShadowAliens.java`
4. Click the run button

### VS Code
1. Open the project folder in VS Code
2. Install "Extension Pack for Java" if not already installed
3. Open `src/main/java/game/ShadowAliens.java`
4. Click "Run" above the main method

#### Run Configuration Details (IntelliJ)
- **Main class**: `game.ShadowAliens`
- **VM options**: `-DgameData="gameData.properties"`
- **Working directory**: Project root directory

### JVM Argument Handling
The game accepts a custom properties file via JVM argument:
- **Argument**: `-DgameData="path/to/file.properties"`
- **Default**: If not provided, uses `gameData.properties` in the working directory
- **Error Handling**: If the file is not found, prints "Error with {file path}" and exits with code -1
## Assumptions
None
* 

## AI Statement
I acknowledge the use of DeepSeek [chat.deepseek.com] in helping me fix runtime errors.

I used the error outputs to ask DeepSeek what it means and revised my code accordingly.
## Code References

* 

## Design Report

### OOP
**Extension:** Project 2b tasked us with completely implementing all the features of our game that being, the start screen and end screen along with more functionality to the gameplay. The existing Enemy class used in Project 1 was resued but with more functionality. Now, there are different enemy types and each of them have different behaviour. This required the addition of new attributes: "Xdirection", "hasInitialisedDirection", "lastShotTime", "shootingFireRate", lines 17-22, to the enemy class to address their strafing and shooting aspects such as "updateStrafing()" and "updateShooting()" in lines 109 and 132 respectively. Furthermore, Enemy class now inherits from a newly created class in this project called NonPlayerEntity class. This is an abstract class that handles the common behaviour of the entities in the game that are not the player such as image, drawing and arrival status. The exisitng Enemy class made it easy to add these features without building anything from scratch. But because one class now handles common behviour, it meant refactoring Enemy class to facilitate NonPlayerEntity class.

New screens like the pause screen were needed so an abstract Screen class was created to handle all the shared behaviour of the screens including the existing Pause screen. Many methods of Pause class: "setTitleConfig()", "setControlsConfig", "drawCenteredtext", were moved to Screen class because other screens would also need to use these methods.

There are now 'Powerups' in the game which the player can use. Powerups and enemies both inherit from NonPlayerEntity because they share common behaviour that they have an image, they need to be drawn, they arrive on screen after a certain amount of time and they can interact with the player

**Outcome:** The NonPlayerEntity demonstrates inheritance and polymorphism. This class also allows for addition of more entities to the game with specific behaviour defined in the concrete sub classes.

This project still maintained its core principles of achieving modularity and code fully utilised core principles of OOP using abstraction and inheritance as seen in the Enemy and Powerups classes. The current state of the code provides room for future functionality such as more powerups or more screens or an entirely new entity in the game.

However, the modulation done in this project has made the main ShadowAliens file very long and quite tedious to follow. Further addition to this project will complicate ShadowAliens even further unless the class itself is divided to handle specific parts of the game. For example, one class will only handle updating entities, one class to handle loading all data and properties and so on. These classes would depend on abstraction rather than concrete classes.

## Design Report References

* 