package physical;

import java.util.ArrayList;
import java.util.List;

import linalg.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;


public class GridSpringMassSystem {
    public interface FixedMassDecider {
        boolean isFixed(int i, int j, int m, int n);
    }

    public enum Layout {
        XY, YZ, ZX
    }

    final PApplet parent;
    final int m;
    final int n;
    final PImage clothTexture;
    final ArrayList<ArrayList<SpringMass>> springMasses = new ArrayList<ArrayList<SpringMass>>();
    final float mass;

    final List<Spring> springs = new ArrayList<>();
    final float restLength;
    final float forceConstant;
    final float dampConstant;

    public AmbientAir air = null;

    public GridSpringMassSystem(PApplet parent,
                                int m, int n,
                                float mass,
                                float restLength, float forceConstant, float dampConstant,
                                PImage clothTexture,
                                float extensionFactor, float offsetX, float offsetY, float offsetZ,
                                FixedMassDecider fixedMassDecider,
                                Layout layout) {
        this.parent = parent;
        this.m = m;
        this.n = n;
        this.mass = mass;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;
        this.clothTexture = clothTexture;
        
        for (int i = 0; i < m; ++i) {
        	springMasses.add(new ArrayList<SpringMass>());
        }

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                Vec3 position = Vec3.zero();
                switch (layout) {
                    case XY:
                        position = Vec3.of(
                                j * restLength * extensionFactor + offsetX,
                                i * restLength * extensionFactor + offsetY,
                                offsetZ);
                        break;
                    case YZ:
                        position = Vec3.of(
                                offsetX,
                                j * restLength * extensionFactor + offsetY,
                                i * restLength * extensionFactor + offsetZ);
                        break;
                    case ZX:
                        position = Vec3.of(
                                i * restLength * extensionFactor + offsetX,
                                offsetY,
                                j * restLength * extensionFactor + offsetZ);
                        break;
                }

                SpringMass currentSpringMass =
                        new SpringMass(
                                parent,
                                mass,
                                position,
                                Vec3.zero(),
                                Vec3.zero(),
                                fixedMassDecider.isFixed(i, j, m, n)
                        );
                
                springMasses.get(i).add(currentSpringMass);
                if (i > 0) {
                	SpringMass prevRowSpringMass = springMasses.get(i-1).get(j);
                    springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevRowSpringMass, currentSpringMass));
                }
                if (j > 0) {
                	SpringMass prevColSpringMass = springMasses.get(i).get(j-1);
                    springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevColSpringMass, currentSpringMass));
                }
            }
        }
    }

    private void addDragForces() {
        if (air == null) {
            return;
        }
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                SpringMass mass1 = springMasses.get(i).get(j);
                SpringMass mass2 = springMasses.get(i + 1).get(j);
                if (j < n - 1) {
                    SpringMass mass3 = springMasses.get(i).get(j + 1);
                    // x - *
                    // | /
                    // x
                    addDragToTriangle(mass1, mass2, mass3);
                }
                if (j > 0) {
                    SpringMass mass4 = springMasses.get(i + 1).get(j - 1);
                    //     x
                    //   / |
                    // * - x
                    addDragToTriangle(mass1, mass2, mass4);
                }
            }
        }
    }

    private void addDragToTriangle(SpringMass mass1, SpringMass mass2, SpringMass mass3) {
        Vec3 r12 = mass2.position.minus(mass1.position);
        Vec3 r13 = mass3.position.minus(mass1.position);
        Vec3 normal = r12.cross(r13);

        Vec3 surfaceVelocity = mass1.velocity
					                .plus(mass2.velocity)
					                .plus(mass3.velocity)
					                .scale(1f / 3);
        Vec3 windVelocity = air.windDirection.scale(air.windSpeed);
        Vec3 relativeVelocity = surfaceVelocity.minus(windVelocity);

        float vSquareAN = relativeVelocity.dot(normal) * relativeVelocity.abs();

        float scaleFactor = -0.5f * air.dragCoefficient * vSquareAN;
        Vec3 dragForce = normal.unit().scale(scaleFactor);
        
        Vec3 airFriction = relativeVelocity.scale(-1f * air.frictionCoefficient * mass1.mass);
        dragForce.plusAccumulate(airFriction);

        mass1.addDragForce(dragForce);
        mass2.addDragForce(dragForce);
        mass3.addDragForce(dragForce);
    }

    public void update(Ball ball, float dt) throws Exception {
        addDragForces();
        SpringMass s = null;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
	            s = springMasses.get(i).get(j);
	            s.update(ball);
//	            s.eularianIntegrate(dt);
	            s.secondOrderIntegrate(dt);
            }
        }
    }

    public void update(float dt) throws Exception {
        addDragForces();
        SpringMass s = null;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
	            s = springMasses.get(i).get(j);
	            s.update();
	            s.eularianIntegrate(dt);
            }
        }
    }

    public void draw() {
        parent.noStroke();
        parent.noFill();
        parent.textureMode(PConstants.NORMAL);
        for (int i = 0; i < m - 1; ++i) {
            parent.beginShape(PConstants.TRIANGLE_STRIP);
            parent.texture(this.clothTexture);
            for (int j = 0; j < n; ++j) {
                SpringMass sMass1 = springMasses.get(i).get(j);
                SpringMass sMass2 = springMasses.get(i + 1).get(j);

                if (sMass1.getIsBroken() || sMass2.getIsBroken()) {
                    parent.endShape();
                    parent.beginShape(PConstants.TRIANGLE_STRIP);
                    parent.texture(this.clothTexture);
                    continue;
                }
                Vec3 pos1 = sMass1.position;
                float u1 = PApplet.map(i, 0, m - 1, 0, 1);
                float v = PApplet.map(j, 0, n - 1, 0, 1);
                parent.vertex(pos1.x, pos1.y, pos1.z, u1, v);

                Vec3 pos2 = sMass2.position;
                float u2 = PApplet.map(i + 1, 0, m - 1, 0, 1);
                parent.vertex(pos2.x, pos2.y, pos2.z, u2, v);
            }
            parent.endShape();
        }
    }

    public void startBurning() {
        int start_i = 0;
        int start_j = n;
        SpringMass sMass = springMasses.get(start_i).get(start_j);
        sMass.setIsBurning(true);
    }
}
