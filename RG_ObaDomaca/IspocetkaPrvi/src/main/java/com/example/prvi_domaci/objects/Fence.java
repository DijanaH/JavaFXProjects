package com.example.prvi_domaci.objects;

import com.example.prvi_domaci.Main;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


public class Fence extends Group {
    public Fence(double width) {

        Image fenceImg = new Image(Main.class.getClassLoader().getResourceAsStream("fence.jpg"));
        ImagePattern fencePattern = new ImagePattern(fenceImg);

        Rectangle r1 = new Rectangle(Main.WINDOW_WIDTH,width);

        Rectangle r2 = new Rectangle(Main.WINDOW_WIDTH,width);
        r2.getTransforms().addAll(new Translate(0,Main.WINDOW_HEIGHT-width));

        Rectangle r3 = new Rectangle(width,Main.WINDOW_HEIGHT);
        r3.getTransforms().addAll();

        Rectangle r4 = new Rectangle(width,Main.WINDOW_HEIGHT);
        r4.getTransforms().addAll(new Translate(Main.WINDOW_WIDTH-width,0));


        r1.setFill(fencePattern);
        r2.setFill(fencePattern);
        r3.setFill(fencePattern);
        r4.setFill(fencePattern);



        super.getChildren().addAll(r1,r2,r3,r4);

    }



}
