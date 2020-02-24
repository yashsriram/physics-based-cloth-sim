package physical;

import math.Vec3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SeriesThreadMassSystem {
    public interface FixedMassDecider {
        boolean isFixed(int i, int m);
    }

    final PApplet parent;
    final List<Thread> threads = new ArrayList<>();
    final List<PointMass> pointMasses = new ArrayList<>();
    final float mass;
    final float restLength;
    final float forceConstant;
    final float dampConstant;
    final FixedMassDecider fixedMassDecider;

    public static SeriesThreadMassSystem random(PApplet parent, Vec3 initialPoint, int numMasses, float mass, float restLength, float forceConstant, float dampConstant, FixedMassDecider fixedMassDecider) {
        return new SeriesThreadMassSystem(parent, initialPoint, numMasses, mass, restLength, forceConstant, dampConstant, fixedMassDecider);
    }

    public static SeriesThreadMassSystem horizontal(PApplet parent, float width, float height, int numMasses, float mass, float restLength, float forceConstant, float dampConstant, FixedMassDecider fixedMassDecider) {
        return new SeriesThreadMassSystem(parent, width, height, numMasses, mass, restLength, forceConstant, dampConstant, fixedMassDecider);
    }

    public void update(float dt) throws Exception {
        for (PointMass s : pointMasses) {
            s.update();
        }
        for (PointMass s : pointMasses) {
            s.firstOrderIntegrate(dt);
        }
    }

    public void update(List<Ball> balls, float dt) throws Exception {
        for (PointMass s : pointMasses) {
            s.update(balls);
            s.acceleration.plusAccumulate(s.velocity.scale(-0.08f));
        }
        for (PointMass s : pointMasses) {
            s.firstOrderIntegrate(dt);
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

    public void draw2D() {
        for (PointMass s : pointMasses) {
            s.draw2D();
        }
        for (Thread s : threads) {
            s.draw2D();
        }
    }

    private SeriesThreadMassSystem(PApplet parent, Vec3 initialPoint, int numMasses, float mass, float restLength, float forceConstant, float dampConstant, FixedMassDecider fixedMassDecider) {
        this.parent = parent;
        this.mass = mass;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;
        this.fixedMassDecider = fixedMassDecider;

        int rdm = ((int) parent.random(5f) % 2 == 0) ? -1 : 1;
        Vec3 displacement = Vec3.of(20, 0, 0).scale(rdm);
        for (int i = 0; i < numMasses; ++i) {
            boolean isFixed = fixedMassDecider.isFixed(i, numMasses);
            addMass(new PointMass(parent, mass, initialPoint.plus(displacement.scale(i)), Vec3.zero(), Vec3.zero(), isFixed));
        }
    }

    private SeriesThreadMassSystem(PApplet parent, float width, float height, int numMasses, float mass, float restLength, float forceConstant, float dampConstant, FixedMassDecider fixedMassDecider) {
        this.parent = parent;
        this.mass = mass;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;
        this.fixedMassDecider = fixedMassDecider;

        Vec3 displacement = Vec3.of(restLength, 0, 0);
        Vec3 offset = Vec3.of((width - (numMasses - 1) * restLength) / 2f, height * 0.65f, 0);
        for (int i = 0; i < numMasses; ++i) {
            boolean isFixed = fixedMassDecider.isFixed(i, numMasses);
            addMass(new PointMass(parent, mass, offset.plus(displacement.scale(i)), Vec3.zero(), Vec3.zero(), isFixed));
        }
    }

    private void addMass(PointMass pointMass) {
        if (pointMasses.size() > 0) {
            threads.add(new Thread(parent, restLength, forceConstant, dampConstant, pointMasses.get(pointMasses.size() - 1), pointMass));
        }
        pointMasses.add(pointMass);
    }

}
