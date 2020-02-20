package physical;

import linalg.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.List;

public class PointMass {
    public static Vec3 gravity = Vec3.of(0, .5, 0);
    private static int nextId = 1;
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
    List<Thread> threads = new ArrayList<>();
    Vec3 dragForce = Vec3.zero();
    private boolean isBurning;
    private int dragForceCount = 0;

    public PointMass(PApplet parent, float mass, Vec3 position, Vec3 velocity, Vec3 acceleration, boolean isFixed) {
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
        for (Thread thread : threads) {
            totalSpringForce = totalSpringForce.plus(thread.forceOn(this));
        }
        Vec3 weight = gravity.scale(mass);
        Vec3 totalForce = totalSpringForce.plus(weight);
        if (dragForceCount > 0) {
            totalForce = totalForce.plus(dragForce.scale(1f / dragForceCount));
        }
        // reset to 0 for the next iteration.
        this.resetDragForce();
        // F = ma
        acceleration = totalForce.scale(1 / mass);
    }

    public void update(Ball ball) throws Exception {
        if (isFixed || isBroken) {
            return;
        }
        // Calculate all forces
        Vec3 totalSpringForce = Vec3.zero();
        for (Thread thread : threads) {
            totalSpringForce.plusAccumulate(thread.forceOn(this));
        }
        Vec3 weight = gravity.scale(mass);
        Vec3 totalForce = totalSpringForce.plus(weight);
        if (dragForceCount > 0) {
            totalForce.plusAccumulate(dragForce.scale(1f / dragForceCount));
        }
        // reset to 0 for the next iteration.
        this.resetDragForce();

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
            velocity.minusAccumulate(ballToMassUnit.scale(ballToMassUnit.dot(velocity)));
        }
        // F = ma
        acceleration = totalForce.scale(1 / mass);
    }

    public void eularianIntegrate(float dt) {
        if (isBroken) {
            return;
        }
        position.plusAccumulate(velocity.scale(dt));
        velocity.plusAccumulate(acceleration.scale(dt));
    }
    
    public void secondOrderIntegrate(float dt) {
        if (isBroken) {
            return;
        }
        position.plusAccumulate(velocity.scale(dt).plus(acceleration.scale(0.5f*dt*dt)));
        velocity.plusAccumulate(acceleration.scale(dt));
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

    public void resetDragForce() {
        this.dragForce = Vec3.zero();
        this.dragForceCount = 0;
    }

    public void addDragForce(Vec3 force) {
        this.dragForce.plusAccumulate(force);
        this.dragForceCount += 1;
    }

    public boolean getIsBroken() {
        return this.isBroken;
    }

    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public void setIsBurning(boolean isBurning) {
        this.isBurning = isBurning;
    }

}
