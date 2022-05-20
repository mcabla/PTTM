package tools;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Files {

    public final static String FILE_PREFIX = "assets/";
    public final static String IMAGE_PREFIX = FILE_PREFIX + "images/";
    public final static String AUDIO = FILE_PREFIX + "music/8-punk-8-bit-music.wav";
    public final static String STYLESHEET = FILE_PREFIX + "styles/%s.css";
    public final static String HIGHSCORE_PREFIX = FILE_PREFIX + "highscores/";
    public final static String HIGHSCORE_SUFFIX = "HighScores.txt";
    public final static String WORD_PREFIX = FILE_PREFIX + "/words/";
    public final static String WORD_SUFFIX = "Words.txt";


    /**
     * Reads a given file to a string.
     * @param pathname
     * @return the content of the file as a String.
     * @throws IOException In case the file does not exist.
     */
    public static String readFile(String pathname) throws IOException {
        File file = new File(FILE_PREFIX + pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }


    public static String generatePreSuffix(String prefix, String arg, String suffix){
        return prefix + arg + suffix;
    }
}
