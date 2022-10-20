package com.example.prvi_domaci;

import com.example.prvi_domaci.objects.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements EventHandler<MouseEvent> {
    //videti da li ostaviti public
    public static final double WINDOW_WIDTH = 600;
    public static final double WINDOW_HEIGHT = 800;

    private static final double FENCE_WIDTH = 20;
    private static final double SPEED_INDICATOR_WIDTH = 10;
    private static final double SPEED_INDICATOR_HEIGHT = WINDOW_HEIGHT - 20;
    private static final double OBSTACLE_WIDTH = 15;
    private static final double OBSTACLE_HEIGHT = WINDOW_HEIGHT * 0.17;

    private static final double PLAYER_WIDTH = 20;
    private static final double PLAYER_HEIGHT = 80;
    private static final double PLAYER_MAX_ANGLE_OFFSET = 60;
    private static final double PLAYER_MIN_ANGLE_OFFSET = -60;

    private static final double MS_IN_S = 1e3;
    private static final double NS_IN_S = 1e9;
    private static final double MAXIMUM_HOLD_IN_S = 3;
    private static final double MAXIMUM_BALL_SPEED1 = 1500;
    private static final double MAXIMUM_BALL_SPEED2 = 1300;
    private static final double MAXIMUM_BALL_SPEED3 = 1700;

    private static final double BALL_RADIUS = Main.PLAYER_WIDTH / 4;
    private static final double BALL_DAMP_FACTOR = 0.995;
    private static final double MIN_BALL_SPEED = 1;


    private static final double HOLE_RADIUS = 3 * BALL_RADIUS;
    private static final double LIVE_RADIUS = Main.PLAYER_WIDTH / 3;

    private static final int COIN_POINT = 2;

    private static final Integer STARTTIME = 120;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
    private Rectangle timeRepresentation;


    private Group root0;
    private ToggleButton teren1;
    private ToggleButton teren2;
    private ToggleButton teren3;
    private Scene scene0;
    private StackPane root1;
    private ToggleButton top1;
    private ToggleButton top2;
    private ToggleButton top3;
    private Scene scene1;


    private Group root;
    private Player player;
    private Ball ball;
    private long time;
    private Hole holes[];
    private Obstacle obstacles[];
    private IceandMud iceandMuds[];
    private List<Coin> coins = new ArrayList<>();
    private List<Live> lives;
    private Teleport teleport1;
    private Teleport teleport2;
    private List<FlyingMonster> flyingMonsters = new ArrayList<>();
    private SpeedIndicator speedind;
    private boolean isInHole = false;
    private int numbOfLives = 5;
    private Text points;
    private int holeColl;
    private int hole_level_points = 0;
    private Timeline timelineRepr;
    private double tmax = 120;

    private int teren;
    private int top;

    private boolean prvi = false;

    private void setTimer() {
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.RED);
        timerLabel.setStyle("-fx-font-size: 1em;");
        timerLabel.getTransforms().add(new Translate(260, 0));

        Rectangle beaty1 = new Rectangle(484, 12);
        beaty1.setFill(Color.BLACK);
        beaty1.getTransforms().addAll(new Translate(20, 2));

        timeRepresentation = new Rectangle(480, 10);
        timeRepresentation.getTransforms().addAll(new Translate(22, 3));
        timeRepresentation.setFill(Color.CYAN);


        this.root.getChildren().addAll(beaty1, timeRepresentation, timerLabel);
    }

    private void generateCoins() {
        int rand = (int)(Math.random()*3);
        Coin coin = new Coin(rand,7, FENCE_WIDTH, WINDOW_WIDTH, WINDOW_HEIGHT, iceandMuds, obstacles, holes, coins,flyingMonsters);
        this.root.getChildren().addAll(coin);
        coins.add(coin);
    }

    private void coinPointsRealization(int tip) {

        switch (tip) {
            case 0: //dodavanje poena
                String p = points.getText().toString();
                int pp = Integer.parseInt(p) + COIN_POINT;
                points.setText(Integer.toString(pp));
                break;
            case 2:
                //dodavanje zivota
                Live l = null;
                if (numbOfLives > 0) {
                    l = new Live(LIVE_RADIUS, new Translate(this.lives.get(numbOfLives - 1).getBoundsInParent().getCenterX() - 2 * LIVE_RADIUS - 5, (FENCE_WIDTH - LIVE_RADIUS * 2) / 2 + LIVE_RADIUS));
                } else {
                    l = new Live(LIVE_RADIUS, new Translate(WINDOW_WIDTH - 15 , (FENCE_WIDTH - LIVE_RADIUS * 2) / 2 + LIVE_RADIUS));
                }
                this.root.getChildren().addAll(l);
                this.lives.add(l);
                numbOfLives++;
                break;
            case 1:

                //ZAAA VREEMEMEM
                //int time = timeSeconds.intValue() + 30;
                timeline.stop();
                int t = this.timeSeconds.intValue();
                t += 30;
                timeSeconds.set(t);
                timeline = new Timeline();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(timeSeconds.intValue() + 1),
                                new KeyValue(timeSeconds, 0)));
                timeline.playFromStart();

                double bla = timeSeconds.intValue() * 480. / tmax;

                timelineRepr.stop();
                this.root.getChildren().remove(timeRepresentation);
                timeRepresentation = null;

                if (bla > 480) {
                    timeRepresentation = new Rectangle(480, 10);
                    tmax = timeSeconds.intValue();
                } else {
                    timeRepresentation = new Rectangle(bla, 10);
                }
                timeRepresentation.getTransforms().addAll(new Translate(22, 3));
                timeRepresentation.setFill(Color.CYAN);

                this.root.getChildren().addAll(timeRepresentation);

                Scale scale = new Scale(1, 1);

                timelineRepr = new Timeline(
                        new KeyFrame(Duration.seconds(0), new KeyValue(scale.xProperty(), 1, Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(timeSeconds.intValue()), new KeyValue(scale.xProperty(), 0, Interpolator.LINEAR))

                );
                timeRepresentation.getTransforms().addAll(scale);
                timelineRepr.play();
                tmax = timeSeconds.intValue();


                this.root.getChildren().remove(timerLabel);
                this.root.getChildren().add(timerLabel);
                break;
        }

    }

    private void eliminateFirstCoin() {
        if (!coins.isEmpty()) {
            this.root.getChildren().remove(coins.get(0));
            coins.remove(0);
        }
    }

    private void addTeleport() {
        Teleport t1 = new Teleport(15, FENCE_WIDTH, WINDOW_WIDTH, WINDOW_HEIGHT, PLAYER_HEIGHT, iceandMuds, obstacles, holes, coins, null);
        this.teleport1 = t1;
        this.root.getChildren().addAll(teleport1);

        Teleport t2 = new Teleport(15, FENCE_WIDTH, WINDOW_WIDTH, WINDOW_HEIGHT, PLAYER_HEIGHT, iceandMuds, obstacles, holes, coins, teleport1);
        this.teleport2 = t2;
        this.root.getChildren().addAll(teleport2);
    }


    private void addFlyingMonster() {

        for (int i = 0; i < 4; i++) {
            double x = Math.random() * 100 - 50;
            double y = Math.random() * 100 - 50;
            FlyingMonster fl = new FlyingMonster(7, new Point2D(x, y), FENCE_WIDTH, WINDOW_WIDTH, WINDOW_HEIGHT, iceandMuds, obstacles, holes, coins, teleport1, teleport2);
            this.root.getChildren().addAll(fl);
            flyingMonsters.add(fl);
        }
    }

    private void addFourHoles() {
        Translate hole0Position = new Translate(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.1
        );
        Hole hole0 = new Hole(Main.HOLE_RADIUS, hole0Position, 3);
        this.root.getChildren().addAll(hole0);

        Translate hole1Position = new Translate(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.4
        );
        Hole hole1 = new Hole(Main.HOLE_RADIUS, hole1Position, 1);
        this.root.getChildren().addAll(hole1);

        Translate hole2Position = new Translate(
                Main.WINDOW_WIDTH / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Hole hole2 = new Hole(Main.HOLE_RADIUS, hole2Position, 2);
        this.root.getChildren().addAll(hole2);

        Translate hole3Position = new Translate(
                Main.WINDOW_WIDTH * 2 / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Hole hole3 = new Hole(Main.HOLE_RADIUS, hole3Position, 2);
        this.root.getChildren().addAll(hole3);

        this.holes = new Hole[]{
                hole0,
                hole1,
                hole2,
                hole3,
        };
    }

    private void addThreeObstacles() {

        Translate obstacle0Position = new Translate(
                Main.WINDOW_WIDTH / 2 - OBSTACLE_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.165
        );
        Obstacle obstacle0 = new Obstacle(OBSTACLE_WIDTH, OBSTACLE_HEIGHT, obstacle0Position, new Rotate(0), false);
        this.root.getChildren().addAll(obstacle0);


        Translate obstacle1Position = new Translate(
                Main.WINDOW_WIDTH / 3 + OBSTACLE_HEIGHT / 2,
                Main.WINDOW_HEIGHT / 2 - OBSTACLE_WIDTH / 2
        );

        Obstacle obstacle1 = new Obstacle(OBSTACLE_WIDTH, OBSTACLE_HEIGHT, obstacle1Position, new Rotate(90), true);
        this.root.getChildren().addAll(obstacle1);


        Translate obstacle2Position = new Translate(
                2 * Main.WINDOW_WIDTH / 3 + OBSTACLE_HEIGHT / 2,
                Main.WINDOW_HEIGHT / 2 - OBSTACLE_WIDTH / 2
        );

        Obstacle obstacle2 = new Obstacle(OBSTACLE_WIDTH, OBSTACLE_HEIGHT, obstacle2Position, new Rotate(90), true);
        this.root.getChildren().addAll(obstacle2);

        this.obstacles = new Obstacle[]{
                obstacle0,
                obstacle1,
                obstacle2
        };

    }

    private void addIceandMud() {

        Translate mud0position = new Translate(
                0.2 * WINDOW_WIDTH - HOLE_RADIUS,
                0.15 * WINDOW_HEIGHT
        );
        IceandMud mud0 = new IceandMud(2 * HOLE_RADIUS, mud0position, false);


        Translate ice0position = new Translate(
                0.8 * WINDOW_WIDTH - HOLE_RADIUS,
                0.15 * WINDOW_HEIGHT
        );
        IceandMud ice0 = new IceandMud(2 * HOLE_RADIUS, ice0position, true);

        Translate ice1position = new Translate(
                0.2 * WINDOW_WIDTH - HOLE_RADIUS,
                0.65 * WINDOW_HEIGHT
        );
        IceandMud ice1 = new IceandMud(2 * HOLE_RADIUS, ice1position, true);

        Translate mud1position = new Translate(
                0.8 * WINDOW_WIDTH - HOLE_RADIUS,
                0.65 * WINDOW_HEIGHT
        );
        IceandMud mud1 = new IceandMud(2 * HOLE_RADIUS, mud1position, false);

        this.root.getChildren().addAll(mud0, ice0, mud1, ice1);

        this.iceandMuds = new IceandMud[]{
                mud0,
                ice0,
                mud1,
                ice1

        };

    }


    private void addLives() {
        this.lives = new ArrayList<>();

        for (int i = 0; i < numbOfLives; i++) {
            Live live = new Live(LIVE_RADIUS, new Translate(WINDOW_WIDTH - 15 - i * 5 - i * LIVE_RADIUS * 2, (FENCE_WIDTH - LIVE_RADIUS * 2) / 2 + LIVE_RADIUS));
            this.root.getChildren().addAll(live);
            this.lives.add(live);
        }

    }

    private void setFirstScene() {
        root0 = new Group();

        Text name = new Text("GOLFER");
        name.getTransforms().addAll(new Translate(WINDOW_WIDTH / 2.5, WINDOW_HEIGHT / 9));
        name.setStyle("-fx-font-size: 3em");
        name.setFill(Color.RED);


        Text choose = new Text("Izaberi teren:");
        choose.getTransforms().addAll(new Translate(WINDOW_WIDTH / 3 - choose.getBoundsInParent().getWidth(), WINDOW_HEIGHT / 5));
        choose.setStyle("-fx-font-size: 2em;");

        teren1 = new ToggleButton("TRAVA");
        teren2 = new ToggleButton("PESAK");
        teren3 = new ToggleButton("DRVO");

        teren1.getTransforms().addAll(new Translate(WINDOW_WIDTH / 4 - 20, WINDOW_HEIGHT / 4));
        teren2.getTransforms().addAll(new Translate(2 * WINDOW_WIDTH / 4 - 20, WINDOW_HEIGHT / 4));
        teren3.getTransforms().addAll(new Translate(3 * WINDOW_WIDTH / 4 - 20, WINDOW_HEIGHT / 4));
        root0.getChildren().addAll(teren1, teren2, teren3, choose, name);

        Image golferImg = new Image(Main.class.getClassLoader().getResourceAsStream("golfer.jpg"));
        ImagePattern golferBackground = new ImagePattern(golferImg);

        scene0 = new Scene(root0, Main.WINDOW_WIDTH, WINDOW_HEIGHT, golferBackground);

    }

    private void setSecondScene() {
        root1 = new StackPane();
        Text text = new Text("Izaberi top:");
        text.setFont(Font.font ("Verdana", 20));
        top1 = new ToggleButton("srednji");
        top2 = new ToggleButton("spori");
        top3 = new ToggleButton("brzi");

        root1.getChildren().addAll(top1, top2, top3,text);
        top1.getTransforms().addAll(new Translate(0, -WINDOW_HEIGHT/4));
        top2.getTransforms().addAll(new Translate(0, 0));
        top3.getTransforms().addAll(new Translate(0, WINDOW_HEIGHT/4));
        StackPane.setAlignment(text, Pos.TOP_CENTER);

        root1.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        this.scene1 = new Scene(root1, Main.WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void addPlayer() {
        Translate playerPosition = new Translate(
                Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2,
                Main.WINDOW_HEIGHT - Main.PLAYER_HEIGHT
        );
        this.player = new Player(
                Main.PLAYER_WIDTH,
                Main.PLAYER_HEIGHT,
                playerPosition,
                top
        );
        this.root.getChildren().addAll(this.player);
    }


    @Override
    public void start(Stage stage) throws IOException {
        this.root = new Group();

        Image grassImg = new Image(Main.class.getClassLoader().getResourceAsStream("grasss.jpg"));
        ImagePattern grassBackground = new ImagePattern(grassImg);

        Image sandImg = new Image(Main.class.getClassLoader().getResourceAsStream("sand.jpg"));
        ImagePattern sandBackground = new ImagePattern(sandImg);

        Image woodImg = new Image(Main.class.getClassLoader().getResourceAsStream("woodd.jpg"));
        ImagePattern woodBackground = new ImagePattern(woodImg);

        Image topSrednjiImg = new Image(Main.class.getClassLoader().getResourceAsStream("tops.jpg"));
        ImageView topSrednji = new ImageView(topSrednjiImg);

        Image topSporiiImg = new Image(Main.class.getClassLoader().getResourceAsStream("spori.jpg"));
        ImageView topSpori = new ImageView(topSporiiImg);

        Image topBrziImg = new Image(Main.class.getClassLoader().getResourceAsStream("brzi.jpg"));
        ImageView topBrzi = new ImageView(topBrziImg);

        Scene scene = new Scene(this.root, Main.WINDOW_WIDTH, WINDOW_HEIGHT, grassBackground);
        //
        this.setFirstScene();
        //
        this.setSecondScene();

        //

        top1.setGraphic(topSrednji);
        top2.setGraphic(topSpori);
        top3.setGraphic(topBrzi);

        top1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    stage.setScene(scene);
                    top = 1;
                    this.addPlayer();
                }
        );
        top2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    stage.setScene(scene);
                    top = 2;
                    this.addPlayer();

                }
        );
        top3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    stage.setScene(scene);
                    top = 3;
                    this.addPlayer();
                }
        );

        teren1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    stage.setScene(scene1);
                    scene.setFill(grassBackground);
                }
        );
        teren2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    stage.setScene(scene1);
                    scene.setFill(sandBackground);
                    ;
                }
        );
        teren3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    stage.setScene(scene1);
                    scene.setFill(woodBackground);
                }
        );


        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        //System.out.println("Spaceee ");
                        this.root.getChildren().remove(this.ball);
                        this.ball = null;

                    }
                }
        );
