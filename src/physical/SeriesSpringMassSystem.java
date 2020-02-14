package physical;

import linalg.Vec3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SeriesSpringMassSystem {
    final PApplet parent;
    final List<Spring> springs = new ArrayList<>();
    final List<SpringMass> springMasses = new ArrayList<>();
    final float mass;
    final float restLength;
    final float forceConstant;
    final float dampConstant;

    public SeriesSpringMassSystem(PApplet parent, Vec3 initialPoint, int numMasses, float mass, float restLength, float forceConstant, float dampConstant) {
        this.parent = parent;
        this.mass = mass;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;

        for (int i = 0; i < numMasses; ++i) {
            boolean isFixed = i == 0;
            addMass(new SpringMass(parent, mass, initialPoint.plus(Vec3.sphereRandom(5f).scale(i)), Vec3.zero(), Vec3.zero(), isFixed));
        }
    }

    private void addMass(SpringMass springMass) {
        if (springMasses.size() > 0) {
            springs.add(new Spring(parent, restLength, forceConstant, dampConstant, springMasses.get(springMasses.size() - 1), springMass));
        }
        springMasses.add(springMass);
    }

    public void update(float dt) throws Exception {
        for (SpringMass s : springMasses) {
            s.parallelUpdate();
            s.integrate(dt);
        }
    }

    public void draw() {
        for (SpringMass s : springMasses) {
            s.draw();
        }
        for (Spring s : springs) {
            s.draw();
        }
    }
}
