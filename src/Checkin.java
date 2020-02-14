import camera.LiamCam;
import linalg.Vec3;
import physical.SeriesSpringMassSystem;
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

        seriesSpringMassSystem1 = new SeriesSpringMassSystem(this, Vec3.of(-60, -80, -200), 4, 100, 20, 50, 50f);
        seriesSpringMassSystem2 = new SeriesSpringMassSystem(this, Vec3.of(-30, -80, -200), 4, 50, 20, 50, 50f);
        seriesSpringMassSystem3 = new SeriesSpringMassSystem(this, Vec3.of(0, -80, -200), 4, 10, 20, 50, 50f);
        seriesSpringMassSystem4 = new SeriesSpringMassSystem(this, Vec3.of(30, -80, -200), 4, 50, 20, 50, 50f);
        seriesSpringMassSystem5 = new SeriesSpringMassSystem(this, Vec3.of(60, -80, -200), 4, 100, 20, 50, 50f);
    }

    public void draw() {
        liamCam.Update(1.0f / frameRate);
        surface.setTitle("Processing FPS: " + frameRate);

        try {
            for (int i = 0; i < 20; ++i) {
                seriesSpringMassSystem1.update(0.01f);
                seriesSpringMassSystem2.update(0.01f);
                seriesSpringMassSystem3.update(0.01f);
                seriesSpringMassSystem4.update(0.01f);
                seriesSpringMassSystem5.update(0.01f);
            }
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
