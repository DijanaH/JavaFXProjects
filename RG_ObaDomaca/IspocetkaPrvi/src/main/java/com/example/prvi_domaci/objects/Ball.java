package com.example.prvi_domaci.objects;

import com.example.prvi_domaci.Utilities;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.Arrays;

public class Ball extends Circle {
    private Translate position;
    private Point2D speed;
    private boolean uteleportu = false;


    public Ball(double radius, Translate position, Point2D speed) {
        super(radius, Color.RED);

        this.position = position;
        this.speed = speed;

        super.getTransforms().addAll(this.position);


    }

    public Translate getPosition() {
        return position;
    }

    public void setPosition(Translate position) {
        this.position = position;
    }

    public Point2D getSpeed() {
        return speed;
    }

    public void setSpeed(Point2D speed) {
        this.speed = speed;
    }

    public boolean update(Teleport teleport1, Teleport teleport2, IceandMud[] iceandMuds, Obstacle[] obstacles, int hole_level_points, double ds, double left, double right, double top, double bottom, double fenceWidth, double dampFactor, double minBallSpeed) {

        boolean result = false;

        double newX = this.position.getX();
        double newY = this.position.getY();

        if (hole_level_points == 0) {
            newX += this.speed.getX() * ds;
            newY += this.speed.getY() * ds;
        }


        Translate t1 = teleport2.handleCollision(this, teleport1);
        Translate t2 = teleport1.handleCollision(this, teleport2);

        if (t1 != null && !uteleportu) {
            this.position.setX(t1.getX());
            this.position.setY(t1.getY());
            uteleportu = true;
            return false;
        } else if (t2 != null && !uteleportu) {
            this.position.setX(t2.getX());
            this.position.setY(t2.getY());
            uteleportu = true;
            return false;
        }


        if (t1 == null && t2 == null) {
            uteleportu = false;
        }




        double radius = super.getRadius();

        double minX = left + radius + fenceWidth;
        double maxX = right - radius - fenceWidth;
        double minY = top + radius + fenceWidth;
        double maxY = bottom - radius - fenceWidth;

        for (int j = 0; j < iceandMuds.length; j++) {
            iceandMuds[j].collision(this);
        }

        for (int i = 0; i < obstacles.length; i++) {
            Bounds obstacleBounds = obstacles[i].getBoundsInParent();


            double obstacleMinX = obstacleBounds.getMinX();
            double obstacleMaxX = obstacleBounds.getMaxX();
            double obstacleMinY = obstacleBounds.getMinY();
            double obstacleMaxY = obstacleBounds.getMaxY();
            if (position.getX() + getRadius() >= obstacleMinX && position.getX() - getRadius() <= obstacleMaxX && position.getY() + getRadius() >= obstacleMinY && position.getY() - getRadius() <= obstacleMaxY) {


                if (position.getY() > obstacleMaxY || position.getY() < obstacleMinY /*|| (position.getY()>obstacleMinY && position.getY()<obstacleMaxX && position.getX()<obstacleMaxX-2*getRadius() && position.getX()>minX+2*getRadius())*/) {

                    this.setSpeed(new Point2D(this.speed.getX(), -this.speed.getY()));

                } else {

                    this.setSpeed(new Point2D(-this.speed.getX(), this.speed.getY()));

                }
            } else {
                this.position.setX(Utilities.clamp(newX, minX, maxX));
                this.position.setY(Utilities.clamp(newY, minY, maxY));

                if (newX < minX || newX > maxX) {
                    this.speed = new Point2D(-this.speed.getX(), this.speed.getY());
                }

                if (newY < minY || newY > maxY) {
                    this.speed = new Point2D(this.speed.getX(), -this.speed.getY());
                }
            }
        }


        this.speed = this.speed.multiply(dampFactor);

        double ballSpeed = this.speed.magnitude();

        if (ballSpeed < minBallSpeed) {
            result = true;
        }

        return result;
    }

    public Timeline fallingInHole() {
        Scale ballScale = new Scale(1, 1);

        super.getTransforms().addAll(ballScale);

        Timeline tlBallReducingX = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(ballScale.xProperty(), 0.1, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(2), new KeyValue(ballScale.xProperty(), 1, Interpolator.LINEAR))
        );
        Timeline tlBallReducingY = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(ballScale.yProperty(), 1, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(2), new KeyValue(ballScale.yProperty(), 0, Interpolator.LINEAR))
        );

        tlBallReducingX.setCycleCount(Animation.INDEFINITE);
        tlBallReducingX.play();

        tlBallReducingY.setCycleCount(2);
        tlBallReducingY.play();

        return tlBallReducingY;

    }

}
