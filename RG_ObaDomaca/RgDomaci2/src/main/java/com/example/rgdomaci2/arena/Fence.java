package com.example.rgdomaci2.arena;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Fence extends Box {

    public Fence(double x, double y,double z, Translate position) {
        super(x,y,z);
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setDiffuseColor(Color.DARKRED);
        this.getTransforms().addAll(
                position
        );
        this.setMaterial(phongMaterial);



        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(2),new KeyValue(phongMaterial.diffuseColorProperty(),Color.BLUE, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(4),new KeyValue(phongMaterial.diffuseColorProperty(),Color.GREEN, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(6),new KeyValue(phongMaterial.diffuseColorProperty(),Color.RED, Interpolator.LINEAR))
        );


        tl.setCycleCount(Animation.INDEFINITE);
        tl.setAutoReverse(true);
        tl.play();

    }

    public boolean handleCollision ( Ball ball ) {
        Bounds ballBounds = ball.getBoundsInParent ( );

        double ballX = ballBounds.getCenterX ( );
        double ballZ = ballBounds.getCenterZ ( );
        double ballRadius = ball.getRadius();

        Bounds fenceBounds = super.getBoundsInParent ( );
        double fenceMaxX = fenceBounds.getMaxX();
        double fenceMaxZ = fenceBounds.getMaxZ();
        double fenceMinX = fenceBounds.getMinX();
        double fenceMinZ = fenceBounds.getMinZ();

        if(ballX-ballRadius<=fenceMaxX && ballX+ballRadius>fenceMinX && ballZ+ballRadius>=fenceMinZ && ballZ-ballRadius<=fenceMaxZ ){

            if(ballZ>fenceMaxZ || ballZ<fenceMinZ){
                ball.setSpeed(new Point3D(ball.getSpeed().getX(),ball.getSpeed().getY(),-ball.getSpeed().getZ()));
            }else{
                ball.setSpeed(new Point3D(-ball.getSpeed().getX(),ball.getSpeed().getY(),ball.getSpeed().getZ()));
            }
            return true;
        }
        return false;
    }

    public boolean handleCollisionPosition(double ballX, double ballZ, double ballRadius){

        Bounds fenceBounds = super.getBoundsInParent ( );
        double fenceMaxX = fenceBounds.getMaxX();
        double fenceMaxZ = fenceBounds.getMaxZ();
        double fenceMinX = fenceBounds.getMinX();
        double fenceMinZ = fenceBounds.getMinZ();

        if(ballX-ballRadius<=fenceMaxX && ballX+ballRadius>fenceMinX && ballZ+ballRadius>fenceMinZ && ballZ-ballRadius<fenceMaxZ){
             return true;
        }
        return false;
    }

}
