package my_base;

import base.PeriodicLoop;

public class MyPeriodicLoop extends PeriodicLoop {

	private AppContent content = App.content();
	
	@Override
	public void execute() {
		// Let the super class do its work first
		super.execute();		
		// Then do your own work here ...
		content.oceanGameBackend().moveFishByIndex(1, 10, 0);
		content.oceanGameBackend().moveFishByIndex(2, -5, 0);

	}

}
