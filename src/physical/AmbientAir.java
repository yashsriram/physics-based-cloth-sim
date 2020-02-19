package physical;

import linalg.Vec3;

public class AmbientAir {
	float density = 1.2f;
	float dragCoeff = 1f;
	Vec3 windDirection = Vec3.of(0,0,1);
	float windSpeed = 0;
	
	public AmbientAir(float density, Vec3 windDirection, float windSpeed) {
		this.density = density;
		this.windDirection = windDirection;
		this.windSpeed = windSpeed;
	}
	
	public AmbientAir() {
	}
}
