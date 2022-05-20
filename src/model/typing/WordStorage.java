package model.typing;

import tools.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordStorage {

    private Map<Direction, Word> words;
    private List<String> dummyDictionary;
    private String currentString;

    public WordStorage(){
        words = new HashMap<>();
        dummyDictionary = new ArrayList<>();
    }

    //
    //Initializers
    //

    public void initializeFromArray(String[] arr){
        dummyDictionary.addAll(Arrays.asList(arr));
        newWords();
        //dummyDictionary.addAll(direction); -> Eventueel, niet nodig als elk woord maar 1x kan worden gebruikt.
    }

    public void initializeFromArray(){
        String[] strs = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"};
        initializeFromArray(strs);
    }

    public void initializeFromFile(String filename){
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNextLine()){
                dummyDictionary.add(sc.nextLine());
            }
            sc.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File doesn't exist!"); //Melding op scherm?
            initializeFromArray();
        }
        newWords();
    }

    //
    //Core
    //

    /**
     * Generates a new set of 4 words. Each word is tied to a direction the player can move to.
     */
    public void newWords(){

        words.clear();
        int wordlength = -1; //Length of the first word found. Each subsequent word will have the same length.
        for(Direction d: Direction.values()){
            int r = (int)(Math.random() * dummyDictionary.size());

            //Only gets called when the first word is found.
            if(wordlength < 0){
                wordlength = dummyDictionary.get(r).length();
            }

            boolean added = false;
            while(!added) {
                if (checkLength(r, wordlength)) {
                    words.put(d, new Word(dummyDictionary.get(r), true, d));
                    dummyDictionary.remove(r);
                    added = true;
                }
                r = (int)(Math.random() * dummyDictionary.size());
            }
        }

        //woorden opnieuw toevoegen
        for(Word w: words.values()){
            dummyDictionary.add(w.getString());
        }

    }

    /**
     * Checks whether the current word has the same length as the first word chosen. This ensures there will be no game-breaking overlapping, like 'no' and 'now'.
     * @param position Position in the dummyDictionary of the word you want to check
     * @param length Length of the first word generated
     * @return
     */

    private boolean checkLength(int position, int length){
        if(dummyDictionary.get(position).length() == length){
            return true;
        }
        return false;
    }

    //
    //Getters
    //

    public Map<Direction, Word> getWords() {
        return words;
    }

    public Word getWord(Direction d){
        return words.get(d);
    }

    /**
     * Compares the four directions and saves the one with the longest substring. If all four have a length 0 (for example after a reset),
     * returns the completed word from the previous set of words.
     * @return String with the longest length
     */
    public String getCurrentString(){
        String str = "";
        for(Word w: words.values()){
            if(w.getCurrentPos() > str.length()){
                str = w.getString().substring(0,w.getCurrentPos());
            }
        }
        if(str.equals("")){
            return currentString;
        }
        currentString = str;
        return str;
    }
}
