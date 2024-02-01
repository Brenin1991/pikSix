package sample.classes;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class Tools {
    BoxBlur bb = new BoxBlur();
    DropShadow ds = new DropShadow();

    public Rectangle rect(Color fillColor, Color strokeColor, Double posX, Double posY, Double x, Double y, Double lineSize){
        Rectangle rectangle;
        rectangle = new Rectangle(posX, posY, x, y);
        rectangle.setStrokeWidth(lineSize);
        rectangle.setFill(fillColor);
        rectangle.setStroke(strokeColor);

        return rectangle;
    }

    public ImageView image(Color fillColor, Color strokeColor, Double posX, Double posY, Double x, Double y){
        FileChooser fileChooser = new FileChooser();
        Image imagem = null;
        fileChooser.setTitle("Open image");
        //fileChooser.setInitialDirectory(new File("/home"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG File", "*.png"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG File", "*.jpg"));

        try{
            File file = fileChooser.showOpenDialog(new Stage());
            if(file != null){
                imagem = new Image(file.toURI().toString());

                //ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null), "png", selectedFile);
            }
        } catch (Exception e){

        }


        ImageView img = new ImageView();

        img.setImage(imagem);
        img.setX(posX);
        img.setY(posY);

        img.setFitWidth(x);
        img.setFitHeight(y);

        return img;
    }

    public Rectangle oval(Color fillColor, Color strokeColor, Double posX, Double posY, Double x, Double lineSize){
        Rectangle circle;
        circle = new Rectangle(posX, posY, x, x);
        circle.setStrokeWidth(lineSize);
        circle.setFill(fillColor);
        circle.setStroke(strokeColor);
        circle.setArcHeight(100000000);
        circle.setArcWidth(100000000);

        return circle;
    }

    public Line line(Double lineSize, Color strokeColor, Double posX, Double posY, Double p2x, Double p2y){
        Line line = new Line(posX, posY, p2x, p2y);
        line.setStrokeWidth(lineSize);
        line.setStroke(strokeColor);

        return line;
    }

    public Text text(String string, String textSize, Color textColor, String fontName, Double posX, Double posY){
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

