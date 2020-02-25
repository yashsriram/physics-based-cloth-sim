package physical;

import math.Vec3;
import processing.core.PApplet;

public class Thread {
    private PApplet parent;
    private float restLength;
    private float forceConstant;
    private float dampConstant;
    private PointMass m1;
    private PointMass m2;
    private boolean isBroken = false;

    public Thread(PApplet parent, float restLength, float forceConstant, float dampConstant, PointMass m1, PointMass m2) {
        this.parent = parent;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;
        this.m1 = m1;
        this.m2 = m2;
        m1.threads.add(this);
        m2.threads.add(this);
    }

    public Vec3 forceOn(PointMass m) throws Exception {
        if (this.isBroken) {
            return Vec3.zero();
        }
        Vec3 lengthVector = Vec3.zero();
        PointMass mOther = m;
        if (m.id == m1.id) {
            // m1 requested force
            lengthVector = m2.position.minus(m1.position);
            mOther = m2;
        } else if (m.id == m2.id) {
            // m2 requested force
            lengthVector = m1.position.minus(m2.position);
            mOther = m1;
        } else {
            throw new Exception("Force requested on unrelated spring mass");
        }
        float springLength = lengthVector.abs();
        Vec3 forceDir = lengthVector.unit();
        // spring on compression does not exert force
        Vec3 springForce = Vec3.zero();
        float extension = springLength - restLength;
        if (extension > 10) {
            for (Thread s : m1.threads) {
                s.setBroken(true);
            }
            m1.setIsBroken(true);
            for (Thread s : m2.threads) {
                s.setBroken(true);
            }
            m2.setIsBroken(true);
        }
        if (extension > 0) {
            springForce.plusAccumulate(forceDir.scale(forceConstant * extension));
        }
        // dampening force along spring
        springForce.plusAccumulate(forceDir.scale(-1 * dampConstant * (m.velocity.dot(forceDir) - mOther.velocity.dot(forceDir))));
        return springForce;
    }

    public void draw() {
        if (this.isBroken) {
            parent.stroke(255, 0, 0, 255);
            return;
        } else {
            parent.stroke(255);
        }
        parent.line(m1.position.x, m1.position.y, m1.position.z, m2.position.x, m2.position.y, m2.position.z);
    }

    public void draw2D() {
        if (this.isBroken) {
            parent.stroke(255, 0, 0, 255);
            return;
        } else {
            parent.stroke(255);
        }
        parent.line(m1.position.x, m1.position.y, m2.position.x, m2.position.y);
    }

    public void setBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }
}
