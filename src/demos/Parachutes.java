package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Thread;
import physical.*;
import processing.core.PApplet;

class Parachute {
    final PApplet parent;
    final GridThreadMassSystem gridThreadMassSystem;
    PointMass payload;
    Thread thread1;
    Thread thread2;
    Thread thread3;
    Thread thread4;

    public Parachute(PApplet parent) {
        this.parent = parent;
        int M = 20;
        int N = 20;
        gridThreadMassSystem = new GridThreadMassSystem(
                parent,
                M, N,
                10,
                6, 50, 100f, parent.loadImage("parachute.jpg"),
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
                parent, 100,
                gridCenter,
                Vec3.zero(),
                Vec3.zero(),
                false);

        float restLen = payload.position.minus(c1.position).abs() + 5;

        thread1 = new Thread(parent, restLen, 20, 1000f, payload, c1);
        thread2 = new Thread(parent, restLen, 20, 1000f, payload, c2);
        thread3 = new Thread(parent, restLen, 20, 1000f, payload, c3);
        thread4 = new Thread(parent, restLen, 20, 1000f, payload, c4);
    }

    public void update(Ball ball) throws Exception {
        gridThreadMassSystem.update(ball, 0.006f);
        payload.update();
        payload.secondOrderIntegrate(0.006f);
    }

    public void draw() {
        gridThreadMassSystem.draw();
        payload.draw();
        thread1.draw();
        thread2.draw();
        thread3.draw();
        thread4.draw();
    }
}

public class Parachutes extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private Parachute parachute;
    Ball ball;


    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        parachute = new Parachute(this);
        ball = new Ball(this, 1, 20, parachute.payload.position.plus(Vec3.of(0, 0, 100)), Vec3.of(255, 255, 0), true);
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 100; ++i) {
                parachute.update(ball);
                ball.update(0.006f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        ball.draw();
        parachute.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " wind: " + parachute.gridThreadMassSystem.air.windSpeed);
    }

    public void keyPressed() {
        if (key == '=') {
            parachute.gridThreadMassSystem.air.increaseSpeed(0.2f);
        }
        if (key == '-') {
            parachute.gridThreadMassSystem.air.decreaseSpeed(0.2f);
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.Parachutes"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
