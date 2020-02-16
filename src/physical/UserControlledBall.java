package physical;

import linalg.Vec3;
import processing.core.PApplet;

public class UserControlledBall {
    final static Vec3 gravity = Vec3.of(0, 5, 0);
    final PApplet parent;
    float radius;
    final Vec3 initialPosition;
    Vec3 position;
    Vec3 velocity;
    Vec3 acceleration;
    Vec3 color;
    boolean isUnderFreeFall;

    public UserControlledBall(PApplet parent, float radius, Vec3 position, Vec3 color) {
        this.parent = parent;
        this.radius = radius;
        this.initialPosition = position;
        this.position = position;
        this.velocity = Vec3.zero();
        this.acceleration = gravity;
        this.color = color;
        this.isUnderFreeFall = false;
        parent.registerMethod("draw", this);
    }

    public void draw() {
        if (parent.keyPressed) {
            switch (parent.key) {
                case '8':
                    update(Vec3.of(0, 0, -10), 0.05f);
                    break;
                case '5':
                    update(Vec3.of(0, 0, 10), 0.05f);
                    break;
                case '4':
                    update(Vec3.of(-10, 0, 0), 0.05f);
                    break;
                case '6':
                    update(Vec3.of(10, 0, 0), 0.05f);
                    break;
                case '7':
                    update(Vec3.of(0, 10, 0), 0.05f);
                    break;
                case '9':
                    update(Vec3.of(0, -10, 0), 0.05f);
                    break;
                case 'r':
                    position = initialPosition;
                    velocity = Vec3.zero();
                    isUnderFreeFall = false;
                    break;
                case 'g':
                    isUnderFreeFall = true;
                    break;
                case 'h':
                    isUnderFreeFall = false;
                    break;
            }
        }
        if (isUnderFreeFall) {
            update(0.05f);
        }

        parent.pushMatrix();
        parent.noStroke();
        parent.fill(color.x, color.y, color.z);
        parent.translate(position.x, position.y, position.z);
        parent.sphere(radius);
        parent.popMatrix();
    }

    private void update(Vec3 instantaneousVelocity, float dt) {
        position = position.plus(instantaneousVelocity.scale(dt));
    }

    private void update(float dt) {
        position = position.plus(velocity.scale(dt));
        velocity = velocity.plus(acceleration.scale(dt));
    }
}
