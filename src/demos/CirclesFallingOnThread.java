package demos;

import math.Vec3;
import physical.Ball;
import physical.SeriesThreadMassSystem;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class CirclesFallingOnThread extends PApplet {
    private static class CircleSystem {
        final PApplet parent;
        final List<Ball> circles = new ArrayList<>();
        final Vec3 center;
        final float radius;
        final float mass;

        public CircleSystem(PApplet parent, Vec3 center, float radius, float mass) {
            this.parent = parent;
            this.center = center;
            this.radius = radius;
            this.mass = mass;
        }

        public void clearForce() {
            for (Ball circles : circles) {
                circles.clearExternalForces();
            }
        }

        public void update(float dt) {
            for (Ball circle : circles) {
                circle.update(dt);
            }
            // collision detection + response
            for (int i = 0; i < circles.size() - 1; ++i) {
                for (int j = i; j < circles.size(); ++j) {
                    Ball circle1 = circles.get(i);
                    Ball circle2 = circles.get(j);
                    Vec3 normal = circle2.position.minus(circle1.position);
                    Vec3 unitNormal = normal.unit();
                    // collision occurred
                    if (normal.abs() < circle1.radius + circle2.radius) {
                        // calculate velocities along normals
                        Vec3 circle1VelocityAlongNormal = unitNormal.scale(unitNormal.dot(circle1.velocity));
                        Vec3 circle2VelocityAlongNormal = unitNormal.scale(unitNormal.dot(circle2.velocity));
                        // exchange the velocities along normals
                        circle1.velocity.minusAccumulate(circle1VelocityAlongNormal).plusAccumulate(circle2VelocityAlongNormal);
                        circle2.velocity.minusAccumulate(circle2VelocityAlongNormal).plusAccumulate(circle1VelocityAlongNormal);
                    }
                }
            }
        }

        public void draw() {
            for (Ball circle : circles) {
                circle.draw2D();
            }
            parent.rectMode(CENTER);
            parent.square(center.x, center.y, radius);
        }

        public void spawnBall(int n) {
            for (int i = 0; i < n; i++) {
                circles.add(
                        new Ball(
                                parent,
                                mass,
                                radius,
                                Vec3.of(center.x, center.y + i * (2 * radius + 10), 0),
                                Vec3.of(128).plusAccumulate(Vec3.sampleOnSphere(128)),
                                false
                        )
                );
            }
        }

        public void slideSource(float dx) {
            center.x += dx;
        }
    }

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private CircleSystem circleSystem;
    private SeriesThreadMassSystem seriesThreadMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P2D);
    }

    public void setup() {
        surface.setTitle("Processing");
        resetSystem();
    }

    private void resetSystem() {
        int numMasses = 100;
        int restLen = 5;
        seriesThreadMassSystem = SeriesThreadMassSystem.horizontal(this,
                WIDTH, HEIGHT,
                numMasses, 20,
                restLen, 500, 700,
                (i, m) -> (i == 0 || i == m - 1));
        circleSystem = new CircleSystem(this,
                Vec3.of((WIDTH - numMasses * restLen) / 2f + 100, 10f, 0),
                30, 80);
    }

    public void draw() {
        if (keyPressed) {
            if (keyCode == LEFT) {
                circleSystem.slideSource(-7);
            }
            if (keyCode == RIGHT) {
                circleSystem.slideSource(7);
            }
        }

        long start = millis();
        // update
        try {
            for (int i = 0; i < 100; ++i) {
                circleSystem.clearForce();
                seriesThreadMassSystem.update(circleSystem.circles, 0.01f);
                circleSystem.update(0.01f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        seriesThreadMassSystem.draw2D();
        circleSystem.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " #balls: " + circleSystem.circles.size() + " Update: " + (update - start) + "ms" + " Draw " + (draw - update) + "ms");
    }

    public void keyPressed() {
        if (key == 'n') {
            circleSystem.spawnBall(1);
        }
        if (key == 'r') {
            resetSystem();
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.CirclesFallingOnThread"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
