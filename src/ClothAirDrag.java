import camera.QueasyCam;
import linalg.Vec3;
import physical.AmbientAir;
import physical.Ball;
import physical.Cutter;
import physical.GridSpringMassSystem;
import processing.core.PApplet;
import processing.core.PShape;

public class ClothAirDrag extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;

    private GridSpringMassSystem gridSpringMassSystem;
    private Ball ball;
    private Cutter cutter;
    private PShape cutterShape;
	private boolean cutterActive;
	private boolean disableQueasyCam;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 2f;
        cutterActive = false;
        disableQueasyCam = false;
        cutterShape = loadShape("Sword_2.obj");
        
        gridSpringMassSystem = new GridSpringMassSystem(
                this,
                30, 30,
                30,
                5, 300, 1500f, loadImage("aladdin-s-carpet.jpeg"),
                1f, -20, -40f, -30f,
                ((i, j, m, n) -> (j == 0)),
                GridSpringMassSystem.Layout.ZX);
        
//        gridSpringMassSystem.addSkipNodes();

        gridSpringMassSystem.air = new AmbientAir(0.08f, 0.08f, Vec3.of(0, 0, 1), 0);
        ball = new Ball(this, 1, 30, Vec3.of(50, 90, 0), Vec3.of(255, 255, 0));
    }

    private void drawOrigin() {
    	stroke(255);
    	sphere(5);
    }
    
    public void draw() {
    	if(cutterActive) {
    		if(cutter == null) {
    			cutter = new Cutter(this.cutterShape, queasyCam);
    		}
    		gridSpringMassSystem.updateCutter(cutter);
    	}
    	if(disableQueasyCam) {
    		queasyCam.controllable = false;
    		disableQueasyCam = false;
    	}
    	
        long start = millis();
        // update
        try {
            for (int i = 0; i < 100; ++i) {
                gridSpringMassSystem.update(ball,0.006f);
                ball.update(0.006f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        
        // draw
        background(0);
        gridSpringMassSystem.draw();
        ball.draw();
        drawOrigin();
        if(cutterActive && cutter!=null) {
        	cutter.draw(this);
        }
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
        if (key == 'x') {
            if(!cutterActive) {
            	cutterActive = true;
            	disableQueasyCam = true;
            }else {
            	disableQueasyCam = false;
            	queasyCam.controllable = true;
            	cutterActive = false;
            }
        }
        if(cutterActive && (key=='w' || key=='s' || key=='a' || key=='d'
        						 || key=='q' || key=='e') ) {
        	cutter.moveCutter(key);
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