package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PikSix extends Application {

    private static Stage stage;
    private static Scene mainScene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("PikSix");

        Parent mainFXML = FXMLLoader.load(getClass().getResource("view/SplashFXML.fxml"));
        mainScene = new Scene(mainFXML, 640, 480);

        mainScene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto");
        mainScene.getStylesheets().add("sample/style/Style.css");

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
