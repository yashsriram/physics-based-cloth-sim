package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Air;
import physical.Ball;
import physical.GridThreadMassSystem;
import processing.core.PApplet;

public class ClothAirDrag extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;

    private GridThreadMassSystem gridThreadMassSystem;
    private Ball ball;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 2f;
        
        gridThreadMassSystem = new GridThreadMassSystem(
                this,
                30, 30,
                30,
                5, 300, 1500f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -20, -40f, -30f,
                ((i, j, m, n) -> (j == 0)),
                GridThreadMassSystem.Layout.ZX);
        
        gridThreadMassSystem.addSkipNodes();

        gridThreadMassSystem.air = new Air(0.08f, 0.08f, Vec3.of(0, 0, 1), 0);
        ball = new Ball(this, 1, 30, Vec3.of(50, 90, 0), Vec3.of(255, 255, 0));
    }

    public void draw() {
        long start = millis();
        // update
        try {
            for (int i = 0; i < 100; ++i) {
                gridThreadMassSystem.update(ball,0.006f);
                ball.update(0.006f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridThreadMassSystem.draw();
        ball.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " wind : " + gridThreadMassSystem.air.windSpeed);
    }

    public void keyPressed() {
        if (key == '=') {
            gridThreadMassSystem.air.increaseSpeed(1f);
        }
        if (key == '-') {
            gridThreadMassSystem.air.decreaseSpeed(1f);
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.ClothAirDrag"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}