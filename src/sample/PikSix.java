package sample;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PikSix extends Application {

    private static Stage stage;
    private static Scene mainScene;
    private static final int COUNT_LIMIT = 500000;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        //stage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("PikSix");
        primaryStage.setMaximized(true);

        Parent mainFXML = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        mainScene = new Scene(mainFXML, 640, 480);

        mainScene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto");
        mainScene.getStylesheets().add("sample/style/Style.css");

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void init() throws Exception {
        for(int i = 0; i < COUNT_LIMIT; i++) {
            double progress = (100 * 1) / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
        }
    }


    public static void main(String[] args) {
        LauncherImpl.launchApplication(PikSix.class, PreLoader.class, args);
    }
}
