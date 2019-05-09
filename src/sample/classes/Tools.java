package sample.classes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class Tools {
    BoxBlur bb = new BoxBlur();
    DropShadow ds = new DropShadow();

    public void retangulo(GraphicsContext gc, Color fillColor, Color strokeColor, Double posX, Double posY, Double fillSize, Double lineSize, Double corner, Double opacity, Double blur, Double shadow){
        gc.setGlobalAlpha(opacity/100);
        gc.applyEffect(blurConfig(blur));
        gc.applyEffect(dropShadowConfig(shadow));
        gc.setLineWidth(lineSize);
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);
        gc.fillRoundRect(posX, posY, fillSize, fillSize, corner,corner);
        gc.strokeRoundRect(posX, posY, fillSize, fillSize, corner, corner);
    }

    public void circulo(GraphicsContext gc, Color fillColor, Color strokeColor, Double posX, Double posY, Double fillSize, Double lineSize, Double corner, Double opacity, Double blur, Double shadow){
        gc.setGlobalAlpha(opacity/100);
        gc.applyEffect(blurConfig(blur));
        gc.applyEffect(dropShadowConfig(shadow));
        gc.setLineWidth(lineSize);
        gc.setFill(fillColor);
        gc.setStroke(strokeColor);
        gc.fillRoundRect(posX, posY, fillSize, fillSize, 100, 100);
        gc.strokeRoundRect(posX, posY, fillSize, fillSize, 100, 100);
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

