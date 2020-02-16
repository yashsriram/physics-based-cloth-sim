package physical;

import linalg.Vec3;
import processing.core.PApplet;

public class UserControlledBall {
    final PApplet parent;
    float radius;
    Vec3 position;
    Vec3 color;

    public UserControlledBall(PApplet parent, float radius, Vec3 position, Vec3 color) {
        this.parent = parent;
        this.radius = radius;
        this.position = position;
        this.color = color;
        parent.registerMethod("draw", this);
    }

    public void update(Vec3 velocity, float dt) {
        position = position.plus(velocity.scale(dt));
    }

    public void draw() {
        // ball update
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
            }
        }

        parent.pushMatrix();
        parent.noStroke();
        parent.fill(color.x, color.y, color.z);
        parent.translate(position.x, position.y, position.z);
        parent.sphere(radius);
        parent.popMatrix();
    }

}
