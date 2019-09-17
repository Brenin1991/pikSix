package sample.controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.classes.Tools;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PikSixController implements Initializable{

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXComboBox<String> cbTools;

    @FXML
    private JFXComboBox<String> cbShapes;

    @FXML
    private JFXComboBox<?> cbMedias;

    @FXML
    private Accordion acExp;

    @FXML
    private TitledPane tpTransform;

    @FXML
    private JFXTextField tfPositionX;

    @FXML
    private JFXTextField tfPositionY;

    @FXML
    private JFXTextField tfSizeW;

    @FXML
    private JFXTextField tfSizeH;

    @FXML
    private JFXSlider sldAngle;

    @FXML
    private TitledPane tpAppearence;

    @FXML
    private JFXSlider sldCorner;

    @FXML
    private JFXSlider sldOpacity;

    @FXML
    private TitledPane tpFills;

    @FXML
    private JFXColorPicker cpFillColor;

    @FXML
    private TitledPane tpBorders;

    @FXML
    private JFXSlider sldBorderSize;

    @FXML
    private JFXSlider sldFillSize;

    @FXML
    private JFXColorPicker cpBorderColor;

    @FXML
    private TitledPane tpEffects;

    @FXML
    private JFXSlider sldEffectBlur;

    @FXML
    private JFXSlider sldEffectShadow;

    @FXML
    private Canvas MainCanva;


    // Variaveis
    private GraphicsContext gc;
    private double posicaox = 0;
    private double posicaoy = 0;
    private String item;

    // Objetos
    private Tools tools;

    static interface ManuseiaContexto {
        public void configura(MouseEvent m, GraphicsContext ctx);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = MainCanva.getGraphicsContext2D();
        newProject();
        tools = new Tools();
        cbToolsConfig();
        cbShapesConfig();
        draw();
        debug();
        saveFile();
    }

    void debug(){
        System.out.println("Posição x: "+posicaox);
        System.out.println("Posição y: "+posicaoy);
    }

    void cbToolsConfig(){
        cbTools.getItems().addAll("Brush Circle","Brush Retangle");
    }

    void cbShapesConfig(){
        cbShapes.getItems().addAll("Circle","Retangle");
    }

    public void draw(){
        cbToolsSelect();
        cbShapesSelect();
    }

    void cbShapesSelect(){
        cbShapes.setOnMouseEntered(event -> {
            item = cbShapes.getSelectionModel().getSelectedItem();

            MainCanva.setOnMouseClicked(event1 -> {
                posicaox = event1.getX();
                posicaoy = event1.getY();
                if(item == "Circle") {
                    System.out.println(item);
                    tools.circulo(gc, cpFillColor.getValue(), cpBorderColor.getValue(), posicaox, posicaoy, sldFillSize.getValue(), sldBorderSize.getValue(), sldCorner.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue());
                }

                if(item == "Retangle") {
                    System.out.println(item);
                    tools.retangulo(gc, cpFillColor.getValue(), cpBorderColor.getValue(), posicaox, posicaoy, sldFillSize.getValue(), sldBorderSize.getValue(), sldCorner.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue());
                }
            });
        });
    }

    void cbToolsSelect(){
        cbTools.setOnMouseEntered(event -> {
            item = cbTools.getSelectionModel().getSelectedItem();

            MainCanva.setOnMouseDragged(event1 -> {
                posicaox = event1.getX();
                posicaoy = event1.getY();
                if(item == "Brush Circle") {
                    System.out.println(item);
                    tools.circulo(gc, cpFillColor.getValue(), cpBorderColor.getValue(), posicaox, posicaoy, sldFillSize.getValue(), sldBorderSize.getValue(), sldCorner.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue());
                }

                if(item == "Brush Retangle") {
                    System.out.println(item);
                    tools.retangulo(gc, cpFillColor.getValue(), cpBorderColor.getValue(), posicaox, posicaoy, sldFillSize.getValue(), sldBorderSize.getValue(), sldCorner.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue());
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
                    Image snapshot = MainCanva.snapshot(null, null);

                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null), "png", selectedFile);
                }
            } catch (Exception e){

            }
        });
    }

    public void newProject(){
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,800, 600);
    }

}