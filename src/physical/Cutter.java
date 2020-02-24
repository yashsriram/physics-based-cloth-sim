package physical;

import camera.QueasyCam;
import math.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Cutter {

	private PVector position;
	private PVector forward;
	private PVector right;
	private PVector up;
	private PShape shape;
	
	public Cutter(PShape shape, QueasyCam cam){
		this.shape = shape;
		this.position = cam.getAim(20);
		this.forward = cam.getForward();
		this.right = cam.getRight();
		this.up = cam.getUp();
	}
	
	
	/*
	 * Taken from: https://stackoverflow.com/a/36425155/4031302
	 */
	private float distanceToSegment( Vec3 v, Vec3 a, Vec3 b )
	 {
	   final Vec3 ab  = b.minus( a ) ;
	   final Vec3 av  = v.minus( a ) ;

	   if (av.dot(ab) <= 0.0)           // Point is lagging behind start of the segment, so perpendicular distance is not viable.
	     return av.abs( ) ;         // Use distance to start of segment instead.

	   final Vec3 bv  = v.minus( b ) ;

	   if (bv.dot(ab) >= 0.0)           // Point is advanced past the end of the segment, so perpendicular distance is not viable.
	     return bv.abs( ) ;         // Use distance to end of the segment instead.

	   return (ab.cross( av )).abs() / ab.abs() ;       // Perpendicular distance of point to segment.
	}
	
	public boolean isTouching(Vec3 v) {
		// End-points of the line segment representing the sword.
		Vec3 a = Vec3.of(position.x, position.y, position.z-43); 
		Vec3 b = Vec3.of(position.x, position.y, position.z-7);
		
		float collisionRadius = 5.0f;
		return (distanceToSegment(v, a, b) <= collisionRadius); 
	}
	
	public void draw(PApplet p) {
		p.pushMatrix();
		p.translate(position.x, position.y, position.z);
		p.scale(10);
		p.rotateX(-PConstants.PI/2);
		p.rotateY(-PConstants.PI/2);
		p.shape(this.shape);
		p.popMatrix();
		
		// Display collision segment
//		p.pushMatrix();
//		p.stroke(255,255,0);
//		p.line(position.x-5, position.y, position.z-43, position.x-5, position.y, position.z-7);
//		p.popMatrix();
	}


	public void moveCutter(char key) {
		float dx = 10;
		if(key == 'w') {
        	moveForward(dx);
        }
        if(key == 's') {
        	moveForward(-dx);
        }
        if(key == 'a') {
        	moveRight(dx);
        }
        if(key == 'd') {
        	moveRight(-dx);
        }
        if(key == 'q') {
        	moveUp(dx);
        }
        if(key == 'e') {
        	moveUp(-dx);
        }
	}

	private void moveUp(float dx) {
		this.position.add( PVector.mult(up,dx));
	}

	private void moveRight(float dx) {
		this.position.add( PVector.mult(right,dx));
	}
	
	private void moveForward(float dx) {
		this.position.add( PVector.mult(forward,dx));
	}
}
