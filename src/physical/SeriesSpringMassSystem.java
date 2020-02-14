package physical;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SeriesSpringMassSystem {
    final PApplet parent;
    final List<Spring> springs = new ArrayList<>();
    final List<SpringMass> springMasses = new ArrayList<>();

    public SeriesSpringMassSystem(PApplet parent) {
        this.parent = parent;
    }

    public void addMass(SpringMass springMass) {
        if (springMasses.size() > 0) {
            springs.add(new Spring(parent, 20, 5, .2f, springMasses.get(springMasses.size() - 1), springMass));
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
