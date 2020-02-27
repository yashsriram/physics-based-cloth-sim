package physical;

import math.Vec3;
import processing.core.PApplet;

public class FireParticle {
    PApplet parent;
    Vec3 position;
    Vec3 velocity;
    Vec3 acceleration;
    Vec3 color;
    final float totalLifeSpan;
    int age;
    float radius;
    final float initialRadius;
    boolean isDead;

    public FireParticle(PApplet parent, Vec3 position, Vec3 velocity, Vec3 acceleration, int totalLifeSpan) {
        this.parent = parent;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.totalLifeSpan = totalLifeSpan;
        this.age = 0;
        this.isDead = false;
        this.radius = parent.random(2);
        this.initialRadius = radius;
    }

    public void update(float dt) {
        if (isDead) {
            return;
        }
        position.plusAccumulate(velocity.scale(dt));
        velocity.plusAccumulate(acceleration.scale(dt));
        age++;
        float ageFraction = age / totalLifeSpan;
        float factor = ageFraction * ageFraction;
        this.color = Vec3.of(255, 255 * factor, 255 * factor * ageFraction);
        radius = (1 - ageFraction) * initialRadius;
    }

    public void draw() {
        if (isDead) {
            return;
        }
        if (age > totalLifeSpan) {
            isDead = true;
            return;
        }
        parent.pushMatrix();
        parent.fill(color.x, color.y, color.z);
        parent.translate(position.x, position.y, position.z);
        parent.box(radius);
        parent.popMatrix();
    }
}
