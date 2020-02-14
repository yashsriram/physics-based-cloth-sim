package physical;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SeriesSpringMassSystem {
    final PApplet parent;
    final List<Spring> springs = new ArrayList<>();
    final List<SpringMass> springMasses = new ArrayList<>();
    final float restLength;
    final float forceConstant;
    final float dampConstant;

    public SeriesSpringMassSystem(PApplet parent, float restLength, float forceConstant, float dampConstant) {
        this.parent = parent;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;
    }

    public void addMass(SpringMass springMass) {
        if (springMasses.size() > 0) {
            springs.add(new Spring(parent, restLength, forceConstant, dampConstant, springMasses.get(springMasses.size() - 1), springMass));
        }
        springMasses.add(springMass);
    }

    public void update(float dt) throws Exception {
        for (SpringMass s : springMasses) {
            s.update(dt);
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
