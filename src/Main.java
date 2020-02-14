import camera.QueasyCam;
import physical.GridSpringMassSystem;
import processing.core.PApplet;

public class Main extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    //    private LiamCam liamCam;
    private QueasyCam queasyCam;

    //    private SeriesSpringMassSystem seriesSpringMassSystem;
    private GridSpringMassSystem gridSpringMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
//        liamCam = new LiamCam(this);
        queasyCam = new QueasyCam(this);

//        seriesSpringMassSystem = new SeriesSpringMassSystem(this, 20, 50, 5f);
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(5, -80, -200), Vec3.zero(), Vec3.zero(), true));
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(6, -70, -200), Vec3.zero(), Vec3.zero(), false));
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(7, -60, -200), Vec3.zero(), Vec3.zero(), false));
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(8, -50, -200), Vec3.zero(), Vec3.zero(), false));

        gridSpringMassSystem = new GridSpringMassSystem(this, 30, 30, 10, 5, 500, 1000f);
    }

    public void draw() {
//        liamCam.Update(1.0f / frameRate);

        long start = millis();
        try {
//            seriesSpringMassSystem.update(0.05f);
            for (int i = 0; i < 140; ++i) {
                gridSpringMassSystem.update(0.002f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();

        background(0);
        gridSpringMassSystem.draw();
//        seriesSpringMassSystem.draw();
        long draw = millis();
        surface.setTitle("Processing - FPS: " + frameRate + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms");
    }

    public void keyPressed() {
//        liamCam.HandleKeyPressed();
    }

    public void keyReleased() {
//        liamCam.HandleKeyReleased();
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
