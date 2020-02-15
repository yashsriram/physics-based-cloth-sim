package physical;

import linalg.Vec3;
import processing.core.PApplet;

public class Ball {
    final PApplet parent;
    float radius;
    Vec3 position;
    Vec3 color;

    public Ball(PApplet parent, float radius, Vec3 position, Vec3 color) {
        this.parent = parent;
        this.radius = radius;
        this.position = position;
        this.color = color;
    }

    public void update(Vec3 velocity, float dt) {
        position = position.plus(velocity.scale(dt));
    }

    public void draw() {
        parent.pushMatrix();
        parent.noStroke();
        parent.fill(color.x, color.y, color.z);
        parent.translate(position.x, position.y, position.z);
        parent.sphere(radius);
        parent.popMatrix();
    }

}
