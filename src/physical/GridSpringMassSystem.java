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
    final Map<Coordinates, Spring> springMapVertical = new HashMap<>();
    final Map<Coordinates, Spring> springMapHorizontal = new HashMap<>();
    final float mass;

    final List<Spring> springs = new ArrayList<>();
    final float restLength;
    final float forceConstant;
    final float dampConstant;

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
                
                Spring spring = null;
                if (i > 0) {
                    spring = new Spring(parent, restLength, forceConstant, dampConstant, prevRowSpringMass, currentSpringMass);
                    springs.add(spring);
                    springMapVertical.put(Coordinates.of(i, j), spring);
                }
                if (j > 0) {
                    spring = new Spring(parent, restLength, forceConstant, dampConstant, prevColSpringMass, currentSpringMass);
                    springs.add(spring);
                    springMapHorizontal.put(Coordinates.of(i, j), spring);
                }
            }
        }
    }
    
//    public void cutSpring(float mouseX, float mouseY) {
//    	Spring s = null;
//    	for(int i=1; i<m; i++) {
//    		s = springMapVertical.get(Coordinates.of(i, n/2));
//    		s.setBroken();
//    	}
//    	for(int j=1; j<n; j++) {
//    		s = springMapHorizontal.get(Coordinates.of(m/2, j));
//    		s.setBroken();
//    	}
//    }

    public void update(UserControlledBall userControlledBall, float dt) throws Exception {
        for (Map.Entry<Coordinates, SpringMass> s : springMasses.entrySet()) {
            s.getValue().update(userControlledBall);
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
                Vec3 pos1 = sMass1.position;
                float u1 = PApplet.map(i, 0, m - 1, 0, 1);
                float v = PApplet.map(j, 0, n - 1, 0, 1);
                parent.vertex(pos1.x, pos1.y, pos1.z, u1, v);
                
                // The mass right below the previous mass in the grid
                SpringMass sMass2 = springMasses.get(Coordinates.of(i + 1, j));
                Vec3 pos2 = sMass2.position;
                float u2 = PApplet.map(i + 1, 0, m - 1, 0, 1);
                parent.vertex(pos2.x, pos2.y, pos2.z, u2, v);
            }
            parent.endShape();
        }
    }
}
