package physical;

import linalg.Vec3;

public class AmbientAir {
    float density;
    float dragCoefficient;
    Vec3 windDirection;
    public float windSpeed;

    public AmbientAir(float density, float dragCoefficient, Vec3 windDirection, float windSpeed) {
        this.density = density;
        this.dragCoefficient = dragCoefficient;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
    }

    public void increaseSpeed(float dv) {
        this.windSpeed += dv;
    }

    public void decreaseSpeed(float dv) {
        this.windSpeed -= dv;
    }
}
