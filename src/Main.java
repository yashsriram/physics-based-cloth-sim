import camera.QueasyCam;
import linalg.Vec3;
import physical.Ground;
import physical.RigidBody;
import processing.core.PApplet;
import processing.core.PMatrix3D;

public class Main extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    QueasyCam queasyCam;
    RigidBody rigidBody;
    Ground ground;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");

        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 2f;
        queasyCam.speed = 2f;

        rigidBody = new RigidBody(this, 10,
                Vec3.zero(), Vec3.of(0, -10, 1), Vec3.of(0, 10, 0),
                new PMatrix3D(), Vec3.of(1, 1, 1), Vec3.zero());


        // ground
        ground = new Ground(this,
                Vec3.of(0, 100, 0), Vec3.of(0, 0, 1), Vec3.of(1, 0, 0),
                1024, 1024,
                loadImage("grass.jpg"));
    }

    public void draw() {
        long start = millis();
        // update
        rigidBody.update(0.05f);
        long update = millis();
        // draw
        background(0);
        ground.render();
        rigidBody.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms");
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"Main"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
