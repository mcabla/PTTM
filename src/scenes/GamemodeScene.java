package scenes;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.highscore.HighscoreDialog;
import tools.UI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GamemodeScene extends  ANavigatableScene {

    /**
     * These are the variables of the class GamemodeScene
     */
    private Button buttonEasy;
    private Button buttonMedium;
    private Button buttonHard;
    private GameScene gs;

    /**
     * Eventhandlers of buttonEasy, buttonMedium and buttonHard will be set
     * @param root the root Node for the scene graph
     * @param stage the top level JavaFX container
     */
    public GamemodeScene(Pane root, Stage stage) {
        super(root);

        setEventHandler(buttonEasy,stage,"easy");
        setEventHandler(buttonMedium,stage,"medium");
        setEventHandler(buttonHard,stage,"hard");
    }

    /**
     * Overrides the method 'initialize()' in the class AScene
     * The text and style of buttonEasy, buttonMedium and buttonHard will be set
     */
    @Override
    protected void initialize() {
        buttonEasy = new Button("Easy");
        buttonMedium = new Button("Medium");
        buttonHard = new Button("Hard");

        setStyle(buttonEasy, buttonMedium, buttonHard);
    }

    /**
     * Two classnames will be added to each button of the variable buttons
     * The size of each button of the variable buttons will be set
     * @param buttons contains zero or more buttons
     */
    private void setStyle(Button... buttons) {
        for (Button b: buttons) {
            b.setPrefSize(UI.getMaxWidth()/3,UI.getMaxHeight()/10);
            UI.addClassName(String.format("button-%s",b.getText().toLowerCase()),b);
            UI.addClassName("gamemode-button",b);
        }
    }

    /**
     * Sets the eventhandler(OnMouseClicked) of the button (the button is given as parameter)
     * Sets the eventhandler(OnMouseClicked) of the buttonBack in the gameScene
     * @param button The eventhandler will be added to this button
     * @param stage The top level JavaFX container
     * @param difficulty The difficulty of the game
     */
    private void setEventHandler(Button button, Stage stage, String difficulty) {
        button.setOnMouseClicked(e -> { //eventhandler van de buttonstart
            gs = new GameScene(new Pane(), difficulty); //Er wordt een GameScene aangemaakt met bijhorende moeilijkheidsgraad

            gs.buttonBack.setOnMouseClicked(event -> { //de eventhandler van de buttonback in de gs(GameScene) wordt ingesteld (er wordt terugverwezen naar deze GamemodeScene)
                stage.setScene(this);
                this.buttonMute.setImage(gs.buttonMute.getImage());
            });
            stage.setScene(gs); //Er wordt doorverwezen naar de gs(GameScene)
        });
    }

    /**
     * The method will return a top layout
     * A Label and three buttons will be given as parameter to the method 'getLayoutButtonsAndLabels(...)' to make a VBox
     * A classname will be added to the label that has been made in this method
     */
    @Override
    protected Pane getTopLayout() {
        Label label = new Label("Choose the difficulty:"); //Er wordt een label aangemaakt met daarin de titel
        UI.addClassName("title", label);
        Button highscoreEasy = makeHighscoreButton("easy");
        Button highscoreMedium = makeHighscoreButton("medium");
        Button highscoreHard = makeHighscoreButton("hard");

        BorderPane bp = new BorderPane();
        VBox layout = getLayoutButtonsAndLabels(label, highscoreEasy, highscoreMedium, highscoreHard);

        UI.setPositions(bp, layout, label);

        return bp;
    }

    /**
     * The method will return a Button and this method will set the size and the text of the button
     * An eventhandler will be added to the button that has been made in this method
     * Two classnames will be added to the button that has been made in this method
     * @param difficulty The difficulty of the game
     */
    private Button makeHighscoreButton(String difficulty) {
        Button button = new Button("Highscore");
        button.setPrefSize(UI.getMaxWidth()/9,UI.getMaxHeight()/30);
        UI.addClassName(String.format("button-%s",difficulty),button);
        UI.addClassName("highscore-button",button);
        setEventHandlerHighscore(button, difficulty);

        return button;
    }

    /**
     * This method will return a VBox
     * three VBoxes and a Label will be added to the VBox
     * @param label The label that will be on top of the VBox
     * @param highscoreEasy A button which will show the highscores of level easy if it is clicked on with the mouse
     * @param highscoreMedium A button which will show the highscores of level medium if it is clicked on with the mouse
     * @param highscoreHard A button which will show the highscores of level hard if it is clicked on with the mouse
     */
    private VBox getLayoutButtonsAndLabels(Label label, Button highscoreEasy, Button highscoreMedium, Button highscoreHard) {
        VBox layout = new VBox(UI.getMaxHeight()/10);

        HBox easyButtons = makeHBox(buttonEasy, highscoreEasy);
        HBox mediumButtons = makeHBox(buttonMedium, highscoreMedium);
        HBox hardButtons = makeHBox(buttonHard, highscoreHard);

        layout.getChildren().addAll(label, easyButtons, mediumButtons, hardButtons);
        layout.setPadding(new Insets(UI.getMaxHeight()/10, 0, 0,UI.getMaxWidth()/2 - buttonEasy.getPrefWidth()/2));

        return layout;
    }

    /**
     * This method return a HBox
     * The elements added to the HBox are 'buttons' (buttons is given as parameter)
     * @param buttons contains zero or more buttons
     */
    private HBox makeHBox(Button... buttons) {
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(buttons);

        return hbox;
    }

    /**
     * Sets the eventhandler(OnMouseClicked) of the button (the button is given as parameter)
     * @param button The eventhandler will be added to this button
     * @param difficulty The difficulty of the game
     */
    private void setEventHandlerHighscore(Button button, String difficulty) {
        button.setOnMouseClicked(event -> {
            HighscoreDialog highscoreDialog = new HighscoreDialog(difficulty);
            highscoreDialog.showAndWait();
        });
    }
}
