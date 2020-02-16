import camera.QueasyCam;
import linalg.Vec3;
import physical.Ball;
import physical.GridSpringMassSystem;
import processing.core.PApplet;

public class ClothFallingOnObject extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;
    private Ball ball;

    private GridSpringMassSystem gridSpringMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);

        gridSpringMassSystem = new GridSpringMassSystem(
                this,
                30, 30,
                10,
                2, 500, 1000f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -30, -50f, -30f,
                (i, j, m, n) -> (false),
                GridSpringMassSystem.Layout.ZX);
        ball = new Ball(this, 10, Vec3.of(0, 0, 0), Vec3.of(255, 255, 0));
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 140; ++i) {
                gridSpringMassSystem.update(ball, 0.002f);
                ball.update(0.002f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridSpringMassSystem.draw();
        ball.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms");
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"ClothFallingOnObject"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
