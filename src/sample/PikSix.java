package sample;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PikSix extends Application {


    private static Scene mainScene;
    private static Stage stage;

    int COUNT_LIMIT = 1000;
    public SplashScreenController splashScreen = new SplashScreenController();

    Image icon = new Image("sample/images/AngularEngine.png");

    public PikSix() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.getIcons().add(icon);
        //stage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("PikSix");
        primaryStage.setMaximized(true);

        Parent mainFXML = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        mainScene = new Scene(mainFXML, 640, 480);

        mainScene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto");
        mainScene.getStylesheets().add("sample/style/Style.css");

        primaryStage.setScene(mainScene);

        //Show the SplashScreen
        splashScreen.showWindow();

        //I am using the code below so the Primary Stage of the application
        //doesn't appear for 2 seconds , so the splash screen is displayed
        PauseTransition splashScreenDelay = new PauseTransition(Duration.seconds(10));
        splashScreenDelay.setOnFinished(f -> {
            primaryStage.show();
            splashScreen.hideWindow();
        });
        splashScreenDelay.playFromStart();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
