import linalg.Vec3;
import physical.Spring;
import physical.SpringMass;
import processing.core.PApplet;
import queasycam.QueasyCam;

public class Main extends PApplet {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private QueasyCam cam;

    private Spring s1;
    private Spring s2;
    private Spring s3;
    private SpringMass m1;
    private SpringMass m2;
    private SpringMass m3;
    private SpringMass m4;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
        cam = new QueasyCam(this);
        cam.sensitivity = 2f;
        cam.speed = 2f;

        m1 = new SpringMass(this, 5, Vec3.of(5, -50, 0), Vec3.zero(), Vec3.zero());
        m2 = new SpringMass(this, 5, Vec3.of(5, 0, 0), Vec3.zero(), Vec3.zero());
        m3 = new SpringMass(this, 5, Vec3.of(5, 50, 0), Vec3.zero(), Vec3.zero());
        m4 = new SpringMass(this, 5, Vec3.of(5, 100, 0), Vec3.zero(), Vec3.zero());
        s1 = new Spring(this, 40, 2, m1, m2);
        s2 = new Spring(this, 40, 2, m2, m3);
        s3 = new Spring(this, 40, 2, m3, m4);
    }

    public void draw() {
        try {
            m1.update(0.05f);
            m2.update(0.05f);
            m3.update(0.05f);
            m4.update(0.05f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        background(0);
        m1.draw();
        m2.draw();
        m3.draw();
        m4.draw();
        s1.draw();
        s2.draw();
        s3.draw();
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
