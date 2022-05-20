package model.highscore;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.image.ImageCache;
import tools.Files;
import tools.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class HighscoreDialog extends Stage {

    /**
     * These are the variables of the class MyDialog
     * 3 arrays where the name, score and date is stored when read from the file
     */
    private String [] name = new String[10];
    private String [] score = new String[10];
    private String [] date = new String[10];

    /**
     * @param difficulty when this object is used, there will be added a difficulty/level to show the highscores from the correct level
     * constructor from the MyDialog class
     */
    public HighscoreDialog(String difficulty) {
        readHighScoreFile(Files.generatePreSuffix(Files.HIGHSCORE_PREFIX,difficulty,Files.HIGHSCORE_SUFFIX)); //De instantievariabelen worden geïnitialiseerd

        VBox totalLayout = getTotalLayout();
        UI.addStylesheets(totalLayout.getStylesheets());
        UI.setBackground(totalLayout);

        this.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(totalLayout);
        this.setResizable(false);
        this.setTitle("Highscores");
        this.getIcons().add(ImageCache.getImage("patrick.png"));
        this.setScene(scene);
    }

    /**
     * this method will read the correct file and store the elements in the arrays
     * @param filename defines the file where this method has to handle with
     */
    private void readHighScoreFile(String filename){
        try {
            Scanner sc = new Scanner(new File(filename));
            for (int i = 0; i < 10; i++) {
                String line = sc.nextLine();
                String[] parts = line.split(";");
                name[i] = parts[0];
                score[i] = parts[1];
                date[i] = parts[2];
            }
            sc.close();
        }catch(FileNotFoundException e){
            System.out.println("File doesn't exist!");
        }

    }

    /**
     * @return the VBox with the table and the backbutton
     */
    private VBox getTotalLayout() {
        VBox totalLayout = new VBox(10);
        GridPane grid = getTableLayout();
        HBox hb = getBackButtonLayout();
        totalLayout.getChildren().addAll(grid, hb);

        return totalLayout;
    }

    /**
     * @return the gridpane with all the rows (1 to 10) and columns (number, name, score, date) needed for the table
     */
    private GridPane getTableLayout() {
        GridPane grid = new GridPane();
        UI.addClassName("dialog-root", grid);
        grid.setGridLinesVisible(true);                     // to show the lines like a normal table

        String [] number = {"1.", "2.", "3.", "4.", "5.", "6.", "7.", "8.", "9.", "10." };
        grid.addColumn(0, getChildrenLabels("#", number));
        grid.addColumn(1, getChildrenLabels("NAME",name));
        grid.addColumn(2, getChildrenLabels("SCORE", score));
        grid.addColumn(3, getChildrenLabels("DATE", date));

        return grid;
    }


    /**
     * makes the labels (11: 1 header, 10 data) for one row
     * @param header the value for the first row, the content of the whole column
     * @param array the other 10 rows has to be filled with the data from this array
     * @return
     */
    private Label[] getChildrenLabels(String header, String[] array) {
        Label[] labelName = new Label[11];
        for(int i=0; i<11;i++) {
            labelName[i] = makeLabel();
        }
        setLabels(labelName, header, array);

        return labelName;
    }

    /**
     * @return make a label and set the padding and style for all labels in the table
     */
    private Label makeLabel() {
        Label l = new Label();
        UI.addClassName("label-highscore", l);
        l.setPadding(new Insets(10));
        return l;
    }

    /**
     * @param labels the array of labels for one column
     * @param header the value of the first row
     * @param array the values of the other rows
     */
    private void setLabels(Label[] labels, String header, String[] array) {
        labels[0].setText(header);
        for(int i=1; i<labels.length; i++) {
            labels[i].setText(array[i-1]);
        }
    }

    /**
     * @return the HBox for the return button
     */
    private HBox getBackButtonLayout() {
        Button back = new Button("Return");
        back.setOnMouseClicked(event -> close());
        UI.addClassName("button", back);

        HBox hb = new HBox(10);
        hb.getChildren().addAll(back);
        hb.setPadding(new Insets(0,0,30,30));

        return hb;
    }

}
