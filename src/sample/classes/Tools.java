package sample.classes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

public class Tools {
    BoxBlur bb = new BoxBlur();
    DropShadow ds = new DropShadow();

    public void rect(GraphicsContext gc, Color fillColor, Color strokeColor, Double posX, Double posY, Double p2x, Double p2y, Double fillSize, Double lineSize, Double corner, Double opacity, Double blur, Double shadow){
        gc.setGlobalAlpha(opacity/100);
        gc.applyEffect(blurConfig(blur));
        gc.applyEffect(dropShadowConfig(shadow));
        gc.setLineWidth(lineSize);
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);
        gc.fillRoundRect(posX, posY,p2x-posX,p2y-posY, corner, corner);
        gc.strokeRoundRect(posX, posY,p2x-posX,p2y-posY, corner, corner);
    }

    public void oval(GraphicsContext gc, Color fillColor, Color strokeColor, Double posX, Double posY, Double p2x, Double p2y, Double fillSize, Double lineSize, Double opacity, Double blur, Double shadow){
        gc.setGlobalAlpha(opacity/100);
        gc.applyEffect(blurConfig(blur));
        gc.applyEffect(dropShadowConfig(shadow));
        gc.setLineWidth(lineSize);
        gc.setFill(fillColor);
        gc.setStroke(strokeColor);
        gc.fillOval(posX, posY,p2x-posX,p2y-posY);
        gc.strokeOval(posX, posY,p2x-posX,p2y-posY);
    }

    public void line(GraphicsContext gc, Double lineSize, Color strokeColor, Double posX, Double posY, Double p2x, Double p2y, Double opacity){
        gc.setGlobalAlpha(opacity/100);
        gc.setStroke(strokeColor);
        gc.setLineWidth(lineSize);
        gc.strokeLine(posX, posY, p2x, p2y);
    }

    public void text(GraphicsContext gc, String text, String textSize, Color textColor, String fontName, Double posX, Double posY, Double opacity){
        gc.setGlobalAlpha(opacity/100);
        Double fs = Double.parseDouble(textSize);
        gc.setFont(new Font(fontName,fs));
        gc.setFill(textColor);
        gc.fillText(text, posX, posY);
    }

    public void brushRect(GraphicsContext gc, Color fillColor, Double posX, Double posY, Double fillSize, Double corner, Double opacity){
        gc.setGlobalAlpha(opacity/100);
        gc.setFill(fillColor);
        gc.fillRoundRect(posX, posY, fillSize, fillSize, corner,corner);
    }

        public void brushCircle(GraphicsContext gc, Color fillColor, Double posX, Double posY, Double fillSize, Double opacity){
        gc.setGlobalAlpha(opacity/100);
        gc.setFill(fillColor);
        gc.setLineWidth(0);
        gc.fillRoundRect(posX, posY, fillSize, fillSize, 100, 100);
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

