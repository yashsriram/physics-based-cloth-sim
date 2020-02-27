package demos;

import camera.QueasyCam;
import math.Vec3;
import physical.Air;
import physical.Ball;
import physical.GridThreadPointMassSystem;
import processing.core.PApplet;
import processing.core.PVector;

public class BurnableCloth extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private GridThreadPointMassSystem system;
    private Ball ball;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 2f;
        resetSystem();
    }

    private void resetSystem() {
        system = new GridThreadPointMassSystem(
                this,
                30, 30,
                30,
                5, 300, 900f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -20, -40f, -30f,
                ((i, j, m, n) -> (j == 0 && (i % 3 == 0 || i == m - 1))),
                GridThreadPointMassSystem.Layout.ZX);

        system.addSkipNodes();

        system.air = new Air(0.06f, 0f, Vec3.of(0, 0, 1), 0);
        ball = new Ball(this, 1, 10, Vec3.of(50, 90, 0), Vec3.of(255, 0, 0), true);
    }

    public void draw() {
        long start = millis();
        // update
        PVector aim = queasyCam.getAim(100);
        ball.update(Vec3.of(aim.x, aim.y, aim.z));
        try {
            for (int i = 0; i < 100; ++i) {
                system.updateWithBurnCheck(ball, 0.006f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        system.drawWithFireParticles();
        ball.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " wind : " + system.air.windSpeed);
    }

    public void keyPressed() {
        if (key == '=') {
            system.air.increaseSpeed(1f);
        }
        if (key == '-') {
            system.air.decreaseSpeed(1f);
        }
        if (key == 'r') {
            resetSystem();
        }
        if (key == 'b') {
            for (int i = 0; i < 2; ++i) {
                for (int j = 0; j < 2; ++j) {
                    system.pointMasses.get(10 + i).get(10 + j).setTemperature(10000);
                }
            }
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.BurnableCloth"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}