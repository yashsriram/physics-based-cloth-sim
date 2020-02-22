package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Air;
import physical.Ball;
import physical.GridThreadPointMassSystem;
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

        gridThreadPointMassSystem = new GridThreadPointMassSystem(
                this,
                30, 30,
                10,
                2, 500, 1000f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -100, 10f, -30f,
                (i, j, m, n) -> (false),
                GridThreadPointMassSystem.Layout.ZX);
        gridThreadPointMassSystem.air = new Air(0.08f, 0.08f, Vec3.of(0, 0, 1), 0);

        ball = new Ball(this, 1, 20, Vec3.of(-70, 70, 0), Vec3.of(255, 255, 0), true);
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 140; ++i) {
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

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.ClothFallingOnBall"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
