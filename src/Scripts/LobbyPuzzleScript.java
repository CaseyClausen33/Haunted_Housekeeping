package Scripts;

import java.util.ArrayList;
import Level.MapTile;
import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.LobbyPuzzle;

public class LobbyPuzzleScript extends Script {

    private boolean solvedBefore = false;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                solvedBefore = LobbyPuzzle.solved;
                if (!solvedBefore) {
                    LobbyPuzzle.launch();
                }
                return ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                return LobbyPuzzle.isOpen() ? ScriptState.RUNNING : ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new UnlockPlayerScriptAction());

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                if (LobbyPuzzle.solved && !solvedBefore) {
                    MapTile oldTile = LobbyPuzzleScript.this.map.getMapTile(11, 5);
                    MapTile stairsTile = LobbyPuzzleScript.this.map.getTileset().getTile(33)
                            .build(oldTile.getX(), oldTile.getY());
                    LobbyPuzzleScript.this.map.setMapTile(11, 5, stairsTile);
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
} 