package demos;

import camera.QueasyCam;
import math.Vec3;
import physical.Ball;
import physical.Ground;
import physical.VolumeSpringPointMassSystem;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class DeformableObjectAndBalls extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private VolumeSpringPointMassSystem volumeSpringPointMassSystem;
    private List<Ball> balls = new ArrayList<>();
    private Ground ground;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 2f;
        ground = new Ground(this,
                Vec3.of(0, 100, 0), Vec3.of(0, 0, 1), Vec3.of(1, 0, 0),
                400, 400,
                loadImage("ground.jpg"));
        balls.add(new Ball(this, 10, 12, Vec3.of(10, -75, 10), Vec3.of(128, 0, 0), true));
        balls.add(new Ball(this, 10, 12, Vec3.of(20, 0, 50), Vec3.of(128, 128, 0), true));
        balls.add(new Ball(this, 10, 12, Vec3.of(20, 0, 75), Vec3.of(0, 128, 0), true));
        resetSystem();
    }

    private void resetSystem() {
        volumeSpringPointMassSystem = new VolumeSpringPointMassSystem(
                this,
                4, 4, 10,
                200,
                10, 250, 250f,
                0.9f, 0, -150, 0,
                ((i, j, k, m, n, o) -> false)
        );
    }

    public void draw() {
        long start = millis();
        // update
        try {
            for (int i = 0; i < 300; ++i) {
                volumeSpringPointMassSystem.update(balls, ground, 0.003f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        volumeSpringPointMassSystem.draw();
        for (Ball b: balls) {
            b.draw();
        };
        ground.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms");
    }

    public void keyPressed() {
        if (key == 'r') {
            resetSystem();
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.DeformableObjectAndBalls"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}