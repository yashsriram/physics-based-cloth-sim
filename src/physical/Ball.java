package physical;

import linalg.Vec3;
import processing.core.PApplet;

public class Ball {
    final static Vec3 gravity = Vec3.of(0, .5, 0);
    final PApplet parent;
    float radius;
    final Vec3 initialPosition;
    Vec3 position;
    Vec3 velocity;
    Vec3 acceleration;
    Vec3 color;
    boolean isPaused;

    public Ball(PApplet parent, float radius, Vec3 position, Vec3 color) {
        this.parent = parent;
        this.radius = radius;
        this.initialPosition = position;
        this.position = position;
        this.velocity = Vec3.zero();
        this.acceleration = gravity;
        this.color = color;
        this.isPaused = true;
    }

    public void update(float dt) {
        if (parent.keyPressed) {
            switch (parent.key) {
                case '8':
                    eularianIntegrate(Vec3.of(0, 0, -1), dt);
                    break;
                case '5':
                    eularianIntegrate(Vec3.of(0, 0, 1), dt);
                    break;
                case '4':
                    eularianIntegrate(Vec3.of(-1, 0, 0), dt);
                    break;
                case '6':
                    eularianIntegrate(Vec3.of(1, 0, 0), dt);
                    break;
                case '7':
                    eularianIntegrate(Vec3.of(0, 1, 0), dt);
                    break;
                case '9':
                    eularianIntegrate(Vec3.of(0, -1, 0), dt);
                    break;
                case 'r':
                    position = initialPosition;
                    velocity = Vec3.zero();
                    isPaused = true;
                    break;
                case 'g':
                    isPaused = false;
                    break;
                case 'h':
                    isPaused = true;
                    break;
            }
        }
        if (!isPaused) {
            eularianIntegrate(dt);
        }
    }

    private void eularianIntegrate(float dt) {
        position = position.plus(velocity.scale(dt));
        velocity = velocity.plus(acceleration.scale(dt));
    }

    private void eularianIntegrate(Vec3 instantaneousVelocity, float dt) {
        position = position.plus(instantaneousVelocity.scale(dt));
    }

    public void draw() {
        parent.pushMatrix();
        parent.noStroke();
        parent.fill(color.x, color.y, color.z);
        parent.translate(position.x, position.y, position.z);
        parent.sphere(radius);
        parent.popMatrix();
    }

    public void accumulateForce(Vec3 force) {

    }
}
