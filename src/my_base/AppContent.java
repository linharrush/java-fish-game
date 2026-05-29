package my_base;

import team.control.OceanGameBackend;
import team.control.MovementController;
import team.control.CollisionController;
import team.control.GameController;
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
	private GameState gameState;
	private MovementController movementController;
	private CollisionController collisionController;
	private GameController gameController;
	private OceanGameBackend oceanGameBackend;
	
	public void initContent() {
		canvas.initCanvas();
		gameState = new GameState(canvas, new LevelProgress());
		gameController = new GameController(gameState);
		movementController = new MovementController(gameState);
		collisionController = new CollisionController(gameState);
		oceanGameBackend = new OceanGameBackend();
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

	public OceanGameBackend oceanGameBackend() {
		return oceanGameBackend;
	}

}
