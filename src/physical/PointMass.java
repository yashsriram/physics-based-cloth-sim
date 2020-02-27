package physical;

import math.Integrator;
import math.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.List;

public class PointMass {
    public static float breakingTemperature = 800;
    public static float selfIgniteTemperature = 400;
    public static float onContactTemperature = 1000;
    public static float temperatureTransferRate = 0.0001f;
    public static float selfIgniteRate = 0.005f;
    public static Vec3 gravity = Vec3.of(0, .5, 0);
    private static int nextId = 1;

    private static int nextId() {
        int id = nextId;
        nextId++;
        return id;
    }

    final PApplet parent;
    final int id;
    float mass;
    public Vec3 position;
    public Vec3 velocity;
    public Vec3 acceleration;
    boolean isFixed;
    boolean isBroken;
    List<Thread> threads = new ArrayList<>();
    Vec3 dragForce = Vec3.zero();
    private float temperature;
    private float dT;
    private int dragForceCount;
	private Vec3 positionOld;
	private Vec3 velocityOld;

    public PointMass(PApplet parent, float mass, Vec3 position, Vec3 velocity, Vec3 acceleration, boolean isFixed) {
        this.parent = parent;
        this.id = nextId();
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.isFixed = isFixed;
        this.isBroken = false;
        this.temperature = 0;
        this.dragForceCount = 0;
    }

    public void update() throws Exception {
        if (isFixed || isBroken) {
            return;
        }
        // Calculate all forces
        Vec3 totalForce1 = Vec3.zero();
        for (Thread thread : threads) {
            totalForce1.plusAccumulate(thread.forceOn(this));
        }
        // Weight
        totalForce1.plusAccumulate(gravity.scale(mass));
        // Air drag
        if (dragForceCount > 0) {
            totalForce1.plusAccumulate(dragForce.scale(1f / dragForceCount));
        }
        // Reset to 0 for the next iteration.
        this.resetDragForce();
        // F = ma
        acceleration = totalForce1.scale(1 / mass);
    }

    public void update(Ball ball) throws Exception {
        if (isFixed || isBroken) {
            return;
        }
        // Calculate all forces
        Vec3 totalForce = Vec3.zero();
        for (Thread thread : threads) {
            totalForce.plusAccumulate(thread.forceOn(this));
        }
        // Weight
        totalForce.plusAccumulate(gravity.scale(mass));
        // Air drag
        if (dragForceCount > 0) {
            totalForce.plusAccumulate(dragForce.scale(1f / dragForceCount));
        }
        // Reset to 0 for the next iteration.
        this.resetDragForce();

        // Ball Interaction
        ballInteraction(ball, totalForce);

        // F = ma
        acceleration = totalForce.scale(1 / mass);
    }

    public void update(List<Ball> balls) throws Exception {
        if (isFixed || isBroken) {
            return;
        }
        // Calculate all forces
        Vec3 totalForce = Vec3.zero();
        for (Thread thread : threads) {
            totalForce.plusAccumulate(thread.forceOn(this));
        }
        // Weight
        totalForce.plusAccumulate(gravity.scale(mass));
        // Air drag
        if (dragForceCount > 0) {
            totalForce.plusAccumulate(dragForce.scale(1f / dragForceCount));
        }
        // reset to 0 for the next iteration.
        this.resetDragForce();

        // Ball Interaction
        for (Ball ball : balls) {
            ballInteraction(ball, totalForce);
        }

        // F = ma
        acceleration = totalForce.scale(1 / mass);
    }

