import camera.LiamCam;
import linalg.Vec3;
import physical.SeriesSpringMassSystem;
import physical.SpringMass;
import processing.core.PApplet;

public class Checkin extends PApplet {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;

    private LiamCam liamCam;

    private SeriesSpringMassSystem seriesSpringMassSystem1;
    private SeriesSpringMassSystem seriesSpringMassSystem2;
    private SeriesSpringMassSystem seriesSpringMassSystem3;
    private SeriesSpringMassSystem seriesSpringMassSystem4;
    private SeriesSpringMassSystem seriesSpringMassSystem5;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
        liamCam = new LiamCam(this);

        seriesSpringMassSystem1 = new SeriesSpringMassSystem(this);
        seriesSpringMassSystem2 = new SeriesSpringMassSystem(this);
        seriesSpringMassSystem3 = new SeriesSpringMassSystem(this);
        seriesSpringMassSystem4 = new SeriesSpringMassSystem(this);
        seriesSpringMassSystem5 = new SeriesSpringMassSystem(this);

        seriesSpringMassSystem1.addMass(new SpringMass(this, 5, Vec3.of(-40, -80, -200), Vec3.zero(), Vec3.zero(), true));
        seriesSpringMassSystem1.addMass(new SpringMass(this, 5, Vec3.of(-41, -50, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem1.addMass(new SpringMass(this, 5, Vec3.of(-42, -15, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem1.addMass(new SpringMass(this, 5, Vec3.of(-43, 20, -200), Vec3.zero(), Vec3.zero(), false));

        seriesSpringMassSystem2.addMass(new SpringMass(this, 5, Vec3.of(0, -80, -200), Vec3.zero(), Vec3.zero(), true));
        seriesSpringMassSystem2.addMass(new SpringMass(this, 5, Vec3.of(1, -60, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem2.addMass(new SpringMass(this, 5, Vec3.of(2, -20, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem2.addMass(new SpringMass(this, 5, Vec3.of(3, -40, -200), Vec3.zero(), Vec3.zero(), false));

        seriesSpringMassSystem3.addMass(new SpringMass(this, 5, Vec3.of(40, -80, -200), Vec3.zero(), Vec3.zero(), true));
        seriesSpringMassSystem3.addMass(new SpringMass(this, 5, Vec3.of(41, -50, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem3.addMass(new SpringMass(this, 5, Vec3.of(42, -10, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem3.addMass(new SpringMass(this, 5, Vec3.of(43, 10, -200), Vec3.zero(), Vec3.zero(), false));

        seriesSpringMassSystem4.addMass(new SpringMass(this, 5, Vec3.of(80, -80, -200), Vec3.zero(), Vec3.zero(), true));
        seriesSpringMassSystem4.addMass(new SpringMass(this, 5, Vec3.of(81, -50, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem4.addMass(new SpringMass(this, 5, Vec3.of(82, -10, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem4.addMass(new SpringMass(this, 5, Vec3.of(83, -10, -200), Vec3.zero(), Vec3.zero(), false));

        seriesSpringMassSystem5.addMass(new SpringMass(this, 5, Vec3.of(-80, -80, -200), Vec3.zero(), Vec3.zero(), true));
        seriesSpringMassSystem5.addMass(new SpringMass(this, 5, Vec3.of(-81, -20, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem5.addMass(new SpringMass(this, 5, Vec3.of(-82, -10, -200), Vec3.zero(), Vec3.zero(), false));
        seriesSpringMassSystem5.addMass(new SpringMass(this, 5, Vec3.of(-83, 10, -200), Vec3.zero(), Vec3.zero(), false));
    }

    public void draw() {
        liamCam.Update(1.0f / frameRate);

        try {
            seriesSpringMassSystem1.update(0.05f);
            seriesSpringMassSystem2.update(0.05f);
            seriesSpringMassSystem3.update(0.05f);
            seriesSpringMassSystem4.update(0.05f);
            seriesSpringMassSystem5.update(0.05f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        background(0);
        seriesSpringMassSystem1.draw();
        seriesSpringMassSystem2.draw();
        seriesSpringMassSystem3.draw();
        seriesSpringMassSystem4.draw();
        seriesSpringMassSystem5.draw();
    }

    public void keyPressed() {
        liamCam.HandleKeyPressed();
    }

    public void keyReleased() {
        liamCam.HandleKeyReleased();
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"Checkin"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
