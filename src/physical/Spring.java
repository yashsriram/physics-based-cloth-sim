package physical;

import linalg.Vec3;
import processing.core.PApplet;

public class Spring {
    private PApplet parent;
    private float restLength;
    private float forceConstant;
    private float dampConstant;
    private SpringMass m1;
    private SpringMass m2;

    public Spring(PApplet parent, float restLength, float forceConstant, float dampConstant, SpringMass m1, SpringMass m2) {
        this.parent = parent;
        this.restLength = restLength;
        this.forceConstant = forceConstant;
        this.dampConstant = dampConstant;
        this.m1 = m1;
        this.m2 = m2;
        m1.springs.add(this);
        m2.springs.add(this);
    }

    public Vec3 forceOn(SpringMass m) throws Exception {
        Vec3 lengthVector = Vec3.zero();
        SpringMass mOther = m;
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
        Vec3 springForce = Vec3.zero();
        float extension = springLength - restLength;
        if (extension > 0) {
            springForce = forceDir.scale(forceConstant * (extension));
        }
        Vec3 dampForce = forceDir.scale(-1 * dampConstant * (m.velocity.dot(forceDir) - mOther.velocity.dot(forceDir)));
        return springForce.plus(dampForce);
    }

    public void draw() {
        parent.stroke(255);
        parent.line(m1.position.x, m1.position.y, m1.position.z, m2.position.x, m2.position.y, m2.position.z);
    }

}
