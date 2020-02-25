package demos;

import camera.QueasyCam;
import math.Vec3;
import physical.Air;
import physical.Ball;
import physical.Cutter;
import physical.GridThreadPointMassSystem;
import processing.core.PApplet;
import processing.core.PShape;

public class SwordCutCloth extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;

    private GridThreadPointMassSystem gridThreadPointMassSystem;
    private Ball ball;
    private Cutter cutter;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");

        queasyCam = new QueasyCam(this);
        queasyCam.speed = 2f;

        resetSystem();
        ball = new Ball(this, 1, 30, Vec3.of(50, 90, 0), Vec3.of(255, 255, 0), true);

        PShape cutterShape = loadShape("Sword_2.obj");
        cutterShape.scale(10);
        cutter = new Cutter(this, cutterShape, queasyCam);
    }

    public void resetSystem() {
        gridThreadPointMassSystem = new GridThreadPointMassSystem(
                this,
                30, 30,
                30,
                5, 300, 1500f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -20, -40f, -30f,
                ((i, j, m, n) -> (j == 0)),
                GridThreadPointMassSystem.Layout.ZX);

        gridThreadPointMassSystem.air = new Air(0.08f, 0f, Vec3.of(0, 0, 1), 0);
    }

    public void draw() {
        long start = millis();
        // update
        try {
            cutter.update();
            for (int i = 0; i < 100; ++i) {
                gridThreadPointMassSystem.update(ball, 0.005f);
                ball.update(0.005f);
            }
            cutter.cut(gridThreadPointMassSystem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridThreadPointMassSystem.draw();
        ball.draw();
        cutter.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " wind : " + gridThreadPointMassSystem.air.windSpeed);
    }

    public void keyPressed() {
        if (key == '=') {
            gridThreadPointMassSystem.air.increaseSpeed(1f);
        }
        if (key == '-') {
            gridThreadPointMassSystem.air.decreaseSpeed(1f);
        }
        if (key == 'x') {
            cutter.toggleIsPaused();
        }
        if (key == 'r') {
            resetSystem();
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.SwordCutCloth"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}