package physical;

import camera.QueasyCam;
import math.Vec3;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Cutter {

    private PApplet parent;
    private QueasyCam cam;
    private PVector position;
    private float xEular;
    private float yEular;
    private float zEular;
    private PShape shape;
    private boolean underKeyboardControl;

    public Cutter(PApplet parent, PShape shape, QueasyCam cam) {
        this.parent = parent;
        this.shape = shape;
        this.position = cam.getAim(100);
        this.cam = cam;
        this.underKeyboardControl = false;
        this.xEular = 0;
        this.yEular = PConstants.PI;
        this.zEular = -PConstants.PI / 2;
    }

    public void update() {
        if (!underKeyboardControl) {
            this.position.set(cam.getAim(100));
            final PVector right = cam.getRight();
            this.xEular = (float) Math.atan2(right.x, right.z) - PConstants.PI;
        }
    }

    public void draw() {
        parent.pushMatrix();

        // These three form orthonormal basis for left coordinate system
//        final PVector forward = cam.getForward();
//        final PVector right = cam.getRight();
//        final PVector up = forward.cross(right);

        // Reference lines
//        parent.stroke(255, 255, 0); // yellow
//        PVector f = PVector.add(position, PVector.mult(forward, 100));
//        parent.line(position.x, position.y, position.z, f.x, f.y, f.z);
//
//        parent.stroke(0, 255, 255); // cyan
//        PVector r = PVector.add(position, PVector.mult(right, 100));
//        parent.line(position.x, position.y, position.z, r.x, r.y, r.z);
//
//        parent.stroke(255, 0, 255); // pink
//        PVector u = PVector.add(position, PVector.mult(up, 100));
//        parent.line(position.x, position.y, position.z, u.x, u.y, u.z);

        parent.translate(position.x, position.y, position.z);
        parent.rotateZ(zEular);
        parent.rotateY(yEular);
        parent.rotateX(xEular);

//        parent.applyMatrix(
//                u.x, u.y, u.z, 0,
//                f.x, f.y, f.z, 0,
//                r.x, r.y, r.z, 0,
//                0, 0, 0, 1
//        );
//        parent.applyMatrix(
//                u.x, f.x, r.x, 0,
//                u.y, f.y, r.y, 0,
//                u.z, f.z, r.z, 0,
//                0, 0, 0, 1
//        );
//        parent.applyMatrix(
//                1, 0, 0, 0,
//                0, 1, 0, 0,
//                0, 0, 1, 0,
//                0, 0, 0, 1
//        );

        parent.shape(shape);

        // Reference lines
//        parent.stroke(255, 0, 0);
//        parent.line(0, 0, 0, 50, 0, 0);
//        parent.stroke(0, 255, 0);
//        parent.line(0, 0, 0, 0, 50, 0);
//        parent.stroke(0, 0, 255);
//        parent.line(0, 0, 0, 0, 0, 50);

        parent.popMatrix();
    }

    public void cut(GridThreadPointMassSystem system) {
        for (int i = 0; i < system.m; i++) {
            for (int j = 0; j < system.n; j++) {
                PointMass mass = system.pointMasses.get(i).get(j);
                if (isTouching(mass.position)) {
                    for (Thread spring : mass.threads) {
                        spring.setBroken(true);
                    }
                    mass.setIsBroken(true);
                }
            }
        }
    }

    public void toggleIsPaused() {
        underKeyboardControl = !underKeyboardControl;
    }

    private boolean isTouching(Vec3 v) {
        // End-points of the line segment representing the sword.
        PVector forwardProjOnZX = cam.getForward();
        forwardProjOnZX.y = 0;
        PVector a = PVector.add(position, PVector.mult(forwardProjOnZX, 43));
        PVector b = PVector.add(position, PVector.mult(forwardProjOnZX, 7));

        float collisionRadius = 5f;
        return distanceToSegment(v, Vec3.of(a.x, a.y, a.z), Vec3.of(b.x, b.y, b.z)) <= collisionRadius;
    }

    /*
     * Taken from: https://stackoverflow.com/a/36425155/4031302
     */
    private float distanceToSegment(Vec3 v, Vec3 a, Vec3 b) {
        final Vec3 ab = b.minus(a);
        final Vec3 av = v.minus(a);

        if (av.dot(ab) <= 0.0) {
            // Point is lagging behind start of the segment, so perpendicular distance is not viable.
            // Use distance to start of segment instead.
            return av.abs();
        }

        final Vec3 bv = v.minus(b);

        if (bv.dot(ab) >= 0.0) {
            // Point is advanced past the end of the segment, so perpendicular distance is not viable.
            // Use distance to end of the segment instead.
            return bv.abs();
        }

        // Perpendicular distance of point to segment.
        return (ab.cross(av)).abs() / ab.abs();
    }

    public void moveCutter(char key) {
        if (!underKeyboardControl) {
            return;
        }
        float dx = 1f;
        if (key == 'w') {
            moveForward(dx);
        }
        if (key == 's') {
            moveForward(-dx);
        }
        if (key == 'a') {
            moveRight(dx);
        }
        if (key == 'd') {
            moveRight(-dx);
        }
        if (key == 'q') {
            moveUp(dx);
        }
        if (key == 'e') {
            moveUp(-dx);
        }
    }

    private void moveUp(float dx) {
        this.position.add(PVector.mult(cam.getUp(), dx));
    }

    private void moveRight(float dx) {
        this.position.add(PVector.mult(cam.getRight(), dx));
    }

    private void moveForward(float dx) {
        this.position.add(PVector.mult(cam.getForward(), dx));
    }
}
