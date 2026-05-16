# Architecture

## Core idea

The project separates input, routing, game logic, and rendering into distinct layers:

```text
DrawingPanel
   |
   v
MainRouter
   |
   v
OceanGameRouter
   |
   v
OceanGameBackend
   |
   v
Canvas / Fish
   |
   v
OceanGameUiPort
   |
   v
OceanGameUiPortImpl
```

## Layer responsibilities

### UI layer

Main classes:

- `ai.ui.Ui`
- `ai.ui.DrawingPanel`
- `ai.ui.OceanGameUiPortImpl`

Responsibilities:

- Create the window.
- Render objects.
- Capture mouse input.
- Send user actions to the router.
- Apply drawing updates requested by the backend.

The UI should not own game rules.

### Routing layer

Main classes:

- `shared.MainRouter`
- `shared.routers.OceanGameRouter`

Responsibilities:

- Receive string routes such as `/ocean/fish/move`.
- Dispatch them to the correct backend method.
- Keep the UI decoupled from concrete backend classes.

### Backend layer

Main class:

- `team.control.OceanGameBackend`

Responsibilities:

- Own gameplay decisions.
- Update the domain model.
- Check collisions / win / lose conditions.
- Send output commands back to the UI through `OceanGameUiPort`.

### Model layer

Main classes:

- `team.model.Canvas`
- `team.model.Fish`

Responsibilities:

- Hold the current game state.
- Represent fish position, size, direction, and whether a fish belongs to the player.

## Current ocean-game flow

### Startup

1. `App.main()` initializes `AppContent`.
2. `Canvas` creates the starting fish model.
3. `Ui.start()` calls `/ocean/start`.
4. `OceanGameBackend.startScenario()` reads the fish from `Canvas`.
5. The backend tells the UI to draw those fish.

### Dragging the player fish

1. `DrawingPanel` detects a mouse drag on a fish marked as `player`.
2. The UI sends `/ocean/fish/move`.
3. `OceanGameRouter` calls `OceanGameBackend.moveFish(...)`.
4. The backend updates the `Fish` object in `Canvas`.
5. The backend asks the UI port to redraw the fish.
6. The UI sends `/ocean/fish/eat` to check overlap after movement.

### Periodic movement

1. `MyPeriodicLoop` runs on a scheduler.
2. It asks `OceanGameBackend` to move selected fish by index.
3. The backend updates fish positions in `Canvas`.
4. The UI receives updated fish coordinates and repaints.

## Legacy path

The older `ex3` path still uses `Point` and `Circle`:

- `team.control.Ex3Backend`
- `shared.routers.Ex3Router`
- `shared.ui_ports.Ex3UiPort`

Those classes are currently separate from the ocean-game logic and should remain conceptually isolated from it.
