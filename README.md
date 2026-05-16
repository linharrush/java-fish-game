# Ocean Game

Java desktop project for an ocean-themed game built on top of a small routed architecture.

## What the project currently does

- Opens a Swing-based game window with a looping ocean background.
- Renders fish on screen.
- Lets the player drag one designated fish with the mouse.
- Moves selected non-player fish periodically.
- Detects overlap between fish and prints `WIN!` or `LOSE!` according to fish size.

## Main runtime flow

```text
UI event
  -> MainRouter
  -> OceanGameRouter
  -> OceanGameBackend
  -> Canvas / Fish model
  -> OceanGameUiPort
  -> OceanGameUiPortImpl
  -> repaint
```

## Project structure

```text
src/
  ai/ui/               Swing UI and rendering classes
  shared/              routing layer and UI-port abstractions
  team/control/        backend game logic
  team/model/          domain objects such as Fish, Point, Circle, Canvas
  my_base/             application bootstrap and periodic loop
```

## Important design note

The ocean game now uses `Fish` as its real gameplay model.  
`Circle` and `Point` still exist because they belong to the older `ex3` exercise path, but the `ocean` flow should not depend on them.

## Running the project

The entry point is:

```text
src/my_base/App.java
```

The project depends on the libraries in `lib/`, including JavaFX jars used by the background-video component.

## Documentation

- Architecture overview: `docs/ARCHITECTURE.md`
