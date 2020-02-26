package demos;

import camera.QueasyCam;
import math.Vec3;
import physical.Ball;
import physical.Ground;
import physical.VolumeSpringPointMassSystem;
import processing.core.PApplet;
import processing.core.PVector;

public class DeformableObject extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private VolumeSpringPointMassSystem volumeSpringPointMassSystem;
    private Ball ball;
    private Ground ground;
    private boolean isBallFixedToCam;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 1f;
        queasyCam.speed = 2f;
        ground = new Ground(this,
                Vec3.of(0, 100, 0), Vec3.of(0, 0, 1), Vec3.of(1, 0, 0),
                400, 400,
                loadImage("ground.jpg"));
        ball = new Ball(this, 10, 12, Vec3.of(4, 75, 0), Vec3.of(255, 255, 255), true);
        isBallFixedToCam = false;
        resetSystem();
    }

    private void resetSystem() {
        volumeSpringPointMassSystem = new VolumeSpringPointMassSystem(
                this,
                4, 10, 4,
                200,
                10, 250, 250f,
                0.9f, 0, -50, 0,
                loadImage("jelly-blue.jpg"),
                loadImage("jelly-green.jpg"),
                ((i, j, k, m, n, o) -> false)
        );
    }

    public void draw() {
        long start = millis();
        // update
        if (isBallFixedToCam) {
            PVector aim = queasyCam.getAim(200);
            ball.update(Vec3.of(aim.x , aim.y, aim.z));
        }
        try {
            for (int i = 0; i < 300; ++i) {
                volumeSpringPointMassSystem.update(ball, ground, 0.003f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        volumeSpringPointMassSystem.draw();
        ball.draw();
        ground.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms");
    }

    public void keyPressed() {
        if (key == 'r') {
            resetSystem();
        }
        if (key == 'p') {
            isBallFixedToCam = !isBallFixedToCam;
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.DeformableObject"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}