package demos;

import math.Vec2;
import math.Vec3;
import physical.Circle;
import processing.core.PApplet;

public class CirclesFallingOnThread extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private Circle circle;

    public void settings() {
        size(WIDTH, HEIGHT, P2D);
    }

    public void setup() {
        surface.setTitle("Processing");
        resetSystem();
    }

    private void resetSystem() {
        circle = new Circle(this, 5, 50, Vec2.zero(), Vec3.of(255), true);
    }

    public void draw() {

        long start = millis();
        // update
        circle.update(0.01f);
        long update = millis();
        // draw
        background(0);
        circle.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " #balls: " + 1);
    }

    public void keyPressed() {
        if (key == 'n') {
        }
        if (key == 'r') {
            resetSystem();
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.CirclesFallingOnThread"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
