package physical;

import linalg.Vec3;
import processing.core.PApplet;
import processing.core.PMatrix3D;

public class RigidBody {
    final PApplet parent;

    float mass;

    // translational
    Vec3 position;
    Vec3 velocity;
    Vec3 acceleration;

    // rotational
    Vec3 angularVelocity;
    Vec3 angularAcceleration;
    PMatrix3D orientation;

    public RigidBody(PApplet parent, float mass, Vec3 position, Vec3 velocity, Vec3 acceleration, PMatrix3D orientation, Vec3 angularVelocity, Vec3 angularAcceleration) {
        this.parent = parent;
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.orientation = orientation;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
    }

    public void update(float dt) {
        firstOrderIntegration(dt);
    }

    private void firstOrderIntegration(float dt) {
        // translation
        position.plusAccumulate(velocity.scale(dt));
        velocity.plusAccumulate(acceleration.scale(dt));

        // orientation
        Vec3 angularVelocityDir = angularVelocity.unit();
        orientation.rotate(angularVelocity.abs() * dt, angularVelocityDir.x, angularVelocityDir.y, angularVelocityDir.z);
        angularVelocity = angularVelocity.plus(angularAcceleration.scale(dt));
    }

    public void draw() {
        parent.fill(255);
        parent.stroke(0);
        parent.pushMatrix();
        parent.translate(position.x, position.y, position.z);
        parent.applyMatrix(orientation);
        parent.box(20);
        parent.popMatrix();
    }

}
