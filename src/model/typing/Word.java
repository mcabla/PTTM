package model.typing;

import tools.Direction;

public class Word {
    private final String word;
    private boolean canGoDirection;
    private final Direction direction;
    private int currentPos = 0;

    public Word(String word, boolean canGoDirection, Direction direction) {
        this.word = word;
        this.canGoDirection = canGoDirection;
        this.direction = direction;
    }

    //
    //Core
    //

    /**
     * Increases the position of the word and returns it if it's completed.
     * @return If the word has been completed
     */
    public boolean moveAndCheckIfCompleted() {
        this.currentPos++;
        return currentPos == word.length();
    }

    public void resetPos(){
        this.currentPos = 0;
    }

    //
    //Getters and Setters
    //

    public String getString() {
        return word;
    }

    public char getCurrentChar() {
        return word.charAt(currentPos);
    }

    public boolean getCanGoDirection() {
        return canGoDirection;
    }

    public void setCanGoDirection(boolean canGoDirection){
        this.canGoDirection = canGoDirection;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getCurrentPos(){
        return currentPos;
    }
}
