package scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.image.ImageCache;
import tools.DataSingleton;
import tools.UI;

import java.awt.*;
import java.util.Optional;

public abstract class AScene extends Scene {

    /**
     * These are the variables of the class AScene
     */
    protected Button buttonQuit;
    protected ImageView buttonMute;

    /**
     * The background of the class Ascene will be set to a backgroundimage
     * A stylesheet will be added to the parameter root
     * ButtonQuit and buttonMute will be initialized
     * Things can be initialized with the method 'initialize()'. (this method doesn't do anything now but it will be needed in classes that extend 'AScene')
     * A layout will be added to the parameter root
     * @param root the root Node for the scene graph
     */
    public AScene(Pane root) {
        super(root);
        UI.addStylesheets(root.getStylesheets());
        UI.setBackground(root);
        initializeButtonQuit();
        initializeButtonMute();
        initialize();
        root.getChildren().addAll(getCenteredLayout());
    }

    private Pane getCenteredLayout(){
        GridPane centeredLayout = new GridPane();

        ColumnConstraints columnFluid = new ColumnConstraints(UI.getMaxWidth());
        columnFluid.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnSide = new ColumnConstraints((Toolkit.getDefaultToolkit().getScreenSize().getWidth()-UI.getMaxWidth())/2.0);
        centeredLayout.getColumnConstraints().addAll(columnSide, columnFluid,columnSide);

        centeredLayout.add(getLayout(),1,0);


        return centeredLayout;
    }

    /**
     * Abstract method that will be implemented in the class(es) that extend AScene
     */
    protected abstract Pane getLayout();

    /**
     * Method that can be overridden in class(es) that extends Ascene
     * This method is not abstract because it will not be overridden in each class that extends Ascene
     */
    protected void initialize(){}

    /**
     * Sets the size, imageView and eventhandler(onMouseClicked) of buttonmute
     */
    private void initializeButtonMute() {
        buttonMute = new ImageView(DataSingleton.getInstance().getMusic().getImage());
        buttonMute.setPreserveRatio(true);
        buttonMute.setFitWidth(UI.getMaxWidth()/32);
        buttonMute.setOnMouseClicked(e -> muteHandler());
    }

    /**
     * Sets the text and eventhandler(OnMouseClicked) of buttonQuit
     */
    private void initializeButtonQuit() {
        buttonQuit = new Button("Quit");
        buttonQuit.setOnMouseClicked(e -> {
            makeQuitAlert();
        });
    }

    /**
     * Shows an alert if the user clicks on the buttonquit to make sure that the user is sure he wants to quit the game
     */
    private void makeQuitAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        setTextDialog(alert);

        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){                 //checks if the user presses the button with text "yes" (button with ButtonType.OK)
            System.exit(0);
        }
    }

    /**
     * The alert will be added to a stylesheet and the background of the alert will be set
     * The icon of het alert will be set to a certain image
     * The text, title, and content of the alert will be set
     * @param alert the alert that will be shown
     */
    private void setTextDialog(Alert alert) {
        UI.addStylesheets(alert.getDialogPane().getStylesheets());
        UI.setBackground(alert.getDialogPane());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImage("patrick.png"));

        alert.setTitle("Patrick The Typing Mole");
        alert.setGraphic(null);                 //makes sure that the icon of the alertype will not be showed in the header of the alert
        alert.setHeaderText("You are going to quit this game");
        alert.setContentText("Are you sure?");
    }

    /**
     * The music will change its state (muted or unmuted)
     * The image of the buttonmute will be changed
     */
    private void muteHandler(){
        DataSingleton.getInstance().getMusic().mute();
        buttonMute.setImage(DataSingleton.getInstance().getMusic().getImage());
    }

}
