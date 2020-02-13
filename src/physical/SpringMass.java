package physical;

import linalg.Vec3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SpringMass {
    private static int nextId = 1;

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
    List<Spring> springs = new ArrayList<>();

    public SpringMass(PApplet parent, float mass, Vec3 position, Vec3 velocity, Vec3 acceleration) {
        this.parent = parent;
        this.id = nextId();
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public void update(float dt) throws Exception {
        Vec3 totalForce = Vec3.zero();
        for (Spring spring : springs) {
            totalForce = totalForce.plus(spring.forceOn(this));
        }
        Vec3 acceleration = totalForce.scale(1 / mass);
        position = position.plus(velocity.scale(dt));
        velocity = velocity.plus(acceleration.scale(dt));
    }

    public void draw() {
        parent.pushMatrix();
        parent.translate(position.x, position.y, position.z);
        parent.fill(255);
        parent.sphere(2);
        parent.popMatrix();
    }
}
