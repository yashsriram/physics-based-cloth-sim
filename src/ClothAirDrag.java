import camera.QueasyCam;
import linalg.Vec3;
import physical.AmbientAir;
import physical.GridSpringMassSystem;
import processing.core.PApplet;

public class ClothAirDrag extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;

    private GridSpringMassSystem gridSpringMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 2f;

        gridSpringMassSystem = new GridSpringMassSystem(
                this,
                30, 30,
                30,
                5, 400, 1500f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -30, -20f, -30f,
                ((i, j, m, n) -> (j == 0)),
                GridSpringMassSystem.Layout.ZX);

        gridSpringMassSystem.air = new AmbientAir(0.05f, 0.6f, Vec3.of(0, 0, 1), 7);
    }

    public void draw() {
        long start = millis();
        // update
        try {
            for (int i = 0; i < 70; ++i) {
                gridSpringMassSystem.update(0.006f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridSpringMassSystem.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " wind : " + gridSpringMassSystem.air.windSpeed);
    }

    public void keyPressed() {
        if (key == '=') {
            gridSpringMassSystem.air.increaseSpeed(1f);
        }
        if (key == '-') {
            gridSpringMassSystem.air.decreaseSpeed(1f);
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"ClothAirDrag"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}