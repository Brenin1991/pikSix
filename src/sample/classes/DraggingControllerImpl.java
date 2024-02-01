package sample.classes;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;
import jfxtras.labs.util.event.EventHandlerGroup;

class DraggingControllerImpl {
    private double nodeX;
    private double nodeY;
    private double mouseX;
    private double mouseY;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mousePressedEventHandler;

    public DraggingControllerImpl() {
    }

    public void apply(Node n, EventHandlerGroup<MouseEvent> draggedEvtHandler, EventHandlerGroup<MouseEvent> pressedEvtHandler) {
        this.init(n);
        draggedEvtHandler.addHandler(this.mouseDraggedEventHandler);
        pressedEvtHandler.addHandler(this.mousePressedEventHandler);
    }

    private void init(final Node n) {
        this.mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                DraggingControllerImpl.this.performDrag(n, event);
                event.consume();
            }
        };
        this.mousePressedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                DraggingControllerImpl.this.performDragBegin(n, event);
                event.consume();
            }
        };
    }

    public void performDrag(Node n, MouseEvent event) {
        double parentScaleX = ((Transform)n.getParent().localToSceneTransformProperty().getValue()).getMxx();
        double parentScaleY = ((Transform)n.getParent().localToSceneTransformProperty().getValue()).getMyy();
        double offsetX = event.getSceneX() - this.mouseX;
        double offsetY = event.getSceneY() - this.mouseY;
        this.nodeX += offsetX;
        this.nodeY += offsetY;
        double scaledX = this.nodeX * 1.0 / parentScaleX;
        double scaledY = this.nodeY * 1.0 / parentScaleY;
        n.setLayoutX(scaledX);
        n.setLayoutY(scaledY);
        this.mouseX = event.getSceneX();
        this.mouseY = event.getSceneY();
    }

    public void performDragBegin(Node n, MouseEvent event) {
        double parentScaleX = ((Transform)n.getParent().localToSceneTransformProperty().getValue()).getMxx();
        double parentScaleY = ((Transform)n.getParent().localToSceneTransformProperty().getValue()).getMyy();
        this.mouseX = event.getSceneX();
        this.mouseY = event.getSceneY();
        this.nodeX = n.getLayoutX() * parentScaleX;
        this.nodeY = n.getLayoutY() * parentScaleY;
       // n.toFront();
    }
}
