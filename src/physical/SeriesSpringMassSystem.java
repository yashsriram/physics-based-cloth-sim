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
        
        int rdm = ((int)parent.random(5f) % 2 == 0) ? -1 : 1;
        Vec3 displacement = Vec3.of(20, 0, 0).scale(rdm);
        for (int i = 0; i < numMasses; ++i) {
            boolean isFixed = i == 0;
            addMass(new SpringMass(parent, mass, initialPoint.plus(displacement.scale(i)), Vec3.zero(), Vec3.zero(), isFixed));
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
            s.update();
            s.eularianIntegrate(dt);
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
