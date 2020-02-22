package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Air;
import physical.Ball;
import physical.GridThreadPointMassSystem;
import processing.core.PApplet;

public class BallFallingOnCloth extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    //    private LiamCam liamCam;
    private QueasyCam queasyCam;
    private Ball ball;

    private GridThreadPointMassSystem gridThreadPointMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
//        liamCam = new LiamCam(this);
        queasyCam = new QueasyCam(this);

        gridThreadPointMassSystem = new GridThreadPointMassSystem(
                this,
                30, 30,
                6,
                4, 500, 1000f, loadImage("aladdin-s-carpet.jpeg"),
                1.05f, -100, 30f, -30f,
                (i, j, m, n) -> ((j == 0 && i == m - 1) || (i == 0 && j == n - 1) || (i == m - 1 && j == n - 1) || (i == 0 && j == 0)),
                GridThreadPointMassSystem.Layout.ZX);

        gridThreadPointMassSystem.air = new Air(0.02f, 0.4f, Vec3.of(0, 0, 1), 0);

        ball = new Ball(this, 1000, 10, Vec3.of(-70, 0, 0), Vec3.of(255, 255, 0), true);
        Ball.userControlVelocity = 5;
    }

    public void draw() {
//        liamCam.Update(1.0f / frameRate);

        long start = millis();
        // update
        try {
            for (int i = 0; i < 140; ++i) {
                ball.clearExternalForces();
                gridThreadPointMassSystem.update(ball, 0.002f);
                ball.update(0.002f);
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
//        liamCam.HandleKeyPressed();
    }

    public void keyReleased() {
//        liamCam.HandleKeyReleased();
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.BallFallingOnCloth"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}