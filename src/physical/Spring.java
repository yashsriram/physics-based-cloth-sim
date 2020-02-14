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
        if (m.id == m1.id) {
            // m1 requested force
            lengthVector = m2.position.minus(m1.position);
        } else if (m.id == m2.id) {
            // m2 requested force
            lengthVector = m1.position.minus(m2.position);
        } else {
            throw new Exception("Force requested on unrelated spring mass");
        }
        float springLength = lengthVector.abs();
        Vec3 forceDir = lengthVector.unit();
        Vec3 springForce = forceDir.scale(forceConstant * (springLength - restLength));
        Vec3 dampForce = m.velocity.scale(-1 * dampConstant);
        return springForce.plus(dampForce);
    }

    public void draw() {
        parent.stroke(255);
        parent.line(m1.position.x, m1.position.y, m1.position.z, m2.position.x, m2.position.y, m2.position.z);
    }

}
