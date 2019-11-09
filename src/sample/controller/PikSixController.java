package sample.controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Credits;
import sample.classes.Tools;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PikSixController implements Initializable{

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXComboBox<String> cbTools;

    @FXML
    private JFXComboBox<String> cbShapes;

    @FXML
    private JFXSlider sldCorner;

    @FXML
    private JFXSlider sldOpacity;

    @FXML
    private JFXColorPicker cpFillColor;

    @FXML
    private JFXSlider sldBorderSize;

    @FXML
    private JFXSlider sldFillSize;

    @FXML
    private JFXColorPicker cpBorderColor;

    @FXML
    private JFXSlider sldEffectBlur;

    @FXML
    private JFXSlider sldEffectShadow;

    @FXML
    private JFXTextField tfTextString;

    @FXML
    private JFXTextField tfFontSize;

    @FXML
    private JFXColorPicker cpTextColor;

    @FXML
    private JFXComboBox cbbFontStyle;

    @FXML
    private ScrollPane spMain = new ScrollPane();

    @FXML
    private Pane mainPane;

    @FXML
    private JFXTextField tfCanvaW;

    @FXML
    private JFXTextField tfCanvaH;

    @FXML
    private JFXButton btnResize =  new JFXButton();

    @FXML
    private JFXButton btnClear =  new JFXButton();

    private Canvas mainCanva = new Canvas(800,600);

    // Var
    public GraphicsContext gc;
    private double positionX = 0;
    private double positionY = 0;
    private String item;
    private Credits credits;

    // Objecto
    private Tools tools;

    static interface ManuseiaContexto {
        public void configura(MouseEvent m, GraphicsContext ctx);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = mainCanva.getGraphicsContext2D();
        newProject();
        tools = new Tools();
        cbToolsConfig();
        cbShapesConfig();
        cbFontStyleConfig();
        draw();
        debug();
        saveFile();
        spMain.setPrefSize(1100, 600);
        spMain.setContent(mainCanva);

        btnResize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetCanvaSize();
                System.out.println("Resize");
            }
        });

        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newProject();
                System.out.println("Clear");
            }
        });
    }

    void debug(){
        System.out.println("Position x: "+positionX);
        System.out.println("Position y: "+positionY);
    }

    void cbToolsConfig(){
        cbTools.getItems().addAll("Brush Circle","Brush Rectangle", "Background Color");
    }

    void cbShapesConfig(){
        cbShapes.getItems().addAll("Rectangle", "Oval", "Line", "Text", "Image");
    }

    void cbFontStyleConfig() { cbbFontStyle.getItems().addAll(Font.getFontNames());}

    public void draw(){
        cbToolsSelect();
        cbShapesSelect();
    }

    void cbShapesSelect(){
        cbShapes.setOnMouseEntered(event -> {
            item = cbShapes.getSelectionModel().getSelectedItem();

            tools = new Tools();

            mainCanva.setOnMouseClicked(event1 -> {
                positionX = event1.getX();
                positionY = event1.getY();

                if(item == "Oval") {
                    System.out.println(item);
                    mainCanva.setOnMouseClicked(event2 -> {
                        double p1x = positionX;
                        double p1y = positionY;
                        double p2x = event2.getX();
                        double p2y = event2.getY();
                        tools.oval(gc, cpFillColor.getValue(), cpBorderColor.getValue(), p1x, p1y, p2x, p2y, sldFillSize.getValue(), sldBorderSize.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue());
                        positionX = p2x;
                        positionY = p2y;
                    });
                }

                if(item == "Rectangle") {
                    System.out.println(item);
                    mainCanva.setOnMouseClicked(event2 -> {
                        double p1x = positionX;
                        double p1y = positionY;
                        double p2x = event2.getX();
                        double p2y = event2.getY();
                        tools.rect(gc, cpFillColor.getValue(), cpBorderColor.getValue(), p1x, p1y, p2x, p2y, sldFillSize.getValue(), sldBorderSize.getValue(), sldCorner.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue());
                        positionX = p2x;
                        positionY = p2y;
                    });

                }

                if(item == "Line") {
                    System.out.println(item);
                    mainCanva.setOnMouseClicked(event2 -> {
                        double p1x = positionX;
                        double p1y = positionY;
                        double p2x = event2.getX();
                        double p2y = event2.getY();
                        tools.line(gc, sldBorderSize.getValue(), cpBorderColor.getValue(), p1x, p1y, p2x, p2y, sldOpacity.getValue());
                        positionX = p2x;
                        positionY = p2y;
                    });

                }
                if(item == "Text") {
                    System.out.println(item);
                    tools.text(gc, tfTextString.getText(), tfFontSize.getText(), cpTextColor.getValue(), cbbFontStyle.getValue().toString(), positionX, positionY, sldOpacity.getValue());
                }
            });
        });
    }

    void cbToolsSelect(){
        cbTools.setOnMouseEntered(event -> {
            item = cbTools.getSelectionModel().getSelectedItem();

            tools = new Tools();

            mainCanva.setOnMouseDragged(event1 -> {
                positionX = event1.getX();
                positionY = event1.getY();
                if(item == "Brush Circle") {
                    System.out.println(item);
                    tools.brushCircle(gc, cpFillColor.getValue(), positionX, positionY, sldFillSize.getValue(), sldOpacity.getValue());
                }

                if(item == "Brush Rectangle") {
                    System.out.println(item);
                    tools.brushRect(gc, cpFillColor.getValue(), positionX, positionY, sldFillSize.getValue(), sldCorner.getValue(), sldOpacity.getValue());
                }

                if(item == "Background Color") {
                    System.out.println(item);
                    Double w, h;
                    w = Double.parseDouble(tfCanvaW.getText());
                    h = Double.parseDouble(tfCanvaH.getText());
                    gc.setFill(cpFillColor.getValue());
                    gc.fillRect(0,0,w, h);
                }

            });
        });
    }

    void desableP(){
        cbTools.disableProperty().bind(cbShapes.valueProperty().isEqualTo("Circle"));
    }

    public void onExit(){
        Platform.exit();
    }

    public void saveFile(){
        btnSave.setOnMouseClicked(event1 -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save File");
            fc.setInitialDirectory(new File("/home"));
            fc.setInitialFileName("paint");
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG File", "*.png"));

            try{
                File selectedFile = fc.showSaveDialog(new Stage());
                if(selectedFile != null){
                    Image snapshot = mainCanva.snapshot(null, null);

                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null), "png", selectedFile);
                }
            } catch (Exception e){

            }
        });
    }

    public void newProject(){
        Double w, h;
        w = Double.parseDouble(tfCanvaW.getText());
        h = Double.parseDouble(tfCanvaH.getText());
        gc.setFill(Color.WHITE);
        mainCanva.setWidth(w);
        mainCanva.setHeight(h);
        gc.fillRect(0,0, w, h);
        spMain.setContent(mainCanva);
    }

    public void resetCanvaSize(){
        Double w, h;
        w = Double.parseDouble(tfCanvaW.getText());
        h = Double.parseDouble(tfCanvaH.getText());
        mainCanva.setWidth(w);
        mainCanva.setHeight(h);
        spMain.setContent(mainCanva);
    }

    public void aboutWindow(){
        credits = new Credits();
        try {
            credits.start(new Stage());
        } catch (Exception ex) {
            Logger.getLogger(PikSixController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}