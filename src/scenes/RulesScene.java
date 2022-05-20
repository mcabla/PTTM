package scenes;

import javafx.scene.control.TextArea;
import tools.Files;
import tools.UI;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.image.ImageCache;

import java.io.IOException;

public class RulesScene extends ANavigatableScene {

    /**
     * @param root the root Node for the scene graph
     */
    public RulesScene(Pane root) {
        super(root);
    }

    /**
     * A top layout will be made
     * A classname will be added to the top layout
     * The top layout will have a HBox (title) and a TextArea (rules)
     */
    @Override
    protected Pane getTopLayout() {
        BorderPane topLayout = new BorderPane();
        UI.addClassName("rules-top-layout", topLayout);

        topLayout.setCenter(getRulesArea());
        topLayout.setTop(getTitle());

        return topLayout;
    }

    /**
     * A TextArea will be made
     * The text of the textArea will be set
     * A string 'rules' will contain the text from 'Rules.txt'
     * A classname will be added to the textArea
     */
    private TextArea getRulesArea(){
        TextArea textArea = new TextArea();
        UI.addClassName("text-area", textArea);
        textArea.setPrefSize(UI.getMaxWidth()-100, UI.getMaxHeight()-300);

        try{
            String rules= Files.readFile("rules/Rules.txt");
            textArea.setText(rules);
        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("Could not load rules.");
        }

        UI.addClassName("rules-text", textArea);

        return textArea;
    }

    /**
     * A HBox will be made
     * A classname will be added to the HBox and a classname will be added to the Label 'title'
     * The HBox will have a Label (which contains the title) and an Imageview
     */
    private HBox getTitle() {
        HBox hb = new HBox(0);
        UI.addClassName("rules-header",hb);
        Label title = new Label("Read my Rules");
        UI.addClassName("rules-title",title);
        hb.getChildren().addAll(title, getPatrickForTitle());

        return hb;
    }

    /**
     * An ImageView will be made
     * The size and image of the ImageView will be set
     */
    private ImageView getPatrickForTitle() {
        Image img = ImageCache.getImage("patrick.png");
        ImageView imageView = new ImageView();
        imageView.setImage(img);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(57);

        return imageView;
    }



}