//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.prvi_domaci.objects;

import com.example.prvi_domaci.Utilities;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class FlyingMonster extends Circle {
    private Translate position;
    private Point2D speed;
    private Integer signX = 1;
    private Integer signY = 1;
    boolean collided = false;

    public FlyingMonster(double v, Point2D speed, double fenceWidth, double width, double height, IceandMud[] iceandMuds, Obstacle[] obstacles, Hole[] holes, List<Coin> coins, Teleport teleport1, Teleport teleport2) {
        super(v);
        super.setFill(Color.PURPLE);
        this.speed = speed;
        double x = 0.0D;
        double y = 0.0D;
        boolean a = true;


        while (a) {
            a = false;
            x = Math.random() * (width - 2.0D * fenceWidth - 2.0D * v) + fenceWidth + v;
            y = Math.random() * (height - 2.0D * fenceWidth - 2.0D * v) + fenceWidth + v;

            int i;
            for (i = 0; i < obstacles.length; ++i) {
                if (obstacles[i].collisionCircleCoord(x, y, v)) {
                    a = true;
                }
            }

            for (i = 0; i < holes.length; ++i) {
                if (holes[i].handleCollisioninGeneral(x, y, v)) {
                    a = true;
                }
            }

            for (i = 0; i < iceandMuds.length; ++i) {
                if (iceandMuds[i].collisionCircleCoord(x, y, v)) {
                    a = true;
                }
            }

            for (i = 0; i < coins.size(); ++i) {
                if (((Coin) coins.get(i)).handleCollisioninGeneral(x, y, v)) {
                    a = true;
                }
            }

            if (teleport1 != null && teleport1.handleCollisioninGeneral(x, y, v)) {
                a = true;
            }

            if (teleport2 != null && teleport2.handleCollisioninGeneral(x, y, v)) {
                a = true;
            }
        }

        this.position = new Translate(x, y);
        super.getTransforms().addAll(new Transform[]{this.position});
    }


    public void update(double ds, double left, double right, double top, double bottom, double fenceWidth, Obstacle[] obstacles, Hole[] holes, IceandMud[] iceandMuds, Teleport t1, Teleport t2, List<Coin> coins) {

        double newX = 0;
        double newY = 0;
        double changedNeyX;
        double changedNewY;

        boolean collided = false;
        Point2D newSpeed = new Point2D(0, 0);

        if (!collided && this.isThereCollision(this.position.getX(), this.position.getY(), left, right, top, bottom, fenceWidth, iceandMuds, obstacles, holes, coins, t1, t2)) {
            collided = true;
            this.changeSpeed();
        }

        if (!collided) {
            newX = this.position.getX() + speed.getX() * ds;
            newY = this.position.getY() + speed.getY() * ds;
            this.position.setX(newX);
            this.position.setY(newY);
        } else {
            changedNeyX = this.position.getX() + speed.getX() * ds;
            changedNewY = this.position.getY() + speed.getY() * ds;
            if (!this.isThereCollision(changedNeyX, changedNewY, left, right, top, bottom, fenceWidth, iceandMuds, obstacles, holes, coins, t1, t2)) {
                this.position.setX(changedNeyX);
                this.position.setY(changedNewY);
            }
        }
    }

    private void changeSpeed() {
        this.speed = new Point2D((double) this.randomSign() * ThreadLocalRandom.current().nextDouble(5.0D, 50.0D), (double) this.randomSign() * ThreadLocalRandom.current().nextDouble(5.0D, 50.0D));
    }

    public boolean handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();
        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        Bounds holeBounds = super.getBoundsInParent();
        double holeX = holeBounds.getCenterX();
        double holeY = holeBounds.getCenterY();
        double holeRadius = super.getRadius();
        double distanceX = holeX - ballX;
        double distanceY = holeY - ballY;
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;
        boolean result = distanceSquared < holeRadius * holeRadius;
        return result;
    }

    int randomSign() {
        Random random = new Random();
        return random.nextBoolean() ? 1 : -1;
    }

    private boolean isThereCollision(double x, double y, double left, double right, double top, double bottom, double fenceWidth, IceandMud[] iceandMuds, Obstacle[] obstacles, Hole[] holes, List<Coin> coins, Teleport t1, Teleport t2) {

        double radius = super.getRadius();
        double minX = left + radius + fenceWidth;
        double maxX = right - radius - fenceWidth;
        double minY = top + radius + fenceWidth;
        double maxY = bottom - radius - fenceWidth;

        if (x >= maxX || x <= minX) {
            return true;
        } else if (y >= maxY || y <= minY) {
            return true;
        }

        for (int i = 0; i < obstacles.length; ++i) {
            Bounds obstacleBounds = obstacles[i].getBoundsInParent();
            double obstacleMinX = obstacleBounds.getMinX();
            double obstacleMaxX = obstacleBounds.getMaxX();
            double obstacleMinY = obstacleBounds.getMinY();
            double obstacleMaxY = obstacleBounds.getMaxY();
            if (!collided && x + this.getRadius() >= obstacleMinX && x - this.getRadius() <= obstacleMaxX && y + this.getRadius() >= obstacleMinY && y - this.getRadius() <= obstacleMaxY) {
                return true;
            }

        }

        for (int i = 0; i < holes.length; ++i) {
            if (!collided && holes[i].handleCollisioninGeneral(x, y, this.getRadius())) {
                return true;
            }
        }
        if (!collided && t1.handleCollisioninGeneral(x, y, this.getRadius())) {
            return true;
        }
        if (!collided && t2.handleCollisioninGeneral(x, y, this.getRadius())) {
            return true;
        }

        for (int j = 0; j < iceandMuds.length; j++) {
            if (!collided && iceandMuds[j].collisionCircleCoord(x, y, this.getRadius())) {
                return true;
            }
        }
        for (int j = 0; j < coins.size(); j++) {
            if (!collided && coins.get(j).handleCollisioninGeneral(x, y, this.getRadius())) {
                return true;
            }
        }

        return false;
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


}
