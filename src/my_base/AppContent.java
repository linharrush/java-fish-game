package my_base;

import team.control.OceanGameBackend;
import team.control.MovementController;
import team.model.Canvas;
import team.model.GameState;

/*
 * This class should hold the content of the system, i.e., all elements that are
 * related to the essence of the system.
 * 
 */
public class AppContent {
	private Canvas canvas = new Canvas();
	private GameState gameState;
	private MovementController movementController;
	private OceanGameBackend oceanGameBackend;
	
	public void initContent() {
		canvas.initCanvas();
		gameState = new GameState(canvas);
		movementController = new MovementController(gameState);
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

	public OceanGameBackend oceanGameBackend() {
		return oceanGameBackend;
	}

}
