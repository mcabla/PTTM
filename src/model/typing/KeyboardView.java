package model.typing;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import tools.DataSingleton;
import tools.Direction;
import model.Drawable;
import model.algorithm.Tile;
import model.image.ImageCache;
import tools.UI;

public class KeyboardView extends Drawable {

    private boolean redrawLabel;
    private Keyboard kb;

    //Layout
    private final ImageView imageView;
    private final VBox vBox;
    private final Label up, right, below, left, currentText, timerLabel, support, faults;
    private final String[] correctMessages = {"You can totally do this!", "You are nailing this!", "Keep going!", "Well done!", "You got it!", "You are a talent!" }; //TODO: deze variabelnamen zijn niet goed, ik moet hier komen krijken om een onderschijd te bepalen.
    private final String[] wrongMessages = {"Everyone makes mistakes", "Next time better", "Try again", "Keep trying until you succeed", "You can and you will!"};
    private final String[] wallMessages = {"You can't go that way!", "There's a wall in that way", "You hit a wall, try again"};

    /**
     * Represents the view of the Keyboard.
     * @param x X-position
     * @param y Y-position
     * @param kb Keyboard controlling this view
     */
    public KeyboardView(int x, int y, Keyboard kb){
        super(x,y);
        this.kb = kb;

        //layout
        imageView = getImageViewKeyboard();
        support = new Label();
        faults = new Label();
        updateScoreLabelText();
        up = new Label();
        right = new Label();
        below = new Label();
        left = new Label();
        currentText = new Label();
        vBox = getVBoxPane();
        timerLabel = new Label();
    }

    //
    //Layout
    //

    /**
     * Makes the Gridpane
     */
    private VBox getVBoxPane(){
        GridPane gridPane = new GridPane();


        currentText.setPrefSize(Math.round(UI.getMaxWidth()/3*100.0)/100.0, UI.getMaxHeight()/12);
        currentText.setAlignment(Pos.CENTER);
        currentText.setPadding(new Insets(UI.getMaxHeight()/50,0,UI.getMaxHeight()/35,0));
        UI.addClassName("current-text", currentText);
        UI.removeColorClassNames(currentText);
        UI.addClassName("bg-black", currentText);
        GridPane inputGridPane = getInputGridPane();


        gridPane.setPadding(new Insets(0, 10 ,0, 10));
        addToGridPane(gridPane, up, HPos.CENTER,1,0,1,1);
        addToGridPane(gridPane, below, HPos.CENTER,1,3,1,1);
        addToGridPane(gridPane, left, HPos.RIGHT,0,2,1,1);
        addToGridPane(gridPane, right, HPos.LEFT,2,2,1,1);
        addToGridPane(gridPane, imageView, HPos.CENTER,1,1, 1, 2);

        GridPane.setValignment(left, VPos.CENTER);
        GridPane.setValignment(right, VPos.CENTER);
        //GridPane.setValignment(inputGridPane, VPos.BOTTOM);

        double spacing = (UI.getMaxHeight() < 900)? UI.getMaxHeight()/30 : UI.getMaxHeight()/12;
        VBox vb = new VBox(spacing);
        vb.getChildren().addAll(gridPane, inputGridPane);

        ColumnConstraints columnFluid = new ColumnConstraints(UI.getMaxWidth()/6 + 40);
        columnFluid.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(new ColumnConstraints(UI.getMaxWidth()/8), columnFluid, new ColumnConstraints(UI.getMaxWidth()/8));
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().addAll(rowConstraints, rowConstraints,rowConstraints);

        setLabels(up, right, left, below);
        return vb;
    }

    private void addToGridPane(GridPane gridPane, Node node, HPos hPos, int columnIndex, int rowIndex, int columnSpan, int rowSpan) {
        GridPane.setMargin(node,new Insets(10,0,10,0));
        GridPane.setHalignment(node, hPos);
        gridPane.add(node, columnIndex, rowIndex,columnSpan,rowSpan);
    }

