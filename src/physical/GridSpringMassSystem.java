package physical;

import javafx.util.Pair;
import linalg.Vec3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridSpringMassSystem {
    final PApplet parent;
    final int m;
    final int n;

    final Map<Pair<Integer, Integer>, SpringMass> springMasses = new HashMap<>();
    final float mass;

    final List<Spring> springs = new ArrayList<>();
    final float restLength;
    final float forceConstant;
    final float dampConstant;

    public GridSpringMassSystem(PApplet parent, int m, int n, float mass, float restLength, float forceConstant, float dampConstant) {
        this.parent = parent;
        this.m = m;
        this.n = n;
        this.mass = mass;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                boolean isFixed = (i == 0 && (j == n - 1 || j % 5 == 0));
                SpringMass currentSpringMass = new SpringMass(parent, mass, Vec3.of(j * restLength * 1.5f - 50, i * restLength * 1.5f - 50, -200), Vec3.zero(), Vec3.zero(), isFixed);
                springMasses.put(new Pair<>(i, j), currentSpringMass);
                SpringMass prevColSpringMass = springMasses.get(new Pair<>(i, j - 1));
                SpringMass prevRowSpringMass = springMasses.get(new Pair<>(i - 1, j));
                if (i > 0) {
                    springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevRowSpringMass, currentSpringMass));
                }
                if (j > 0) {
                    springs.add(new Spring(parent, restLength, forceConstant, dampConstant, prevColSpringMass, currentSpringMass));
                }
            }
        }
    }

    public void update(float dt) throws Exception {
        for (Map.Entry<Pair<Integer, Integer>, SpringMass> s : springMasses.entrySet()) {
            s.getValue().update(dt);
        }
    }

    public void draw() {
        for (Map.Entry<Pair<Integer, Integer>, SpringMass> s : springMasses.entrySet()) {
            s.getValue().draw();
        }
        for (Spring s : springs) {
            s.draw();
        }
    }
}
