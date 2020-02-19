package physical;

import linalg.Quaternion;
import linalg.Vec3;

public class RigidBody {
    float mass;

    // translational
    Vec3 position;
    Vec3 velocity;
    Vec3 acceleration;

    // rotational
    Quaternion orientation;
    Vec3 angularVelocity;
    Vec3 angularAcceleration;

    public RigidBody(float mass, Vec3 position, Vec3 velocity, Vec3 acceleration, Quaternion orientation, Vec3 angularVelocity, Vec3 angularAcceleration) {
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.orientation = orientation;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
    }
}
