package shared.routers;

import base.Params;
import base.SubRouter;
import my_base.App;
import team.control.OceanGameBackend;

public class OceanGameRouter implements SubRouter {

    // private final Ex3Backend backend;
    private final OceanGameBackend backend;

    public OceanGameRouter() {
        this.backend = App.content().oceanGameBackend();
    }

    @Override
    public Object route(String subPath, Params p) {
        // Uncomment next line to see routing commands in console
        // System.out.println("Routing Ex3: " + subPath + " with params " + p);
        switch (subPath) {

            // UI calls once on startup
            case "/start":
                backend.startScenario();
                return null;

            // UI input: move fish
            case "/fish/move": {
                int id = p.getInt(0);
                double x = p.getDouble(1);
                double y = p.getDouble(2);
                backend.moveFish(id, x, y);
                return null;
            }

            // UI input: eat check for player fish
            case "/fish/eat": {
                int id = p.getInt(0);
                backend.eat(id);
                return null;
            }

            // UI input: resize circle
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
