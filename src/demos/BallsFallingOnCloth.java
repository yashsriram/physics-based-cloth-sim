package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Air;
import physical.Ball;
import physical.GridThreadPointMassSystem;
import physical.PointMass;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class BallsFallingOnCloth extends PApplet {
    private static class BallSystem {
        final PApplet parent;
        final List<Ball> balls = new ArrayList<>();
        final Vec3 center;
        final float radius;
        final float mass;

        public BallSystem(PApplet parent, Vec3 center, float radius, float mass) {
            this.parent = parent;
            this.center = center;
            this.radius = radius;
            this.mass = mass;
            spawnBall(3);
        }

        public void clearForce() {
            for (Ball ball : balls) {
                ball.clearExternalForces();
            }
        }

        public void update(float dt) {
            for (Ball ball : balls) {
                ball.update(dt);
            }
            // collision detection + response
            for (int i = 0; i < balls.size() - 1; ++i) {
                for (int j = i; j < balls.size(); ++j) {
                    Ball ball1 = balls.get(i);
                    Ball ball2 = balls.get(j);
                    Vec3 normal = ball2.position.minus(ball1.position);
                    Vec3 unitNormal = normal.unit();
                    // collision occurred
                    if (normal.abs() < ball1.radius + ball2.radius) {
                        // calculate velocities along normals
                        Vec3 ball1VelocityAlongNormal = unitNormal.scale(unitNormal.dot(ball1.velocity));
                        Vec3 ball2VelocityAlongNormal = unitNormal.scale(unitNormal.dot(ball2.velocity));
                        // exchange the velocities along normals
                        ball1.velocity.minusAccumulate(ball1VelocityAlongNormal).plusAccumulate(ball2VelocityAlongNormal);
                        ball2.velocity.minusAccumulate(ball2VelocityAlongNormal).plusAccumulate(ball1VelocityAlongNormal);
                    }
                }
            }
        }

        public void draw() {
            for (Ball ball : balls) {
                ball.draw();
            }
        }

        public void spawnBall(int n) {
            for (int i = 0; i < n; i++) {
                balls.add(
                        new Ball(
                                parent,
                                mass,
                                radius,
                                Vec3.of(center.x, center.y - 20 * radius + i * (2 * radius + 1), center.z),
                                Vec3.of(128).plusAccumulate(Vec3.sampleOnSphere(128)),
                                false
                        )
                );
            }
        }
    }

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private BallSystem ballSystem;
    private GridThreadPointMassSystem gridThreadPointMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        resetSystem();
    }

    private void resetSystem() {
        int M = 30;
        int N = 30;
        float restLen = 5f;
        gridThreadPointMassSystem = new GridThreadPointMassSystem(
                this,
                M, N,
                10,
                restLen, 300, 300f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -80, 30f, -M * restLen / 2,
                (i, j, m, n) -> (
                        (j == 0 && i % 3 == 0)
                                || (j % 3 == 0 && i == 0)
                                || (j % 3 == 0 && i == m - 1)
                                || (i % 3 == 0 && j == n - 1)
                ),
                GridThreadPointMassSystem.Layout.ZX);

        gridThreadPointMassSystem.air = new Air(0.02f, 0.4f, Vec3.of(0, 0, 1), 0);

        PointMass c1 = gridThreadPointMassSystem.pointMasses.get(0).get(0);
        PointMass c2 = gridThreadPointMassSystem.pointMasses.get(0).get(N - 1);
        PointMass c3 = gridThreadPointMassSystem.pointMasses.get(M - 1).get(0);
        PointMass c4 = gridThreadPointMassSystem.pointMasses.get(M - 1).get(N - 1);

        Vec3 center = c1.position.plus(c2.position).plus(c3.position).plus(c4.position).scale(0.25f);

        Ball.userControlVelocity = 5;
        ballSystem = new BallSystem(this, center, 6, 5);
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 60; ++i) {
                ballSystem.clearForce();
                gridThreadPointMassSystem.update(ballSystem.balls, 0.01f);
                ballSystem.update(0.01f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridThreadPointMassSystem.draw();
        ballSystem.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " #balls: " + ballSystem.balls.size());
    }

    public void keyPressed() {
        if (key == 'n') {
            ballSystem.spawnBall(3);
        }
        if (key == 'r') {
            resetSystem();
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.BallsFallingOnCloth"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
