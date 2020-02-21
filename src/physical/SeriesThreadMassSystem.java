package physical;

import linalg.Vec3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SeriesThreadMassSystem {
    final PApplet parent;
    final List<Thread> threads = new ArrayList<>();
    final List<PointMass> pointMasses = new ArrayList<>();
    final float mass;
    final float restLength;
    final float forceConstant;
    final float dampConstant;

    public SeriesThreadMassSystem(PApplet parent, Vec3 initialPoint, int numMasses, float mass, float restLength, float forceConstant, float dampConstant) {
        this.parent = parent;
        this.mass = mass;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;

        int rdm = ((int) parent.random(5f) % 2 == 0) ? -1 : 1;
        Vec3 displacement = Vec3.of(20, 0, 0).scale(rdm);
        for (int i = 0; i < numMasses; ++i) {
            boolean isFixed = i == 0;
            addMass(new PointMass(parent, mass, initialPoint.plus(displacement.scale(i)), Vec3.zero(), Vec3.zero(), isFixed));
        }
    }

    private void addMass(PointMass pointMass) {
        if (pointMasses.size() > 0) {
            threads.add(new Thread(parent, restLength, forceConstant, dampConstant, pointMasses.get(pointMasses.size() - 1), pointMass));
        }
        pointMasses.add(pointMass);
    }

    public void update(float dt) throws Exception {
        for (PointMass s : pointMasses) {
            s.update();
            s.eularianIntegrate(dt);
        }
    }

    public void draw() {
        for (PointMass s : pointMasses) {
            s.draw();
        }
        for (Thread s : threads) {
            s.draw();
        }
    }
}
