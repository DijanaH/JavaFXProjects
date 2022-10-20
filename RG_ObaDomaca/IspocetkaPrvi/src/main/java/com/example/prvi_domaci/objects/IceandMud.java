package com.example.prvi_domaci.objects;

import com.example.prvi_domaci.Utilities;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class IceandMud extends Rectangle {

    private static final double BALL_MUD_FACTOR = 0.950;
    private static final double BALL_ICE_FACTOR = 1.050;

    private boolean ice;

    public IceandMud(double a, Translate position, boolean ice) {
        super(a, a);
        this.ice = ice;
        super.getTransforms().addAll(position);
        if (ice) {
            super.setFill(Color.LIGHTBLUE);
        } else {
            super.setFill(Color.DARKRED);
        }
    }

    public void collision(Ball ball) {
        Bounds obstacleBounds = this.getBoundsInParent();

        double obstacleMinX = obstacleBounds.getMinX();
        double obstacleMaxX = obstacleBounds.getMaxX();
        double obstacleMinY = obstacleBounds.getMinY();
        double obstacleMaxY = obstacleBounds.getMaxY();

        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        if (ballX + ballRadius >= obstacleMinX && ballX - ballRadius <= obstacleMaxX && ballY + ballRadius >= obstacleMinY && ballY - ballRadius <= obstacleMaxY) {
            Point2D ballSpeed = ball.getSpeed();
            Point2D newSpeed;
            if (ice) {
                newSpeed = ballSpeed.multiply(BALL_ICE_FACTOR);
            } else {
                newSpeed = ballSpeed.multiply(BALL_MUD_FACTOR);
            }
            ball.setSpeed(newSpeed);

        }
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
