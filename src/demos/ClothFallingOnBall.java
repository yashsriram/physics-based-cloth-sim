package demos;

import camera.QueasyCam;
import math.Vec3;
import physical.Air;
import physical.Ball;
import physical.GridThreadPointMassSystem;
import physical.PointMass;
import processing.core.PApplet;

public class ClothFallingOnBall extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private Ball ball;

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
        float restLen = 3.5f;
        gridThreadPointMassSystem = new GridThreadPointMassSystem(
                this,
                M, N,
                10,
                restLen, 500, 500f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -70, 10f, -M * restLen / 2,
                (i, j, m, n) -> (false),
                GridThreadPointMassSystem.Layout.ZX);
        gridThreadPointMassSystem.air = new Air(0.05f, 0f, Vec3.of(0, 0, 1), 0);

        PointMass c1 = gridThreadPointMassSystem.pointMasses.get(0).get(0);
        PointMass c2 = gridThreadPointMassSystem.pointMasses.get(0).get(N - 1);
        PointMass c3 = gridThreadPointMassSystem.pointMasses.get(M - 1).get(0);
        PointMass c4 = gridThreadPointMassSystem.pointMasses.get(M - 1).get(N - 1);

        Vec3 center = c1.position.plus(c2.position).plus(c3.position).plus(c4.position).scale(0.25f).plus(Vec3.of(0, 50, 0));
        ball = new Ball(this, 1, 20, center, Vec3.of(255, 255, 0), true);
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 120; ++i) {
                gridThreadPointMassSystem.update(ball, 0.005f);
                ball.update(0.005f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridThreadPointMassSystem.draw();
        ball.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms");
    }

    public void keyPressed() {
        if (key == 'r') {
            resetSystem();
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.ClothFallingOnBall"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
