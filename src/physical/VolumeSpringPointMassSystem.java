package physical;

import math.Vec3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;


public class VolumeSpringPointMassSystem {
    public interface FixedMassDecider {
        boolean isFixed(int i, int j, int k, int m, int n, int o);
    }

    final PApplet parent;
    final int m;
    final int n;
    final int o;
    public final List<List<List<PointMass>>> pointMasses = new ArrayList<>();
    final float mass;

    final List<Thread> springs = new ArrayList<>();
    final float restLength;
    final float forceConstant;
    final float dampConstant;

    public VolumeSpringPointMassSystem(PApplet parent,
                                       int m, int n, int o,
                                       float mass,
                                       float restLength, float forceConstant, float dampConstant,
                                       float extensionFactor, float offsetX, float offsetY, float offsetZ,
                                       FixedMassDecider fixedMassDecider) {
        this.parent = parent;
        this.m = m;
        this.n = n;
        this.o = o;
        this.mass = mass;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;

        for (int i = 0; i < m; ++i) {
            List<List<PointMass>> verticalSlice = new ArrayList<>();
            for (int j = 0; j < n; ++j) {
                List<PointMass> verticalPillar = new ArrayList<>();
                verticalSlice.add(verticalPillar);
            }
            pointMasses.add(verticalSlice);
        }

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < o; ++k) {
                    Vec3 position = Vec3.of(
                            i * restLength * extensionFactor + offsetX,
                            j * restLength * extensionFactor + offsetY,
                            k * restLength * extensionFactor + offsetZ);

                    PointMass currentPointMass =
                            new PointMass(
                                    parent,
                                    mass,
                                    position,
                                    Vec3.zero(),
                                    Vec3.zero(),
                                    fixedMassDecider.isFixed(i, j, k, m, n, o)
                            );

                    pointMasses.get(i).get(j).add(currentPointMass);
                }
            }
        }

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < o; ++k) {
                    PointMass currentPointMass = pointMasses.get(i).get(j).get(k);
                    if (i > 0) {
                        PointMass i_1 = pointMasses.get(i - 1).get(j).get(k);
                        springs.add(new Thread(parent, forceConstant, dampConstant, i_1, currentPointMass, Thread.Mode.SPRING));
                    }
                    if (j > 0) {
                        PointMass j_1 = pointMasses.get(i).get(j - 1).get(k);
                        springs.add(new Thread(parent, forceConstant, dampConstant, j_1, currentPointMass, Thread.Mode.SPRING));
                    }
                    if (k > 0) {
                        PointMass k_1 = pointMasses.get(i).get(j).get(k - 1);
                        springs.add(new Thread(parent, forceConstant, dampConstant, k_1, currentPointMass, Thread.Mode.SPRING));
                    }
                    if (i > 0 && j > 0) {
                        PointMass d11 = pointMasses.get(i - 1).get(j - 1).get(k);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d11, currentPointMass, Thread.Mode.SPRING));
                        PointMass d21 = pointMasses.get(i - 1).get(j).get(k);
                        PointMass d22 = pointMasses.get(i).get(j - 1).get(k);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d21, d22, Thread.Mode.SPRING));
                    }
                    if (j > 0 && k > 0) {
                        PointMass d11 = pointMasses.get(i).get(j - 1).get(k - 1);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d11, currentPointMass, Thread.Mode.SPRING));
                        PointMass d21 = pointMasses.get(i).get(j - 1).get(k);
                        PointMass d22 = pointMasses.get(i).get(j).get(k - 1);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d21, d22, Thread.Mode.SPRING));
                    }
                    if (k > 0 && i > 0) {
                        PointMass d11 = pointMasses.get(i - 1).get(j).get(k - 1);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d11, currentPointMass, Thread.Mode.SPRING));
                        PointMass d21 = pointMasses.get(i - 1).get(j).get(k);
                        PointMass d22 = pointMasses.get(i).get(j).get(k - 1);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d21, d22, Thread.Mode.SPRING));
                    }
                    if (i > 0 && j > 0 && k > 0) {
                        PointMass d1 = pointMasses.get(i - 1).get(j - 1).get(k - 1);
                        PointMass d2 = pointMasses.get(i).get(j).get(k);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d1, d2, Thread.Mode.SPRING));
                        PointMass d3 = pointMasses.get(i - 1).get(j).get(k - 1);
                        PointMass d4 = pointMasses.get(i).get(j - 1).get(k);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d3, d4, Thread.Mode.SPRING));
                        PointMass d5 = pointMasses.get(i).get(j - 1).get(k - 1);
                        PointMass d6 = pointMasses.get(i - 1).get(j).get(k);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d5, d6, Thread.Mode.SPRING));
                        PointMass d7 = pointMasses.get(i - 1).get(j - 1).get(k);
                        PointMass d8 = pointMasses.get(i).get(j).get(k - 1);
                        springs.add(new Thread(parent, forceConstant, dampConstant, d7, d8, Thread.Mode.SPRING));
                    }
                }
            }
        }
    }

    public void update(Ball ball, Ground ground, float dt) throws Exception {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; ++k) {
                    PointMass s = pointMasses.get(i).get(j).get(k);
                    s.update(ball);
                    s.acceleration.plusAccumulate(s.velocity.scale(-0.1f));
                    if (s.position.y >= ground.center.y) {
                        s.position.y = ground.center.y - 0.1f;
                        s.velocity.y = -Math.abs(s.velocity.y);
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; ++k) {
                    PointMass s = pointMasses.get(i).get(j).get(k);
                    s.secondOrderIntegrate(dt);
                }
            }
        }
    }

    public void update(List<Ball> balls, Ground ground, float dt) throws Exception {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; ++k) {
                    PointMass s = pointMasses.get(i).get(j).get(k);
                    s.update(balls);
                    s.acceleration.plusAccumulate(s.velocity.scale(-0.1f));
                    if (s.position.y >= ground.center.y) {
                        s.position.y = ground.center.y - 0.1f;
                        s.velocity.y = -Math.abs(s.velocity.y);
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; ++k) {
                    PointMass s = pointMasses.get(i).get(j).get(k);
                    s.secondOrderIntegrate(dt);
                }
            }
        }
    }

    public void draw() {
        parent.strokeWeight(2);
        parent.stroke(255);
        for (Thread s : springs) {
            s.draw();
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; ++k) {
                    PointMass s = pointMasses.get(i).get(j).get(k);
                    s.draw();
                }
            }
        }
    }

}
