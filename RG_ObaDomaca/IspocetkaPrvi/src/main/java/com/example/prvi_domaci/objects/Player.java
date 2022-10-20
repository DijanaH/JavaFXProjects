package com.example.prvi_domaci.objects;

import com.example.prvi_domaci.Utilities;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Player extends Group {
	
	private double width;
	private double height;
	private Translate position;
	private Rotate rotate;
	
	public Player ( double width, double height, Translate position,int top ) {
		this.width = width;
		this.height = height;
		this.position = position;
		
		//Rectangle cannon = new Rectangle ( width, height, Color.LIGHTBLUE );

		Path cannon = new Path();
		Color color1 = Color.LIGHTBLUE;
		Color color2 = Color.CORAL;


		switch (top){
			case 1: {
				cannon = new Path(
						new MoveTo(width/4, 0),
						new LineTo(0,height-width/2),
						new HLineTo(width),
						new LineTo(3*width/4,0),
						new ClosePath()

				);
				color1 = Color.ORANGE;
				cannon.setFill(Color.LIGHTBLUE);
			}break;
			case 2:{
				cannon = new Path(
						new MoveTo(width/8, 0),
						new LineTo(width/4,height/6),
						new LineTo(0,height-width/2),
						new HLineTo(width),
						new LineTo(3*width/4,height/6),
						new LineTo(7*width/8,0),
						new ClosePath()
				);
				Stop stops[] = new Stop[]{
						new Stop(0,Color.LEMONCHIFFON),
						new Stop(1,Color.YELLOW)
				};
				LinearGradient lg = new LinearGradient(0.5,0,0.5,1,true, CycleMethod.NO_CYCLE,stops);
				color1 = Color.CORAL;
				cannon.setFill(lg);
			}break;
			case 3:{
				cannon = new Path(
						new MoveTo(width/8, 0),
						new LineTo(width/4,height-width/2),
						new HLineTo(3*width/4),
						new LineTo(7*width/8,0),
						new ClosePath()

				);
				color1 = Color.LIGHTBLUE;
				Stop stops[] = new Stop[]{
						new Stop(0,Color.LIGHTGREEN),
						new Stop(1,Color.DARKSLATEGRAY)
				};
				LinearGradient lg = new LinearGradient(0.5,0,0.5,1,true, CycleMethod.NO_CYCLE,stops);
				cannon.setFill(lg);
			}
		}

		Circle circle = new Circle(width/2, color1);
		circle.getTransforms().addAll(
				new Translate(width/2,height-width/2)
		);

		
		this.rotate = new Rotate ( );

		cannon.setStroke(null);

		super.getChildren ( ).addAll( cannon,circle );

		super.getTransforms ( ).addAll (
				position,
				new Translate ( width / 2, height ),
				new Translate(0,-width/2),
				rotate,
				new Translate(0,width/2),
				new Translate ( -width / 2, -height )
		);
	}
	
	public void handleMouseMoved ( MouseEvent mouseEvent, double minAngleOffset, double maxAngleOffset ) {
		Bounds bounds = super.getBoundsInParent ( );
		
		double startX = bounds.getCenterX ( );
		double startY = bounds.getMaxY ( );
		
		double endX = mouseEvent.getX ( );
		double endY = mouseEvent.getY ( );
		
		Point2D direction     = new Point2D ( endX - startX, endY - startY ).normalize ( );
		Point2D startPosition = new Point2D ( 0, -1 );
		
		double angle = ( endX > startX ? 1 : -1 ) * direction.angle ( startPosition );
		
		this.rotate.setAngle ( Utilities.clamp ( angle, minAngleOffset, maxAngleOffset ) );
	}
	
	public Translate getBallPosition ( ) {
		double startX = this.position.getX ( ) + this.width / 2;
		double startY = this.position.getY ( ) + this.height-width/2;
		
		double x = startX + Math.sin ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		double y = startY - Math.cos ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		
		Translate result = new Translate ( x, y );
		
		return result;
	}
	
	public Point2D getSpeed ( ) {
		double startX = this.position.getX ( ) + this.width / 2;
		double startY = this.position.getY ( ) + this.height-width/2;
		
		double endX = startX + Math.sin ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		double endY = startY - Math.cos ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		
		Point2D result = new Point2D ( endX - startX, endY - startY );

		Point2D a = result.normalize();

		return result.normalize ( );
	}
}
