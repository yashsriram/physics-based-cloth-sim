import camera.LiamCam;
import camera.QueasyCam;
import physical.GridSpringMassSystem;
import processing.core.PApplet;

public class Main extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private LiamCam liamCam;

//    private SeriesSpringMassSystem seriesSpringMassSystem;
    private GridSpringMassSystem gridSpringMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
        liamCam = new LiamCam(this);

//        seriesSpringMassSystem = new SeriesSpringMassSystem(this, 20, 50, 5f);
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(5, -80, -200), Vec3.zero(), Vec3.zero(), true));
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(6, -70, -200), Vec3.zero(), Vec3.zero(), false));
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(7, -60, -200), Vec3.zero(), Vec3.zero(), false));
//        seriesSpringMassSystem.addMass(new SpringMass(this, 5, Vec3.of(8, -50, -200), Vec3.zero(), Vec3.zero(), false));

        gridSpringMassSystem = new GridSpringMassSystem(this, 6, 5, 5, 10, 50, 4f);
    }

    public void draw() {
        liamCam.Update(1.0f / frameRate);

        try {
//            seriesSpringMassSystem.update(0.05f);
            gridSpringMassSystem.update(0.05f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        background(0);
        gridSpringMassSystem.draw();
//        seriesSpringMassSystem.draw();
    }

    public void keyPressed() {
        liamCam.HandleKeyPressed();
    }

    public void keyReleased() {
        liamCam.HandleKeyReleased();
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
