import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import scenes.StartScene;
import tools.DataSingleton;
import scenes.GamemodeScene;
import scenes.RulesScene;
import tools.UI;

import java.awt.*;

import static tools.Files.IMAGE_PREFIX;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        UI.fixRatio();

        DataSingleton.getInstance().getMusic().start();

        RulesScene rulesScene = new RulesScene(new Pane());
        GamemodeScene gamemodeScene = new GamemodeScene(new Pane(), primaryStage);
        StartScene startScene = new StartScene(new Pane(), primaryStage, gamemodeScene, rulesScene);

        primaryStage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        primaryStage.setHeight(UI.getMaxHeight());
        primaryStage.setTitle("Patrick The Typing Mole");
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(IMAGE_PREFIX + "patrick.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
