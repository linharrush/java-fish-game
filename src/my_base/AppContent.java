package my_base;

import team.control.Ex3Backend;
import team.control.OceanGameBackend;
import team.model.Canvas;

/*
 * This class should hold the content of the system, i.e., all elements that are
 * related to the essence of the system.
 * 
 */
public class AppContent {
	private Canvas canvas = new Canvas();
	private Ex3Backend ex3Backend;
	private OceanGameBackend oceanGameBackend;
	
	public void initContent() {
		ex3Backend = new Ex3Backend();
		oceanGameBackend = new OceanGameBackend();
		canvas.initCanvas();
	};

	public Canvas canvas() {
		return canvas;
	}	
	public Ex3Backend ex3Backend() {
		return ex3Backend;
	}

	public OceanGameBackend oceanGameBackend() {
		return oceanGameBackend;
	}

}
