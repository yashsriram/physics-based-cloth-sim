package physical;

import linalg.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.List;

public class SpringMass {
    public static Vec3 gravity = Vec3.of(0, .5, 0);
    private static int nextId = 1;
    private static final float airDragConstant = 0.1f;
    private static final float ballFrictionConstant = 0.7f;

    private static int nextId() {
        int id = nextId;
        nextId++;
        return id;
    }

    final PApplet parent;
    final int id;
    float mass;
    Vec3 position;
    Vec3 velocity;
    Vec3 acceleration;
    boolean isFixed;
    private boolean isBroken = false;
    List<Spring> springs = new ArrayList<>();
    Vec3 dragForce = Vec3.zero();

    public SpringMass(PApplet parent, float mass, Vec3 position, Vec3 velocity, Vec3 acceleration, boolean isFixed) {
        this.parent = parent;
        this.id = nextId();
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.isFixed = isFixed;
    }

    public void update() throws Exception {
        if (isFixed || isBroken) {
            return;
        }
        // Calculate all forces
        Vec3 totalSpringForce = Vec3.zero();
        for (Spring spring : springs) {
            totalSpringForce = totalSpringForce.plus(spring.forceOn(this));
        }
        
        Vec3 weight = gravity.scale(mass);
        Vec3 airDrag = Vec3.zero();//velocity.scale(-1 * airDragConstant * mass);
        Vec3 totalForce = totalSpringForce.plus(weight).plus(airDrag);
    	totalForce = totalForce.plus(dragForce);
        // F = ma
        acceleration = totalForce.scale(1 / mass);
    }

    public void update(Ball ball) throws Exception {
        if (isFixed || isBroken) {
            return;
        }
        // Calculate all forces
        Vec3 totalSpringForce = Vec3.zero();
        for (Spring spring : springs) {
            totalSpringForce = totalSpringForce.plus(spring.forceOn(this));
        }
        Vec3 weight = gravity.scale(mass);
        Vec3 airDrag = velocity.scale(-1 * airDragConstant * mass);
        Vec3 totalForce = totalSpringForce.plus(weight).plus(airDrag);
        totalForce = totalForce.plus(dragForce);
        
        // Mass user controlled ball interaction
        Vec3 ballToMass = position.minus(ball.position);
        if (ballToMass.abs() <= ball.radius + 1) {
            // Helpful unit vectors
            Vec3 ballToMassUnit = ballToMass.unit();
            Vec3 massToBallUnit = ballToMassUnit.scale(-1);
            // Ball is paused => It exerts enough normal force on mass to cancel the normal component of total force
            Vec3 totalForceAlongNormalDir = massToBallUnit.scale(massToBallUnit.dot(totalForce));
            totalForce = totalForce.minus(totalForceAlongNormalDir);
            ball.accumulateSpringMassForce(totalForceAlongNormalDir);
            // Force along tangent is reduced due to friction
            totalForce = totalForce.scale(ballFrictionConstant);
            // Mass should not be inside ball and velocity along the normal should be 0
            position = ball.position.plus(ballToMassUnit.scale(ball.radius + 1));
            velocity = velocity.minus(ballToMassUnit.scale(ballToMassUnit.dot(velocity)));
        }
        // F = ma
        acceleration = totalForce.scale(1 / mass);
    }

    public void eularianIntegrate(float dt) {
    	if(isBroken) { return;}
        position = position.plus(velocity.scale(dt));
        velocity = velocity.plus(acceleration.scale(dt));
    }

    public void draw() {
        if (!this.isFixed) {
            parent.pushMatrix();
            parent.stroke(80, 204, 133);
            parent.translate(position.x, position.y, position.z);
            parent.sphere(2);
            parent.popMatrix();
        } else {
            parent.fill(150);
            parent.stroke(150);
            parent.beginShape(PConstants.QUAD);
            parent.vertex(position.x + 3, position.y, position.z + 3);
            parent.vertex(position.x - 3, position.y, position.z + 3);
            parent.vertex(position.x - 3, position.y, position.z - 3);
            parent.vertex(position.x + 3, position.y, position.z - 3);
            parent.endShape(PConstants.CLOSE);
        }
    }
    
    public void resetDragForce(Vec3 force) {
    	this.dragForce = Vec3.zero();
    }
    
    public void setDragForce(Vec3 force) {
    	this.dragForce = force;
    }
    public void resetDragForce() {
    	this.dragForce = Vec3.zero();
    }
    public void addDragForce(Vec3 force) {
    	this.dragForce = this.dragForce.plus(force);
    }
    
    public boolean getBroken() {
    	return this.isBroken;
    }
    public void setBroken() {
    	this.isBroken = true;
    }
    public void resetBroken() {
    	this.isBroken = false;
    }
}
