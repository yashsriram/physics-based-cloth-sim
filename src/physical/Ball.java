package physical;

import linalg.Vec3;
import processing.core.PApplet;

public class Ball {
    public static float userControlVelocity = 1;
    public static Vec3 gravity = Vec3.of(0, .5, 0);
    final PApplet parent;
    float mass;
    public float radius;
    final Vec3 initialPosition;
    public Vec3 position;
    public Vec3 velocity;
    Vec3 color;
    boolean isPaused;
    Vec3 externalForces;

    public Ball(PApplet parent, float mass, float radius, Vec3 position, Vec3 color, boolean isPaused) {
        this.parent = parent;
        this.mass = mass;
        this.radius = radius;
        this.initialPosition = position;
        this.position = position;
        this.velocity = Vec3.zero();
        this.color = color;
        this.isPaused = isPaused;
        this.externalForces = Vec3.zero();
    }

    public void update(float dt) {
        if (parent.keyPressed) {
            switch (parent.key) {
                case '4':
                    eularianIntegrate(Vec3.of(0, 0, -userControlVelocity), dt);
                    break;
                case '6':
                    eularianIntegrate(Vec3.of(0, 0, userControlVelocity), dt);
                    break;
                case '5':
                    eularianIntegrate(Vec3.of(-userControlVelocity, 0, 0), dt);
                    break;
                case '8':
                    eularianIntegrate(Vec3.of(userControlVelocity, 0, 0), dt);
                    break;
                case '7':
                    eularianIntegrate(Vec3.of(0, userControlVelocity, 0), dt);
                    break;
                case '9':
                    eularianIntegrate(Vec3.of(0, -userControlVelocity, 0), dt);
                    break;
                case 'r':
                    position = initialPosition;
                    velocity = Vec3.zero();
                    isPaused = true;
                    break;
                case 'g':
                    isPaused = false;
                    break;
                case 'f':
                    isPaused = true;
                    break;
            }
        }
        if (!isPaused) {
            eularianIntegrate(dt);
        }
    }

    private void eularianIntegrate(float dt) {
        position.plusAccumulate(velocity.scale(dt));
        Vec3 totalForce = externalForces.plus(gravity.scale(mass));
        Vec3 acceleration = totalForce.scale(1 / mass);
        velocity.plusAccumulate(acceleration.scale(dt));
    }

    private void eularianIntegrate(Vec3 instantaneousVelocity, float dt) {
        position.plusAccumulate(instantaneousVelocity.scale(dt));
    }

    public void draw() {
        parent.pushMatrix();
        parent.noStroke();
        parent.fill(color.x, color.y, color.z);
        parent.translate(position.x, position.y, position.z);
        parent.sphere(radius);
        parent.popMatrix();
    }

    public void accumulateSpringMassForce(Vec3 force) {
        externalForces.plusAccumulate(force);
    }

    public void clearExternalForces() {
        externalForces = Vec3.zero();
    }

}
