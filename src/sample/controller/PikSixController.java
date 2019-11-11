package sample.controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
    private JFXComboBox<String> cbShapes;

    @FXML
    private JFXSlider sldOpacity;

    @FXML
    private JFXColorPicker cpFillColor;

    @FXML
    private JFXSlider sldBorderSize;

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
    private JFXTextField tfCanvaW;

    @FXML
    private JFXTextField tfCanvaH;

    @FXML
    private JFXButton btnResize =  new JFXButton();

    @FXML
    private JFXButton btnClear =  new JFXButton();

    @FXML
    private Pane mainPane = new Pane();

    @FXML
    private JFXTextField tfShapePosX;

    @FXML
    private JFXTextField tfShapePosY;

    @FXML
    private JFXComboBox cbLayer = new JFXComboBox();

    @FXML
    private JFXButton btnShapeRemove;

    // Var
    public GraphicsContext gc;
    private double positionX = 0;
    private double positionY = 0;
    private String item;
    private Credits credits;

    // Objects
    private Tools tools;

    static interface ManuseiaContexto {
        public void configura(MouseEvent m, GraphicsContext ctx);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newProject();
        tools = new Tools();
        cbShapesConfig();
        cbFontStyleConfig();
        draw();
        debug();
        saveFile();
        mainPane.setPrefSize(1100,600);
        spMain.setContent(mainPane);
        spMain.setPrefSize(1100, 600);

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

        btnShapeRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeShape();
                System.out.println("Remove");
            }
        });
    }

    void debug(){
        System.out.println("Position x: "+positionX);
        System.out.println("Position y: "+positionY);
    }

    void cbShapesConfig(){
        cbShapes.getItems().addAll("Rectangle", "Oval", "Line", "Text", "Image", "Background Color");
    }

    void cbFontStyleConfig() { cbbFontStyle.getItems().addAll(Font.getFontNames());}

    public void draw(){
        cbShapesSelect();
    }
    void cbShapesSelect(){
        cbShapes.setOnMouseEntered(event -> {
            item = cbShapes.getSelectionModel().getSelectedItem();
            tools = new Tools();
            mainPane.setOnMouseClicked(event1 -> {
                positionX = event1.getX();
                positionY = event1.getY();
                Double x = Double.parseDouble(tfShapePosX.getText());
                Double y = Double.parseDouble(tfShapePosY.getText());
                if(item == "Oval") {
                    System.out.println(item);
                    mainPane.getChildren().addAll(tools.oval(cpFillColor.getValue(), cpBorderColor.getValue(), positionX, positionY, x, sldBorderSize.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue()));
                    layerCount();
                }
                if(item == "Rectangle") {
                    System.out.println(item);
                    mainPane.getChildren().addAll(tools.rect(cpFillColor.getValue(), cpBorderColor.getValue(), positionX, positionY, x, y, sldBorderSize.getValue(), sldOpacity.getValue(), sldEffectBlur.getValue(), sldEffectShadow.getValue()));
                    layerCount();
                }
                if(item == "Line") {
                    System.out.println(item);
                    mainPane.setOnMouseClicked(event2 -> {
                        double p1x = positionX;
                        double p1y = positionY;
                        double p2x = event2.getX();
                        double p2y = event2.getY();
                        mainPane.getChildren().addAll(tools.line(sldBorderSize.getValue(), cpBorderColor.getValue(), p1x, p1y, p2x, p2y, sldOpacity.getValue()));
                        positionX = p2x;
                        positionY = p2y;
                        layerCount();
                    });

                }
                if(item == "Text") {
                    System.out.println(item);
                    mainPane.getChildren().addAll(tools.text(tfTextString.getText(), tfFontSize.getText(), cpTextColor.getValue(), cbbFontStyle.getValue().toString(), positionX, positionY, sldOpacity.getValue()));
                    layerCount();

                }

                if(item == "Background Color") {
                    System.out.println(item);
                    Double w, h;
                    w = Double.parseDouble(tfCanvaW.getText());
                    h = Double.parseDouble(tfCanvaH.getText());
                    System.out.println(cpFillColor.getValue().getRed()+", "+cpFillColor.getValue().getGreen()+", "+cpFillColor.getValue().getBlue());
                    Double r, g, b;
                    r =  cpFillColor.getValue().getRed();
                    g =  cpFillColor.getValue().getGreen();
                    b =  cpFillColor.getValue().getBlue();
                    System.out.println(r+", "+g+", "+b);
                    mainPane.setBackground(new Background(new BackgroundFill(Color.color(r, g, b),  CornerRadii.EMPTY,  Insets.EMPTY)));
                }
            });
        });
    }

    public void removeShape() {
        int itemLayer = (int) cbLayer.getSelectionModel().getSelectedItem();

        mainPane.getChildren().remove(itemLayer);
        cbLayer.getItems().remove(itemLayer);
        layerCount();
    }
/*
    public void translateShape() {
        int itemLayer = (int) cbLayer.getSelectionModel().getSelectedItem();

        Double x, y;
        x = Double.parseDouble(tfShapePosX.getText());
        y = Double.parseDouble(tfShapePosY.getText());
        System.out.println(y+" "+x);
        mainPane.getChildren().get(itemLayer).setTranslateX(x);
        mainPane.getChildren().get(itemLayer).setTranslateY(y);
    }*/

    public void layerCount() {
        cbLayer.getItems().clear();
        for(int i = 0; i<mainPane.getChildren().size();i++) {
            cbLayer.getItems().add(i);
        }
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
                    Image snapshot = mainPane.snapshot(null, null);

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
        mainPane.setPrefSize(w,h);
        mainPane.getChildren().clear();
        mainPane.setBackground(new Background(new BackgroundFill(Color.WHITE,  CornerRadii.EMPTY,  Insets.EMPTY)));
        cbLayer.getItems().clear();

    }

    public void resetCanvaSize(){
        Double w, h;
        w = Double.parseDouble(tfCanvaW.getText());
        h = Double.parseDouble(tfCanvaH.getText());
        mainPane.setPrefSize(w,h);
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