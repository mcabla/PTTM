package tools;

import model.highscore.Highscore;
import model.music.Music;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * An object restricted to have only one instance.
 * Used to store some data that can be accessed from every other object in a synchronised manner.
 */
public class DataSingleton {
    private final Map<Direction,Integer> dx, dy;
    private final Map<Direction,Direction> opposite;
    private final Random random;
    private final Music music;
    private final Highscore easyHighscores;
    private final Highscore mediumHighscores;
    private final Highscore hardHighscores;

    private DataSingleton(){
        dx = new HashMap<>();
        dy = new HashMap<>();
        opposite = new HashMap<>();
        random = new Random();
        music = new Music();
        easyHighscores = new Highscore("easy");
        mediumHighscores = new Highscore("medium");
        hardHighscores = new Highscore("hard");
        init();
    }

    /**
     * Make use of the synchronised Java classloader for thread-safety.
     */
    private static class Holder{
        private static final DataSingleton INSTANCE = new DataSingleton();
    }

    public static DataSingleton getInstance(){
        return Holder.INSTANCE;
    }

    private void init(){
        dx.put(Direction.NORTH,0);
        dx.put(Direction.SOUTH,0);
        dx.put(Direction.WEST,-1);
        dx.put(Direction.EAST,1);
        dy.put(Direction.NORTH,-1);
        dy.put(Direction.SOUTH,1);
        dy.put(Direction.WEST,0);
        dy.put(Direction.EAST,0);
        opposite.put(Direction.NORTH,Direction.SOUTH);
        opposite.put(Direction.SOUTH,Direction.NORTH);
        opposite.put(Direction.WEST,Direction.EAST);
        opposite.put(Direction.EAST,Direction.WEST);
    }

    public Map<Direction, Integer> getDX() {
        return new HashMap<>(dx);
    }

    public Map<Direction, Integer> getDy() {
        return new HashMap<>(dy);
    }

    public Map<Direction, Direction> getOpposite() {
        return new HashMap<>(opposite);
    }

    public Random getRandom() {
        return random;
    }

    public Music getMusic() {
        return music;
    }

    public Highscore getHighscores(String difficulty) {
        if (difficulty.equals("easy")) {
            return easyHighscores;
        } else if (difficulty.equals("medium")) {
            return mediumHighscores;
        } else {
            return hardHighscores;
        }
    }

}