    private boolean ballInteraction(Ball ball, Vec3 totalForce) {
        Vec3 ballToMass = position.minus(ball.position);
        if (ballToMass.abs() <= ball.radius + 1) {
            // Helpful unit vectors
            Vec3 ballToMassUnit = ballToMass.unit();
            Vec3 massToBallUnit = ballToMassUnit.scale(-1);
            // Ball is paused => It exerts enough normal force on mass to cancel the normal component of total force
            Vec3 totalForceAlongNormalDir = massToBallUnit.scale(massToBallUnit.dot(totalForce));
            totalForce.minusAccumulate(totalForceAlongNormalDir);
            ball.accumulateSpringMassForce(totalForceAlongNormalDir);
            // Force along tangent is reduced due to friction
            totalForce.scaleAccumulate(Ball.ballFrictionConstant);
            // Mass should not be inside ball and velocity along the normal should be 0
            position = ball.position.plus(ballToMassUnit.scale(ball.radius + 1));
            velocity.minusAccumulate(ballToMassUnit.scale(ballToMassUnit.dot(velocity)));
            return true;
        }
        return false;
    }

    public void firstOrderIntegrate(float dt) {
        if (isBroken) {
            return;
        }
        Integrator.firstOrder(position, velocity, acceleration, dt);
    }

    public void secondOrderHalfStep(float dt) {
        if (isBroken) {
            return;
        }
        positionOld = position.copy();
        velocityOld = velocity.copy();
        Integrator.secondOrderHalfStep(position, velocity, acceleration, dt);
    }
    
    public void secondOrderFullStep(float dt) {
        if (isBroken) {
            return;
        }
    	Integrator.secondOrderFullStep(position, velocity, acceleration, dt);
    }

    public void updateWithBurnCheck(Ball ball) throws Exception {
        if (isFixed) {
            return;
        }
        // Calculate all forces
        Vec3 totalForce = Vec3.zero();
        dT = 0;
        for (Thread thread : threads) {
            totalForce.plusAccumulate(thread.forceOn(this));
            PointMass neighbour = thread.getOther(this);
            dT += neighbour.temperature - temperature;
        }
        dT = dT * temperatureTransferRate;
        // Weight
        totalForce.plusAccumulate(gravity.scale(mass));
        // Air drag
        if (dragForceCount > 0) {
            totalForce.plusAccumulate(dragForce.scale(1f / dragForceCount));
        }
        // Reset to 0 for the next iteration.
        this.resetDragForce();

        // Ball Interaction
        boolean isInContact = ballInteraction(ball, totalForce);
        if (isInContact) {
            temperature = onContactTemperature;
        }

        // F = ma
        acceleration = totalForce.scale(1 / mass);
    }

    public boolean updateTemperature() {
        if (isBroken) {
            return false;
        }
        if (temperature >= breakingTemperature) {
            for (Thread thread : threads) {
                thread.setBroken(true);
            }
            isBroken = true;
            return true;
        }
        if (temperature >= selfIgniteTemperature) {
            temperature += temperature * selfIgniteRate;
        }
        temperature += dT;
        return false;
    }

    public void draw() {
        if (isFixed) {
            parent.fill(150);
            parent.stroke(150);
            parent.beginShape(PConstants.QUAD);
            parent.vertex(position.x + 3, position.y, position.z + 3);
            parent.vertex(position.x - 3, position.y, position.z + 3);
            parent.vertex(position.x - 3, position.y, position.z - 3);
            parent.vertex(position.x + 3, position.y, position.z - 3);
            parent.endShape(PConstants.CLOSE);
        } else {
            parent.pushMatrix();
            parent.fill(temperature / breakingTemperature * 255, 0, 0);
            parent.translate(position.x, position.y, position.z);
            parent.box(2);
            parent.popMatrix();
        }
    }

    public void draw2D() {
        if (this.isFixed) {
            parent.fill(150);
            parent.stroke(150);
            parent.beginShape(PConstants.QUAD);
            parent.vertex(position.x + 3, position.y);
            parent.vertex(position.x - 3, position.y);
            parent.vertex(position.x - 3, position.y);
            parent.vertex(position.x + 3, position.y);
            parent.endShape(PConstants.CLOSE);
        } else {
            parent.pushMatrix();
            parent.circle(position.x, position.y, 1);
            parent.popMatrix();
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

    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
