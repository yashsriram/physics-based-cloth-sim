package physical;

import math.Integrator;
import math.Vec3;
import processing.core.PApplet;

public class Ball {
    public static final float ballFrictionConstant = 0.7f;
    public static float userControlVelocity = 1;
    public static Vec3 gravity = Vec3.of(0, .5, 0);

    final PApplet parent;

    public float radius;
    final Vec3 initialPosition;
    public Vec3 position;
    public Vec3 velocity;

    float mass;
    Vec3 externalForces;

    Vec3 color;

    boolean isPaused;

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

    public void update(Vec3 position) {
        this.position = position;
    }

    public void update(float dt) {
        if (parent.keyPressed) {
            switch (parent.key) {
                case '4':
                    firstOrder(Vec3.of(0, 0, -userControlVelocity), dt);
                    break;
                case '6':
                    firstOrder(Vec3.of(0, 0, userControlVelocity), dt);
                    break;
                case '5':
                    firstOrder(Vec3.of(-userControlVelocity, 0, 0), dt);
                    break;
                case '8':
                    firstOrder(Vec3.of(userControlVelocity, 0, 0), dt);
                    break;
                case '7':
                    firstOrder(Vec3.of(0, userControlVelocity, 0), dt);
                    break;
                case '9':
                    firstOrder(Vec3.of(0, -userControlVelocity, 0), dt);
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
            firstOrder(dt);
        }
    }

    private void firstOrder(float dt) {
        Vec3 totalForce = externalForces.plus(gravity.scale(mass));
        Vec3 acceleration = totalForce.scale(1 / mass);
        Integrator.firstOrder(position, velocity, acceleration, dt);
    }

    private void firstOrder(Vec3 instantaneousVelocity, float dt) {
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

    public void draw2D() {
        parent.pushMatrix();
        parent.noStroke();
        parent.fill(color.x, color.y, color.z);
        parent.circle(position.x, position.y, radius);
        parent.popMatrix();
    }

    public void accumulateSpringMassForce(Vec3 force) {
        externalForces.plusAccumulate(force);
    }

    public void clearExternalForces() {
        externalForces = Vec3.zero();
    }

}
