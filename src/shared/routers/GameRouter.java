package shared.routers;

import base.Params;
import base.SubRouter;
import my_base.App;
import team.control.MovementController;

public class GameRouter implements SubRouter {

    private final MovementController movementController;

    public GameRouter() {
        this.movementController = App.content().movementController();
    }

    @Override
    public Object route(String subPath, Params p) {
        // Uncomment next line to see routing commands in console
        // System.out.println("Routing ocean: " + subPath + " with params " + p);
        switch (subPath) {

            // UI calls once on startup
            case "/start": {
                int selectedSkin = p.getInt(0);
                App.content().gameController().startGame(selectedSkin);
                return null;
            }

            // UI input: move player fish
            case "/player/move": {
                double x = p.getDouble(1);
                double y = p.getDouble(2);
                movementController.movePlayer(x, y);
                return null;
            }

            // UI input: eat check for player fish
            case "/fish/eat": {
                int id = p.getInt(0);
                App.content().collisionController().handlePlayerCollision(id);
                return null;
            }

            case "/periodic": {
                App.content().gameLoopController().toggleRunPeriodic();
                return null;
            }

            case "/menu": {
                App.content().gameController().showMainMenu();
                return null;
            }

            default:
                throw new RuntimeException("Unknown ocean game route: " + subPath);
        }
    }
}
