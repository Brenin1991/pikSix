package sample.classes;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import jfxtras.labs.scene.control.window.SelectableNode;
import jfxtras.labs.util.NodeUtil;
import jfxtras.labs.util.WindowUtil;
import jfxtras.labs.util.event.EventHandlerGroup;

import java.util.Iterator;

class RectangleSelectionControllerImpl {
    private Rectangle rectangle;
    private Parent root;
    private double nodeX;
    private double nodeY;
    private double firstX;
    private double firstY;
    private double secondX;
    private double secondY;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mousePressedHandler;
    private EventHandler<MouseEvent> mouseReleasedHandler;

    public RectangleSelectionControllerImpl() {
    }

    public void apply(Parent root, Rectangle rect, EventHandlerGroup<MouseEvent> draggedEvtHandler, EventHandlerGroup<MouseEvent> pressedEvtHandler, EventHandlerGroup<MouseEvent> releasedEvtHandler) {
        this.init(root, rect);
        draggedEvtHandler.addHandler(this.mouseDraggedEventHandler);
        pressedEvtHandler.addHandler(this.mousePressedHandler);
        releasedEvtHandler.addHandler(this.mouseReleasedHandler);
    }

    private void init(final Parent root, Rectangle rect) {
        this.rectangle = rect;
        this.root = root;
        root.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                WindowUtil.getDefaultClipboard().unselectAll();
            }
        });
        this.mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                RectangleSelectionControllerImpl.this.performDrag(root, event);
                event.consume();
            }
        };
        this.mousePressedHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                RectangleSelectionControllerImpl.this.performDragBegin(root, event);
                event.consume();
            }
        };
        this.mouseReleasedHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                RectangleSelectionControllerImpl.this.performDragEnd(root, event);
                event.consume();
            }
        };
    }

    public void performDrag(Parent root, MouseEvent event) {
        double parentScaleX = ((Transform)root.localToSceneTransformProperty().getValue()).getMxx();
        double parentScaleY = ((Transform)root.localToSceneTransformProperty().getValue()).getMyy();
        this.secondX = event.getSceneX();
        this.secondY = event.getSceneY();
        this.firstX = Math.max(this.firstX, 0.0);
        this.firstY = Math.max(this.firstY, 0.0);
        this.secondX = Math.max(this.secondX, 0.0);
        this.secondY = Math.max(this.secondY, 0.0);
        double x = Math.min(this.firstX, this.secondX);
        double y = Math.min(this.firstY, this.secondY);
        double width = Math.abs(this.secondX - this.firstX);
        double height = Math.abs(this.secondY - this.firstY);
        this.rectangle.setX(x / parentScaleX);
        this.rectangle.setY(y / parentScaleY);
        this.rectangle.setWidth(width / parentScaleX);
        this.rectangle.setHeight(height / parentScaleY);
    }

    public void performDragBegin(Parent root, MouseEvent event) {
        if (this.rectangle.getParent() == null) {
            this.firstX = event.getSceneX();
            this.firstY = event.getSceneY();
            NodeUtil.addToParent(root, this.rectangle);
            this.rectangle.setWidth(0.0);
            this.rectangle.setHeight(0.0);
            this.rectangle.setX(this.firstX);
            this.rectangle.setY(this.firstY);
            this.rectangle.toFront();
        }
    }

    public void performDragEnd(Parent root, MouseEvent event) {
        NodeUtil.removeFromParent(this.rectangle);
        Iterator i$ = root.getChildrenUnmodifiable().iterator();

        while(i$.hasNext()) {
            Node n = (Node)i$.next();
            if (this.rectangle.intersects(n.getBoundsInParent()) && n instanceof SelectableNode) {
                WindowUtil.getDefaultClipboard().select((SelectableNode)n, true);
            }
        }

    }
}
