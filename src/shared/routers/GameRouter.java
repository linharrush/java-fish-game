package shared.routers;

import base.Params;
import base.SubRouter;
import my_base.App;
import team.control.MovementController;
import team.control.OceanGameBackend;

public class GameRouter implements SubRouter {

    private final OceanGameBackend backend;
    private final MovementController movementController;

    public GameRouter() {
        this.backend = App.content().oceanGameBackend();
        this.movementController = App.content().movementController();
    }

    @Override
    public Object route(String subPath, Params p) {
        // Uncomment next line to see routing commands in console
        // System.out.println("Routing ocean: " + subPath + " with params " + p);
        switch (subPath) {

            // UI calls once on startup
            case "/start":
                backend.startScenario();
                return null;

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
                backend.eat(id);
                return null;
            }

            case "/periodic": {
                backend.toggleRunPeriodic();
                return null;
            }


            case "/oceanView": {
                backend.oceanView();
                return null;
            }

            case "/normalView": {
                backend.normalView();
                return null;
            }

            default:
                throw new RuntimeException("Unknown ocean game route: " + subPath);
        }
    }
}
