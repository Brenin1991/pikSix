package sample.classes;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

// Inspired by varren from video https://www.youtube.com/watch?v=peUcqBTWw34.

public class NodeDragResizer {

    public interface Resizer{
        public void resize(Node node, double width, double height);
    }
    enum S {
        DEFAULT,
        DRAG,
        RESIZE_E,
        RESIZE_W,
        RESIZE_N,
        RESIZE_S,
        RESIZE_NE,
        RESIZE_SE,
        RESIZE_NW,
        RESIZE_SW
    }

    protected static final Resizer resizer = new Resizer() {
        @Override
        public void resize(Node node, double width, double height) {

        }
    };
    protected Node node;
    protected S state = S.DEFAULT;
    protected Bounds initialBounds;
    protected double clickX, clickY;
    protected final double MARGIN = 3;

    public NodeDragResizer(Node node){
        this.node = node;
        initialBounds = node.getBoundsInParent();
    }

    public static void makeDragResizable(Node node){
        NodeDragResizer nodeDragResizer = new NodeDragResizer(node);
        nodeDragResizer.setDragResizer();

    }

    public void setDragResizer(){
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setInitialClick(event);
                setInitialBounds(node);
                if (state == S.DRAG)
                    node.setCursor(Cursor.CLOSED_HAND);
            }
        });

        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setCursor(getCursorForState(state));
                setInitialBounds(node);
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getScreenX();
                double mouseY = event.getScreenY();
                double x = initialBounds.getMinX();
                double y = initialBounds.getMinY();
                double width = initialBounds.getWidth();
                double height = initialBounds.getHeight();


                if (state == S.DRAG){
                    x += mouseX-clickX;
                    y += mouseY-clickY;
                } else {
                    if (state == S.RESIZE_W || state == S.RESIZE_NW || state == S.RESIZE_SW) {
                        x += mouseX - clickX;
                        width += clickX-mouseX;
                    }
                    if (state == S.RESIZE_E || state == S.RESIZE_NE || state == S.RESIZE_SE)
                        width += mouseX - clickX;
                    if (state == S.RESIZE_N || state == S.RESIZE_NE || state == S.RESIZE_NW) {
                        y += mouseY - clickY;
                        height += clickY-mouseY;
                    }
                    if (state == S.RESIZE_S || state == S.RESIZE_SE || state == S.RESIZE_SW)
                        height += mouseY - clickY;
                }
                // Case when width/height goes negative:
                if (width<0)
                    x+=width;
                if (height < 0)
                    y+=height;

                setBounds(node,x,y,Math.abs(width),Math.abs(height));
            }
        });
        node.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                state = getStateFor(event);
                node.setCursor(getCursorForState(state));
            }
        });
    }

    public void setInitialClick(MouseEvent e){
        clickX = e.getScreenX();
        clickY = e.getScreenY();
    }
    public void setInitialBounds(Node node){
        initialBounds = node.getBoundsInParent();
    }
    public Cursor getCursorForState(S s){
        return switch (s){
            case RESIZE_NE -> Cursor.NE_RESIZE;
            case RESIZE_SE -> Cursor.SE_RESIZE;
            case RESIZE_NW -> Cursor.NW_RESIZE;
            case RESIZE_SW -> Cursor.SW_RESIZE;
            case RESIZE_E -> Cursor.E_RESIZE;
            case RESIZE_W -> Cursor.W_RESIZE;
            case RESIZE_N -> Cursor.N_RESIZE;
            case RESIZE_S -> Cursor.S_RESIZE;
            case DRAG -> Cursor.HAND;
            default -> Cursor.DEFAULT;
        };
    }
    public S getStateFor(MouseEvent e){
        if (isNearLeftSide(e) && isNearTopSide(e))
            return S.RESIZE_NW;
        if (isNearLeftSide(e) && isNearBottomSide(e))
            return S.RESIZE_SW;
        if (isNearRightSide(e) && isNearTopSide(e))
            return S.RESIZE_NE;
        if (isNearRightSide(e) && isNearBottomSide(e))
            return S.RESIZE_SE;
        if (isNearLeftSide(e))
            return S.RESIZE_W;
        if (isNearRightSide(e))
            return S.RESIZE_E;
        if (isNearTopSide(e))
            return S.RESIZE_N;
        if (isNearBottomSide(e))
            return S.RESIZE_S;
        if (isWithinDragBounds(e))
            return S.DRAG;
        return S.DEFAULT;
    }

    public double nodeLocalMinX(){
        return node.getBoundsInLocal().getMinX();
    }
    public double nodeLocalMaxX(){
        return node.getBoundsInLocal().getMaxX();
    }
    public double nodeLocalMinY(){
        return node.getBoundsInLocal().getMinY();
    }
    public double nodeLocalMaxY(){
        return node.getBoundsInLocal().getMaxY();
    }
    public double nodeWidth(){
        return node.getBoundsInLocal().getWidth();
    }
    public double nodeHeight(){
        return node.getBoundsInLocal().getHeight();
    }

    public boolean isNearLeftSide(MouseEvent e){
        return intersect(e.getX(),nodeLocalMinX());
    }
    public boolean isNearRightSide(MouseEvent e){
        return intersect(e.getX(),nodeLocalMaxX());
    }
    public boolean isNearTopSide(MouseEvent e){
        return intersect(e.getY(),nodeLocalMinY());
    }
    public boolean isNearBottomSide(MouseEvent e){
        return intersect(e.getY(),nodeLocalMaxY());
    }
    public boolean intersect(double a, double b){
        return (Math.abs(a-b)<=MARGIN);
    }
    public boolean isWithinDragBounds(MouseEvent e){

        return (e.getX()>nodeLocalMinX()+MARGIN
                && e.getX()<nodeLocalMaxX()-MARGIN
                && e.getY()>nodeLocalMinY()+MARGIN
                && e.getY()<nodeLocalMaxY()-MARGIN);
    }

    public void setBounds(Node node, double x, double y, double width, double height){
        System.out.println("("+x+", "+y+", "+width+", "+height+")");

        if (    // making sure node stays within the bounds of its parent
                x>0
                        && y>0
                        && x<node.getParent().getLayoutBounds().getWidth()-width
                        && y<node.getParent().getLayoutBounds().getHeight()-height
        ) {
            //node.resizeRelocate(x,y,width,height);
            node.relocate(x,y);
            resizer.resize(node,width,height);
        }
    }
}