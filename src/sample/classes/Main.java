package sample.classes;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override//from  ww  w.j av  a2s . c  om
    public void start(Stage primaryStage) {
        // Create two panes
        Pane pane1 = new Pane();
        pane1.setRotate(180);
        pane1.setPadding(new Insets(72, 0, 0, 75));
        Pane pane2 = new Pane();

        // Create a polyline
        Polyline polyline1 = new Polyline();
        pane1.getChildren().add(polyline1);
        ObservableList<Double> list = polyline1.getPoints();
        polyline1.setStroke(Color.ORANGE);
        double scaleFactor = 0.05;
        for (int x = -100; x <= 100; x++) {
            list.add(x + 200.0);
            list.add(scaleFactor * x * x);
        }

        // Create two lines
        Line lineX = new Line(10, 200, 350, 200);
        // pane.getChildren().addAll(stackPane, lineX);

        Line lineY = new Line(lineX.getEndX() / 2, 250, lineX.getEndX() / 2, 30);
        pane2.getChildren().addAll(pane1, lineX, lineY);

        // Create two polylines
        Polyline polyline2 = new Polyline();
        polyline2.setStroke(Color.RED);
        pane2.getChildren().add(polyline2);
        ObservableList<Double> list2 = polyline2.getPoints();
        list2.addAll(lineY.getEndX() - 10, lineY.getEndY() + 20, lineY.getEndX(), lineY.getEndY(), lineY.getEndX() + 10,
                lineY.getEndY() + 20);

        Polyline polyline3 = new Polyline();
        polyline3.setStroke(Color.RED);
        pane2.getChildren().add(polyline3);
        ObservableList<Double> list3 = polyline3.getPoints();
        list3.addAll(lineX.getEndX() - 20, lineX.getEndY() - 10, lineX.getEndX(), lineX.getEndY(), lineX.getEndX() - 20,
                lineX.getEndY() + 10);

        // Create two text objects
        Text textX = new Text(lineX.getEndX() - 10, lineX.getEndY() - 20, "X");
        Text textY = new Text(lineY.getEndX() + 20, lineY.getEndY() + 10, "Y");
        pane2.getChildren().addAll(textX, textY);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane2);
        primaryStage.setTitle("java2s.com");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}