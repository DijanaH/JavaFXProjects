package com.example.rgdomaci2;

import com.example.rgdomaci2.arena.*;
import com.example.rgdomaci2.timer.Timer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static final double WINDOW_WIDTH = 800;
    private static final double WINDOW_HEIGHT = 800;

    private static final double PODIUM_WIDTH = 2000;
    private static final double PODIUM_HEIGHT = 10;
    private static final double PODIUM_DEPTH = 2000;

    private static final double CAMERA_FAR_CLIP = 100000;
    private static final double CAMERA_Z = -5000;
    private static final double CAMERA_X_ANGLE = -45;

    private static final double BALL_RADIUS = 50;
    private static final double LAMP_DIM = 100;

    private static final double FENCE_WIDTH = 20;
    private static final double FENCE_HEIGHT = 2 * BALL_RADIUS;

    private static final double BALL_DAMP = 0.999;
    private static final double ARENA_DAMP = 0.995;

    private static final double MAX_ANGLE_OFFSET = 30;
    private static final double MAX_ACCELERATION = 400;

    private static final int NUMBER_OF_HOLES = 4;
    private static final double HOLE_RADIUS = 2 * Main.BALL_RADIUS;
    private static final double HOLE_HEIGHT = PODIUM_HEIGHT;

    private static final int MAX_NUMB_LIVES = 5;
    private static final int LIVE_RADIUS = 10;

    private Scene scene;
    private Group root;
    private Ball ball;
    private Arena arena;
    private List<Hole> holes  = new ArrayList<>();
    Group mainroot;
    private SubScene s1;
    private SubScene s2;
    private SubScene s3;
    private Group root3;
    //   private Scene scene;
    private Camera defaultCamera;
    private Camera birdViewCamera;
    private Fence fences[];
    private List<Coin> coins = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private Box lamp;
    private boolean lampOn = true;
    private PointLight pointLight;
    private PhongMaterial lampMat;

    private Translate scroll;
    private double x = 0, y = 0;
    private Rotate dragX;
    private Rotate dragY;
    private Rotate dragXx;
    private Rotate dragYy;

    private Text points;

    private Line speedLine;

    private Button ball0;
    private Button ball1;
    private Button ball2;
    private int choosenBall;

    private Button court0;
    private Button court1;
    private Button court2;
    private int choosenCourt;


    private void createBall(){

        Translate ballPosition = new Translate();

        switch (choosenCourt){
            case 0:{
                ballPosition = new Translate(
                        -(Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS),
                        -(Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2),
                        Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS
                );break;
            }
            case 1:{
                ballPosition = new Translate(
                        -(Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS),
                        -(Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2),
                        0
                );break;
            }
            case 2:{
                ballPosition = new Translate(
                        (Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS),
                        -(Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2),
                        Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS
                );break;
            }
        }


        birdViewCamera = new PerspectiveCamera(true);
        birdViewCamera.setFarClip(CAMERA_FAR_CLIP);
        birdViewCamera.getTransforms().addAll(
                new Translate(0, -1500, 0),
                ballPosition,
                new Rotate(-90, Rotate.X_AXIS)
        );



        this.ball = new Ball(Main.BALL_RADIUS,  ballPosition,choosenBall);
        this.arena.getChildren().add(this.ball);
    }


    private Scene chooseBallScene(Stage stage){
        Text mainText = new Text("Izaberi loptu:");

        ball0 = new Button("Spora");
        ball1 = new Button("Srednja");
        ball2 = new Button("Brza");

        Image imgBall0 = new Image(this.getClass().getClassLoader().getResourceAsStream("ball0.jpg"),100, 100, false, false);
        Image imgBall1 = new Image(this.getClass().getClassLoader().getResourceAsStream("ball1.jpg"),100, 100, false, false);
        Image imgBall2 = new Image(this.getClass().getClassLoader().getResourceAsStream("ball2.jpg"),100, 100, false, false);

        ball0.setGraphic(new ImageView(imgBall0));
        ball1.setGraphic(new ImageView(imgBall1));
        ball2.setGraphic(new ImageView(imgBall2));

        ball0.setFont(Font.font("Verdana", 20));
        ball1.setFont(Font.font("Verdana", 20));
        ball2.setFont(Font.font("Verdana", 20));
        mainText.setFont(Font.font("Verdana", 30));


        mainText.setFill(Color.ORANGE);

        ball0.setTextAlignment(TextAlignment.CENTER);
        ball1.setTextAlignment(TextAlignment.CENTER);
        ball2.setTextAlignment(TextAlignment.CENTER);
        mainText.setTextAlignment(TextAlignment.CENTER);


        StackPane root0 = new StackPane(ball0, ball1, ball2,mainText);
        StackPane.setAlignment(mainText,Pos.TOP_CENTER);
        ball0.getTransforms().addAll(new Translate(0,-WINDOW_HEIGHT/4));
        ball2.getTransforms().addAll(new Translate(0,WINDOW_HEIGHT/4));

        ball0.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            stage.setScene(this.chooseCourtScene(stage));
            choosenBall = 0;
        });
        ball1.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            stage.setScene(this.chooseCourtScene(stage));
            choosenBall = 1;
        });
        ball2.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            stage.setScene(this.chooseCourtScene(stage));
            choosenBall = 2;
        });


        Image backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("backgroun.jpg"));
        Scene scene0 = new Scene(root0,WINDOW_WIDTH,WINDOW_HEIGHT);
        root0.setBackground(new Background(new BackgroundFill(new ImagePattern(backgroundImage), CornerRadii.EMPTY, Insets.EMPTY)));
        return scene0;
    }

    private Scene chooseCourtScene(Stage stage){
        Text mainText = new Text("Izaberi teren:");

        court0 = new Button();
        court1 = new Button();
        court2 = new Button();


        Image imgCourt0 = new Image(this.getClass().getClassLoader().getResourceAsStream("court0.jpg"),180, 180, false, false);
        Image imgCourt1 = new Image(this.getClass().getClassLoader().getResourceAsStream("court1.jpg"),180, 180, false, false);
        Image imgCourt2 = new Image(this.getClass().getClassLoader().getResourceAsStream("court2.jpg"),180, 180, false, false);

        court0.setGraphic(new ImageView(imgCourt0));
        court1.setGraphic(new ImageView(imgCourt1));
        court2.setGraphic(new ImageView(imgCourt2));

       // court0.setFont(Font.font("Verdana", 20));
       // court1.setFont(Font.font("Verdana", 20));
       // court2.setFont(Font.font("Verdana", 20));
        mainText.setFont(Font.font("Verdana", 30));

        mainText.setFill(Color.ORANGE);

        court0.setTextAlignment(TextAlignment.CENTER);
        court1.setTextAlignment(TextAlignment.CENTER);
        court2.setTextAlignment(TextAlignment.CENTER);
        mainText.setTextAlignment(TextAlignment.CENTER);


        StackPane root0 = new StackPane(court0,court1,court2,mainText);
        StackPane.setAlignment(mainText,Pos.TOP_CENTER);
        court0.getTransforms().addAll(new Translate(0,-WINDOW_HEIGHT/4));
        court2.getTransforms().addAll(new Translate(0,WINDOW_HEIGHT/4));

        court0.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            choosenCourt = 0;
            createBall();
            this.addHoles();
            this.addObstacles();
            this.addCoins();
            stage.setScene(scene);

        });
        court1.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            choosenCourt = 1;
            createBall();
            this.addHoles();
            this.addObstacles();
            this.addCoins();
            stage.setScene(scene);
        });
        court2.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            choosenCourt = 2;
            createBall();
            this.addHoles();
            this.addObstacles();
            this.addCoins();
            stage.setScene(scene);
        });



        Image backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("backgroun.jpg"));
        Scene scene0 = new Scene(root0,WINDOW_WIDTH,WINDOW_HEIGHT);
        root0.setBackground(new Background(new BackgroundFill(new ImagePattern(backgroundImage), CornerRadii.EMPTY, Insets.EMPTY)));
        return scene0;
    }

    private void backToStart(){
        Translate ballPosition = new Translate();

        switch (choosenCourt){
            case 0:{
                ballPosition = new Translate(
                        -(Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS),
                        -(Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2),
                        Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS
                );break;
            }
            case 1:{
                ballPosition = new Translate(
                        -(Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS),
                        -(Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2),
                        0
                );break;
            }
            case 2:{
                ballPosition = new Translate(
                        (Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS),
                        -(Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2),
                        Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS
                );break;
            }
        }

        this.ball = new Ball(Main.BALL_RADIUS,ballPosition,choosenBall);
        this.arena.getChildren().add(this.ball);

        this.arena.update(0);

        birdViewCamera = null;
        birdViewCamera = new PerspectiveCamera(true);
        birdViewCamera.setFarClip(CAMERA_FAR_CLIP);
        birdViewCamera.getTransforms().addAll(
                new Translate(0, -1500, 0),
                ballPosition,
                new Rotate(-90, Rotate.X_AXIS)
        );
    }

    private void endOfGame(){
        this.arena.update(0);

        StackPane r5 = new StackPane();
        Text text = new Text("Kraj Igre");
        text.setFill(Color.DARKRED);
        text.setFont(Font.font("Verdana", 20));
        text.setTextAlignment(TextAlignment.CENTER);

        r5.getChildren().addAll(text);
        StackPane.setAlignment(text, Pos.CENTER);
        r5.setBackground(null);
        timeline.stop();

        SubScene s5 = new SubScene(r5, 150, 50,true,SceneAntialiasing.BALANCED);
        s5.getTransforms().addAll(new Translate(WINDOW_WIDTH/2-s5.getWidth()/2,WINDOW_HEIGHT/2-s5.getHeight()/2));
        //s5.setFill(Color.AZURE);
        mainroot.getChildren().addAll(s5);
    }

    private static final Integer STARTTIME = 120;
    private Timeline timeline = new Timeline();
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);


    private void setTimer(Stage stage) {
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.YELLOW);
        timerLabel.setStyle("-fx-font-size: 2em;");

        StackPane r = new StackPane(timerLabel);
        r.setBackground(null);
        SubScene subScene = new SubScene(r,50,50,true,SceneAntialiasing.BALANCED);
        subScene.getTransforms().addAll(new Translate(WINDOW_WIDTH/2 - subScene.getWidth()/2,0));

        mainroot.getChildren().addAll(subScene);
    }



    private Scale scaleSPP;
    private void addSpeedIndicator(){
        double a = 160;
        Rectangle rectangle = new Rectangle(a,a);
        rectangle.setFill(Color.GREEN);
        rectangle.setStroke(Color.RED);
        rectangle.setStrokeWidth(5);

        this.speedLine = new Line();
        this.speedLine.setStartX(a/2);
        this.speedLine.setEndX(a/2);
        this.speedLine.setStartY(a/2);
        this.speedLine.setEndY(a/2);

        scaleSPP = new Scale(1,1);
        speedLine.getTransforms().addAll(scaleSPP);

        speedLine.setStroke(Color.RED);
        speedLine.setStrokeWidth(2);

        Group r = new Group(rectangle,speedLine);


        SubScene speedIndSceen = new SubScene(r,a,a,true,SceneAntialiasing.BALANCED);
        speedIndSceen.getTransforms().addAll(new Translate(0,WINDOW_HEIGHT-speedIndSceen.getHeight()));
        mainroot.getChildren().addAll(speedIndSceen);



    }
    private double oldXAngle=0;
    private double oldZAngle=0;
    private boolean firstTime = true;

    private void handleSpeedInd(){

        if(oldZAngle==0){
            oldZAngle = arena.getZAngle();
        }
        if(oldXAngle==0){
            oldXAngle = arena.getXAngle();
        }/*
        if(firstTime && (oldXAngle==0 || oldXAngle==0 ) && (arena.getXAngle()!=0. || arena.getZAngle()!=0.)){

            timeSeconds.set(STARTTIME);
            timeline = new Timeline();
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(STARTTIME + 1),
                            new KeyValue(timeSeconds, 0)));
            timeline.playFromStart();
            firstTime = false;

        }*/

        double newXAngle = arena.getXAngle();
        double newZAngle = arena.getZAngle();

        double rotateX = newXAngle - oldXAngle;
        double rotateY = newZAngle - oldZAngle;

        oldZAngle = newZAngle;
        oldXAngle = newXAngle;

        speedLine.setEndX(speedLine.getEndX()+rotateY*4);
        speedLine.setEndY(speedLine.getEndY()+rotateX*4);

    }

    private void addLamp() {
        this.lamp = new Box(LAMP_DIM, LAMP_DIM, LAMP_DIM);
        lamp.getTransforms().addAll(
                new Translate(0, -1000, 0)
        );

        this.lampMat = new PhongMaterial();
        lampMat.setDiffuseColor(Color.GRAY);
      //  lampMat.setSpecularColor(Color.WHITE);
      //  lampMat.setSpecularPower(20);
       lampMat.setSelfIlluminationMap(new Image(this.getClass().getClassLoader().getResourceAsStream("self.png")));

        this.pointLight = new PointLight(Color.WHITE);

        pointLight.getTransforms().addAll(
                new Translate(0, -1000, 0)
        );

        lamp.setMaterial(lampMat);
        this.arena.getChildren().addAll(pointLight,lamp);
    }


    private void addCoins() {
        int coinwidth = 10;

        Translate pos0 = new Translate();
        Translate pos1 = new Translate();
        Translate pos2 = new Translate();
        Translate pos3 = new Translate();

        switch (choosenCourt){
            case 0:{
                pos0 = new Translate(0, -PODIUM_HEIGHT - 50 - BALL_RADIUS, -PODIUM_DEPTH / 4);
                pos1 = new Translate(PODIUM_WIDTH / 4, -PODIUM_HEIGHT - 50 - BALL_RADIUS, 0);
                pos2 = new Translate(0, -PODIUM_HEIGHT - 50 - BALL_RADIUS, PODIUM_DEPTH / 4);
                pos3 = new Translate(-PODIUM_WIDTH / 4, -PODIUM_HEIGHT - 50 - BALL_RADIUS, 0);
            }break;
            case 1:{
                pos0 = new Translate(PODIUM_WIDTH/6, -PODIUM_HEIGHT - 50 - BALL_RADIUS, -PODIUM_DEPTH / 8);
                pos1 = new Translate(-PODIUM_WIDTH/2.5, -PODIUM_HEIGHT - 50 - BALL_RADIUS, -PODIUM_DEPTH / 3);
                pos2 = new Translate(PODIUM_WIDTH/6, -PODIUM_HEIGHT - 50 - BALL_RADIUS, PODIUM_DEPTH / 8);
                pos3 = new Translate(-PODIUM_WIDTH/2.5, -PODIUM_HEIGHT - 50 - BALL_RADIUS, PODIUM_DEPTH / 3);
            }break;
            case 2:{
                pos0 = new Translate(PODIUM_WIDTH/3, -PODIUM_HEIGHT - 50 - BALL_RADIUS, -PODIUM_DEPTH / 3);
                pos1 = new Translate(-PODIUM_WIDTH/3, -PODIUM_HEIGHT - 50 - BALL_RADIUS, -PODIUM_DEPTH / 3);
                pos2 = new Translate(PODIUM_WIDTH/3, -PODIUM_HEIGHT - 50 - BALL_RADIUS, PODIUM_DEPTH / 3);
                pos3 = new Translate(-PODIUM_WIDTH/3, -PODIUM_HEIGHT - 50 - BALL_RADIUS, PODIUM_DEPTH / 3);
            }break;
        }

        Coin coin0 = new Coin(BALL_RADIUS, coinwidth, pos0);
        coin0.getTransforms().addAll(
                new Rotate(-90, Rotate.X_AXIS)
        );

        Coin coin1 = new Coin(BALL_RADIUS, coinwidth, pos1);
        coin1.getTransforms().addAll(
                new Rotate(-90, Rotate.X_AXIS)
        );

        Coin coin2 = new Coin(BALL_RADIUS, coinwidth, pos2);
        coin2.getTransforms().addAll(
                new Rotate(-90, Rotate.X_AXIS)
        );

        Coin coin3 = new Coin(BALL_RADIUS, coinwidth, pos3);
        coin3.getTransforms().addAll(
                new Rotate(-90, Rotate.X_AXIS)
        );

        arena.getChildren().addAll(coin0, coin1, coin2, coin3);
        coins.add(coin0);
        coins.add(coin1);
        coins.add(coin2);
        coins.add(coin3);


    }
    private void addObstacles(){
        double obsHeight = 200;
        double obsBase = 50;

        Translate pos0 = new Translate();
        Translate pos1 = new Translate();
        Translate pos2 = new Translate();
        Translate pos3 = new Translate();
        Translate pos4 = new Translate();
        Translate pos5 = new Translate();

        switch(choosenCourt){
            case 0:{
                pos0 = new Translate(-PODIUM_WIDTH/4, -PODIUM_HEIGHT- obsHeight/2, -PODIUM_DEPTH / 4);
                pos1 = new Translate(+PODIUM_WIDTH/4, -PODIUM_HEIGHT- obsHeight/2, -PODIUM_DEPTH / 4);
                pos2 = new Translate(-PODIUM_WIDTH/4, -PODIUM_HEIGHT - obsHeight/2, PODIUM_DEPTH / 4);
                pos3 = new Translate(PODIUM_WIDTH/4, -PODIUM_HEIGHT-obsHeight/2, PODIUM_DEPTH / 4);
                pos4 = new Translate(3*PODIUM_WIDTH/8, -PODIUM_HEIGHT-obsHeight/2, 0);
                pos5 = new Translate(-3*PODIUM_WIDTH/8, -PODIUM_HEIGHT-obsHeight/2, 0);
            }break;
            case 1:{
                pos0 = new Translate(PODIUM_WIDTH/2.5, -PODIUM_HEIGHT- obsHeight/2, -PODIUM_DEPTH / 3);
                pos1 = new Translate(+PODIUM_WIDTH/4, -PODIUM_HEIGHT- obsHeight/2, -PODIUM_DEPTH / 9);
                pos2 = new Translate(PODIUM_WIDTH/2.5, -PODIUM_HEIGHT - obsHeight/2, PODIUM_DEPTH / 3);
                pos3 = new Translate(PODIUM_WIDTH/4, -PODIUM_HEIGHT-obsHeight/2, PODIUM_DEPTH / 9);
                pos4 = new Translate(0, -PODIUM_HEIGHT-obsHeight/2, 0);
                pos5 = new Translate(PODIUM_WIDTH/4, -PODIUM_HEIGHT-obsHeight/2, 0);
            }break;
            case 2:{
                double r = WINDOW_WIDTH/3;
                double alfa = 360/6;
                pos0 = new Translate(r*Math.cos(Math.toRadians(0*alfa)), -PODIUM_HEIGHT- obsHeight/2, r*Math.sin(0*Math.toRadians(alfa)));
                pos1 = new Translate(r*Math.cos(Math.toRadians(1*alfa)), -PODIUM_HEIGHT- obsHeight/2, r*Math.sin(1*Math.toRadians(alfa)));
                pos2 = new Translate(r*Math.cos(Math.toRadians(2*alfa)), -PODIUM_HEIGHT - obsHeight/2,r*Math.sin(2*Math.toRadians(alfa)));
                pos3 = new Translate(r*Math.cos(Math.toRadians(3*alfa)), -PODIUM_HEIGHT-obsHeight/2, r*Math.sin(3*Math.toRadians(alfa)));
                pos4 = new Translate(r*Math.cos(Math.toRadians(4*alfa)), -PODIUM_HEIGHT-obsHeight/2, r*Math.sin(4*Math.toRadians(alfa)));
                pos5 = new Translate(r*Math.cos(Math.toRadians(5*alfa)), -PODIUM_HEIGHT-obsHeight/2, r*Math.sin(5*Math.toRadians(alfa)));
            }break;
        }

        Obstacle obs0 = new Obstacle(obsBase,obsHeight,100,false);
        obs0.getTransforms().addAll(
                pos0
        );

        Obstacle obs1 = new Obstacle(obsBase,obsHeight,100,false);
        obs1.getTransforms().addAll(
                pos1
        );

        Obstacle obs2 = new Obstacle(obsBase,obsHeight,100,false);
        obs2.getTransforms().addAll(
                pos2
        );

        Obstacle obs3 = new Obstacle(obsBase,obsHeight,100,false);
        obs3.getTransforms().addAll(
                pos3
        );

        Obstacle obs4 = new Obstacle(obsBase,obsHeight,100,true);
        obs4.getTransforms().addAll(
                pos4
        );

        Obstacle obs5 = new Obstacle(obsBase,obsHeight,100,true);
        obs5.getTransforms().addAll(
                pos5
        );



        obstacles.add(obs0);
        obstacles.add(obs1);
        obstacles.add(obs2);
        obstacles.add(obs3);
        obstacles.add(obs4);
        obstacles.add(obs5);
        arena.getChildren().addAll(obs0,obs1,obs2,obs3,obs4,obs5);


    }

    private void addPoints(int number) {
        String p = points.getText().toString();
        int pp = Integer.parseInt(p);
        pp += number;
        points.setText(Integer.toString(pp));
    }

    private List<Circle> lives = new ArrayList<>();
    private int numbOfLives = 0;

    private void addLives(){
        int s3width = LIVE_RADIUS*15;
        int s3height = 50;

        root3 = new Group();

        for(int i = 0; i<MAX_NUMB_LIVES;i++){
            Circle live = new Circle(LIVE_RADIUS);
            live.getTransforms().addAll(
                new Translate((i+1)*LIVE_RADIUS*2.3 ,LIVE_RADIUS*2)
            );
            live.setFill(Color.RED);
            lives.add(live);
            root3.getChildren().addAll(live);
            numbOfLives++;
        }


        this.s3 = new SubScene(root3,s3width,s3height,true,SceneAntialiasing.BALANCED);
        //s3.setFill(Color.AZURE);
    }

    private void addPontsText() {
        int s2width = 50;
        int s2height = 70;

        this.points = new Text("0");
        this.points.setFill(Color.RED);
        this.points.setFont(Font.font("Verdana", 20));
        this.points.setTextAlignment(TextAlignment.CENTER);

        StackPane r2 = new StackPane(points);
        StackPane.setAlignment(this.points,Pos.CENTER);
        r2.setBackground(null);
        /*
        this.points = new Text("0");

        double pointsWidthScale = s2width/points.getBoundsInParent().getWidth();
        double pointsHeightScale = s2height/points.getBoundsInParent().getHeight();

        points.getTransforms().addAll(new Translate(0,40),new Scale(pointsWidthScale,pointsHeightScale)); //nesto ga povecano pomera
        double xx = points.getBoundsInParent().getHeight();
      //  points.getTransforms().add(new Translate(0,100));
        points.setFill(Color.RED);


        Group r2 = new Group(points);
        */

        this.s2 = new SubScene(r2,s2width,s2height,true,SceneAntialiasing.BALANCED);
       // s2.setFill(Color.GREEN);
        s2.getTransforms().add(new Translate(WINDOW_WIDTH-s2width*2,0));


    }

    private void addHoles(){

        double x0=0;
        double z0=0;
        double x1=0;
        double z1=0;
        double x2=0;
        double z2=0;
        double x3=0;
        double z3=0;


        switch (choosenCourt){
            case 0:{
                 x0 = (Main.PODIUM_WIDTH / 2 - 2 * Main.HOLE_RADIUS);
                 z0 = -(Main.PODIUM_DEPTH / 2 - 2 * Main.HOLE_RADIUS);
                 x1 = (-(Main.PODIUM_WIDTH / 2 - 2 * Main.HOLE_RADIUS));
                 z1 = -(Main.PODIUM_DEPTH / 2 - 2 * Main.HOLE_RADIUS);
                 x2 = 0;
                 z2 = 0;
                 x3 = ((Main.PODIUM_WIDTH / 2 - 2 * Main.HOLE_RADIUS));
                 z3 = (Main.PODIUM_DEPTH / 2 - 2 * Main.HOLE_RADIUS);
            }break;
            case 1:{
                x0 = (Main.PODIUM_WIDTH / 2 - 2 * Main.HOLE_RADIUS);
                z0 = 0;
                x1 = 0;
                z1 = -(Main.PODIUM_DEPTH / 4 -  Main.HOLE_RADIUS);
                x2 = -(Main.PODIUM_DEPTH/4);
                z2 = 0;
                x3 = 0;
                z3 = (Main.PODIUM_DEPTH / 4 -  Main.HOLE_RADIUS);
            }break;
            case 2:{
                x0 = 0;
                z0 = 0;
                x1 = PODIUM_WIDTH/3*Math.sqrt(2)/2;
                z1 = PODIUM_DEPTH/3*Math.sqrt(2)/2;
                x2 = -PODIUM_WIDTH/3;
                z2 = 0;
                x3 = 0;
                z3 = -PODIUM_DEPTH/3;
            }break;
        }


        Translate holePosition0 = new Translate(x0, -30, z0);

        Hole hole0 = new Hole(
                Main.HOLE_RADIUS,
                Main.HOLE_HEIGHT,
                holePosition0,
                true
        );

        Translate holePosition1 = new Translate(x1, -30, z1);

        Hole hole1 = new Hole(
                Main.HOLE_RADIUS,
                Main.HOLE_HEIGHT,
                holePosition1,
                false
        );


        Translate holePosition2 = new Translate(x2, -30, z2);

        Hole hole2 = new Hole(
                Main.HOLE_RADIUS,
                Main.HOLE_HEIGHT,
                holePosition2,
                false
        );


        Translate holePosition3 = new Translate(x3, -30, z3);

        Hole hole3 = new Hole(
                Main.HOLE_RADIUS,
                Main.HOLE_HEIGHT,
                holePosition3,
                false
        );


        this.holes.add(hole0);
        this.holes.add(hole1);
        this.holes.add(hole2);
        this.holes.add(hole3);
        this.arena.getChildren().addAll(hole0,hole1,hole2,hole3);
    }


    private void addFence() {
        Translate position1 = new Translate(-PODIUM_WIDTH / 2 + FENCE_WIDTH / 2, -PODIUM_HEIGHT - FENCE_HEIGHT / 2, 0);
        Fence fence1 = new Fence(FENCE_WIDTH, FENCE_HEIGHT, PODIUM_WIDTH / 2, position1);

        Translate position2 = new Translate(PODIUM_WIDTH / 2 - FENCE_WIDTH / 2, -PODIUM_HEIGHT - FENCE_HEIGHT / 2, 0);
        Fence fence2 = new Fence(FENCE_WIDTH, FENCE_HEIGHT, PODIUM_WIDTH / 2, position2);

        Translate position3 = new Translate(0, -PODIUM_HEIGHT - FENCE_HEIGHT / 2, -PODIUM_DEPTH / 2 + FENCE_WIDTH / 2);
        Fence fence3 = new Fence(FENCE_WIDTH, FENCE_HEIGHT, PODIUM_WIDTH / 2, position3);
        fence3.getTransforms().addAll(new Rotate(-90, Rotate.Y_AXIS));

        Translate position4 = new Translate(0, -PODIUM_HEIGHT - FENCE_HEIGHT / 2, +PODIUM_DEPTH / 2 - FENCE_WIDTH / 2);
        Fence fence4 = new Fence(FENCE_WIDTH, FENCE_HEIGHT, PODIUM_WIDTH / 2, position4);
        fence4.getTransforms().addAll(new Rotate(-90, Rotate.Y_AXIS));

        arena.getChildren().addAll(fence1, fence2, fence3, fence4);

        fences = new Fence[]{
                fence1,
                fence2,
                fence3,
                fence4

        };
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.root = new Group();

        this.s1 = new SubScene(
                root,
                Main.WINDOW_WIDTH,
                Main.WINDOW_HEIGHT,true,SceneAntialiasing.BALANCED
        );

        Box podium = new Box(
                Main.PODIUM_WIDTH,
                Main.PODIUM_HEIGHT,
                Main.PODIUM_DEPTH
        );
        podium.setMaterial(new PhongMaterial(Color.BLUE));

        this.scroll = new Translate(0, 0, 0);
        this.dragX = new Rotate(0, Rotate.X_AXIS);
        this.dragY = new Rotate(0, Rotate.Y_AXIS);


        defaultCamera = new PerspectiveCamera(true);
        defaultCamera.setFarClip(Main.CAMERA_FAR_CLIP);
        defaultCamera.getTransforms().addAll(
                dragY,
                dragX,
                new Rotate(Main.CAMERA_X_ANGLE, Rotate.X_AXIS),
                new Translate(0, 0, CAMERA_Z),
                this.scroll
        );
        this.root.getChildren().add(defaultCamera);
        s1.setCamera(defaultCamera);

        //DEOOOOO za loptu
/*
        double x = (Main.PODIUM_WIDTH / 2 - 2 * Main.HOLE_RADIUS);
        double z = -(Main.PODIUM_DEPTH / 2 - 2 * Main.HOLE_RADIUS);

        Translate holePosition = new Translate(x, -30, z);
        Material holeMaterial = new PhongMaterial(Color.YELLOW);

        this.hole = new Hole(
                Main.HOLE_RADIUS,
                Main.HOLE_HEIGHT,
                holeMaterial,
                holePosition,
                true
        );*/


        this.arena = new Arena();
        this.arena.getChildren().add(podium);
        //addBall
        //this.addHoles();


        //this.addCoins();
        //this.addObstacles();
        this.addFence();

        this.addLamp();

        this.addPontsText();
        this.addLives();

        this.root.getChildren().add(this.arena);

        Timer timer = new Timer(
                deltaSeconds -> {
                    this.arena.update(ARENA_DAMP);

                    this.arena.startTime(timeSeconds,timeline,STARTTIME);


                    if (Main.this.ball != null) {
                        boolean outOfArena = Main.this.ball.update(
                                deltaSeconds,
                                Main.PODIUM_DEPTH / 2,
                                -Main.PODIUM_DEPTH / 2,
                                -Main.PODIUM_WIDTH / 2,
                                Main.PODIUM_WIDTH / 2,
                                this.arena.getXAngle(),
                                this.arena.getZAngle(),
                                Main.MAX_ANGLE_OFFSET,
                                Main.MAX_ACCELERATION,
                                Main.BALL_DAMP,
                                fences
                        );

                        if(timeSeconds.intValue()==0){
                            this.arena.getChildren().remove(this.ball);
                            Main.this.ball = null;
                            this.endOfGame();
                        }


                        this.handleSpeedInd();
                        boolean isInHole = false;
                        boolean good = false;

                        for(int i = 0; i<holes.size(); i++){
                            isInHole = holes.get(i).handleCollision(this.ball);
                            if(isInHole) {
                                good = holes.get(i).isGood();
                                break;
                            }
                        }


                        for(int i = 0; i<obstacles.size();i++) {
                            obstacles.get(i).handleCollision(this.ball);

                        }


                        for (int i = 0; i < coins.size(); i++) {
                            if (coins.get(i).handleCollision(this.ball)) {
                                addPoints(5);
                                this.arena.getChildren().remove(coins.get(i));
                                coins.remove(i);
                                break;
                            }
                        }

                        if (outOfArena || isInHole) {
                            this.arena.getChildren().remove(this.ball);
                            Main.this.ball = null;

                            this.root3.getChildren().remove(this.lives.get(numbOfLives-1));
                            this.lives.remove(--numbOfLives);
                            if(numbOfLives>0)
                                this.backToStart();
                            else
                                this.endOfGame();
                        }
                        if (isInHole) {
                            if(good){
                                addPoints(5);
                            }else{
                                addPoints(-5);
                            }

                        }
                    }
                }
        );
        timer.start();

        Image backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("backgroun.jpg"));

        mainroot = new Group(s1,s2,s3);

        this.addSpeedIndicator();
        this.setTimer(stage);

        scene = new Scene(mainroot,WINDOW_WIDTH,WINDOW_HEIGHT,true,
                SceneAntialiasing.BALANCED);

        scene.addEventHandler(KeyEvent.ANY, event -> this.arena.handleKeyEvent(event, Main.MAX_ANGLE_OFFSET));
        scene.addEventHandler(KeyEvent.ANY, this::handleKeyEvent);
        scene.addEventHandler(MouseEvent.ANY, this::handleMouseEvent);
        scene.addEventHandler(ScrollEvent.ANY, this::handleScrollEvent);

        scene.setFill(new ImagePattern(backgroundImage));
        stage.setTitle("Rolling Ball");
        //stage.setScene(scene);
        stage.setScene(this.chooseBallScene(stage));







        stage.show();
    }

    private void handleScrollEvent(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            this.scroll.setZ(this.scroll.getZ() + 15);
        } else {
            this.scroll.setZ(this.scroll.getZ() - 15);
        }
    }

    private void handleMouseEvent(MouseEvent event) {

        if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {

            if (x == 0) {
                x = event.getSceneX();
            }
            if (y == 0) {
                y = event.getSceneY();
            }

            double xx = event.getSceneX();
            double yy = event.getSceneY();
            System.out.println(xx + "," + yy);

            double factx = x < xx ? 1 : -1;
            double facty = y < yy ? 1 : -1;

            x = xx;
            y = yy;

            if (event.isPrimaryButtonDown()) {
                double angleX = dragX.getAngle() - facty * 0.5;
                this.dragX.setAngle(Utilities.clamp(angleX, -45, 45));
                this.dragY.setAngle(dragY.getAngle() - factx * 0.5);


            }
        }


    }

    private void handleKeyEvent(KeyEvent event) {
       // if(event.getCode().equals((KeyCode.)))
        if (event.getCode().equals(KeyCode.DIGIT1)) {
            this.s1.setCamera(defaultCamera);
        } else if (event.getCode().equals(KeyCode.DIGIT2)) {
            this.s1.setCamera(birdViewCamera);
        } else if (event.getEventType().equals(KeyEvent.KEY_PRESSED) && event.getCode().equals(KeyCode.DIGIT0)) {
            if (lampOn) {
                this.arena.getChildren().remove(pointLight);
                lampMat.setSelfIlluminationMap(null);

            } else {
                this.arena.getChildren().add(pointLight);
                lampMat.setSelfIlluminationMap(new Image(this.getClass().getClassLoader().getResourceAsStream("self.png")));
            }
            lampOn = !lampOn;

        }



        ///////////

        if ( event.getCode ( ).equals ( KeyCode.UP ) ) {

        } else if ( event.getCode ( ).equals ( KeyCode.DOWN ) ) {

        } else if ( event.getCode ( ).equals ( KeyCode.LEFT ) ) {
           // speedLine.setStartX(speedLine.getStartX()+1);
        } else if ( event.getCode ( ).equals ( KeyCode.RIGHT ) ) {

        }
    }


    public static void main(String[] args) {
        launch();
    }
}