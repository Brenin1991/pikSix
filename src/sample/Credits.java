package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Credits extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/about.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto");
        scene.getStylesheets().add("sample/style/Style.css");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setTitle("About");

        SetStage(stage);
    }

    public static Stage GetStage(){
        return stage;
    }

    public static void SetStage(Stage stage){
        Credits.stage = stage;
    }
}