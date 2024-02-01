package sample.classes;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SplineCurve extends Application {

    private List<Anchor> points = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group, 500,500, Color.BISQUE);

        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                double x = event.getX(), y = event.getY();
                if (!points.isEmpty()) {
                    Anchor start = points.get(points.size() - 1);
                    CubicCurve curve = createCurve(start, x, y, group);
                    Anchor end = new Anchor(Color.TOMATO, curve.endXProperty(), curve.endYProperty(), 5);
                    group.getChildren().add(end);
                    points.add(end);
                } else {
                    Anchor anchor = new Anchor(Color.TOMATO, x, y, 5);
                    anchor.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        if (e.getButton() == MouseButton.SECONDARY) {
                            Anchor start = points.get(points.size() - 1);
                            CubicCurve curve = createCurve(start, anchor.getCenterX(), anchor.getCenterY(), group);
                            curve.endXProperty().bind(anchor.centerXProperty());
                            curve.endYProperty().bind(anchor.centerYProperty());
                            points.clear();
                            e.consume();
                        }
                    });
                    group.getChildren().add(anchor);
                    points.add(anchor);
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private CubicCurve createCurve(Anchor from, double x2, double y2, Group group) {
        double x1 = from.getCenterX(), y1 = from.getCenterY();
        double distance = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
        CubicCurve curve = new CubicCurve();
        curve.setStartX(x1);
        curve.setStartY(y1);
        curve.setControlX1(x1 + 20 * (x2 - x1) / distance);
        curve.setControlY1(y1 + 20 * (y2 - y1) / distance);
        curve.setControlX2(x2 - 20 * (x2 - x1) / distance);
        curve.setControlY2(y2 - 20 * (y2 - y1) / distance);
        curve.setEndX(x2);
        curve.setEndY(y2);
        curve.setStroke(Color.BLUEVIOLET);
        curve.setStrokeWidth(4);
        curve.setStrokeLineCap(StrokeLineCap.ROUND);
        curve.setFill(Color.TRANSPARENT);
        curve.startXProperty().bind(from.centerXProperty());
        curve.startYProperty().bind(from.centerYProperty());
        Line controlLine1 = new ControlLine(curve.controlX1Property(), curve.controlY1Property(), curve.startXProperty(), curve.startYProperty());
        Line controlLine2 = new ControlLine(curve.controlX2Property(), curve.controlY2Property(), curve.endXProperty(), curve.endYProperty());
        Anchor control1 = new Anchor(Color.FORESTGREEN, curve.controlX1Property(), curve.controlY1Property(), 3);
        Anchor control2 = new Anchor(Color.FORESTGREEN, curve.controlX2Property(), curve.controlY2Property(), 3);
        group.getChildren().addAll(curve, control1, control2, controlLine1, controlLine2);
        return curve;
    }

    class ControlLine extends Line {
        ControlLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
            startXProperty().bind(startX);
            startYProperty().bind(startY);
            endXProperty().bind(endX);
            endYProperty().bind(endY);
            setStrokeWidth(2);
            setStroke(Color.FORESTGREEN.deriveColor(0, 1, 1, 0.5));
        }
    }

    // a draggable anchor displayed around a point.
    class Anchor extends Circle {
        Anchor(Color color, DoubleProperty x, DoubleProperty y, double radius) {
            super(x.get(), y.get(), radius);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);
            x.bind(centerXProperty());
            y.bind(centerYProperty());
            enableDrag();
        }
        Anchor(Color color, double x, double y, double radius) {
            super(x, y, radius);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);
            enableDrag();
        }
        // make a node movable by dragging it around with the mouse.
        private void enableDrag() {
            final Delta dragDelta = new Delta();
            setOnMousePressed(mouseEvent -> {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getCenterX() - mouseEvent.getX();
                dragDelta.y = getCenterY() - mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
            });
            setOnMouseReleased(mouseEvent -> getScene().setCursor(Cursor.HAND));
            setOnMouseDragged(mouseEvent -> {
                double newX = mouseEvent.getX() + dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth()) {
                    setCenterX(newX);
                }
                double newY = mouseEvent.getY() + dragDelta.y;
                if (newY > 0 && newY < getScene().getHeight()) {
                    setCenterY(newY);
                }
            });
            setOnMouseEntered(mouseEvent -> {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.HAND);
                }
            });
            setOnMouseExited(mouseEvent -> {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.DEFAULT);
                }
            });
        }
        // records relative x and y co-ordinates.
        private class Delta { double x, y; }
    }
}