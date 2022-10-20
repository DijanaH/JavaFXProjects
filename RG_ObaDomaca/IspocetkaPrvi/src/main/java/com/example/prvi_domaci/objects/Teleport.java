package com.example.prvi_domaci.objects;

import com.example.prvi_domaci.Main;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

import java.util.List;

public class Teleport extends Circle {

    public Teleport(double v, double fenceWidth, double width, double height, double playerHeight, IceandMud iceandMuds[], Obstacle obstacles[], Hole holes[], List<Coin> coins, Teleport teleport) {
        super(v);
        Stop[] stops = new Stop[]{new Stop(1, Color.YELLOW), new Stop(0.5, Color.CYAN), new Stop(0, Color.PURPLE)};
        RadialGradient rg = new RadialGradient(0, 0, 0.5, 0.5, 0.2, true, CycleMethod.REFLECT, stops);

        super.setFill(rg);

        double x = 0;
        double y = 0;

        boolean a = true;

        while (a) {
            a = false;
            x = Math.random() * (width - 2 * fenceWidth - 2 * v) + fenceWidth + v;
            y = Math.random() * (height - 2 * fenceWidth - 2 * v-playerHeight) + fenceWidth + v;

            for (int i = 0; i < obstacles.length; i++) {
                if (obstacles[i].collisionCircleCoord(x, y, v)) {
                    a = true;
                }
            }
            for (int i = 0; i < holes.length; i++) {
                if (holes[i].handleCollisioninGeneral(x, y, v)) {
                    a = true;
                }
            }
            for (int i = 0; i < iceandMuds.length; i++) {
                if (iceandMuds[i].collisionCircleCoord(x, y, v)) {
                    a = true;
                }
            }
            for (int i = 0; i < coins.size(); i++) {
                if (coins.get(i).handleCollisioninGeneral(x, y, v)) {
                    a = true;
                }
            }
            if (teleport != null) {
                if (teleport.handleCollisioninGeneral(x, y, v)) {
                    a = true;
                }
            }
        }
        super.getTransforms().addAll(new Translate(x, y));
    }


    public Translate handleCollision(Ball ball, Teleport teleport2) {

        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds holeBounds = super.getBoundsInParent();

        double holeX = holeBounds.getCenterX();
        double holeY = holeBounds.getCenterY();
        double holeRadius = super.getRadius();

        double distanceX = holeX - ballX;
        double distanceY = holeY - ballY;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = distanceSquared < (holeRadius * holeRadius);

        if (result) {
            Bounds teleport2Bounds = teleport2.getBoundsInParent();

            double t2X = teleport2Bounds.getCenterX();
            double t2Y = teleport2Bounds.getCenterY();
            double t2Rad = teleport2.getRadius();

            return new Translate(t2X,t2Y);
        }

        return null;
    }

    ;

    public boolean handleCollisioninGeneral(double x, double y, double rad) {

        Bounds holeBounds = super.getBoundsInParent();

        double holeX = holeBounds.getCenterX();
        double holeY = holeBounds.getCenterY();
        double holeRadius = super.getRadius();

        double distanceX = holeX - x;
        double distanceY = holeY - y;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = distanceSquared < ((holeRadius + rad) * (holeRadius + rad));

        return result;
    }

    ;


}