/*
        Translate playerPosition = new Translate(
                Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2,
                Main.WINDOW_HEIGHT - Main.PLAYER_HEIGHT
        );

        this.player = new Player(
                Main.PLAYER_WIDTH,
                Main.PLAYER_HEIGHT,
                playerPosition,
                top
        );*/

        this.root.getChildren().addAll(new Fence(FENCE_WIDTH));
        //      this.root.getChildren().addAll(this.player);

        this.addFourHoles();
        this.addThreeObstacles();
        this.addIceandMud();
        this.addLives();
        this.setTimer();
        this.addTeleport();
        this.addFlyingMonster();

        this.points = new Text("0");
        points.getTransforms().addAll(new Translate(0, 15), new Scale(1.3, 1.6));
        this.root.getChildren().addAll(points);

        timeSeconds.addListener(observable -> {
            if (timeSeconds.intValue() % 5 == 0) {
                this.generateCoins();
            }
            if (timeSeconds.intValue() % 20 == 0) {
                this.eliminateFirstCoin();
            }
        });

        scene.addEventHandler(
                MouseEvent.MOUSE_MOVED,
                mouseEvent -> this.player.handleMouseMoved(
                        mouseEvent,
                        Main.PLAYER_MIN_ANGLE_OFFSET,
                        Main.PLAYER_MAX_ANGLE_OFFSET
                )
        );

        scene.addEventHandler(MouseEvent.ANY, this);

        Timer timer = new Timer(

                deltaNanoseconds -> {

                    double deltaSeconds = (double) deltaNanoseconds / Main.NS_IN_S;

                    for (int i = 0; i < flyingMonsters.size(); i++) {
                        flyingMonsters.get(i).update(deltaSeconds, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, FENCE_WIDTH, obstacles, holes, iceandMuds, teleport1, teleport2,coins);
                    }

                    if (this.ball != null) {
                        //isInHole = Arrays.stream(this.holes).anyMatch(hole -> hole.handleCollision(hole_level_points,points,this.ball, this.ball.getSpeed()));

                        for (int i = 0; i < holes.length; i++) {
                            this.hole_level_points = holes[i].handleCollision(hole_level_points, points, this.ball, this.ball.getSpeed());
                            if (this.hole_level_points != 0) break;
                        }

                        //boolean isCollision = Arrays.stream(this.obstacles).anyMatch(obstacle -> obstacle.handleCollision(this.ball));

                        boolean stopped = this.ball.update(
                                teleport1,
                                teleport2,
                                iceandMuds,
                                obstacles,
                                hole_level_points,
                                deltaSeconds,
                                0,
                                Main.WINDOW_WIDTH,
                                0,
                                Main.WINDOW_HEIGHT,
                                Main.FENCE_WIDTH,
                                Main.BALL_DAMP_FACTOR,
                                Main.MIN_BALL_SPEED
                        );
                        boolean eaten = false;

                        for (int j = 0; j < flyingMonsters.size(); j++) {
                            eaten = flyingMonsters.get(j).handleCollision(this.ball);
                            if (eaten) break;
                        }

                        if (eaten) {
                            this.root.getChildren().remove(this.ball);
                            this.ball = null;
                        }

                        //boolean isInHole = Arrays.stream ( this.holes ).anyMatch ( hole -> hole.handleCollision ( this.ball , this.ball.getSpeed()) );
                        boolean coinPoints = false;

                        int tipNovcica = 0;
                        for (Coin c : coins
                        ) {
                            coinPoints = c.handleCollisionPoints(this.root, this.coins, this.ball);
                            if (coinPoints) {
                                tipNovcica = c.getTip();
                                break;
                            }
                        }

                        if (coinPoints) {
                            this.coinPointsRealization(tipNovcica);
                        }
                        if (numbOfLives == 0) {
                            timeline.stop();
                            timelineRepr.stop();
                        }

                        if (stopped) {

                            this.root.getChildren().remove(this.ball);
                            this.ball = null;
                            //  if (numbOfLives == 0) ;
                        }

                        if (this.hole_level_points != 0 && this.ball != null && !prvi) {

                            prvi = true;
                            Scale ballScale = new Scale(1, 1);

                            this.ball.getTransforms().addAll(ballScale);

                            Timeline tlBallReducing = new Timeline(
                                    new KeyFrame(Duration.seconds(0), new KeyValue(ballScale.xProperty(), 1, Interpolator.LINEAR)),
                                    new KeyFrame(Duration.seconds(2), new KeyValue(ballScale.xProperty(), 0, Interpolator.LINEAR)),
                                    new KeyFrame(Duration.seconds(0), new KeyValue(ballScale.yProperty(), 1, Interpolator.LINEAR)),
                                    new KeyFrame(Duration.seconds(2), new KeyValue(ballScale.yProperty(), 0, Interpolator.LINEAR))

                            );

                            tlBallReducing.setCycleCount(1);
                            tlBallReducing.play();

                            tlBallReducing.setOnFinished(actionEvent -> {
                                this.root.getChildren().remove(this.ball);
                                this.ball = null;
                                if (hole_level_points != 0) {
                                    String p = this.points.getText();
                                    int a = Integer.parseInt(p);
                                    hole_level_points += Integer.parseInt(p);
                                    points.setText(Integer.toString(hole_level_points));
                                    hole_level_points = 0;
                                }
                            });


                        }


                    }
                }
        );
        timer.start();

        scene.setCursor(Cursor.NONE);

        stage.setTitle("Golfer");
        stage.setResizable(false);
        stage.setScene(scene0);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED) && mouseEvent.isPrimaryButtonDown()) {
            this.time = System.currentTimeMillis();

            speedind = new SpeedIndicator(SPEED_INDICATOR_WIDTH, SPEED_INDICATOR_HEIGHT, new Translate(FENCE_WIDTH - SPEED_INDICATOR_WIDTH + (FENCE_WIDTH - SPEED_INDICATOR_WIDTH) / 2, SPEED_INDICATOR_HEIGHT + (WINDOW_HEIGHT - SPEED_INDICATOR_HEIGHT) / 2));

            if (this.ball == null && numbOfLives > 0 && timeSeconds.intValue() > 0) {

                root.getChildren().add(speedind);

                speedind.updateSpeedInd(MAXIMUM_HOLD_IN_S);


            }


        } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            root.getChildren().remove(speedind);

            if (this.time != -1 && this.ball == null && numbOfLives > 0 && timeSeconds.intValue() > 0) {

                //vreme drzanja misa u sekundama
                double value = (System.currentTimeMillis() - this.time) / Main.MS_IN_S;
                //vreme u granicama
                double deltaSeconds = Utilities.clamp(value, 0, Main.MAXIMUM_HOLD_IN_S);

                double ballSpeedFactor = 0;
                switch (top) {
                    case 1:
                        ballSpeedFactor = deltaSeconds / Main.MAXIMUM_HOLD_IN_S * Main.MAXIMUM_BALL_SPEED1;
                        break;
                    case 2:
                        ballSpeedFactor = deltaSeconds / Main.MAXIMUM_HOLD_IN_S * Main.MAXIMUM_BALL_SPEED2;
                        break;
                    case 3:
                        ballSpeedFactor = deltaSeconds / Main.MAXIMUM_HOLD_IN_S * Main.MAXIMUM_BALL_SPEED3;
                        break;
                }

                Translate ballPosition = this.player.getBallPosition();
                Point2D ballSpeed = this.player.getSpeed().multiply(ballSpeedFactor);

                prvi = false;
                this.ball = new Ball(Main.BALL_RADIUS, ballPosition, ballSpeed);
                this.root.getChildren().addAll(this.ball);

                if (numbOfLives == 5) {
                    if (timeline != null) {
                        timeline.stop();
                    }
                    timeSeconds.set(STARTTIME);
                    timeline = new Timeline();
                    timeline.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(STARTTIME + 1),
                                    new KeyValue(timeSeconds, 0)));
                    timeline.playFromStart();

                    Scale scale = new Scale(1, 1);
                    timeRepresentation.getTransforms().addAll(scale);

                    timelineRepr = new Timeline(
                            new KeyFrame(Duration.seconds(0), new KeyValue(scale.xProperty(), 1, Interpolator.LINEAR)),
                            new KeyFrame(Duration.seconds(STARTTIME), new KeyValue(scale.xProperty(), 0, Interpolator.LINEAR))

                    );
                    timelineRepr.play();
                }

                if (numbOfLives >= 1) {
                    this.root.getChildren().remove(this.lives.get(numbOfLives - 1));
                    this.lives.remove(numbOfLives - 1);
                    numbOfLives--;
                }
            }
            this.time = -1;
        }
    }
}