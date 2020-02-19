package physical;

import linalg.Coordinates;
import linalg.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class GridSpringMassSystem {
    public interface FixedMassDecider {
        public boolean isFixed(int i, int j, int m, int n);
    }

    public enum Layout {
        XY, YZ, ZX
    }

    final PApplet parent;
    final int m;
    final int n;
    final PImage clothTexture;

    final Map<Coordinates, SpringMass> springMasses = new HashMap<>();
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
                springMasses.put(Coordinates.of(i, j), currentSpringMass);
                SpringMass prevColSpringMass = springMasses.get(Coordinates.of(i, j - 1));
                SpringMass prevRowSpringMass = springMasses.get(Coordinates.of(i - 1, j));
                
                if (i > 0) {
                	springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevRowSpringMass, currentSpringMass));
                }
                if (j > 0) {
                	springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevColSpringMass, currentSpringMass));
                }
            }
        }
    }
    
    private void addDragForces() {
    	if(air == null) {return;}
    	for(int i=0; i<m-1; i++) {
    		for(int j=0; j<n; j++) {
    			SpringMass sMass1 = springMasses.get(Coordinates.of(i, j));
    			SpringMass sMass2 = springMasses.get(Coordinates.of(i+1, j));
    			if(j < n-1) {
	    			SpringMass sMass3 = springMasses.get(Coordinates.of(i, j+1));
	    			addDragToTriangle(sMass1, sMass2, sMass3);
    			}
    			if(j > 0) {
    				SpringMass sMass4 = springMasses.get(Coordinates.of(i+1, j-1));
    				addDragToTriangle(sMass1, sMass2, sMass4);
    			}
    		}
    	}
    }

    private void addDragToTriangle(SpringMass sMass1, SpringMass sMass2, SpringMass sMass3) {
    	Vec3 r12 = sMass2.position.minus(sMass1.position);
		Vec3 r13 = sMass3.position.minus(sMass1.position);
		Vec3 n_star = r12.cross(r13);
		
		Vec3 surfaceVelocity = sMass1.velocity.plus(sMass2.velocity)
											   .plus(sMass3.velocity)
											   .scale(0.333f);
		Vec3 windVelocity = air.windDirection.scale(air.windSpeed);
		Vec3 v = surfaceVelocity.minus(windVelocity);
		
		float vsqan = v.dot(n_star) * v.abs() * 0.5f;
		
		float scaleFactor = -0.5f * air.density * air.dragCoeff * vsqan;
		Vec3 dragForce = n_star.unit().scale(scaleFactor);
		
		sMass1.addDragForce(dragForce.scale(1));
		sMass2.addDragForce(dragForce.scale(1));
		sMass3.addDragForce(dragForce.scale(1));
	}

	public void update(Ball ball, float dt) throws Exception {
    	addDragForces();
        for (Map.Entry<Coordinates, SpringMass> s : springMasses.entrySet()) {
            s.getValue().update(ball);
            s.getValue().eularianIntegrate(dt);
        }
    }
    
    public void update(float dt) throws Exception {
    	addDragForces();
        for (Map.Entry<Coordinates, SpringMass> s : springMasses.entrySet()) {
        	s.getValue().update();
            s.getValue().eularianIntegrate(dt);
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
                SpringMass sMass1 = springMasses.get(Coordinates.of(i, j));
                SpringMass sMass2 = springMasses.get(Coordinates.of(i + 1, j));
                
                if(sMass1.getBroken() || sMass2.getBroken()) {
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
//    	for(Entry<Coordinates, SpringMass> s : springMasses.entrySet()) {
//    		s.getValue().draw();
//    	}
    }

	public void startBurning() {
		int start_i = 0;
		int start_j = n;
		SpringMass sMass = springMasses.get(Coordinates.of(start_i, start_j));
		sMass.setBurning();
	}
}
