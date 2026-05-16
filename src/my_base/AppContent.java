package my_base;

import team.control.OceanGameBackend;
import team.model.Canvas;

/*
 * This class should hold the content of the system, i.e., all elements that are
 * related to the essence of the system.
 * 
 */
public class AppContent {
	private Canvas canvas = new Canvas();
	private OceanGameBackend oceanGameBackend;
	
	public void initContent() {
		oceanGameBackend = new OceanGameBackend();
		canvas.initCanvas();
	};

	public Canvas canvas() {
		return canvas;
	}	
	public OceanGameBackend oceanGameBackend() {
		return oceanGameBackend;
	}

}
