package scenes;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.image.ImageCache;
import tools.UI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartScene extends AScene {

    /**
     * These are the variables of the class AScene
     */
    private Button buttonStart;
    private Button buttonRules;

    /**
     * Eventhandlers will be set
     * @param root the root Node for the scene graph
     * @param stage the top level JavaFX container
     * @param gamemodeScene the scene where the user can decide the difficulty of the game or watch the highscores
     * @param rulesScene the scene where the rules will be showed
     */
    public StartScene(Pane root, Stage stage, GamemodeScene gamemodeScene, RulesScene rulesScene) {
        super(root);

        setEventHandlers(stage, buttonStart, gamemodeScene);
        setEventHandlers(stage, buttonRules, rulesScene);
    }

    /**
     * Sets the eventhandler(OnMouseClicked) of buttonStart
     * Sets the eventhandler(OnMouseClicked) of the buttonback in the gamemodescene
     * Sets the eventhandler(OnMouseClicked) of buttonRules
     * Sets the eventhandler(OnMouseClicked) of the buttonback in the rulesscene
     * @param stage the top level JavaFX container
     * @param scene the scene where eventhandlers will be set
     */
    private void setEventHandlers(Stage stage, Button button, ANavigatableScene scene) {
        button.setOnMouseClicked(e -> {
            stage.setScene(scene);
            scene.buttonMute.setImage(this.buttonMute.getImage());
        });
        scene.buttonBack.setOnMouseClicked(event -> {
            stage.setScene(this) ;
            this.buttonMute.setImage(scene.buttonMute.getImage());
        });
    }

    /**
     * Overrides the method 'initialize()' in the class AScene
     * The text and style of buttonStart, buttonRules and buttonQuit will be set
     */
    @Override
    protected void initialize() {
        buttonStart = new Button("Start typing");
        buttonRules = new Button("Read the Rules");
        buttonQuit.setText("Quit the game");

        setStyle(buttonStart, buttonRules, buttonQuit);
    }

    /**
     * A classname will be added to each button of the variable buttons
     * the size of each button of the variable buttons will be set
     * @param buttons contains zero or more buttons
     */
    private void setStyle(Button... buttons) {
        UI.addClassName("start-button",buttons);
        for (Button b : buttons) {
            b.setPrefSize(UI.getMaxWidth()/ 3, UI.getMaxHeight()/ 10);
        }
    }

    /**
     * The method will return a layout
     * A bottom layout and a top layout will be added to this layout (the top layout will contain a label)
     */
    @Override
    protected Pane getLayout() {
        Label label = new Label("Welcome to Patrick The Typing Mole"); //TODO: project name
        UI.addClassName("title", label);

        AnchorPane root = new AnchorPane();
        BorderPane mutePane = getBottomLayout();
        BorderPane topLayout = getTopLayout(label);
        ImageView left = getPatrick();
        ImageView right = getPatrick();


        root.setPrefSize(UI.getMaxWidth(), UI.getMaxHeight()-50);
        root.getChildren().addAll(mutePane, topLayout, left, right);
        AnchorPane.setBottomAnchor(mutePane, 0.0);
        AnchorPane.setTopAnchor(topLayout, 0.0);
        AnchorPane.setLeftAnchor(left, UI.getMaxWidth()/10);
        AnchorPane.setTopAnchor(left, UI.getMaxHeight()/3);
        AnchorPane.setRightAnchor(right, UI.getMaxWidth()/8);
        AnchorPane.setTopAnchor(right, UI.getMaxHeight()/3);

        return root;
    }

    /**
     * The method will return an ImageView from Patrick used for the layout left and right
     */

    private ImageView getPatrick() {
        Image img = ImageCache.getImage("patrick.png");
        ImageView imageView = new ImageView();
        imageView.setImage(img);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(UI.getMaxWidth()/20);

        return imageView;
    }

    /**
     * A bottom layout will be made
     * The bottom layout will have a buttonMute
     * A classname will be added to the bottom layout
     */
    private BorderPane getBottomLayout() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(UI.getMaxWidth()-20);
        borderPane.setCenter(buttonMute);
        UI.addClassName("navigation-pane", borderPane);

        return borderPane;
    }

    /**
     * A top layout will be made
     * The top layout will have a label and a VBox
     * The positions of the label will be set with a method of the class UI
     * @param label the label that will be shown in the top layout
     */
    private BorderPane getTopLayout(Label label) {
        BorderPane bp = new BorderPane();
        VBox layout = getLayoutButtonsAndLabel(label);

        UI.setPositions(bp, layout, label);

        return bp;
    }

    /**
     * A layout with buttons and a label will be made
     * @param label the label that will be shown in the top layout
     */
    private VBox getLayoutButtonsAndLabel(Label label) {
        VBox layout = new VBox(UI.getMaxHeight()/10);
        layout.getChildren().add(label);
        layout.getChildren().addAll(buttonStart, buttonRules, buttonQuit);
        layout.setPadding(new Insets(UI.getMaxHeight()/10, 0, 0,UI.getMaxWidth()/2 - buttonStart.getPrefWidth()/2));

        return layout;
    }
}