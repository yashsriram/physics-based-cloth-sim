import processing.core.PApplet;
import queasycam.QueasyCam;

public class Main extends PApplet {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private QueasyCam cam;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        noStroke();
        surface.setTitle("Processing");
        cam = new QueasyCam(this);
        cam.sensitivity = 2f;
        cam.speed = 2f;
    }

    public void draw() {
        background(0);
        fill(255, 0, 0);
        translate(20, 0, 0);
        sphere(10);
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
