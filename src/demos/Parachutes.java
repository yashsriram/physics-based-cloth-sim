package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Thread;
import physical.*;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

class SkyDiver {
    final PApplet parent;
    GridThreadMassSystem gridThreadMassSystem;
    PointMass payload;
    PShape payloadShape;
    Thread thread1;
    Thread thread2;
    Thread thread3;
    Thread thread4;
    final int M = 20;
    final int N = 20;
    final float restLenGrid = 4;
    final float extensionFactor = 1f;

    public SkyDiver(PApplet parent, Vec3 initialPayloadPosition, PShape payloadShape) {
        this.parent = parent;
        this.payloadShape = payloadShape;

        PointMass.gravity = Vec3.of(0, .25, 0);
        payload = new PointMass(
                parent,
                100,
                initialPayloadPosition,
                Vec3.zero(),
                Vec3.zero(),
                false);
        newParachute();
    }

    public void update(Ball ball) throws Exception {
        gridThreadMassSystem.update(ball, 0.006f);
        payload.update();
        payload.secondOrderIntegrate(0.006f);
    }

    public void draw() {
        gridThreadMassSystem.draw();
        drawPayload();
        thread1.draw();
        thread2.draw();
        thread3.draw();
        thread4.draw();
    }

    private void drawPayload() {
        parent.pushMatrix();
        parent.translate(payload.position.x, payload.position.y, payload.position.z);
        parent.shape(payloadShape);
        parent.popMatrix();
    }

    public void newParachute() {
        gridThreadMassSystem = new GridThreadMassSystem(
                parent,
                M, N,
                10,
                restLenGrid, 50, 100f, parent.loadImage("parachute.jpg"),
                extensionFactor,
                payload.position.x - (M * restLenGrid * extensionFactor) / 2,
                payload.position.y,
                payload.position.z - (N * restLenGrid * extensionFactor) / 2,
                (i, j, m, n) -> (false),
                GridThreadMassSystem.Layout.ZX);
        gridThreadMassSystem.air = new Air(0.05f, 0.5f, Vec3.of(0, -1, 0), 0);

        PointMass c1 = gridThreadMassSystem.pointMasses.get(0).get(0);
        PointMass c2 = gridThreadMassSystem.pointMasses.get(0).get(N - 1);
        PointMass c3 = gridThreadMassSystem.pointMasses.get(M - 1).get(0);
        PointMass c4 = gridThreadMassSystem.pointMasses.get(M - 1).get(N - 1);

        float restLen = payload.position.minus(c1.position).abs() + 5;

        thread1 = new Thread(parent, restLen, 20, 1000f, payload, c1);
        thread2 = new Thread(parent, restLen, 20, 1000f, payload, c2);
        thread3 = new Thread(parent, restLen, 20, 1000f, payload, c3);
        thread4 = new Thread(parent, restLen, 20, 1000f, payload, c4);
    }

    public void breakParachute() {
        thread1.setBroken(true);
        thread2.setBroken(true);
        thread3.setBroken(true);
        thread4.setBroken(true);
    }
}

public class Parachutes extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private SkyDiver skyDiver;
    Ball ball;


    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        PShape skyDiver = loadShape("mario/Mario.obj");
        skyDiver.rotateX(PConstants.PI / 2);
        skyDiver.rotateY(-PConstants.PI / 2);
        skyDiver.scale(10f);
        this.skyDiver = new SkyDiver(this, Vec3.of(100, 0, 0), skyDiver);
        ball = new Ball(
                this,
                1,
                5,
                this.skyDiver.payload.position.plus(Vec3.of(0, 0, 100)),
                Vec3.of(255, 255, 0),
                true
        );
    }

    public void draw() {
        PVector aim = queasyCam.getAim(100);
        ball.position = Vec3.of(aim.x, aim.y, aim.z);

        long start = millis();
        // update
        try {
            for (int i = 0; i < 100; ++i) {
                skyDiver.update(ball);
//                ball.update(0.006f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        ball.draw();
        skyDiver.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " wind: " + skyDiver.gridThreadMassSystem.air.windSpeed);
    }

    public void keyPressed() {
        if (key == '=') {
            skyDiver.gridThreadMassSystem.air.increaseSpeed(0.2f);
        }
        if (key == '-') {
            skyDiver.gridThreadMassSystem.air.decreaseSpeed(0.2f);
        }
        if (key == 'p') {
            skyDiver.newParachute();
        }
        if (key == 'b') {
            skyDiver.breakParachute();
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
