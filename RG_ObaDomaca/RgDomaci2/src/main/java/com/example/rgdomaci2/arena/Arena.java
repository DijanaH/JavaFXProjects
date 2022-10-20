package com.example.rgdomaci2.arena;

import com.example.rgdomaci2.Utilities;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Arena extends Group {
	
	private Rotate rotateX;
	private Rotate rotateZ;
	private boolean justOnce  = true;
	private boolean isArrow = false;
	
	public Arena ( Node... children ) {
		super ( children );
		
		this.rotateX = new Rotate ( 0, Rotate.X_AXIS );
		this.rotateZ = new Rotate ( 0, Rotate.Z_AXIS );
		
		super.getTransforms ( ).addAll (
				this.rotateX,
				this.rotateZ
		);
	}
	
	public void handleKeyEvent (KeyEvent event, double maxOffset) {
		double dxAngle = 0;
		double dzAngle = 0;

		isArrow = false;
		if ( event.getCode ( ).equals ( KeyCode.UP ) ) {
			dxAngle = -1;
			isArrow = true;
		} else if ( event.getCode ( ).equals ( KeyCode.DOWN ) ) {
			dxAngle = 1;
			isArrow = true;
		} else if ( event.getCode ( ).equals ( KeyCode.LEFT ) ) {
			dzAngle = -1;
			isArrow = true;
		} else if ( event.getCode ( ).equals ( KeyCode.RIGHT ) ) {
			dzAngle = 1;
			isArrow = true;
		}
		
		double newXAngle = Utilities.clamp ( this.rotateX.getAngle ( ) + dxAngle, -maxOffset, maxOffset );
		double newZAngle = Utilities.clamp ( this.rotateZ.getAngle ( ) + dzAngle, -maxOffset, maxOffset );

		
		this.rotateX.setAngle ( newXAngle );
		this.rotateZ.setAngle ( newZAngle );

	}
	
	public double getXAngle ( ) {
		return this.rotateX.getAngle ( );
	}
	
	public double getZAngle ( ) {
		return this.rotateZ.getAngle ( );
	}


	public void update(double dump){
		this.rotateX.setAngle(this.getXAngle()*dump);
		this.rotateZ.setAngle(this.getZAngle()*dump);
	}

	public void startTime(IntegerProperty timeSeconds, Timeline timeline, Integer starttime){
		if(this.rotateX.getAngle()!=0 || this.rotateZ.getAngle()!=0){
			if(justOnce ){
				timeSeconds.set(starttime);
				timeline.getKeyFrames().add(
						new KeyFrame(Duration.seconds(starttime + 1),
								new KeyValue(timeSeconds, 0)));
				timeline.playFromStart();
				justOnce = false;
			}
		}
	}

}
