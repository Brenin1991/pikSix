package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * This class contains the SplashScreen of the Application
 *
 */
public class SplashScreenController extends StackPane {

    // -------------------------------------------------------------


    private Scene scene;

    Stage window = new Stage();

    Image icon = new Image("sample/images/AngularEngine.png");

    public SplashScreenController() throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("view/SplashFXML.fxml"));
        scene = new Scene(root1);

        //Window
        window.setTitle("Splash Screen");
        window.getIcons().add(icon);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("icon64.png")))
        window.initStyle(StageStyle.TRANSPARENT);
        window.setScene(scene);


    }


    /**
     * Shows the window of the SplashScreen
     */
    public void showWindow() {
        window.show();
    }

    /**
     * Hides the window of the SplashScreen
     */
    public void hideWindow() {
        window.hide();
    }

    /**
     * Called as soon as .fxml is initialized
     */

    /**
     * Gets the screen width.
     *
     * @return The screen <b>Width</b> based on the <b> bounds </b> of the Screen.
     */
    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    /**
     * Gets the screen height.
     *
     * @return The screen <b>Height</b> based on the <b> bounds </b> of the Screen.
     */
    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

    /**
     * Gets the visual screen width.
     *
     * @return The screen <b>Width</b> based on the <b>visual bounds</b> of the Screen.These bounds account for objects in the native windowing system
     *         such as task bars and menu bars. These bounds are contained by Screen.bounds.
     */
    public static double getVisualScreenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }

    /**
     * Gets the visual screen height.
     *
     * @return The screen <b>Height</b> based on the <b>visual bounds</b> of the Screen.These bounds account for objects in the native windowing
     *         system such as task bars and menu bars. These bounds are contained by Screen.bounds.
     */
    public static double getVisualScreenHeight() {
        return Screen.getPrimary().getVisualBounds().getHeight();
    }

}
