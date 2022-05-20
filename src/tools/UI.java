package tools;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import model.image.ImageCache;

import java.awt.*;

public class UI {

    /**
     * A className will be added to each Node of nodes
     * @param className The className that is given
     * @param nodes The node on which a className will be added
     */
    public static void addClassName(String className, Node... nodes) {
        for (Node node: nodes) {
            node.getStyleClass().add(className);
        }
    }

    public static void removeClassName(String className, Node... nodes) {
        for (Node node: nodes) {
            node.getStyleClass().remove(className);
        }
    }

    public static void removeColorClassNames(Node... nodes){
        removeClassName("bg-black", nodes);
        removeClassName("bg-red", nodes);
        removeClassName("bg-green", nodes);
        removeClassName("bg-orange", nodes);
    }

    private static double maxHeight = Screen.getPrimary().getVisualBounds().getHeight();

    /**
     * returns the maximum height of the visuals bounds of the screen (the screen of the device which the user is using)
     */
    public static double getMaxHeight() {
        return maxHeight;
    }

    private static double maxWidth = Screen.getPrimary().getVisualBounds().getWidth();

    /**
     * returns the maximum width of the visuals bounds of the screen (the screen of the device which the user is using)
     */
    public static double getMaxWidth() {
        return maxWidth;
    }

    public static void fixRatio(){
        double ratio = 16/9.0;

        if(maxWidth / maxHeight > ratio) maxWidth = maxHeight * ratio;
        else maxHeight = maxWidth / ratio;
    }

    /**
     * This method wil set the positions of a label and a VBox in a BorderPane
     * The size and the padding of a BorderPane will be set
     * @param borderPane The Pane which will have VBox and a Label
     * @param vbox The VBox that will be added to the borderPane
     * @param label the Label that will be added to the borderPane
     */
    public static void setPositions(BorderPane borderPane, VBox vbox, Label label){
        borderPane.setCenter(vbox);
        borderPane.setTop(label);
        BorderPane.setAlignment(label, Pos.CENTER);
        borderPane.setPadding(new Insets(UI.getMaxHeight()/10,0,0,0));
        borderPane.setPrefSize(UI.getMaxWidth()-20, UI.getMaxHeight()-200);
    }

    /**
     * The background of a Pane will be set
     * @param root the root on which the background will be set
     */
    public static void setBackground(Pane root) {
        BackgroundImage backgroundImage = new BackgroundImage( //nieuwe BackgroundImage wordt aangemaakt
                ImageCache.getImage("background.png"),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        root.setBackground(new Background(backgroundImage)); //Voor elke AScene wordt de background van root-layout ingesteld op de BackgroundImage
    }

    public static void addStylesheets(ObservableList<String> stylesheets){
        addStylesheet(stylesheets, "style");

        //HD: 1280 x 720
        //Full HD: 1920 x 1080
        //QUad HD: 2560 x 1440
        //4K: 3840 x 2160

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle windowSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        int marge = screenSize.height - windowSize.height + 20;
        if (marge > 100) marge = 50;

        if (maxHeight >= 2160 - marge) addStylesheet(stylesheets, "4K");
        else if (maxHeight >= 1440 - marge) addStylesheet(stylesheets, "QHD");
        else if (maxHeight >= 1080 - marge) addStylesheet(stylesheets, "FHD");
        else addStylesheet(stylesheets, "HD");
    }

    private static void addStylesheet(ObservableList<String> stylesheets,String sheetName){
        stylesheets.add(String.format(Files.STYLESHEET,sheetName));
    }
}
