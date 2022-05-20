package scenes;

import tools.UI;
import buttons.ButtonBack;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public abstract class ANavigatableScene extends AScene{

    /**
     * This is the variable of the class ANavigatableScene
     */
    protected ButtonBack buttonBack;

    /**
     * @param root the root Node for the scene graph
     */
    public ANavigatableScene(Pane root) {
        super(root);
    }

    /**
     * Abstract method that will be implemented in the class(es) that extend AScene
     */
    protected abstract Pane getTopLayout();

    /**
     * The method will return a layout
     * A classname will be added to the variable buttonBack and the variable buttonQuit
     */
    @Override
    protected Pane getLayout() {
        buttonBack = new ButtonBack();
        UI.addClassName("navigation-button", buttonBack, buttonQuit);

        return getTotalLayout();
    }

    /**
     * The layout of the scene will be made
     * A bottom layout and a top layout will be added to this layout
     */
    private AnchorPane getTotalLayout() {
        AnchorPane totalLayout = new AnchorPane();
        totalLayout.setPrefSize(UI.getMaxWidth(), UI.getMaxHeight()-50);
        BorderPane bottomLayout = getBottomlayout();
        Pane topLayout = getTopLayout();
        totalLayout.getChildren().addAll(bottomLayout, topLayout);
        AnchorPane.setBottomAnchor(bottomLayout, 0.0);
        AnchorPane.setTopAnchor(topLayout, 0.0);

        return totalLayout;
    }

    /**
     * A bottom layout will be made
     * The bottom layout will have a buttonBack, buttonMute and buttonQuit
     * A classname will be added to the bottom layout
     */
    private BorderPane getBottomlayout() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(UI.getMaxWidth()-20);
        borderPane.setLeft(buttonBack);
        borderPane.setCenter(buttonMute);
        borderPane.setRight(buttonQuit);
        UI.addClassName("navigation-pane", borderPane);

        return borderPane;
    }

}
