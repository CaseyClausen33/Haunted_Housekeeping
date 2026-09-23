package Level;

public interface GameListener {
    void onWin();
    default void onMapChange(Map nextMap) { }
}
