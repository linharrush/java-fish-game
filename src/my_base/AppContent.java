package my_base;

import java.util.Random;
import team.control.MovementController;
import team.control.CollisionController;
import team.control.DifficultyController;
import team.control.GameController;
import team.control.GameLoopController;
import team.control.SpawnController;
import team.model.Canvas;
import team.model.GameState;
import team.model.LevelProgress;

/*
 * This class should hold the content of the system, i.e., all elements that are
 * related to the essence of the system.
 * 
 */
public class AppContent {
	private Canvas canvas = new Canvas();
	private Random random = new Random();
	private GameState gameState;
	private DifficultyController difficultyController;
	private SpawnController spawnController;
	private MovementController movementController;
	private CollisionController collisionController;
	private GameController gameController;
	private GameLoopController gameLoopController;
	
	public void initContent() {
		canvas.initCanvas();
		gameState = new GameState(canvas, new LevelProgress());
		difficultyController = new DifficultyController(random);
		spawnController = new SpawnController(gameState, difficultyController, random);
		gameController = new GameController(gameState, spawnController);
		movementController = new MovementController(gameState);
		collisionController = new CollisionController(gameState, gameController);
		gameLoopController = new GameLoopController(gameState, movementController, spawnController, collisionController);
	};

	public Canvas canvas() {
		return canvas;
	}	
	public GameState gameState() {
		return gameState;
	}

	public MovementController movementController() {
		return movementController;
	}

	public CollisionController collisionController() {
		return collisionController;
	}

	public GameController gameController() {
		return gameController;
	}

	public SpawnController spawnController() {
		return spawnController;
	}

	public GameLoopController gameLoopController() {
		return gameLoopController;
	}

}