    /**
     * Generates the Pane where the input is shown.
     * @return
     */
    private GridPane getInputGridPane() {

        GridPane inputGridPane = new GridPane();
        inputGridPane.add(currentText,0,0);

        //Adds walls around the label ('borders')
        double height = UI.getMaxHeight()/20+currentText.getPrefHeight();
        double width = currentText.getPrefWidth()+5;
        ImageView box = new ImageView(ImageCache.getImage("box.png"));
        box.setFitHeight(height);
        box.setFitWidth(width);
        inputGridPane.add(box,0,0);

        inputGridPane.setPadding(new Insets(0,0,0,UI.getMaxWidth()/24 + 30));

        return inputGridPane;
    }

    private void setLabels(Label... labels) {
        for (Label l: labels) {
            l.setPadding(new Insets(5));
            UI.addClassName("keyboard-labels", l);
        }
    }

    /**
     * Updates the Gridpane
     */
    @Override
    public VBox getNode(){
        updateNode();
        return vBox;
    }

    public void updateNode(){
        up.setText(kb.getWordStorage().getWord(Direction.NORTH).getString());
        right.setText(kb.getWordStorage().getWord(Direction.EAST).getString());
        below.setText(kb.getWordStorage().getWord(Direction.SOUTH).getString());
        left.setText(kb.getWordStorage().getWord(Direction.WEST).getString());
    }

    /**
     * Returns the image with the keyboard keys
     */
    public ImageView getImageViewKeyboard() {
        Image img = ImageCache.getImage("arrow_keyboard.png");
        ImageView imageView = new ImageView();
        imageView.setImage(img);
        imageView.setFitWidth(UI.getMaxWidth()/6);
        imageView.setFitHeight(0.677*UI.getMaxWidth()/6);
        return imageView;
    }

    public Label getFaultsLabel() {
        return faults;
    }

    public void updateScoreLabelText(){
        faults.setText(String.format("Faults: %d",kb.getVarMap().get("errors")));
    }

    /**
     * Letter typed, but none of the support actions take place.
     */
    public void checkRedrawLabel(){
        if (redrawLabel){
            UI.removeColorClassNames(currentText);
            UI.addClassName("bg-black", currentText);
            redrawLabel = false;
        }
    }

    //
    //Timer label
    //

    public void setTimerLabelText(int time){
        timerLabel.setText("Time: " + time);
    }

    public Label getTimerLabel(){
        return timerLabel;
    }

    //
    //Support messages //TODO: betere namen vinden voor de variabelen en methodes
    //

    /**
     * Correct word typed
     */
    public void setSupportMessageCorrect() {
        int getal = DataSingleton.getInstance().getRandom().nextInt(correctMessages.length);
        support.setText(correctMessages[getal]);
        UI.removeColorClassNames(currentText);
        UI.addClassName("bg-green", currentText);
        redrawLabel = true;
    }

    /**
     * Wrong letter typed
     */
    public void setSupportMessageWrong() {
        int getal = DataSingleton.getInstance().getRandom().nextInt(wrongMessages.length);
        support.setText(wrongMessages[getal]);
        UI.removeColorClassNames(currentText);
        UI.addClassName("bg-red", currentText);
        redrawLabel = true;
    }

    /**
     * Correct word typed, but hit a wall
     */
    public void setSupportMessageWall() {
        int getal = DataSingleton.getInstance().getRandom().nextInt(wallMessages.length);
        support.setText(wallMessages[getal]);
        UI.removeColorClassNames(currentText);
        UI.addClassName("bg-orange", currentText);
        redrawLabel = true;
    }

    public Label getSupportMessage() {
        return support;
    }

    public void setCurrentText() {
        currentText.setText(kb.getCurrentString());
    }

    public void setCurrentText(String str) {
        currentText.setText(str);
    }

}
