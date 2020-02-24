package physical;

import math.Integrator;
import math.Vec2;
import math.Vec3;
import processing.core.PApplet;

public class Circle {
    public static final float ballFrictionConstant = 0.7f;
    public static float userControlVelocity = 200;
    public static Vec2 gravity = Vec2.of(0, .5);

    final PApplet parent;

    public float radius;
    final Vec2 initialPosition;
    public Vec2 position;
    public Vec2 velocity;

    float mass;
    Vec2 externalForces;

    Vec3 color;

    boolean isPaused;

    public Circle(PApplet parent, float mass, float radius, Vec2 position, Vec3 color, boolean isPaused) {
        this.parent = parent;
        this.mass = mass;
        this.radius = radius;
        this.initialPosition = position;
        this.position = position;
        this.velocity = Vec2.zero();
        this.color = color;
        this.isPaused = isPaused;
        this.externalForces = Vec2.zero();
    }

    public void update(Vec2 position) {
        this.position = position;
    }

    public void update(float dt) {
        if (parent.keyPressed) {
            switch (parent.key) {
                case '4':
                    firstOrder(Vec2.of(-userControlVelocity, 0), dt);
                    break;
                case '6':
                    firstOrder(Vec2.of(userControlVelocity, 0), dt);
                    break;
                case '5':
                    firstOrder(Vec2.of(0, userControlVelocity), dt);
                    break;
                case '8':
                    firstOrder(Vec2.of(0, -userControlVelocity), dt);
                    break;
                case 'r':
                    position = initialPosition;
                    velocity = Vec2.zero();
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
            firstOrder(dt);
        }
    }

    private void firstOrder(float dt) {
        Vec2 totalForce = externalForces.plus(gravity.scale(mass));
        Vec2 acceleration = totalForce.scale(1 / mass);
        Integrator.firstOrder(position, velocity, acceleration, dt);
    }

    private void firstOrder(Vec2 instantaneousVelocity, float dt) {
        position.plusAccumulate(instantaneousVelocity.scale(dt));
    }

    public void draw() {
        parent.pushMatrix();
        parent.noStroke();
        parent.fill(color.x, color.y, color.z);
        parent.circle(position.x, position.y, radius);
        parent.popMatrix();
    }

    public void accumulateSpringMassForce(Vec2 force) {
        externalForces.plusAccumulate(force);
    }

    public void clearExternalForces() {
        externalForces = Vec2.zero();
    }

}
