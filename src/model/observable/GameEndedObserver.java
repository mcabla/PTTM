package model.observable;

import java.util.Map;

public interface GameEndedObserver {
    void gameEnded(Map<String,Integer> varMap);
}
