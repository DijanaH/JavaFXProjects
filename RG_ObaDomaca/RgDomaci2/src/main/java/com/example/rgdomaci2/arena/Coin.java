package com.example.rgdomaci2.arena;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Coin extends Cylinder {

    public Coin(double x, double y, Translate position) {
        super(x,y);

        PhongMaterial pm = new PhongMaterial();
        pm.setDiffuseColor(Color.YELLOW);
        super.setMaterial(pm);

        Rotate rotate = new Rotate(0,Rotate.Y_AXIS);
        Translate translate = new Translate(0,0,0);

        super.getTransforms().addAll(position,translate,rotate);

        double random = Math.random()*360;

        Timeline rotatey = new Timeline(
                new KeyFrame(Duration.seconds(0),new KeyValue(rotate.angleProperty(),random, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(5),new KeyValue(rotate.angleProperty(),random+360, Interpolator.LINEAR))
                  );

        Timeline t = new Timeline(
                new KeyFrame(Duration.seconds(0),new KeyValue(translate.yProperty(),-30, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(2),new KeyValue(translate.yProperty(),30, Interpolator.LINEAR))
        );

        rotatey.setCycleCount(Animation.INDEFINITE);
        rotatey.play();
        t.setCycleCount(Animation.INDEFINITE);
        t.setAutoReverse(true);
        t.play();


    }


    public boolean handleCollision ( Sphere ball ) {
        Bounds ballBounds = ball.getBoundsInParent ( );

        double ballX = ballBounds.getCenterX ( );
        double ballZ = ballBounds.getCenterZ ( );

        Bounds holeBounds = super.getBoundsInParent ( );
        double holeX      = holeBounds.getCenterX ( );
        double holeZ      = holeBounds.getCenterZ ( );
        double holeRadius = super.getRadius ( );

        double dx = holeX - ballX;
        double dz = holeZ - ballZ;

        double distance = dx * dx + dz * dz;

        boolean isInHole = distance < holeRadius * holeRadius;

        return isInHole;
    }

}
