import linalg.Vec3;
import physical.Spring;
import physical.SpringMass;
import processing.core.PApplet;
import queasycam.QueasyCam;

public class Main extends PApplet {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private QueasyCam cam;

    private Spring spring;
    private SpringMass m1;
    private SpringMass m2;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
        cam = new QueasyCam(this);
        cam.sensitivity = 2f;
        cam.speed = 2f;

        m1 = new SpringMass(this, 5, Vec3.of(5, 0, -25), Vec3.zero(), Vec3.zero());
        m2 = new SpringMass(this, 5, Vec3.of(5, 0, 25), Vec3.zero(), Vec3.zero());
        spring = new Spring(this, 40, 2, m1, m2);
    }

    public void draw() {
        try {
            m1.update(0.05f);
            m2.update(0.05f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        background(0);
        m1.draw();
        m2.draw();
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
