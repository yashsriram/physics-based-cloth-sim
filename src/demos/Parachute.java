package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Thread;
import physical.*;
import processing.core.PApplet;

public class Parachute extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;

    private GridThreadMassSystem gridThreadMassSystem;
    private PointMass payload;
    private Ball ball;
    private Thread thread1;
    private Thread thread2;
    private Thread thread3;
    private Thread thread4;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);

        int M = 20;
        int N = 20;
        gridThreadMassSystem = new GridThreadMassSystem(
                this,
                M, N,
                10,
                6, 50, 100f, loadImage("parachute.jpg"),
                1f, -100, -50f, -30f,
                (i, j, m, n) -> (false),
                GridThreadMassSystem.Layout.ZX);
        gridThreadMassSystem.air = new Air(0.1f, 0.5f, Vec3.of(0, -1, 0), 0);

        PointMass c1 = gridThreadMassSystem.pointMasses.get(0).get(0);
        PointMass c2 = gridThreadMassSystem.pointMasses.get(0).get(N - 1);
        PointMass c3 = gridThreadMassSystem.pointMasses.get(M - 1).get(0);
        PointMass c4 = gridThreadMassSystem.pointMasses.get(M - 1).get(N - 1);

        Vec3 gridCenter = c1.position.plus(c2.position).plus(c3.position).plus(c4.position).scale(0.25f);

        payload = new PointMass(
                this, 100,
                gridCenter,
                Vec3.zero(),
                Vec3.zero(),
                false);

        float restLen = payload.position.minus(c1.position).abs() + 5;

        thread1 = new Thread(this, restLen, 20, 1000f, payload, c1);
        thread2 = new Thread(this, restLen, 20, 1000f, payload, c2);
        thread3 = new Thread(this, restLen, 20, 1000f, payload, c3);
        thread4 = new Thread(this, restLen, 20, 1000f, payload, c4);

        ball = new Ball(this, 1, 20, gridCenter.plus(Vec3.of(0, 0, 100)), Vec3.of(255, 255, 0), true);
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 100; ++i) {
                gridThreadMassSystem.update(ball, 0.006f);
                ball.update(0.006f);
                payload.update();
                payload.secondOrderIntegrate(0.006f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridThreadMassSystem.draw();
        ball.draw();
        payload.draw();
        thread1.draw();
        thread2.draw();
        thread3.draw();
        thread4.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " wind: " + gridThreadMassSystem.air.windSpeed);
    }

    public void keyPressed() {
        if (key == '=') {
            gridThreadMassSystem.air.increaseSpeed(0.2f);
        }
        if (key == '-') {
            gridThreadMassSystem.air.decreaseSpeed(0.2f);
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.Parachute"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
