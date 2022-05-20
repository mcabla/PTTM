package model.highscore;

import tools.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Highscore {

    /**
     * These are the variables of the class Highscore
     */
    private String[] names;
    private String[] scores;
    private String[] dates;
    private String difficulty;

    /**
     * @param difficulty A string that contains the difficulty of the gamemode
     */
    public Highscore(String difficulty) {
        scores = new String[11];
        names = new String[11];
        dates = new String[11];
        this.difficulty = difficulty;
    }

    /**
     * Adds a highscore item to the highscores in a .txt file
     * Formats the parameter date to dd/mm/yyyy
     * Sorts all highscore items by score
     * Writes all the sorted highscore items to a .txt file
     * @param score The score the player had
     * @param name The name of the player
     * @param date The date of when the player played the game
     */

    public void addHighscoreItem(int score, String name, Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat = formatter.format(date);

        sortScores(score, name, dateFormat);
        writeToFile();
    }

    /**
     * Adds the highscore item to the highscores in the .txt file that contains the difficulty in its name
     * The highscore items are sorted by the score (the highscore item that needs to be added, will be added at the correct place)
     * Each element (score, name and date) of the highscore item will be placed in the corresponding variables of the class Highscore
     * @param score The score the player had
     * @param name The name of the player
     * @param date The date of when the player played the game
     */
    private void sortScores(int score, String name, String date) {
        File file = new File(Files.generatePreSuffix(Files.HIGHSCORE_PREFIX,difficulty,Files.HIGHSCORE_SUFFIX));
        try {
            Scanner sc = new Scanner(file);
            int i=0;
            boolean scoreOpJuistePlaats = false;

            while (i<10) {
                String line = sc.nextLine();
                String[] parts = line.split(";");

                if(!scoreOpJuistePlaats){
                    if (score > Integer.parseInt(parts[1])) {
                        names[i] = name;
                        scores[i] = score + "";
                        dates[i] = date;
                        scoreOpJuistePlaats = true;

                        names[i+1] = parts[0];
                        scores[i+1] = parts[1];
                        dates[i+1] = parts[2];
                    } else {
                        names[i] = parts[0];
                        scores[i] = parts[1];
                        dates[i] = parts[2];
                    }
                } else {
                    names[i+1] = parts[0];
                    scores[i+1] = parts[1];
                    dates[i+1] = parts[2];
                }
                i++;
            }
            sc.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Het bestand " + Files.generatePreSuffix(Files.HIGHSCORE_PREFIX,difficulty,Files.HIGHSCORE_SUFFIX) + " werd niet gevonden");
        }
    }

    /**
     * In the .txt file that contains the difficulty in its name, will the text be set to the highscore items
     * The highscore item which has the worst score, will be ignored and not added to the .txt file
     * Each element of a highscore item will be seperated with a ';' and each highscor item will be seperated with an enter
     */
    private void writeToFile() {
        try {
            PrintWriter pw = new PrintWriter(Files.generatePreSuffix(Files.HIGHSCORE_PREFIX,difficulty,Files.HIGHSCORE_SUFFIX));
            for (int i=0; i<10; i++) {
                pw.print(names[i] + ";" + scores[i] + ";" + dates[i] + "\n");
            }
            pw.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Het bestand " + Files.generatePreSuffix(Files.HIGHSCORE_PREFIX,difficulty,Files.HIGHSCORE_SUFFIX) + " werd niet gevonden");
        }
    }

}
