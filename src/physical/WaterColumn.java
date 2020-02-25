package physical;

import processing.core.PApplet;

public class WaterColumn {
	PApplet parent;
	public float height;
	public float momentum;
	public float midpointMomentum;
	public float midpointHeight;

    public WaterColumn(PApplet parent, float height, float momentum) {
        this.parent = parent;
        this.height = height;
        this.momentum = momentum;
    }

}
