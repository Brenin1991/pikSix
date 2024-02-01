package sample.controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Credits;
import sample.classes.MouseControlUtil;
import sample.classes.Tools;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PikSixController implements Initializable{

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> cbShapes;

    @FXML
    private Slider sldOpacity;

    @FXML
    private ColorPicker cpFillColor;

    @FXML
    private Slider sldBorderSize;

    @FXML
    private Slider sldZoom;

    @FXML
    private Pane spAnchor;

    @FXML
    private ColorPicker cpBorderColor;

    @FXML
    private Slider sldEffectBlur;

    @FXML
    private Slider sldEffectShadow;

    @FXML
    private TextField tfTextString;

    @FXML
    private TextField tfFontSize;

    @FXML
    private ColorPicker cpTextColor;

    @FXML
    private ComboBox cbbFontStyle;

    @FXML
    private ScrollPane spMain = new ScrollPane();

    @FXML
    private TextField tfCanvaW;

    @FXML
    private TextField tfCanvaH;

    @FXML
    private Button btnResize =  new Button();

    @FXML
    private Button btnClear =  new Button();

    @FXML
    private Pane mainPane = new Pane();

    @FXML
    private ComboBox cbLayer = new ComboBox();

    @FXML
    private Button btnShapeRemove;

    @FXML
    private TextField tfShapeRotation;

    @FXML
    private Button btnShapeUpdate = new Button();

    @FXML
    private Button btnAddShape = new Button();

    @FXML
    private Pane panelGridCoord = new Pane();

    @FXML
    private Button btnSwapUp = new Button();

    @FXML
    private TextField tfTransPosX = new TextField();

    @FXML
    private TextField tfTransPosY = new TextField();

    @FXML
    private TextField tfTransSizeX = new TextField();

    @FXML
    private TextField tfTransSizeY = new TextField();

    @FXML
    private Text lbCoord = new Text();

    @FXML
    private VBox vbInspector = new VBox();

    @FXML
    private Slider sldHorizontal = new Slider();

    @FXML
    private Slider sldVertical = new Slider();

    @FXML
    private Pane zoomPanel = new Pane();

    @FXML
    private CheckBox cbxImageRatio = new CheckBox();

    @FXML
    private ColorPicker cpEffectShadowColor = new ColorPicker();

    @FXML
    private Slider sldEffectShadow1 = new Slider();

    @FXML
    private Slider sldEffectShadowOffsetX = new Slider();

    @FXML
    private Slider sldEffectShadowOffsetY = new Slider();

    @FXML
    private Slider sldEffectGlowLevel = new Slider();

    @FXML
    private Slider sldEffectBloomThreshold = new Slider();


    @FXML
    private ColorPicker cpEffectGlowColor = new ColorPicker();

    @FXML
    private Slider sldCornerSize = new Slider();

    // Var
    public GraphicsContext gc;
    private double positionX = 0;
    private double positionY = 0;
    private String item;
    private Credits credits;

    // Objects
    private Tools tools;

    private int zoomFactor = 1;

    Object currentObjSel;

    Robot robot = new Robot();

    //CURSOR
    Rectangle rec = new Rectangle();

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
        zoomCanva();
        saveFile();

        cbShapes.getSelectionModel().select(0);
        item = cbShapes.getSelectionModel().getSelectedItem();

        rec.setFill(Color.rgb(0,0,0,0));
        rec.setStroke(Color.rgb(0,0,1,1));
        rec.setStrokeWidth(0.9);

        //zoomPanel.getChildren().addAll(mainPane,sldVertical, sldHorizontal);
        zoomPanel.setPrefSize(1100,600);
        mainPane.setPrefSize(1100,600);
        spMain.setContent(zoomPanel);
        spMain.setContent(spAnchor);
        spMain.setPrefSize(1100, 600);

        btnResize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetCanvaSize();
                System.out.println("Resize");
            }
        });

        actionsButtons();

        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newProject();
                System.out.println("Clear");
            }
        });

        spAnchor.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            lbCoord.setText( (int)e.getX() + ", " + (int)e.getY());
            sldHorizontal.setValue((int)e.getX());
            sldVertical.setValue((int)e.getY());
        });

        btnShapeRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeShape();
                System.out.println("Remove");
            }
        });

        btnShapeUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateShape();
                System.out.println("Update");
            }
        });

        cbLayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //updateToolsValues();
                System.out.println("Update tools");
            }
        });
    }

    void actionsButtons(){
        cpFillColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateShape();
            }
        });

        cpTextColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateShape();
            }
        });

        cpEffectShadowColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateShape();
            }
        });

        sldZoom.setOnMouseMoved(e -> {
            zoomCanva();
        });

        tfTextString.setOnKeyTyped(e -> {
            updateShape();
        });

        tfTransSizeX.setOnKeyTyped(e -> {
            updateShape();
        });

        tfTransSizeY.setOnKeyTyped(e -> {
            updateShape();
        });

        tfTransPosX.setOnKeyTyped(e -> {
            updateShape();
        });

        tfTransPosY.setOnKeyTyped(e -> {
            updateShape();
        });

        tfFontSize.setOnKeyTyped(e -> {
            updateShape();
        });

        cbbFontStyle.setOnMouseClicked(e -> {
            updateShape();
        });

        cpBorderColor.setOnMouseClicked(e -> {
            updateShape();
        });

        tfShapeRotation.setOnKeyTyped(e -> {
            updateShape();
        });

        sldBorderSize.setOnMouseMoved(e -> {
            updateShape();
        });

        sldEffectShadow.setOnMouseMoved(e -> {
            updateShape();
        });

        sldEffectGlowLevel.setOnMouseMoved(e -> {
            updateShape();
        });

        sldEffectBloomThreshold.setOnMouseMoved(e -> {
            updateShape();
        });

        sldCornerSize.setOnMouseMoved(e -> {
            updateShape();
        });

        sldEffectShadow1.setOnMouseMoved(e -> {
            updateShape();
        });

        sldEffectShadowOffsetX.setOnMouseMoved(e -> {
            updateShape();
        });

        sldEffectShadowOffsetY.setOnMouseMoved(e -> {
            updateShape();
        });

        cbxImageRatio.setOnMouseMoved(e -> {
            updateShape();
        });
    }

    void debug(){
        System.out.println("Position x: "+positionX);
        System.out.println("Position y: "+positionY);

    }

    void cbShapesConfig(){
        cbShapes.getItems().addAll("Cursor", "Rectangle", "Oval", "Line", "Text", "Image", "Background Color");
    }

    void cbFontStyleConfig() {
        cbbFontStyle.getItems().addAll(Font.getFontNames());
        cbbFontStyle.setValue("Roboto");
    }

    public void draw(){
        cbShapesSelect();
    }
    void cbShapesSelect(){
        if(item == "Rectangle") {

        }
        mainPane.setOnMousePressed(event1 -> {
            Double initx = event1.getX();
            Double inity = event1.getY();

            rec.setLayoutX(initx);
            rec.setLayoutY(inity);

            mainPane.setOnMouseDragged(event3 ->{
                Double curx = event3.getX();
                Double cury = event3.getY();
                rec.setWidth(curx - initx);
                rec.setHeight(cury - inity);

            });

            //zoomPanel.getChildren().add(c1);

            mainPane.setOnMouseReleased(event2 ->{
                rec.setWidth(0);
                rec.setHeight(0);

                Double finalx = event2.getX();
                Double finaly = event2.getY();

                if(item == "Rectangle") {
                    System.out.println(item);
                    Shape shape;
                    shape = tools.rect(cpFillColor.getValue(), cpBorderColor.getValue(), 0.0, 0.0, 10.0, 10.0, sldBorderSize.getValue());
                    shape.setLayoutX(initx);
                    shape.setLayoutY(inity);
                    ((Rectangle) shape).setWidth(finalx-initx);
                    ((Rectangle) shape).setHeight(finaly-inity);
                    ((Rectangle) shape).setArcWidth(sldCornerSize.getValue());
                    ((Rectangle) shape).setArcHeight(sldCornerSize.getValue());

                    MouseControlUtil.makeDraggable(shape);

                    shape.onMouseClickedProperty().setValue(mouseEvent -> {
                        updateToolsValues(shape);
                    });
                    mainPane.getChildren().addAll(shape);
                    layerCount();
                    cbShapes.getSelectionModel().select(0);
                    item = cbShapes.getSelectionModel().getSelectedItem();

                    //DROP SHADOW EFFECT
                    Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setColor(shadowColor);
                    dropShadow.setWidth(sldEffectShadow.getValue());
                    dropShadow.setHeight(sldEffectShadow1.getValue());
                    dropShadow.setOffsetX(sldEffectShadowOffsetX.getValue());
                    dropShadow.setOffsetY(sldEffectShadowOffsetY.getValue());
                    //GLOW EFFECT
                    Glow glow = new Glow();
                    glow.setLevel(sldEffectGlowLevel.getValue());
                    //BLOOM EFFECT
                    Bloom bloom = new Bloom();
                    bloom.setThreshold(sldEffectBloomThreshold.getValue());

                    dropShadow.setInput(glow);
                    glow.setInput(bloom);

                    shape.setEffect(dropShadow);
                }
                if(item == "Oval") {
                    Shape shape;
                    shape = tools.oval(cpFillColor.getValue(), cpBorderColor.getValue(), initx, inity, finalx-initx, sldBorderSize.getValue());

                    MouseControlUtil.makeDraggable(shape);
                    shape.onMouseClickedProperty().setValue(mouseEvent -> updateToolsValues(shape));
                    mainPane.getChildren().addAll(shape);
                    layerCount();
                    cbShapes.getSelectionModel().select(0);
                    item = cbShapes.getSelectionModel().getSelectedItem();

                    //DROP SHADOW EFFECT
                    Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setColor(shadowColor);
                    dropShadow.setWidth(sldEffectShadow.getValue());
                    dropShadow.setHeight(sldEffectShadow1.getValue());
                    dropShadow.setOffsetX(sldEffectShadowOffsetX.getValue());
                    dropShadow.setOffsetY(sldEffectShadowOffsetY.getValue());
                    //GLOW EFFECT
                    Glow glow = new Glow();
                    glow.setLevel(sldEffectGlowLevel.getValue());
                    //BLOOM EFFECT
                    Bloom bloom = new Bloom();
                    bloom.setThreshold(sldEffectBloomThreshold.getValue());

                    dropShadow.setInput(glow);
                    glow.setInput(bloom);

                    shape.setEffect(dropShadow);
                }
                if(item == "Text") {
                    Text text;
                    System.out.println(item);
                    text = tools.text(tfTextString.getText(), tfFontSize.getText(), cpTextColor.getValue(), cbbFontStyle.getValue().toString(), initx, inity);
                    MouseControlUtil.makeDraggable(text);
                    text.onMouseClickedProperty().setValue(mouseEvent -> updateToolsValues(text));

                    mainPane.getChildren().addAll(text);
                    layerCount();
                    cbShapes.getSelectionModel().select(0);
                    item = cbShapes.getSelectionModel().getSelectedItem();

                    //DROP SHADOW EFFECT
                    Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setColor(shadowColor);
                    dropShadow.setWidth(sldEffectShadow.getValue());
                    dropShadow.setHeight(sldEffectShadow1.getValue());
                    dropShadow.setOffsetX(sldEffectShadowOffsetX.getValue());
                    dropShadow.setOffsetY(sldEffectShadowOffsetY.getValue());
                    //GLOW EFFECT
                    Glow glow = new Glow();
                    glow.setLevel(sldEffectGlowLevel.getValue());
                    //BLOOM EFFECT
                    Bloom bloom = new Bloom();
                    bloom.setThreshold(sldEffectBloomThreshold.getValue());

                    dropShadow.setInput(glow);
                    glow.setInput(bloom);

                    text.setEffect(dropShadow);
                }
                if(item == "Image") {
                    System.out.println(item);
                    ImageView shape;

                    shape = tools.image(cpFillColor.getValue(), cpBorderColor.getValue(), 0.0, 0.0, 0.0, 0.0);

                    shape.setLayoutX(initx);
                    shape.setLayoutY(inity);
                    shape.setFitWidth(finalx-initx);
                    shape.setFitHeight(finaly-inity);

                    //DROP SHADOW EFFECT
                    Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setColor(shadowColor);
                    dropShadow.setWidth(sldEffectShadow.getValue());
                    dropShadow.setHeight(sldEffectShadow1.getValue());
                    dropShadow.setOffsetX(sldEffectShadowOffsetX.getValue());
                    dropShadow.setOffsetY(sldEffectShadowOffsetY.getValue());
                    //GLOW EFFECT
                    Glow glow = new Glow();
                    glow.setLevel(sldEffectGlowLevel.getValue());
                    //BLOOM EFFECT
                    Bloom bloom = new Bloom();
                    bloom.setThreshold(sldEffectBloomThreshold.getValue());

                    dropShadow.setInput(glow);
                    glow.setInput(bloom);

                    shape.setEffect(dropShadow);

                    MouseControlUtil.makeDraggable(shape);
                    shape.onMouseClickedProperty().setValue(mouseEvent -> updateToolsValues(shape));



                    mainPane.getChildren().addAll(shape);
                    layerCount();
                    cbShapes.getSelectionModel().select(0);
                    item = cbShapes.getSelectionModel().getSelectedItem();
                }
            });

        });
        cbShapes.setOnMouseEntered(ev -> {

                item = cbShapes.getSelectionModel().getSelectedItem();
                System.out.println(item);


                if(cbShapes.getSelectionModel().getSelectedIndex() == 3){

                    List<Anchor> points = new ArrayList<>();
                    Group group = new Group();
                    currentObjSel = group;
                    mainPane.getChildren().add(group);
                    //MouseControlUtil.makeDraggable(group);
                    cbShapes.getSelectionModel().select(3);
                    item = cbShapes.getSelectionModel().getSelectedItem();

                    group.onMouseClickedProperty().setValue(mouseEvent -> {
                        currentObjSel = group;
                        cbShapes.getSelectionModel().select(3);
                        item = cbShapes.getSelectionModel().getSelectedItem();
                    });

                    group.onMouseEnteredProperty().setValue(mouseEvent -> {
                        if(currentObjSel == group){
                            cbShapes.getSelectionModel().select(3);
                            item = cbShapes.getSelectionModel().getSelectedItem();
                            for (Node obj:group.getChildren()) {
                                if(obj instanceof Anchor || obj instanceof Line){
                                    obj.setOpacity(1);
                                    obj.setDisable(false);
                                }
                            }
                        } else {
                            cbShapes.getSelectionModel().select(0);
                            item = cbShapes.getSelectionModel().getSelectedItem();
                            for (Node obj:group.getChildren()) {
                                if(obj instanceof Anchor || obj instanceof Line){
                                    obj.setOpacity(0);
                                    obj.setDisable(true);

                                }
                            }
                        }
                    });

                    //DROP SHADOW EFFECT
                    Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setColor(shadowColor);
                    dropShadow.setWidth(sldEffectShadow.getValue());
                    dropShadow.setHeight(sldEffectShadow1.getValue());
                    dropShadow.setOffsetX(sldEffectShadowOffsetX.getValue());
                    dropShadow.setOffsetY(sldEffectShadowOffsetY.getValue());
                    //GLOW EFFECT
                    Glow glow = new Glow();
                    glow.setLevel(sldEffectGlowLevel.getValue());
                    //BLOOM EFFECT
                    Bloom bloom = new Bloom();
                    bloom.setThreshold(sldEffectBloomThreshold.getValue());

                    dropShadow.setInput(glow);
                    glow.setInput(bloom);

                    group.setEffect(dropShadow);

                    mainPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.SECONDARY && cbShapes.getSelectionModel().getSelectedIndex() == 3) {
                                double x = event.getX(), y = event.getY();
                                if (!points.isEmpty()) {
                                    PikSixController.Anchor start = points.get(points.size() - 1);

                                    CubicCurve curve = createCurve(start, x, y, group);
                                    PikSixController.Anchor end = new PikSixController.Anchor(Color.TOMATO, curve.endXProperty(), curve.endYProperty(), 5);

                                    group.getChildren().add(end);
                                    points.add(end);
                                    start.toFront();
                                    end.toFront();
                                } else {
                                    PikSixController.Anchor anchor = new PikSixController.Anchor(Color.TOMATO, x, y, 5);
                                    anchor.toFront();
                                    anchor.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                                        if (e.getButton() == MouseButton.SECONDARY) {
                                            PikSixController.Anchor start = points.get(points.size() - 1);
                                            CubicCurve curve = createCurve(start, anchor.getCenterX(), anchor.getCenterY(), group);
                                            curve.endXProperty().bind(anchor.centerXProperty());
                                            curve.endYProperty().bind(anchor.centerYProperty());
                                            points.clear();
                                            e.consume();
                                        }
                                    });
                                    group.getChildren().add(anchor);
                                    points.add(anchor);
                                }
                            }
                        }
                    });
            }else {

                }
        });
    }

    public void removeShape() {


        mainPane.getChildren().remove(currentObjSel);
        layerCount();
    }

    private void updateLine(Line line, Node start, Node end) {

        Node target = line.getParent();

        Bounds startBounds = convertBoundsToTarget(start, target);
        Bounds endBounds = convertBoundsToTarget(end, target);


        line.setStartX(startBounds.getMinX() + startBounds.getWidth() / 2);
        line.setStartY(startBounds.getMinY() + startBounds.getHeight() / 2);
        line.setEndX(endBounds.getMinX() + endBounds.getWidth() / 2);
        line.setEndY(endBounds.getMinY() + endBounds.getHeight() / 2);
    }

    private Bounds convertBoundsToTarget(Node node, Node target) {
        Bounds boundsInScene = node.localToScene(node.getBoundsInLocal());
        return target.sceneToLocal(boundsInScene);
    }

    public void updateShape() {
        Double rotate;

        if(currentObjSel instanceof Text){
            Text shape;
            shape = (Text) currentObjSel;
            rotate = Double.parseDouble(tfShapeRotation.getText());
            shape.setRotate(rotate);
            shape.setText(tfTextString.getText());
            Double fs = Double.parseDouble(tfFontSize.getText().toString());
            shape.setFont(new Font(cbbFontStyle.getValue().toString(), fs));
            shape.setFill(cpTextColor.getValue());

            shape.setLayoutX(Double.parseDouble(tfTransPosX.getText()));
            shape.setLayoutY(Double.parseDouble(tfTransPosY.getText()));

            shape.setScaleX(Double.parseDouble(tfTransSizeX.getText()));
            shape.setScaleY(Double.parseDouble(tfTransSizeY.getText()));
            // DROPSHADOW
            Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
            ((DropShadow) shape.getEffect()).setColor(shadowColor);
            ((DropShadow) shape.getEffect()).setWidth(sldEffectShadow.getValue());
            ((DropShadow) shape.getEffect()).setHeight(sldEffectShadow1.getValue());
            ((DropShadow) shape.getEffect()).setOffsetX(sldEffectShadowOffsetX.getValue());
            ((DropShadow) shape.getEffect()).setOffsetY(sldEffectShadowOffsetY.getValue());


            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) shape.getEffect()).getInput();
            gl.setLevel(sldEffectGlowLevel.getValue());
            ((DropShadow) shape.getEffect()).setInput(gl);

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            bl.setThreshold(sldEffectBloomThreshold.getValue());
            gl.setInput(bl);

        } else if(currentObjSel instanceof Rectangle){

            Shape shape;
            shape = (Shape) currentObjSel;
            rotate = Double.parseDouble(tfShapeRotation.getText());
            shape.setRotate(rotate);
            shape.setFill(cpFillColor.getValue());
            shape.setStroke(cpBorderColor.getValue());
            shape.setStrokeWidth(sldBorderSize.getValue());

            shape.setLayoutX(Double.parseDouble(tfTransPosX.getText()));
            shape.setLayoutY(Double.parseDouble(tfTransPosY.getText()));


            ((Rectangle) shape).setWidth(Double.parseDouble(tfTransSizeX.getText()));
            ((Rectangle) shape).setHeight(Double.parseDouble(tfTransSizeY.getText()));
            ((Rectangle) shape).setArcWidth(sldCornerSize.getValue());
            ((Rectangle) shape).setArcHeight(sldCornerSize.getValue());

            // DROPSHADOW
            Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
            ((DropShadow) shape.getEffect()).setColor(shadowColor);
            ((DropShadow) shape.getEffect()).setWidth(sldEffectShadow.getValue());
            ((DropShadow) shape.getEffect()).setHeight(sldEffectShadow1.getValue());
            ((DropShadow) shape.getEffect()).setOffsetX(sldEffectShadowOffsetX.getValue());
            ((DropShadow) shape.getEffect()).setOffsetY(sldEffectShadowOffsetY.getValue());

            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) shape.getEffect()).getInput();
            gl.setLevel(sldEffectGlowLevel.getValue());
            ((DropShadow) shape.getEffect()).setInput(gl);

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            bl.setThreshold(sldEffectBloomThreshold.getValue());
            gl.setInput(bl);

        } else if(currentObjSel instanceof ImageView){
            ImageView shape;


            shape = (ImageView) currentObjSel;

            rotate = Double.parseDouble(tfShapeRotation.getText());
            shape.setRotate(rotate);

            shape.setLayoutX(Double.parseDouble(tfTransPosX.getText()));
            shape.setLayoutY(Double.parseDouble(tfTransPosY.getText()));
            shape.setFitWidth(Double.parseDouble(tfTransSizeX.getText()));
            shape.setFitHeight(Double.parseDouble(tfTransSizeY.getText()));

            shape.setStyle("-fx-image-radius: 15px;");

            // DROPSHADOW
            Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
            ((DropShadow) shape.getEffect()).setColor(shadowColor);
            ((DropShadow) shape.getEffect()).setWidth(sldEffectShadow.getValue());
            ((DropShadow) shape.getEffect()).setHeight(sldEffectShadow1.getValue());
            ((DropShadow) shape.getEffect()).setOffsetX(sldEffectShadowOffsetX.getValue());
            ((DropShadow) shape.getEffect()).setOffsetY(sldEffectShadowOffsetY.getValue());

            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) shape.getEffect()).getInput();
            gl.setLevel(sldEffectGlowLevel.getValue());
            ((DropShadow) shape.getEffect()).setInput(gl);

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            bl.setThreshold(sldEffectBloomThreshold.getValue());
            gl.setInput(bl);



            shape.setPreserveRatio(cbxImageRatio.isSelected());
        } else if (currentObjSel instanceof Group){
            Group g = (Group) currentObjSel;

            for (Node obj:g.getChildren()) {
                if(obj instanceof CubicCurve){
                    ((CubicCurve) obj).setStroke(cpBorderColor.getValue());
                    ((CubicCurve) obj).setStrokeWidth(sldBorderSize.getValue());
                }
            }

            rotate = Double.parseDouble(tfShapeRotation.getText());
            g.setRotate(rotate);

            g.setLayoutX(Double.parseDouble(tfTransPosX.getText()));
            g.setLayoutY(Double.parseDouble(tfTransPosY.getText()));

            // DROPSHADOW
            Color shadowColor = new Color(cpEffectShadowColor.getValue().getRed(),cpEffectShadowColor.getValue().getGreen(),cpEffectShadowColor.getValue().getBlue(),cpEffectShadowColor.getValue().getOpacity());
            ((DropShadow) g.getEffect()).setColor(shadowColor);
            ((DropShadow) g.getEffect()).setWidth(sldEffectShadow.getValue());
            ((DropShadow) g.getEffect()).setHeight(sldEffectShadow1.getValue());
            ((DropShadow) g.getEffect()).setOffsetX(sldEffectShadowOffsetX.getValue());
            ((DropShadow) g.getEffect()).setOffsetY(sldEffectShadowOffsetY.getValue());

            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) g.getEffect()).getInput();
            gl.setLevel(sldEffectGlowLevel.getValue());
            ((DropShadow) g.getEffect()).setInput(gl);

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            bl.setThreshold(sldEffectBloomThreshold.getValue());
            gl.setInput(bl);


        }
    }

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
            //fc.setInitialDirectory(new File("/home"));
            fc.setInitialFileName("project");
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG File", "*.png"));
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG File", "*.jpg"));
            try{
                File selectedFile = fc.showSaveDialog(new Stage());
                if(selectedFile != null){
                    Rectangle clip = new Rectangle();
                    clip.setWidth(mainPane.getWidth());
                    clip.setHeight(mainPane.getHeight());
                    mainPane.setClip(clip);
                    Image snapshot = mainPane.snapshot(null, null);

                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null), "png", selectedFile);
                }
            } catch (Exception e){

            }

            mainPane.setClip(null);
        });
    }

    public void newProject(){
        Double w, h;
        w = Double.parseDouble(tfCanvaW.getText());
        h = Double.parseDouble(tfCanvaH.getText());

        mainPane.setPrefSize(w,h);
        mainPane.getChildren().clear();
        mainPane.setBackground(new Background(new BackgroundFill(Color.WHITE,  CornerRadii.EMPTY,  Insets.EMPTY)));
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY,
                new BorderWidths(1));
        Border border = new Border(borderStroke);

        mainPane.setBorder(border);
        cbLayer.getItems().clear();

        mainPane.getChildren().add(rec);

    }

    public void resetCanvaSize(){
        Double w, h;
        w = Double.parseDouble(tfCanvaW.getText());
        h = Double.parseDouble(tfCanvaH.getText());

        sldVertical.setPrefWidth(40);
        sldVertical.setPrefHeight(10000);

        sldVertical.setMin(1);
        sldVertical.setMax(10000);

        sldVertical.setShowTickLabels(true);
        sldVertical.setShowTickMarks(true);
        sldVertical.setSnapToTicks(false);
        sldVertical.setMajorTickUnit(100);
        sldVertical.setMinorTickCount(1);
        sldVertical.setBlockIncrement(100);
        sldVertical.setBlendMode(BlendMode.LIGHTEN);

        sldHorizontal.setMin(1);
        sldHorizontal.setMax(10000);

        sldHorizontal.setPrefWidth(10000);
        sldHorizontal.setPrefHeight(40);

        sldHorizontal.setBlendMode(BlendMode.LIGHTEN);
        sldHorizontal.setShowTickLabels(true);
        sldHorizontal.setShowTickMarks(true);
        sldHorizontal.setSnapToTicks(false);
        sldHorizontal.setMajorTickUnit(100);
        sldHorizontal.setMinorTickCount(1);
        sldHorizontal.setBlockIncrement(100);
        
        mainPane.setPrefSize(w,h);
    }


    public void zoomCanva(){
        zoomPanel.setScaleX(sldZoom.getValue());
        zoomPanel.setScaleY(sldZoom.getValue());;
        spAnchor.setPrefHeight(10000 * sldZoom.getValue());
        spAnchor.setPrefWidth(10000 * sldZoom.getValue());
    }

    public void aboutWindow(){
        credits = new Credits();
        try {
            credits.start(new Stage());
        } catch (Exception ex) {
            Logger.getLogger(PikSixController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateToolsValues(Object obj){
       // int itemLayer = (int) cbLayer.getSelectionModel().getSelectedItem();
        Double rotate;

        if(obj instanceof Text){
            Text shape;
            shape = (Text) obj;
            tfShapeRotation.setText("" + shape.getRotate());
            tfTextString.setText(shape.getText());
            tfFontSize.setText("" + shape.getFont().getSize());
            cbbFontStyle.setValue(shape.getFont().getName());
            tfTransPosX.setText("" + shape.getLayoutX());
            tfTransPosY.setText("" + shape.getLayoutY());
            tfTransSizeX.setText("" + shape.getScaleX());
            tfTransSizeY.setText("" + shape.getScaleY());
            System.out.println(shape.getFill());

            Color c = (Color) shape.getFill();
            String hex = String.format( "#%02X%02X%02X",
                    (int)( c.getRed() * 255 ),
                    (int)( c.getGreen() * 255 ),
                    (int)( c.getBlue() * 255 ) );
            currentObjSel = shape;

            //DROP SHADOW EFFECT
            sldEffectShadow.setValue(((DropShadow) shape.getEffect()).getWidth());
            sldEffectShadow1.setValue(((DropShadow) shape.getEffect()).getHeight());
            cpEffectShadowColor.setValue(((DropShadow) shape.getEffect()).getColor());
            sldEffectShadowOffsetX.setValue(((DropShadow) shape.getEffect()).getOffsetX());
            sldEffectShadowOffsetY.setValue(((DropShadow) shape.getEffect()).getOffsetY());

            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) shape.getEffect()).getInput();
            sldEffectGlowLevel.setValue(gl.getLevel());

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            sldEffectBloomThreshold.setValue(bl.getThreshold());

            cpTextColor.setValue(Color.web(hex));
        } else if(obj instanceof Rectangle){
            Shape shape;
            shape = (Shape) obj;
            rotate = Double.parseDouble(tfShapeRotation.getText());
            tfShapeRotation.setText("" + shape.getRotate());
            sldBorderSize.setValue(shape.getStrokeWidth());
            tfTransPosX.setText("" + shape.getLayoutX());
            tfTransPosY.setText("" + shape.getLayoutY());
            tfTransSizeX.setText("" + ((Rectangle) shape).getWidth());
            tfTransSizeY.setText("" + ((Rectangle) shape).getHeight());
            sldCornerSize.setValue(((Rectangle) shape).getArcHeight());

            Color cFill = (Color) shape.getFill();
            String hexFill = String.format( "#%02X%02X%02X",
                    (int)( cFill.getRed() * 255 ),
                    (int)( cFill.getGreen() * 255 ),
                    (int)( cFill.getBlue() * 255 ) );

            Color cBorder = (Color) shape.getStroke();
            String hexBorder = String.format( "#%02X%02X%02X",
                    (int)( cBorder.getRed() * 255 ),
                    (int)( cBorder.getGreen() * 255 ),
                    (int)( cBorder.getBlue() * 255 ) );

            currentObjSel = shape;
            //String colorWeb = shape.getFill().toString();
            //Color c = new Color(Color.he(colorWeb));

            cpFillColor.setValue(Color.web(hexFill));
            cpBorderColor.setValue(Color.web(hexBorder));

            //DROP SHADOW EFFECT
            sldEffectShadow.setValue(((DropShadow) shape.getEffect()).getWidth());
            sldEffectShadow1.setValue(((DropShadow) shape.getEffect()).getHeight());
            cpEffectShadowColor.setValue(((DropShadow) shape.getEffect()).getColor());
            sldEffectShadowOffsetX.setValue(((DropShadow) shape.getEffect()).getOffsetX());
            sldEffectShadowOffsetY.setValue(((DropShadow) shape.getEffect()).getOffsetY());

            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) shape.getEffect()).getInput();
            sldEffectGlowLevel.setValue(gl.getLevel());

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            sldEffectBloomThreshold.setValue(bl.getThreshold());

        } else if(obj instanceof ImageView){
            ImageView shape;

            shape = (ImageView) obj;

            rotate = Double.parseDouble(tfShapeRotation.getText());
            tfShapeRotation.setText("" + shape.getRotate());
            tfTransPosX.setText("" + shape.getLayoutX());
            tfTransPosY.setText("" + shape.getLayoutY());

            tfTransSizeX.setText("" + shape.getFitWidth());
            tfTransSizeY.setText("" + shape.getFitHeight());

            cbxImageRatio.setSelected(shape.isPreserveRatio());
            currentObjSel = shape;

            //DROP SHADOW EFFECT
            sldEffectShadow.setValue(((DropShadow) shape.getEffect()).getWidth());
            sldEffectShadow1.setValue(((DropShadow) shape.getEffect()).getHeight());
            cpEffectShadowColor.setValue(((DropShadow) shape.getEffect()).getColor());
            sldEffectShadowOffsetX.setValue(((DropShadow) shape.getEffect()).getOffsetX());
            sldEffectShadowOffsetY.setValue(((DropShadow) shape.getEffect()).getOffsetY());

            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) shape.getEffect()).getInput();
            sldEffectGlowLevel.setValue(gl.getLevel());

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            sldEffectBloomThreshold.setValue(bl.getThreshold());


        } else if(obj instanceof Group){
            Group g = (Group) currentObjSel;
            for (Node ob:g.getChildren()) {
                if(ob instanceof CubicCurve){
                    sldBorderSize.setValue(((CubicCurve) ob).getStrokeWidth());
                    Color cFill = (Color) ((CubicCurve) ob).getStroke();
                    String hexFill = String.format( "#%02X%02X%02X",
                            (int)( cFill.getRed() * 255 ),
                            (int)( cFill.getGreen() * 255 ),
                            (int)( cFill.getBlue() * 255 ) );
                    cpBorderColor.setValue(Color.web(hexFill));
                }
            }

            tfShapeRotation.setText("" + g.getRotate());
            tfTransPosX.setText("" + g.getLayoutX());
            tfTransPosY.setText("" + g.getLayoutY());

            //DROP SHADOW EFFECT
            sldEffectShadow.setValue(((DropShadow) g.getEffect()).getWidth());
            sldEffectShadow1.setValue(((DropShadow) g.getEffect()).getHeight());
            cpEffectShadowColor.setValue(((DropShadow) g.getEffect()).getColor());
            sldEffectShadowOffsetX.setValue(((DropShadow) g.getEffect()).getOffsetX());
            sldEffectShadowOffsetY.setValue(((DropShadow) g.getEffect()).getOffsetY());

            //GLOW EFFECT
            Glow gl = (Glow) ((DropShadow) g.getEffect()).getInput();
            sldEffectGlowLevel.setValue(gl.getLevel());

            //BLOOM EFFECT
            Bloom bl = (Bloom) gl.getInput();
            sldEffectBloomThreshold.setValue(bl.getThreshold());

        }else {
            currentObjSel = null;
        }
    }

    private CubicCurve createCurve(PikSixController.Anchor from, double x2, double y2, Group group) {
        double x1 = from.getCenterX(), y1 = from.getCenterY();
        double distance = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
        CubicCurve curve = new CubicCurve();
        curve.setStartX(x1);
        curve.setStartY(y1);
        curve.setControlX1(x1 + 20 * (x2 - x1) / distance);
        curve.setControlY1(y1 + 20 * (y2 - y1) / distance);
        curve.setControlX2(x2 - 20 * (x2 - x1) / distance);
        curve.setControlY2(y2 - 20 * (y2 - y1) / distance);
        curve.setEndX(x2);
        curve.setEndY(y2);
        curve.setFill(Color.TRANSPARENT);
        curve.setStroke(cpBorderColor.getValue());
        curve.setStrokeWidth(sldBorderSize.getValue());
        curve.setStrokeLineCap(StrokeLineCap.ROUND);
        curve.startXProperty().bind(from.centerXProperty());
        curve.startYProperty().bind(from.centerYProperty());
        Line controlLine1 = new PikSixController.ControlLine(curve.controlX1Property(), curve.controlY1Property(), curve.startXProperty(), curve.startYProperty());
        Line controlLine2 = new PikSixController.ControlLine(curve.controlX2Property(), curve.controlY2Property(), curve.endXProperty(), curve.endYProperty());
        PikSixController.Anchor control1 = new PikSixController.Anchor(Color.FORESTGREEN, curve.controlX1Property(), curve.controlY1Property(), 10);
        PikSixController.Anchor control2 = new PikSixController.Anchor(Color.FORESTGREEN, curve.controlX2Property(), curve.controlY2Property(), 10);
        group.getChildren().addAll(curve, control1, control2, controlLine1, controlLine2);
        return curve;
    }

    class ControlLine extends Line {
        ControlLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
            startXProperty().bind(startX);
            startYProperty().bind(startY);
            endXProperty().bind(endX);
            endYProperty().bind(endY);
            setStrokeWidth(2);
            setStroke(Color.FORESTGREEN.deriveColor(0, 1, 1, 0.5));
        }
    }

    // a draggable anchor displayed around a point.
    class Anchor extends Circle {
        Anchor(Color color, DoubleProperty x, DoubleProperty y, double radius) {
            super(x.get(), y.get(), 20);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);
            x.bind(centerXProperty());
            y.bind(centerYProperty());
            enableDrag();
        }

        Anchor(Color color, double x, double y, double radius) {
            super(x, y, 20);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);
            enableDrag();
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag() {
            final PikSixController.Anchor.Delta dragDelta = new PikSixController.Anchor.Delta();
            setOnMousePressed(mouseEvent -> {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getCenterX() - mouseEvent.getX();
                dragDelta.y = getCenterY() - mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
            });
            setOnMouseReleased(mouseEvent -> getScene().setCursor(Cursor.HAND));
            setOnMouseDragged(mouseEvent -> {
                double newX = mouseEvent.getX() + dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth()) {
                    setCenterX(newX);
                }
                double newY = mouseEvent.getY() + dragDelta.y;
                if (newY > 0 && newY < getScene().getHeight()) {
                    setCenterY(newY);
                }
            });
            setOnMouseEntered(mouseEvent -> {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.HAND);
                }
            });
            setOnMouseExited(mouseEvent -> {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.DEFAULT);
                }
            });
        }

        // records relative x and y co-ordinates.
        private class Delta {
            double x, y;
        }

    }
}


