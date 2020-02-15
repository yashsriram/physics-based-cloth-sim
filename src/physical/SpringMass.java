package physical;

import linalg.Vec3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SpringMass {
    private static int nextId = 1;
    private static final Vec3 gravity = Vec3.of(0, 5, 0);
    private static final float airDragConstant = 0.1f;

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
    List<Spring> springs = new ArrayList<>();

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
        if (isFixed) {
            return;
        }
        Vec3 totalSpringForce = Vec3.zero();
        for (Spring spring : springs) {
            totalSpringForce = totalSpringForce.plus(spring.forceOn(this));
        }
        acceleration = totalSpringForce.scale(1 / mass);
        acceleration = acceleration.plus(gravity);
        acceleration = acceleration.plus(velocity.scale(-1 * airDragConstant));
    }

    public void update(Ball ball) throws Exception {
        if (isFixed) {
            return;
        }
        Vec3 totalSpringForce = Vec3.zero();
        for (Spring spring : springs) {
            totalSpringForce = totalSpringForce.plus(spring.forceOn(this));
        }
        acceleration = totalSpringForce.scale(1 / mass);
        acceleration = acceleration.plus(gravity);
        acceleration = acceleration.plus(velocity.scale(-1 * airDragConstant));
        // mass ball interaction
        Vec3 ballToMass = position.minus(ball.position);
        Vec3 ballToMassUnit = ballToMass.unit();
        if (ballToMass.abs() <= ball.radius) {
            // net force along normal should be 0
            acceleration = acceleration.minus(ballToMassUnit.scale(ballToMassUnit.dot(acceleration)));
            // mass should not be inside ball and velocity along the normal should be 0
            position = ball.position.plus(ballToMassUnit.scale(ball.radius + 3));
            velocity = velocity.minus(ballToMassUnit.scale(ballToMassUnit.dot(velocity)));
        }
    }

    public void eularianIntegrate(float dt) {
        position = position.plus(velocity.scale(dt));
        velocity = velocity.plus(acceleration.scale(dt));
    }

    public void draw() {
        if (!this.isFixed) {
            parent.pushMatrix();
            parent.stroke(0, 255, 0);
            parent.point(position.x, position.y, position.z);
//            parent.translate(position.x, position.y, position.z);
//            parent.fill(255, 100, 0);
//            parent.sphere(2);
            parent.popMatrix();
        } else {
            parent.pushMatrix();
            parent.translate(position.x, position.y, position.z);
            parent.fill(255, 100, 0);
            parent.box(3);
            parent.popMatrix();
        }
    }
}
