package demos;

import camera.LiamCam;
import math.Vec3;
import physical.SeriesThreadMassSystem;
import physical.PointMass;
import processing.core.PApplet;

public class Checkin extends PApplet {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;

    private LiamCam liamCam;

    private SeriesThreadMassSystem seriesThreadMassSystem1;
    private SeriesThreadMassSystem seriesThreadMassSystem2;
    private SeriesThreadMassSystem seriesThreadMassSystem3;
    private SeriesThreadMassSystem seriesThreadMassSystem4;
    private SeriesThreadMassSystem seriesThreadMassSystem5;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
        liamCam = new LiamCam(this);
        PointMass.gravity = Vec3.of(0, 5, 0);
        seriesThreadMassSystem1 = new SeriesThreadMassSystem(this, Vec3.of(-60, -60, -200), 4, 120, 20, 200, 30f);
        seriesThreadMassSystem2 = new SeriesThreadMassSystem(this, Vec3.of(-30, -60, -200), 4, 60, 20, 200, 30f);
        seriesThreadMassSystem3 = new SeriesThreadMassSystem(this, Vec3.of(0, -60, -200), 4, 10, 20, 200, 30f);
        seriesThreadMassSystem4 = new SeriesThreadMassSystem(this, Vec3.of(30, -60, -200), 4, 60, 20, 200, 30f);
        seriesThreadMassSystem5 = new SeriesThreadMassSystem(this, Vec3.of(60, -60, -200), 4, 120, 20, 200, 30f);
    }

    public void draw() {
        liamCam.Update(1.0f / frameRate);
        surface.setTitle("Processing FPS: " + frameRate);

        try {
            for (int i = 0; i < 20; ++i) {
                seriesThreadMassSystem1.update(0.01f);
                seriesThreadMassSystem2.update(0.01f);
                seriesThreadMassSystem3.update(0.01f);
                seriesThreadMassSystem4.update(0.01f);
                seriesThreadMassSystem5.update(0.01f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        background(0);
        seriesThreadMassSystem1.draw();
        seriesThreadMassSystem2.draw();
        seriesThreadMassSystem3.draw();
        seriesThreadMassSystem4.draw();
        seriesThreadMassSystem5.draw();
    }

    public void keyPressed() {
        liamCam.HandleKeyPressed();
    }

    public void keyReleased() {
        liamCam.HandleKeyReleased();
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.Checkin"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
