package com.example.prvi_domaci.objects;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Obstacle extends Rectangle {

    private boolean horizontal;

    public Obstacle(double width, double height, Transform position, Rotate rotation, boolean horizontal) {
        super(width, height);
        super.setFill(Color.GRAY);
        super.getTransforms().addAll(position, rotation);
        this.horizontal = horizontal;

        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(2),new KeyValue(fillProperty(),Color.BLUE, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(4),new KeyValue(fillProperty(),Color.GREEN, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(6),new KeyValue(fillProperty(),Color.RED, Interpolator.LINEAR))
        );


        tl.setCycleCount(Animation.INDEFINITE);
        tl.setAutoReverse(true);
        tl.play();




    }

    public boolean handleCollision(Ball ball) {

        boolean collision= false;

        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds obstacleBounds = super.getBoundsInParent();

        double obstacleMinX = obstacleBounds.getMinX();
        double obstacleMaxX = obstacleBounds.getMaxX();
        double obstacleMinY = obstacleBounds.getMinY();
        double obstacleMaxY = obstacleBounds.getMaxY();


        if (ballX + ballRadius >= obstacleMinX && ballX - ballRadius <= obstacleMaxX && ballY + ballRadius >= obstacleMinY && ballY - ballRadius <= obstacleMaxY) {
            collision = true;

            if (ballY > obstacleMaxY || ballY < obstacleMinY) {
                ball.setSpeed(new Point2D(ball.getSpeed().getX(), -ball.getSpeed().getY()));

            } else {
               ball.setSpeed(new Point2D(-ball.getSpeed().getX(), ball.getSpeed().getY()));

            }
        }

        return collision;
    }


    public boolean collisionCircleCoord(double x, double y, double rad){
        Bounds obstacleBounds = this.getBoundsInParent();

        double obstacleMinX = obstacleBounds.getMinX();
        double obstacleMaxX = obstacleBounds.getMaxX();
        double obstacleMinY = obstacleBounds.getMinY();
        double obstacleMaxY = obstacleBounds.getMaxY();


        if (x + rad >= obstacleMinX && x - rad <= obstacleMaxX && y + rad >= obstacleMinY && y - rad <= obstacleMaxY) {
            return true;
        }
        return false;

    }
}
