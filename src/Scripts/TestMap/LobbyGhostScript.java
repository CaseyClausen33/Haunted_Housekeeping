package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import Puzzles.StoryProgress;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.NPCFacePlayerScriptAction;
import ScriptActions.NPCLockScriptAction;
import ScriptActions.NPCUnlockScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

// script for talking to lobby ghost npc
public class LobbyGhostScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCLockScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());
        /* 
        scriptActions.add(new TextboxScriptAction() {{
            addText("Oh? Are you a guest?", new String[] { "Yes", "No" });
        }});

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 0;
                    }
                });

                addScriptAction(new TextboxScriptAction() {{
                    addText("I’m sorry, but we don’t have any rooms available. \n The hotel is in shambles!");
                    addText("The bedroom is a mess; there’s flooding in the bathroom");
                    addText("and that’s not to mention the disaster that’s \n going on in the kitchen!");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 1;
                    }
                });
                addScriptAction(new TextboxScriptAction() {{
                    addText("Oh? Are you a guest?", new String[] { "I inherited this hotel form my Grandfather"});;
                    addText("Your grandpa? Oh, yes, I remember him! \n He was a great man, and a better boss.", new String[] { "So you work here?"});;
                    addText("You could say that... but the hotel is in complete \n disarray! We haven’t had a guest in so long.", new String[] { "I'll help you clean"});;
                    addText("You will? Oh, thank you!");
                }});
            }});

            
        }});
        */
        scriptActions.add(new TextboxScriptAction() {{
            addText(roomProgressComment());
        }});

        scriptActions.add(new NPCUnlockScriptAction());
        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }

    private static String roomProgressComment() {
        switch (StoryProgress.solvedCount()) {
            case 0: return "Get to work! There are still four rooms left to clean!";
            case 1: return "You've completed your first room. Great job!";
            case 2: return "You're halfway there! Don't give up now!";
            case 3: return "Just one more to go! The hotel is almost ready to open!";
            default: return "You've helped us so much with cleaning! But there's still one thing left to do...";
        }
    }
}
