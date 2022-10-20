package com.example.prvi_domaci.objects;

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
import javafx.stage.Stage;

public class Hole extends Circle {

	private static final double SPEED_LIMIT = 1100;

	private int level;
	
	public Hole ( double radius, Translate position, int level ) {
		super (radius);
		this.level = level;
		Stop stops[];
		switch (level){
			case 1: stops = new Stop[]{new Stop(0,Color.BLACK),new Stop(1,Color.LIGHTGREEN)};break;
			case 2: stops= new Stop[]{new Stop(0,Color.BLACK),new Stop(1,Color.YELLOW)};break;
			case 3: stops = new Stop[]{new Stop(0,Color.BLACK),new Stop(1,Color.BROWN)};break;
			default: stops = new Stop[]{new Stop(0,Color.BLACK),new Stop(1,Color.YELLOW)};
		}


		RadialGradient rg = new RadialGradient(0,0,0.5,0.5,0.5,true, CycleMethod.NO_CYCLE,stops);
		super.setFill(rg);
		
		super.getTransforms ( ).addAll ( position );
	}
	
	public int handleCollision (int hole_level_points,Text text, Circle ball, Point2D ballSpeed) {



		double a = ballSpeed.magnitude();
		if(ballSpeed.magnitude()>SPEED_LIMIT) return 0;

		Bounds ballBounds = ball.getBoundsInParent ( );
		
		double ballX      = ballBounds.getCenterX ( );
		double ballY      = ballBounds.getCenterY ( );
		double ballRadius = ball.getRadius ( );
		
		Bounds holeBounds = super.getBoundsInParent ( );
		
		double holeX      = holeBounds.getCenterX ( );
		double holeY      = holeBounds.getCenterY ( );
		double holeRadius = super.getRadius ( );
		
		double distanceX = holeX - ballX;
		double distanceY = holeY - ballY;
		
		double distanceSquared = distanceX * distanceX + distanceY * distanceY;
		
		boolean result = distanceSquared < ( holeRadius * holeRadius );

		//if(result) return this.level;

		if(result){
			int p;
			switch(level){
				case 1: hole_level_points = 5;break;
				case 2: hole_level_points = 10;break;
				case 3: hole_level_points = 15;break;
				default: hole_level_points = 0;
			}
			//String pp = text.getText().toString();
			//p += Integer.parseInt(pp);
			//.setText(Integer.toString(p));
		}


		return hole_level_points;
	};


	public boolean handleCollisioninGeneral ( double x, double y , double rad) {

		Bounds holeBounds = super.getBoundsInParent ( );

		double holeX      = holeBounds.getCenterX ( );
		double holeY      = holeBounds.getCenterY ( );
		double holeRadius = super.getRadius ( );

		double distanceX = holeX - x;
		double distanceY = holeY - y;

		double distanceSquared = distanceX * distanceX + distanceY * distanceY;

		boolean result = distanceSquared < ( (holeRadius+rad) * (holeRadius+rad));

		return result;
	};

}
