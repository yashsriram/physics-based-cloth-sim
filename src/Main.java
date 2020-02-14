import camera.Camera;
import linalg.Vec3;
import physical.SeriesSpringMassSystem;
import physical.Spring;
import physical.SpringMass;
import processing.core.PApplet;
import queasycam.QueasyCam;

public class Main extends PApplet {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    //    private QueasyCam cam;
    private Camera camera;

    private SeriesSpringMassSystem seriesSpringMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
        camera = new Camera(this);

//        cam = new QueasyCam(this);
//        cam.sensitivity = 1f;
//        cam.speed = 2f;

        seriesSpringMassSystem = new SeriesSpringMassSystem(this);
        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(5, -100, -100), Vec3.zero(), Vec3.zero(), true));
        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(5, -50, -50), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(5, -100, 0), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(5, -100, 50), Vec3.zero(), Vec3.zero(), false));
    }

    public void draw() {
        camera.Update(1.0f / frameRate);

        try {
            seriesSpringMassSystem.update(0.05f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        background(0);
        seriesSpringMassSystem.draw();
    }

    public void keyPressed() {
        camera.HandleKeyPressed();
    }

    public void keyReleased() {
        camera.HandleKeyReleased();
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
