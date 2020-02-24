package demos;

import camera.QueasyCam;
import linalg.Vec3;
import physical.Thread;
import physical.*;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Parachutes extends PApplet {
    private static class SkyDiver {
        final PApplet parent;
        Air air;
        GridThreadPointMassSystem gridThreadPointMassSystem;
        PointMass payload;
        PShape payloadShape;
        Thread thread1;
        Thread thread2;
        Thread thread3;
        Thread thread4;
        final int M = 13;
        final int N = 30;
        final float restLenGrid = 4;
        final float extensionFactor = 1f;

        public SkyDiver(PApplet parent, Vec3 initialPayloadPosition, PShape payloadShape, Air air) {
            this.parent = parent;
            this.payloadShape = payloadShape;
            this.air = air;

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
            gridThreadPointMassSystem.update(ball, 0.006f);
            payload.update();
            payload.secondOrderIntegrate(0.006f);
        }

        public void draw() {
            gridThreadPointMassSystem.draw();
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
            // Add parachute
            gridThreadPointMassSystem = new GridThreadPointMassSystem(
                    parent,
                    M, N,
                    10,
                    restLenGrid, 50, 100f, parent.loadImage("parachute.jpg"),
                    extensionFactor,
                    payload.position.x - (M * restLenGrid * extensionFactor) / 2,
                    payload.position.y,
                    payload.position.z - (N * restLenGrid * extensionFactor) / 2,
                    (i, j, m, n) -> (false),
                    GridThreadPointMassSystem.Layout.ZX);

            // Add air
            gridThreadPointMassSystem.air = air;

            // Get corners
            PointMass c1 = gridThreadPointMassSystem.pointMasses.get(0).get(0);
            PointMass c2 = gridThreadPointMassSystem.pointMasses.get(0).get(N - 1);
            PointMass c3 = gridThreadPointMassSystem.pointMasses.get(M - 1).get(0);
            PointMass c4 = gridThreadPointMassSystem.pointMasses.get(M - 1).get(N - 1);

            // Connect payload and corners with threads
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
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private Air air;
    private SkyDiver skyDiver;
    Ball ball;


    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");

        queasyCam = new QueasyCam(this);

        air = new Air(0.05f, 0.5f, Vec3.of(0, -1, 0), 0);

        PShape skyDiverShape = loadShape("mario/Mario.obj");
        skyDiverShape.rotateX(PConstants.PI / 2);
        skyDiverShape.rotateY(-PConstants.PI / 2);
        skyDiverShape.scale(10f);
        skyDiver = new SkyDiver(this, Vec3.of(100, 0, 0), skyDiverShape, air);

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

        long start = millis();
        // update
        PVector aim = queasyCam.getAim(100);
        ball.update(Vec3.of(aim.x, aim.y, aim.z));
        try {
            for (int i = 0; i < 100; ++i) {
                skyDiver.update(ball);
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

        surface.setTitle("Processing - wind: " + air.windSpeed + " FPS: " + Math.round(frameRate) + " Draw: " + (draw - update) + "ms Update: " + (update - start) + "ms");
    }

    public void keyPressed() {
        if (key == '=') {
            skyDiver.gridThreadPointMassSystem.air.increaseSpeed(0.2f);
        }
        if (key == '-') {
            skyDiver.gridThreadPointMassSystem.air.decreaseSpeed(0.2f);
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
