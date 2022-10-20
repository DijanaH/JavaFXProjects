package com.example.prvi_domaci.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Live extends Circle {
    public Live(double v, Translate position) {
        super(v);
        super.setFill(Color.RED);
        super.getTransforms().addAll(position);
    }
}
