package physical;

import processing.core.PApplet;

import java.util.List;

public class WaterColumn {
	PApplet parent;
	public float height;
	public float momentum;
	public float midpointMomentum;
	public float midpointHeight;

    public WaterColumn(PApplet parent, float height, float momentum) {
        this.parent = parent;
        this.height = height;
        this.momentum = momentum;
    }

    public void update() throws Exception {
        // Calculate all forces
        // Weight
        // Reset to 0 for the next iteration.
        // F = ma
//        acceleration = totalForce.scale(1 / mass);
    }

    public void update(Ball ball) throws Exception {
    }

    public void update(List<Ball> balls) throws Exception {
    }
    
//    public void eularianIntegrate(float dt) {
//        if (isBroken) {
//            return;
//        }
//        position.plusAccumulate(velocity.scale(dt));
//        velocity.plusAccumulate(acceleration.scale(dt));
//    }
//
//    public void secondOrderIntegrate(float dt) {
//        if (isBroken) {
//            return;
//        }
//        position.plusAccumulate(velocity.scale(dt).plus(acceleration.scale(0.5f * dt * dt)));
//        velocity.plusAccumulate(acceleration.scale(dt));
//    }

}
