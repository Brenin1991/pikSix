package sample.classes;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import jfxtras.labs.util.event.EventHandlerGroup;

public class MouseControlUtil {
    private MouseControlUtil() {
        throw new AssertionError();
    }

    public static void makeDraggable(Node n) {
        makeDraggable(n, (EventHandler)null, (EventHandler)null);
    }

    public static void addSelectionRectangleGesture(Parent root, Rectangle rect) {
        addSelectionRectangleGesture(root, rect, (EventHandler)null, (EventHandler)null, (EventHandler)null);
    }

    public static void addSelectionRectangleGesture(Parent root, Rectangle rect, EventHandler<MouseEvent> dragHandler, EventHandler<MouseEvent> pressHandler, EventHandler<MouseEvent> releaseHandler) {
        EventHandlerGroup<MouseEvent> dragHandlerGroup = new EventHandlerGroup();
        EventHandlerGroup<MouseEvent> pressHandlerGroup = new EventHandlerGroup();
        EventHandlerGroup<MouseEvent> releaseHandlerGroup = new EventHandlerGroup();
        if (dragHandler != null) {
            dragHandlerGroup.addHandler(dragHandler);
        }

        if (pressHandler != null) {
            pressHandlerGroup.addHandler(pressHandler);
        }

        if (releaseHandler != null) {
            releaseHandlerGroup.addHandler(releaseHandler);
        }

        root.setOnMouseDragged(dragHandlerGroup);
        root.setOnMousePressed(pressHandlerGroup);
        root.setOnMouseReleased(releaseHandlerGroup);
        RectangleSelectionControllerImpl selectionHandler = new RectangleSelectionControllerImpl();
        selectionHandler.apply(root, rect, dragHandlerGroup, pressHandlerGroup, releaseHandlerGroup);
    }

    public static void makeDraggable(Node n, EventHandler<MouseEvent> dragHandler, EventHandler<MouseEvent> pressHandler) {
        EventHandlerGroup<MouseEvent> dragHandlerGroup = new EventHandlerGroup();
        EventHandlerGroup<MouseEvent> pressHandlerGroup = new EventHandlerGroup();
        if (dragHandler != null) {
            dragHandlerGroup.addHandler(dragHandler);
        }

        if (pressHandler != null) {
            pressHandlerGroup.addHandler(pressHandler);
        }

        n.setOnMouseDragged(dragHandlerGroup);
        n.setOnMousePressed(pressHandlerGroup);
        n.layoutXProperty().unbind();
        n.layoutYProperty().unbind();
        _makeDraggable(n, dragHandlerGroup, pressHandlerGroup);
    }

    private static void _makeDraggable(Node n, EventHandlerGroup<MouseEvent> dragHandler, EventHandlerGroup<MouseEvent> pressHandler) {
        DraggingControllerImpl draggingController = new DraggingControllerImpl();
        draggingController.apply(n, dragHandler, pressHandler);
    }
}
