package com.example.rgdomaci2.arena;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Ball extends Sphere {
    private Translate position;
    private Point3D speed;
    private double ballAcceleration;

    public Point3D getSpeed() {
        return speed;
    }

    public void setSpeed(Point3D speed) {
        this.speed = speed;
    }

    public Ball(double radius,Translate position,int type) {
        super(radius);
        PhongMaterial material = new PhongMaterial();
        this.position = position;

        switch (type){
            case 0: {
                ballAcceleration = 1;
                material.setDiffuseColor(Color.RED);
                break;
            }
            case 1:{
                ballAcceleration = 1.2;
                material.setDiffuseColor(Color.ORANGE);
                material.setSpecularColor(Color.BLACK);
                break;
            }
            case 2:{
                ballAcceleration = 1.4;
                material.setDiffuseColor(Color.LIGHTGREEN);
                material.setSpecularColor(Color.GREEN);
                break;
            }
        }
        super.setMaterial(material);
        super.getTransforms().add(this.position);

        this.speed = new Point3D(0, 0, 0);
    }

    public boolean update(
            double deltaSeconds,
            double top,
            double bottom,
            double left,
            double right,
            double xAngle,
            double zAngle,
            double maxAngleOffset,
            double maxAcceleration,
            double damp,
            Fence fences[]
    ) {

        boolean fenceCollide = false;
        if (fenceCollide == false) {
            for (int i = 0; i < fences.length; i++) {
                if (fences[i].handleCollision(this)) fenceCollide = true;
            }
        }

        double newPositionX = this.position.getX() + this.speed.getX() * deltaSeconds;
        double newPositionZ = this.position.getZ() + this.speed.getZ() * deltaSeconds;

        for (int i = 0; i < fences.length; i++) {
            if (!fences[i].handleCollisionPosition(newPositionX, newPositionZ, this.getRadius())) fenceCollide = false;
        }

        if (!fenceCollide) {
            this.position.setX(newPositionX);
            this.position.setZ(newPositionZ);
        }


        double accelerationX = maxAcceleration * zAngle / maxAngleOffset;
        double accelerationZ = -maxAcceleration * xAngle / maxAngleOffset;

        double newSpeedX = (this.speed.getX() + accelerationX * deltaSeconds*ballAcceleration) * damp;
        double newSpeedZ = (this.speed.getZ() + accelerationZ * deltaSeconds*ballAcceleration) * damp;

        this.speed = new Point3D(newSpeedX, 0, newSpeedZ);

        boolean xOutOfBounds = (newPositionX > right) || (newPositionX < left);
        boolean zOutOfBounds = (newPositionZ > top) || (newPositionZ < bottom);

        return xOutOfBounds || zOutOfBounds;
    }

    private void fenceCollision(Fence fence) {
        Bounds ballBounds = this.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballZ = ballBounds.getCenterZ();
        double ballRadius = this.getRadius();

        Bounds fenceBounds = super.getBoundsInParent();
        double fenceMaxX = fenceBounds.getMaxX();
        double fenceMaxZ = fenceBounds.getMaxZ();
        double fenceMinX = fenceBounds.getMinX();
        double fenceMinZ = fenceBounds.getMinZ();

        if (ballX - ballRadius <= fenceMaxX) {

        }

    }

}
