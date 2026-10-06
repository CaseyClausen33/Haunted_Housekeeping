package Scripts;

import java.util.ArrayList;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import Maps.HotelLobbyMap;
import Puzzles.KitchenPuzzle;
import ScriptActions.*;

// Kitchen exit: leaves the room only once the kitchen puzzle is solved,
// otherwise shows a "door won't budge" message.
// The puzzle flag is checked each time the player interacts, not when the map loads.
public class KitchenExitScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        // Same map-change logic as ChangeMapScript (uses this script's listeners)
        ScriptAction changeMap = new ScriptAction() {
            @Override
            public ScriptState execute() {
                Map nextMap = new HotelLobbyMap();
                for (GameListener listener : KitchenExitScript.this.listeners) {
                    listener.onMapChange(nextMap);
                }
                return ScriptState.COMPLETED;
            }
        };

        // Puzzle solved -> leave
        ConditionalScriptActionGroup solvedGroup = new ConditionalScriptActionGroup();
        solvedGroup.addRequirement(new CustomRequirement() {
            @Override
            public boolean isRequirementMet() {
                return KitchenPuzzle.solved;
            }
        });
        solvedGroup.addScriptAction(changeMap);

        // Puzzle not solved -> blocked, with a message (same pattern as SimpleTextScript)
        ConditionalScriptActionGroup lockedGroup = new ConditionalScriptActionGroup();
        lockedGroup.addRequirement(new CustomRequirement() {
            @Override
            public boolean isRequirementMet() {
                return !KitchenPuzzle.solved;
            }
        });
        lockedGroup.addScriptAction(new LockPlayerScriptAction());
        lockedGroup.addScriptAction(new TextboxScriptAction("The door won't budge... I should solve the kitchen puzzle first."));
        lockedGroup.addScriptAction(new UnlockPlayerScriptAction());

        ConditionalScriptAction conditional = new ConditionalScriptAction();
        conditional.addConditionalScriptActionGroup(solvedGroup);
        conditional.addConditionalScriptActionGroup(lockedGroup);

        scriptActions.add(conditional);
        return scriptActions;
    }
}