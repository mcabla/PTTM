package scenes;

import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.image.ImageCache;
import tools.DataSingleton;
import tools.Files;
import tools.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.typing.Keyboard;
import model.algorithm.Maze;
import model.typing.Player;
import model.observable.GameEndedObserver;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class GameScene extends ANavigatableScene implements GameEndedObserver {

    /**
     * These are the variables of the class GameScene
     */

    private Maze maze;
    private Player player;
    private Keyboard keyboard;
    private Node playerNode;
    private GridPane mazePane;
    private BorderPane topLayout;
    private String difficulty;

    /**
     * @param root the root Node for the scene graph
     * @param difficulty indicates the level (easy, medium, hard) chosen in GamemodeScene
     * This is the constructor of this class
     * Here the maze is generated, the player is made and the observable for checking if the game has ended is added to the player
     * The eventhandlers for the keyboard and the layout is set here as well
     */
    public GameScene(Pane root, String difficulty) {
        super(root);
        this.difficulty = difficulty;
        generateMaze(difficulty);
        player = new Player(maze, keyboard);
        player.getGameEndedObservable().addObserver(this);

        setEventHandlers(root);
        fillLayout();
    }

    /**
     * Overrides the method 'initialize()' from the abstract class AScene
     * The BorderPane for the total layout is made
     */
    @Override
    protected void initialize(){
        topLayout = new BorderPane();
    }


    /**
     * Overrides the method 'getTopLayout()' from the abstract class ANavigatableScene
     * The method will return a layout
     */
    @Override
    protected Pane getTopLayout() {
        return topLayout;
    }


    /**
     * @param level gives the level that was chosen in the previous screen
     * This method generates the Maze, according to the parameter level
     * This varies in size and therefore in difficulty
     */
    private void generateMaze(String level) {
        int dimX, dimY;
        boolean solution;

        if (level.equals("easy")) {
            dimX = 6;
            dimY = 6;
            solution = true;
        }
        else if (level.equals("medium")) {
            dimX = 8;
            dimY = 8;
            solution = false;
        }
        else {
            dimX = 10;
            dimY = 10;
            solution = false;
        }

        keyboard = new Keyboard(Files.generatePreSuffix(Files.WORD_PREFIX,level,Files.WORD_SUFFIX));       // generates the keyboard including the words from the file linked to the level
        keyboard.initKeyboardView(0,0);
        maze = new Maze(dimX,dimY);
        maze.setShowSolution(solution);             // if it is necessary, it's possible to show the path from start to end
        maze.generate();
    }

    /**
     * @param root the root Node for the scene graph, used in the constructor
     * This method makes sure that the player is moved if a correct word was typed
     */
    private void setEventHandlers(Pane root) {
        root.setOnKeyTyped((event) -> {
            player.keyTyped(event);
            mazePane.getChildren().remove(playerNode);
            mazePane.add(playerNode, player.getPosX(), player.getPosY());
        });
    }

    /**
     * This method sets the total game and the label for the difficulty together in one Pane,
     * the variable of this class: topLayout
     */
    private void fillLayout(){
        HBox gameLayout = getGameLayout();
        StackPane difficultyStackPane = getDifficultyStackPane();

        topLayout.setTop(difficultyStackPane);
        topLayout.setCenter(gameLayout);
    }

    /**
     * @return The total gameLayout. In the left part of the HBox is the maze set,
     *   in the right part all the info (faults, time, supportmessage) and the keyboard with the words
     */
    private HBox getGameLayout() {
        HBox hb = new HBox(UI.getMaxHeight() / 16);      //root HBox, omvat volledige linkerdeel en volledige rechterdeel
        GridPane leftNode = getLeftNode();
        VBox rightNode = getRightNode();
        hb.getChildren().addAll(leftNode, rightNode);
        hb.setPadding(new Insets(10,0,10,0));

        return hb;
    }


    /**
     * @return the left node, with just all the maze
     */
    private GridPane getLeftNode() {
        mazePane = maze.getNode();
        playerNode = player.getNode();
        mazePane.add(playerNode, player.getPosX(), player.getPosY());
        mazePane.setPadding(new Insets(0, ((UI.getMaxWidth()/2) - UI.getMaxHeight()*0.79)/2 -40,UI.getMaxHeight()/30, ((UI.getMaxWidth()/2) - UI.getMaxHeight()*0.79)/2));
        return mazePane;
    }

    /**
     * @return the right pane with the keyboard (and words), time and errors and supportmessage
     */
    private VBox getRightNode() {
        VBox rightNode = new VBox(10);
        VBox keyboardPane = keyboard.getKeyboardView().getNode();
        rightNode.getChildren().addAll(getTimeAndErrors(), getSupportStackPane(), keyboardPane);

        rightNode.setPrefSize(UI.getMaxWidth()/2, (UI.getMaxHeight() < 900 )? UI.getMaxHeight()*.7:UI.getMaxHeight() *.75);
        rightNode.setPadding(new Insets(10, ((UI.getMaxWidth()/2) - UI.getMaxHeight()*0.79)/4, 0, 0));
        return rightNode;
    }


    /**
     * @return the small pane with just the label for the errors and the time
     */
    private BorderPane getTimeAndErrors() {
        BorderPane timeAndErrors = new BorderPane();
        timeAndErrors.setPrefWidth(UI.getMaxWidth()/2);
        timeAndErrors.setLeft(getFaultsLabel());
        timeAndErrors.setRight(getTimeLabel());
        timeAndErrors.setPadding(new Insets(20, 10, (UI.getMaxHeight()<900)?5:20, 10));
        return timeAndErrors;
    }

    /**
     * @return the label for the errors, set with the right style
     */
    private Label getFaultsLabel() {
        Label faults = keyboard.getKeyboardView().getFaultsLabel();
        UI.addClassName("faults-text",faults);
        return faults;
    }

    /**
     * @return the label for the time, set with the right style
     */
    private Label getTimeLabel() {
        Label time = keyboard.getKeyboardView().getTimerLabel();
        UI.addClassName("timer-text",time);
        return time;
    }


    /**
     * @return the pane for the supportmessage
     * with the right width en padding
     */
    private StackPane getSupportStackPane() {
        StackPane stackSupport = new StackPane();
        stackSupport.setPrefWidth(UI.getMaxWidth()/2);
        stackSupport.getChildren().add(getSupportLabel());
        stackSupport.setPadding(new Insets(0,0,0,0));
        return stackSupport;
    }

    /**
     * @return the label used for the method getSupportStackPane
     * with the correct style and alignment
     */
    private Label getSupportLabel() {
        Label support = keyboard.getKeyboardView().getSupportMessage();
        UI.addClassName("support-text",support);
        support.setAlignment(Pos.CENTER);
        return support;
    }

    /**
     * @return the pane for the difficultylabel
     *    with the right size en padding
     */
    private StackPane getDifficultyStackPane() {
        StackPane stack = new StackPane();
        stack.setPrefWidth(UI.getMaxWidth());
        stack.getChildren().add(getDifficultyLabel(difficulty));
        stack.setPadding(new Insets(10,0,0,0));
        return stack;
    }

    /**
     * @return the label used for the method getDifficultyStackPane()
     * with the correct style according to the chosen difficulty
     */
    private Label getDifficultyLabel(String difficulty) {
        Label title = new Label(" Difficulty: " + difficulty.toUpperCase() + " ");
        UI.addClassName(String.format("label-%s",difficulty),title);
        UI.addClassName("game-tile",title);

        return title;
    }


    /**
     * @param varMap a Map to simply get all the game info
     * Overrides the method 'gameEnded()' from the GameEndedObservable class
     */
    @Override
    public void gameEnded(Map<String, Integer> varMap) {
        makeLastMovements();

        TextInputDialog td = new TextInputDialog();
        setTextInputDialog(varMap, td);
        Optional<String> result = td.showAndWait();

        if (result.isPresent()) { //als de ok-knop wordt ingeduwd, dan wordt de score toegevoegd aan de highscores. Anders gebeurt er niets.
            String name = td.getEditor().getCharacters().toString();
            if (name.isEmpty()) name = "Anonymous";
            Date date = Calendar.getInstance().getTime();
            DataSingleton.getInstance().getHighscores(difficulty).addHighscoreItem(varMap.get("score"), name, date);
        }

        buttonBack.fireEvent(
                new MouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        buttonBack.getLayoutX(),
                        buttonBack.getLayoutY(),
                        buttonBack.getLayoutX(),
                        buttonBack.getLayoutY(),
                        MouseButton.PRIMARY,
                        1,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        null));
    }


    /**
     * Method that makes sure that the last step of the player is done
     *  before the final dialog with statistics appears
     */
    private void makeLastMovements() {
        mazePane.getChildren().remove(playerNode);
        mazePane.add(playerNode, player.getPosX(), player.getPosY());
    }

    /**
     * Method called in the overriden gameEnded method to set the layout of the TextInputDialog
     * @param varMap a Map to simply get all the game info
     * @param td the textinputdialog is already made in the gameEnded method and will here get his right layout
     */
    private void setTextInputDialog(Map<String, Integer> varMap, TextInputDialog td) {
        DialogPane dialogPane = td.getDialogPane();
        UI.addStylesheets(dialogPane.getStylesheets());
        UI.setBackground(dialogPane);

        Stage stage = (Stage) td.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImage("patrick.png"));

        td.setGraphic(null);
        td.setTitle("Congratulations!");
        td.setHeaderText("Score: " + varMap.get("score") +
                "\n\nCorrect: " + varMap.get("correct")  + " words" +
                "\n\nMistakes: " + varMap.get("errors")  +
                "\n\nTime: " + varMap.get("time") + " seconds");
        td.setContentText("What is your name?");
    }


}
