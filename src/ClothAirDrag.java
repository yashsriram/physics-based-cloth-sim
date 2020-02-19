import camera.QueasyCam;
import physical.AmbientAir;
import physical.GridSpringMassSystem;
import physical.GridSpringMassSystem.FixedMassDecider;
import processing.core.PApplet;

public class ClothAirDrag extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;

    private GridSpringMassSystem gridSpringMassSystem;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }
    
    public class FixClothHorizontalEdge implements FixedMassDecider{
		@Override
		public boolean isFixed(int i, int j, int m, int n) {
			return (j==0);
		}
    }

    public void setup() {
        surface.setTitle("Air Drag on Cloth");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 0.25f;

        gridSpringMassSystem = new GridSpringMassSystem(
                this,
                30, 30,
                10,
                2, 500, 1000f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -30, -50f, -30f,
                new FixClothHorizontalEdge(),
                GridSpringMassSystem.Layout.ZX);
        
        AmbientAir air = new AmbientAir();
        gridSpringMassSystem.setAmbientAir(air);
    }

    public void draw() {

        long start = millis();
        // update
        try {
            for (int i = 0; i < 140; ++i) {
                gridSpringMassSystem.update(0.002f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        gridSpringMassSystem.draw();
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms");
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