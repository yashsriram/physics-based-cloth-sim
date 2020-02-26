package physical;

import processing.core.PApplet;

public class WaterColumn {
	PApplet parent;
	public float height;
	public float momentumZ;
	public float momentumX;
	
	public float midpointHeightZ;
	public float midpointHeightX;
	public float midpointZMomentum_alongZ;
	public float midpointZMomentum_alongX;
	public float midpointXMomentum_alongZ;
	public float midpointXMomentum_alongX;

    public WaterColumn(PApplet parent, float height, float momentum) {
        this.parent = parent;
        this.height = height;
        this.momentumZ = momentum;
    }

}
