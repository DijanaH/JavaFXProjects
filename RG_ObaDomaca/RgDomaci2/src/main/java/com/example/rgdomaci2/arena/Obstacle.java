package com.example.rgdomaci2.arena;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class Obstacle extends Cylinder {

    private boolean dangerous;

    public Obstacle(double v, double v1, int i, boolean dangerous) {
        super(v, v1, i);

        Image obstacleImg = new Image(this.getClass().getClassLoader().getResourceAsStream("ob.jpg"));
        PhongMaterial obstacleMat = new PhongMaterial();
        obstacleMat.setDiffuseMap(obstacleImg);

        Image obstacleImgDang = new Image(this.getClass().getClassLoader().getResourceAsStream("obss.jpg"));
        PhongMaterial obstacleMatDang = new PhongMaterial();
        obstacleMatDang.setDiffuseMap(obstacleImgDang);


        this.dangerous = dangerous;

        if(dangerous){
            super.setMaterial(obstacleMatDang);
        }else {
            super.setMaterial(obstacleMat);
        }
    }

    public void handleCollision(Ball ball){
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

        boolean isInHole = distance < 4*holeRadius * holeRadius;

        if(isInHole){
            if(dangerous){
                ball.setSpeed(new Point3D(-ball.getSpeed().getX(),ball.getSpeed().getY(),-ball.getSpeed().getZ()));
                ball.setSpeed(ball.getSpeed().multiply(2));
            }else{
                ball.setSpeed(new Point3D(-ball.getSpeed().getX(),ball.getSpeed().getY(),-ball.getSpeed().getZ()));
            }
        }
    }
}
