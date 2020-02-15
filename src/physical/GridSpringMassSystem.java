package physical;

import linalg.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.*;

class Coordinates {
    int i;
    int j;

    public Coordinates(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return i == that.i &&
                j == that.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}

public class GridSpringMassSystem {
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

    public GridSpringMassSystem(PApplet parent, int m, int n, float mass, float restLength, float forceConstant, float dampConstant, PImage clothTexture) {
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
                boolean isFixed = (i == 0 && (j == n - 1 || j % 5 == 0));
                SpringMass currentSpringMass =
                        new SpringMass(parent, mass,
                                Vec3.of(j * restLength * 1.5f - 50, i * restLength * 1.5f - 50, -100),
                                Vec3.zero(), Vec3.zero(), isFixed
                        );
                springMasses.put(new Coordinates(i, j), currentSpringMass);
                SpringMass prevColSpringMass = springMasses.get(new Coordinates(i, j - 1));
                SpringMass prevRowSpringMass = springMasses.get(new Coordinates(i - 1, j));
                if (i > 0) {
                    springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevRowSpringMass, currentSpringMass));
                }
                if (j > 0) {
                    springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevColSpringMass, currentSpringMass));
                }
            }
        }
    }

    public void update(Ball ball, float dt) throws Exception {
        for (Map.Entry<Coordinates, SpringMass> s : springMasses.entrySet()) {
            s.getValue().update(ball);
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
                SpringMass sMass1 = springMasses.get(new Coordinates(i, j));
                Vec3 px1 = sMass1.position;
                float u1 = PApplet.map(i, 0, m - 1, 0, 1);
                float v = PApplet.map(j, 0, n - 1, 0, 1);
                parent.vertex(px1.x, px1.y, px1.z, u1, v);

                SpringMass sMass2 = springMasses.get(new Coordinates(i + 1, j));
                Vec3 px2 = sMass2.position;
                float u2 = PApplet.map(i + 1, 0, m - 1, 0, 1);
                parent.vertex(px2.x, px2.y, px2.z, u2, v);
            }
            parent.endShape();
        }
    }
}
