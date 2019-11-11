package sample.classes;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tools {
    BoxBlur bb = new BoxBlur();
    DropShadow ds = new DropShadow();

    public Rectangle rect(Color fillColor, Color strokeColor, Double posX, Double posY, Double x, Double y, Double lineSize, Double opacity, Double blur, Double shadow){
        Rectangle rectangle;
        rectangle = new Rectangle(posX, posY, x, y);
        rectangle.setStrokeWidth(lineSize);
        rectangle.setFill(fillColor);
        rectangle.setStroke(strokeColor);
        rectangle.setOpacity(opacity/100);

        return rectangle;
    }

    public Circle oval(Color fillColor, Color strokeColor, Double posX, Double posY, Double x, Double lineSize, Double opacity, Double blur, Double shadow){
        Circle circle;
        circle = new Circle(posX, posY, x);
        circle.setStrokeWidth(lineSize);
        circle.setFill(fillColor);
        circle.setStroke(strokeColor);
        circle.setOpacity(opacity/100);

        return circle;
    }

    public Line line(Double lineSize, Color strokeColor, Double posX, Double posY, Double p2x, Double p2y, Double opacity){
        Line line = new Line(posX, posY, p2x, p2y);
        line.setStrokeWidth(lineSize);
        line.setStroke(strokeColor);

        return line;
    }

    public Text text(String string, String textSize, Color textColor, String fontName, Double posX, Double posY, Double opacity){
        Text text = new Text(posX, posY, string);
        Double fs = Double.parseDouble(textSize);
        text.setFont(new Font(fontName, fs));
        text.setFill(textColor);

        return text;
    }

    public BoxBlur blurConfig(Double blur){
        bb.setHeight(blur/10);
        bb.setWidth(blur/10);
        bb.setIterations(1);

        return bb;
    }

    public DropShadow dropShadowConfig(Double shadow){
        ds.setHeight(shadow/10);
        ds.setWidth(shadow/10);
        ds.setColor(Color.BLACK);

        return ds;
    }
}

