package physical;

import math.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;


public class GridThreadPointMassSystem {
    public interface FixedMassDecider {
        boolean isFixed(int i, int j, int m, int n);
    }

    public enum Layout {
        XY, YZ, ZX
    }

    final PApplet parent;
    final int m;
    final int n;
    final PImage clothTexture;
    public final ArrayList<ArrayList<PointMass>> pointMasses = new ArrayList<>();
    final float mass;

    final List<Thread> threads = new ArrayList<>();
    final float restLength;
    final float forceConstant;
    final float dampConstant;

    public Air air = null;

    public GridThreadPointMassSystem(PApplet parent,
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
            pointMasses.add(new ArrayList<>());
        }

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

                PointMass currentPointMass =
                        new PointMass(
                                parent,
                                mass,
                                position,
                                Vec3.zero(),
                                Vec3.zero(),
                                fixedMassDecider.isFixed(i, j, m, n)
                        );

                pointMasses.get(i).add(currentPointMass);
                if (i > 0) {
                    PointMass prevRowPointMass = pointMasses.get(i - 1).get(j);
                    threads.add(new Thread(parent, restLength, forceConstant, dampConstant, prevRowPointMass, currentPointMass));
                }
                if (j > 0) {
                    PointMass prevColPointMass = pointMasses.get(i).get(j - 1);
                    threads.add(new Thread(parent, restLength, forceConstant, dampConstant, prevColPointMass, currentPointMass));
                }
            }
        }
    }

    public void addSkipNodes() {
        float skipRestLength = restLength * 2.828f; // 2*sqrt(2) times the regular spring length
        float skipForceConstant = forceConstant * 0.8f;

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                PointMass currentPointMass = pointMasses.get(i).get(j);
                // Skip nodes springs to stiffen cloth
                if (i > 1 && j > 1) {
                    PointMass leftUpperDiagonal = pointMasses.get(i - 2).get(j - 2);
                    threads.add(new Thread(parent, skipRestLength, skipForceConstant, dampConstant, leftUpperDiagonal, currentPointMass));
                }
                if (i < m - 2 && j > 1) {
                    PointMass rightUpperDiagonal = pointMasses.get(i + 2).get(j - 2);
                    // rest length = 2*sqrt(2) times the regular spring length
                    threads.add(new Thread(parent, skipRestLength, skipForceConstant, dampConstant, rightUpperDiagonal, currentPointMass));
                }
            }
        }
    }

    private void addDragForces() {
        if (air == null) {
            return;
        }
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                PointMass mass1 = pointMasses.get(i).get(j);
                PointMass mass2 = pointMasses.get(i + 1).get(j);
                if (j < n - 1) {
                    PointMass mass3 = pointMasses.get(i).get(j + 1);
                    // x - *
                    // | /
                    // x
                    addDragToTriangle(mass1, mass2, mass3);
                }
                if (j > 0) {
                    PointMass mass4 = pointMasses.get(i + 1).get(j - 1);
                    //     x
                    //   / |
                    // * - x
                    addDragToTriangle(mass1, mass2, mass4);
                }
            }
        }
    }

    private void addDragToTriangle(PointMass mass1, PointMass mass2, PointMass mass3) {
        Vec3 r12 = mass2.position.minus(mass1.position);
        Vec3 r13 = mass3.position.minus(mass1.position);
        Vec3 normal = r12.cross(r13);

        Vec3 surfaceVelocity = Vec3.zero();
        surfaceVelocity
                .plusAccumulate(mass1.velocity)
                .plusAccumulate(mass2.velocity)
                .plusAccumulate(mass3.velocity)
                .scaleAccumulate(1f / 3);
        Vec3 windVelocity = air.windDirection.scale(air.windSpeed);
        Vec3 relativeVelocity = surfaceVelocity.minus(windVelocity);

        float vSquareAN = relativeVelocity.dot(normal) * relativeVelocity.abs();

        float scaleFactor = -0.5f * air.dragCoefficient * vSquareAN;
        Vec3 dragForce = normal.unit().scale(scaleFactor);

        Vec3 airFriction = relativeVelocity.scale(-1f * air.frictionCoefficient * mass1.mass);
        dragForce.plusAccumulate(airFriction);

        mass1.addDragForce(dragForce);
        mass2.addDragForce(dragForce);
        mass3.addDragForce(dragForce);
    }
    
    public void update(Ball ball, float dt) throws Exception {
        addDragForces();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.update(ball);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.firstOrderIntegrate(dt);
            }
        }
    }

    public void updateSecondOrder(Ball ball, float dt) throws Exception {
        addDragForces();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.update(ball);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.secondOrderHalfStep(dt);
            }
        }
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.update(ball);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.secondOrderFullStep(dt);
            }
        }
    }

    public void update(List<Ball> balls, float dt) throws Exception {
        addDragForces();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.update(balls);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.firstOrderIntegrate(dt);
            }
        }
    }

    public void updateWithBurnCheck(Ball ball, float dt) throws Exception {
        addDragForces();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.updateWithBurnCheck(ball);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                PointMass s = pointMasses.get(i).get(j);
                s.firstOrderIntegrate(dt);
                s.updateTemperature();
            }
        }
    }

    public void draw() {
        drawTexturedCloth();
//    	drawSpringMatrix();
    }

    private void drawTexturedCloth() {
        parent.noStroke();
        parent.noFill();
        parent.textureMode(PConstants.NORMAL);
        for (int i = 0; i < m - 1; ++i) {
            parent.beginShape(PConstants.TRIANGLE_STRIP);
            parent.texture(this.clothTexture);
            for (int j = 0; j < n; ++j) {
                PointMass sMass1 = pointMasses.get(i).get(j);
                PointMass sMass2 = pointMasses.get(i + 1).get(j);

                if (sMass1.isBroken || sMass2.isBroken) {
                    parent.endShape();
                    parent.beginShape(PConstants.TRIANGLE_STRIP);
                    parent.texture(this.clothTexture);
                    continue;
                }
                Vec3 pos1 = sMass1.position;
                float u1 = PApplet.map(i, 0, m - 1, 0, 1);
                float v = PApplet.map(j, 0, n - 1, 0, 1);
                parent.vertex(pos1.x, pos1.y, pos1.z, u1, v);

                Vec3 pos2 = sMass2.position;
                float u2 = PApplet.map(i + 1, 0, m - 1, 0, 1);
                parent.vertex(pos2.x, pos2.y, pos2.z, u2, v);
            }
            parent.endShape();
        }
    }

    private void drawSpringMatrix() {
        parent.strokeWeight(2);
        parent.stroke(255);
        for (Thread s : threads) {
            s.draw();
        }
    }

}
