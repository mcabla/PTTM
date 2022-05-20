package model.observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameEndedObservable{

    private List<GameEndedObserver> gameEndedObserverList = new ArrayList<>();

    public void addObserver(GameEndedObserver gameEndedObserver) {
        this.gameEndedObserverList.add(gameEndedObserver);
    }

    public void removeObserver(GameEndedObserver gameEndedObserver) {
        this.gameEndedObserverList.remove(gameEndedObserver);
    }

    public void gameEnded(Map<String, Integer> varMap){
        for(GameEndedObserver g: gameEndedObserverList){
            g.gameEnded(varMap);
        }
    }
}
