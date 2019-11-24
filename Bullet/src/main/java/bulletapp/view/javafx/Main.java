package bulletapp.view.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/javafx/main.fxml"));
        primaryStage.setTitle("Bullet");

        double ratio = 4.0 / 5;
        int sceneWidth = (int) (0.5 + Screen.getPrimary().getVisualBounds().getWidth() * ratio);
        int sceneHeight = (int) (0.5 + Screen.getPrimary().getVisualBounds().getHeight() * ratio);

        primaryStage.getIcons().add(new Image("/icons/bullet.png"));

        primaryStage.setScene(new Scene(root, sceneWidth, sceneHeight));
        primaryStage.show();
        primaryStage.setMaximized(true);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
