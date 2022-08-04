package com.example.diceroller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import org.fxyz3d.importers.Importer3D;

import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

public class HelloController {

    private static final int MODEL_SCALE_FACTOR = 50;

    Scene scene;
    @FXML
    SubScene diceScene;

    PerspectiveCamera camera;
    AmbientLight light;

    public VBox rootBox;
    Group dieModel;
    public AnchorPane diceRoot;

    public Button alf;
    public Button bet;
    public Button gam;

    int count = 0;
    AtomicReference<Double> alfNum = new AtomicReference<>((double) 0);
    AtomicReference<Double> betNum = new AtomicReference<>((double) 0);
    AtomicReference<Double> gamNum = new AtomicReference<>((double) 0);

    public void initialize() {

        diceRoot = new AnchorPane();
        Platform.runLater(() -> {

            setScene();

            for (Node node: diceRoot.getChildren()){
                System.out.println(node);
            }

        });
        alf.setOnAction(actionEvent -> matrixRotateNode(dieModel, alfNum.updateAndGet(v -> (double) (v + 10)), betNum.get(), gamNum.get()));
        bet.setOnAction(actionEvent -> matrixRotateNode(dieModel, alfNum.get(), betNum.updateAndGet(v -> (double) (v + 10)), gamNum.get()));
        gam.setOnAction(actionEvent -> matrixRotateNode(dieModel, alfNum.get(), betNum.get(), gamNum.updateAndGet(v -> (double) (v + 10))));


    }

    private void setScene() {

        getModel();

        scene = rootBox.getScene();

        diceScene = new SubScene(diceRoot, 400, 400, true, SceneAntialiasing.BALANCED);
        diceRoot.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null,null)));
        diceScene.setFill(Color.LAVENDER);
        System.out.println(diceScene.getRoot());

        diceScene.widthProperty().bind(rootBox.widthProperty());
        diceScene.heightProperty().bind(diceRoot.heightProperty());
        diceScene.setPickOnBounds(true);
        diceRoot.getChildren().add(dieModel);
        rootBox.getChildren().add(0, diceScene);

        setCamera();
        setLighting();
    }

    public void getModel(){

        try {
            URL fileURL = Paths.get("src/main/resources/com/example/diceroller/Dice_Six.obj").toUri().toURL();
            dieModel = Importer3D.loadAsPoly(fileURL).getRoot();
        } catch (Exception e) {
            e.printStackTrace();
            dieModel = null;
        }

        dieModel.setScaleX(MODEL_SCALE_FACTOR);
        dieModel.setScaleY(MODEL_SCALE_FACTOR);
        dieModel.setScaleZ(MODEL_SCALE_FACTOR);

        dieModel.setTranslateX(250);
        dieModel.setTranslateY(150);

    }

    public void setCamera(){
        camera = new PerspectiveCamera(false);
        camera.setNearClip(.01);
        camera.setFarClip(10000.0);
        //camera.setTranslateX(-100);
        //camera.setTranslateZ(-100);
        //camera.setFieldOfView(200);
        //camera.setTranslateX((diceRoot.getBoundsInLocal().getMaxX() + diceRoot.getBoundsInLocal().getMinX()) / 2d);
        //camera.setTranslateY((diceRoot.getBoundsInLocal().getMaxY() + diceRoot.getBoundsInLocal().getMinY()) / 2d);
        //double max = Math.max(diceRoot.getBoundsInParent().getWidth(), diceRoot.getBoundsInParent().getHeight());
        //System.out.println(max);

        diceScene.setCamera(camera);
    }

    public void setLighting(){
        light = new AmbientLight(Color.WHITE);
        diceRoot.getChildren().add(light);
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ() / 10);
    }

//TODO SOURCE https://stackoverflow.com/questions/30145414/rotate-a-3d-object-on-3-axis-in-javafx-properly
    private void matrixRotateNode(Node n, double alf, double bet, double gam){
        double A11=Math.cos(alf)*Math.cos(gam);
        double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A21=-Math.cos(gam)*Math.sin(alf);
        double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A31=Math.sin(gam);
        double A32=-Math.cos(gam)*Math.sin(bet);
        double A33=Math.cos(bet)*Math.cos(gam);

        double d = Math.acos((A11+A22+A33-1d)/2d);
        if(d!=0d){
            double den=2d*Math.sin(d);
            Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
            n.setRotationAxis(p);
            n.setRotate(Math.toDegrees(d));
        }
    }
}