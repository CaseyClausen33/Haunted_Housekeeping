package Scripts;

import java.util.ArrayList;
import java.util.function.Supplier;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import ScriptActions.ScriptAction;

public class ChangeMapScript extends Script {
    private final Supplier<Map> nextMapSupplier;

    public ChangeMapScript(Supplier<Map> nextMapSupplier) {
        this.nextMapSupplier = nextMapSupplier;
    }

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                Map nextMap = nextMapSupplier.get();
                for (GameListener listener : listeners) {
                    listener.onMapChange(nextMap);
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}
