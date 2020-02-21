package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Air;
import physical.Ball;
import physical.GridThreadMassSystem;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

class BallSystem {
    final PApplet parent;
    final List<Ball> balls = new ArrayList<>();

    public BallSystem(PApplet parent) {
        this.parent = parent;
        spawnBall();
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
        int numBalls = balls.size();
        for (int i = 0; i < numBalls - 1; ++i) {
            for (int j = i; j < numBalls; ++j) {
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

    public void spawnBall() {
        balls.add(
                new Ball(
                        parent, 5, 6,
                        Vec3.of(-40, -80, 0),
                        Vec3.of(128).plusAccumulate(Vec3.sampleOnSphere(128)),
                        false
                )
        );
    }
}

public class BallsFallingOnCloth extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private BallSystem ballSystem;

    private GridThreadMassSystem gridThreadMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);

        gridThreadMassSystem = new GridThreadMassSystem(
                this,
                30, 30,
                6,
                4, 300, 1000f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -100, 30f, -60f,
                (i, j, m, n) -> (
                        (j == 0 && i % 3 == 0)
                                || (j % 3 == 0 && i == 0)
                                || (j % 3 == 0 && i == m - 1)
                                || (i % 3 == 0 && j == n - 1)
                ),
                GridThreadMassSystem.Layout.ZX);

        gridThreadMassSystem.air = new Air(0.02f, 0.4f, Vec3.of(0, 0, 1), 0);

        Ball.userControlVelocity = 5;
        ballSystem = new BallSystem(this);
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 140; ++i) {
                ballSystem.clearForce();
                gridThreadMassSystem.update(ballSystem.balls, 0.0035f);
                ballSystem.update(0.0035f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridThreadMassSystem.draw();
        ballSystem.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " #balls: " + ballSystem.balls.size());
    }

    public void keyPressed() {
        if (key == 'n') {
            ballSystem.spawnBall();
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
