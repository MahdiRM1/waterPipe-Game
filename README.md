# WaterPipe

A desktop puzzle game built with JavaFX. Rotate the pipes on the board to connect
the water source to the destination before you run out of moves or time.

## Features

- 3 levels of increasing difficulty
- Click-to-rotate pipe pieces with a flowing-water animation on success
- Move counter, timer, and undo support
- Sound effects for clicks, hover, rotation, win, and lose

## Requirements

- JDK 21+
- Apache Maven 3.8+

## Running the game

```bash
mvn clean javafx:run
```

## Building a jar

```bash
mvn clean package
```

## Project structure

```
src/main/java/project/
├── WaterPipe.java       # Application entry point
├── LevelSelection.java  # Level select screen
├── GameUI.java          # In-game screen, win/lose panes, animations
├── Board.java           # Grid/puzzle state
├── CellPipe.java        # Single pipe cell: type, rotation, connections
├── Move.java            # Records a single rotation move (for undo)
├── ScoreBoard.java       # Move counter and timer
├── ImageFactory.java    # Image loading/positioning helpers
├── SoundManager.java    # Sound effect playback
└── Constants.java       # Screen size constants

src/main/resources/
├── Pictures/            # Sprites and UI images
└── Audio/                # Sound effects
```

## License

MIT — see [LICENSE](LICENSE).
