package model.typing;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import tools.DataSingleton;
import tools.Direction;
import model.Drawable;
import model.algorithm.Maze;
import model.observable.GameEndedObservable;
import model.image.ImageCache;

import java.util.Map;

public class Player extends Drawable {
    private ImageView imageView;
    private Keyboard keyboard;
    private Maze maze;
    private GameEndedObservable g;
    private final Map<Direction,Integer> dx, dy;

    /**
     * Represents the player of the game.
     * @param maze The instance of the maze this player exists in.
     * @param kb The instance of the keyboard which the player uses for KeyHandling
     */
    public Player(Maze maze, Keyboard kb){
        super(0,0);
        this.maze = maze;
        keyboard = kb;
        imageView = getImageView();
        g = new GameEndedObservable();

        DataSingleton dataSingleton = DataSingleton.getInstance();
        dx = dataSingleton.getDX();
        dy = dataSingleton.getDy();

        updateKeyboardDirections();
    }

    //
    //Core
    //

    /**
     * Moves the player. Also checks if the player has reached the end.
     * @param dir The direction the player is moving. These get converted to x and y movements, and then added to the player's current position.
     */
    public void move(Direction dir){
        //Movement
        setPosX(getPosX() + dx.get(dir));
        setPosY(getPosY() + dy.get(dir));
        if(dx.get(dir) !=0 ) imageView.setScaleX(-dx.get(dir));

        //Checks if player has reached the finish

        if(maze.checkFinished(getPosX(), getPosY())){
            keyboard.calcScore(maze.getSolutionLength());
            keyboard.stopTime();
            g.gameEnded(keyboard.getVarMap());
        }
    }

    /**
     * Will call the corresponding methods of Keyboard, and decide whether the player will move or not depending on the return value.
     * If the player moves, Keyboard will have to reset the words and recheck the maneuverability
     */
    public void keyTyped(KeyEvent keyEvent) {
        Direction direction = keyboard.keyPressed(keyEvent.getCharacter().charAt(0));

        //Player moves
        if(direction != null){
            keyboard.newWords();
            move(direction);
            updateKeyboardDirections();
        }

        //Keyboard UI update
        keyboard.getKeyboardView().updateNode();
        keyboard.getKeyboardView().updateScoreLabelText();
    }

    /**
     * Updates the maneuverability for all the current words (based on the directions).
     */
    private void updateKeyboardDirections(){
        for (Direction dir : Direction.values()) {
            keyboard.updateDirection(dir, maze.canGoDirection(getPosX(), getPosY(), dir));
        }
    }


    //
    //Getters
    //

    public GameEndedObservable getGameEndedObservable(){
        return g;
    }

    //
    //Layout
    //

    @Override
    public ImageView getNode(){
        return imageView;
    }

    /**
     * Returns an Imageview with the player's sprite
     */
    private ImageView getImageView() {
        Image image = ImageCache.getImage("patrick.png");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        double ratio = 2/5.0;
        imageView.setFitWidth(maze.getTileSize() * ratio);
        GridPane.setHalignment(imageView, HPos.CENTER);
        return imageView;
    }
}
