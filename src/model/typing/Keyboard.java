package model.typing;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import tools.Direction;

import java.util.HashMap;
import java.util.Map;

public class Keyboard{

    //Variables
    private Map<String, Integer> varMap = new HashMap<>();
    private boolean errorTyped = true;
    private WordStorage wordStorage;
    private Timeline timeline;
    private KeyboardView keyboardView;

    //Options
    private final boolean caseSensitive = true;

    /**
     * Its function is to manage the words, dictionary and check whether an input corresponds with one of the current words or not.
     * @param url An url that points to the text-document containing all the words of a level.
     */
    public Keyboard(String url){
        //Initializers
        wordStorage = new WordStorage();
        wordStorage.initializeFromFile(url);

        varMap.put("errors", 0);
        varMap.put("time", 0);
        varMap.put("correct", 0);
        varMap.put("score", 0);
    }

    /**
     * Initializes the class KeyboardView
     * @param x xPos on screen
     * @param y yPos on screen
     */
    public void initKeyboardView(int x, int y){

        if(keyboardView != null){
            System.out.println("KeyboardView already generated!");
            return;
        }

        keyboardView = new KeyboardView(x,y, this);

        initTimer();

    }

    /**
     * Initializes, starts and accumulates the timer. Links to a label in KeyboardView to automatically show the time on screen.
     */
    private void initTimer(){
        keyboardView.setTimerLabelText(varMap.get("time"));
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ev -> {
            mapIncrementer("time");
            keyboardView.setTimerLabelText(varMap.get("time"));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    //
    // Core
    //

    /**
     * Main functionality of the class. Gets called from its parent class each time a keyboard button is pressed.
     * @param ch The character that the user has just typed.
     */
    public Direction keyPressed(Character ch){

        //Character isn't a valid keyboard key (filters out enters, backspaces...)
        if(ch <= 30){
            return null;
        }

        // Only contains a word if it has been fully completed.
        Word completedWord = null;

        // For all 4 current words, checks if it's possible to move that direction. If yes, saves
        for(Word w : wordStorage.getWords().values()){
            Word tempWord = wrongCharacter(ch, w);
            if (tempWord != null){
                completedWord = tempWord;
            }
        }

        //Updates the UI
        keyboardView.setCurrentText();
        keyboardView.checkRedrawLabel();

        //Checks if mistakes are made
        checkErrorTyped(ch);

        // A word has been completed
        if(completedWord != null){
            mapIncrementer("correct"); //A completed word will count as a correct point for the end score, no matter if you've run against a wall or not.
            //Succesful move
            if (completedWord.getCanGoDirection()){
                keyboardView.setSupportMessageCorrect();
                return completedWord.getDirection();
            }
            //Ran against a wall
            else {
                completedWord.resetPos();
                keyboardView.setSupportMessageWall();
            }
        }

        return null;
    }

    /**
     * Checks whether the typed letter is the same as the one on the current position of the given word.
     * If they are the same, it will increase the character pointer within that word by one.
     * If they are not the same, it will reset the character pointer within that word to position 0.
     * @param ch The character that the user has just typed.
     * @param w The word which is being checked
     */
    private Word wrongCharacter(Character ch, Word w){
        char[] chars = {ch, w.getCurrentChar()};
        if(!caseSensitive){
            chars = new char[]{Character.toLowerCase(ch), Character.toLowerCase(w.getCurrentChar())};
        }

        if (chars[0] == chars[1]){
            errorTyped = false;
            if(w.moveAndCheckIfCompleted()){
                return w;
            }
        }
        else {
            w.resetPos();
        }
        return null;
    }

    /**
     * Checks if an error has been typed according to the boolean that was set earlier.
     * @param ch The currently typed character. Will be displayed in the current textbox if an error was typed.
     */
    private void checkErrorTyped(Character ch){
        if(errorTyped){
            mapIncrementer("errors");
            keyboardView.setSupportMessageWrong();
            keyboardView.setCurrentText(ch.toString());
        }
        errorTyped = true;
    }


    /**
     * Sets if the player can move to a certain direction (saves this in the Word variable)
     */
    public void updateDirection(Direction dir, boolean canGoDirection){
        wordStorage.getWord(dir).setCanGoDirection(canGoDirection);
    }

    /**
     * Generate 4 random new words
     */
    public void newWords(){
        wordStorage.newWords();
    }

    //Stops timer
    public void stopTime(){
        timeline.stop();
    }

    //
    //Variable calculation and manipulation
    //

    /**
     * Increments the value linked to the parameter key by one.
     * @param key A key in the variable map
     */
    private void mapIncrementer(String key){
        varMap.put(key, varMap.get(key) + 1);
    }

    /**
     * Calculates the score and puts it in the parameter map.
     */
    public void calcScore(int solutionLength){
        int wordsTypedTooMuch = (varMap.get("correct") - (solutionLength -1))*10;
        int faults = varMap.get("errors") * 20;
        int timeTooMuch = (varMap.get("time") - (solutionLength - 1)) * 5;
        int penalty = wordsTypedTooMuch + faults + timeTooMuch;
        varMap.put("score", (100000/penalty));
    }


    //
    //Getters
    //

    public WordStorage getWordStorage() {
        return wordStorage;
    }

    public KeyboardView getKeyboardView() {
        return keyboardView;
    }

    public Map<String, Integer> getVarMap(){
        return varMap;
    }

    public String getCurrentString(){
        return wordStorage.getCurrentString();
    }

}