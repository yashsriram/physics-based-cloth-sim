package physical;

import linalg.Vec3;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public class SpringMass {
    private static int nextId = 1;
    private static int px = -1;
    private static int py = -1;
    private static final Vec3 gravity = Vec3.of(0, 5, 0);
    private static final float airDragConstant = 0.1f;

    private static int nextId() {
        int id = nextId;
        nextId++;
        return id;
    }

    final PApplet parent;
    final int id;
    float mass;
    Vec3 position;
    Vec3 velocity;
    Vec3 acceleration;
    boolean isFixed;
    List<Spring> springs = new ArrayList<>();
    PImage clothTexture;

    public SpringMass(PApplet parent, float mass, Vec3 position, Vec3 velocity, Vec3 acceleration, boolean isFixed) {
    	this.parent = parent;
        this.id = nextId();
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.isFixed = isFixed;
    }

	public SpringMass(PApplet parent, float mass,
					  Vec3 position, Vec3 velocity,
					  Vec3 acceleration, boolean isFixed,
					  PImage clothTexture, int px, int py) {
        this.clothTexture = clothTexture;
        this.parent = parent;
        this.id = nextId();
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.isFixed = isFixed;
    }

    public void parallelUpdate() throws Exception {
        if (isFixed) {
            return;
        }
        Vec3 totalSpringForce = Vec3.zero();
        for (Spring spring : springs) {
            totalSpringForce = totalSpringForce.plus(spring.forceOn(this));
        }
        acceleration = totalSpringForce.scale(1 / mass);
        acceleration = acceleration.plus(gravity);
        acceleration = acceleration.plus(velocity.scale(-1 * airDragConstant));
    }

    public void integrate(float dt) {
        position = position.plus(velocity.scale(dt));
        velocity = velocity.plus(acceleration.scale(dt));
    }

    public void draw() {
        if (!this.isFixed) {
            parent.pushMatrix();
            parent.stroke(0, 255, 0);
            parent.point(position.x, position.y, position.z);
            parent.popMatrix();
        } else {
//            parent.pushMatrix();
//            parent.translate(position.x, position.y, position.z);
//            parent.fill(255, 100, 0);
//            parent.box(3);
//            parent.popMatrix();
        }
        
    }
}
