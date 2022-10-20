package com.example.prvi_domaci.objects;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import java.util.List;

public class Coin extends Circle {
    int tip = 0;

    public int getTip() {
        return tip;
    }

    public Coin(int tip, double v, double fenceWidth, double width, double height, IceandMud iceandMuds[], Obstacle obstacles[], Hole holes[], List<Coin> coins, List<FlyingMonster> flyingMonsters) {
        super(v);
        Stop[] stops = new Stop[]{new Stop(1, Color.ORANGE), new Stop(0, Color.YELLOW)};
        RadialGradient rg = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);

        this.tip = tip;
        switch (tip) {
            case 0:
                break;
            case 1:
                stops = new Stop[]{new Stop(1, Color.BLUE), new Stop(0, Color.AZURE)};
                rg = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);
                break;
            case 2:
                stops = new Stop[]{new Stop(1, Color.RED), new Stop(0, Color.ORANGE)};
                rg = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);
                break;
        }

        super.setFill(rg);

        double x = 0;
        double y = 0;

        boolean a = true;

        while (a) {
            a = false;
            x = Math.random() * (width - 2 * fenceWidth - 2 * v) + fenceWidth + v;
            y = Math.random() * (height - 2 * fenceWidth - 2 * v) + fenceWidth + v;

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
            for (int i = 0; i < flyingMonsters.size(); i++) {
                if (flyingMonsters.get(i).handleCollisioninGeneral(x, y, v)) {
                    a = true;
                }
            }

        }

        super.getTransforms().addAll(new Translate(x, y));


    }

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

    public boolean handleCollisionPoints(Group root, List<Coin> coins, Ball ball) {

        if (ball != null) {


            double x = ball.getBoundsInParent().getCenterX();
            double y = ball.getBoundsInParent().getCenterY();
            double rad = ball.getRadius();

            Bounds holeBounds = super.getBoundsInParent();

            double holeX = holeBounds.getCenterX();
            double holeY = holeBounds.getCenterY();
            double holeRadius = super.getRadius();

            double distanceX = holeX - x;
            double distanceY = holeY - y;

            double distanceSquared = distanceX * distanceX + distanceY * distanceY;

            boolean result = distanceSquared < ((holeRadius + rad) * (holeRadius + rad));

            if (result) {
                coins.remove(this);
                root.getChildren().remove(this);
            }

            return result;
        }
        return false;
    }
}
