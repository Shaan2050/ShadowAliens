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
***Encapsulation:*** src\main\java\game\Player.java lines 8-16 uses encapsulation by making the variables private. There are getters for some of these variables in lines 55-65.
***Method Overloading:*** src\main\java\game\Collision.java lines 11 and 16 use same constructor but with different parameters. This creates different objects based on the usage.
### Design choice
Game was divided into many classes that handle specific parts of the game. Player class handled movement and lives, Enemy class controlled enemy movement and spawns, GameSpeed handles timescale and pause state. src\main\java\game\ShadowAliens.java acts as the central controller for all the classes.

I considered consolidating all logic into ShadowAliens.java which could have eliminated the need for getters and setters.

But the current design is much superior because of modularity- easy to debug and add more features. Allows for simple testing. Any changes will point to an individual file.
## Design Report References

* 