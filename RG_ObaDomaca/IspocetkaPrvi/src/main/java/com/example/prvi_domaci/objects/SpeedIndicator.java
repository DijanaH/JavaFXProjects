package com.example.prvi_domaci.objects;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class SpeedIndicator extends Rectangle {

    public SpeedIndicator(double width, double height, Translate position) {
        super(width,height);
        super.setFill(Color.RED);

        super.getTransforms().addAll(position,new Rotate(180));
    }


    public void updateSpeedInd(double max_hold_in_s){
        Scale speedIndScale = new Scale(1,1);


        Timeline speedIndTl = new Timeline(
                new KeyFrame(Duration.seconds(0),new KeyValue(speedIndScale.yProperty(),0, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(max_hold_in_s),new KeyValue(speedIndScale.yProperty(),1,Interpolator.LINEAR))
        );
        this.getTransforms().addAll(speedIndScale);
        speedIndTl.play();
    }

}
