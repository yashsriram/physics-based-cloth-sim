package math;

public class Integrator {
    public static void firstOrder(Vec2 position, Vec2 velocity, Vec2 acceleration, float dt) {
        position.plusAccumulate(velocity.scale(dt));
        velocity.plusAccumulate(acceleration.scale(dt));
    }

    public static void firstOrder(Vec3 position, Vec3 velocity, Vec3 acceleration, float dt) {
        position.plusAccumulate(velocity.scale(dt));
        velocity.plusAccumulate(acceleration.scale(dt));
    }

    public static void secondOrderHalfStep(Vec3 position, Vec3 velocity, Vec3 acceleration, float dt) {
    	position.plusAccumulate(velocity.scale(dt/2));
        velocity.plusAccumulate(acceleration.scale(dt/2));
    }

	public static void secondOrderFullStep(Vec3 position, Vec3 velocity, Vec3 acceleration,
										   Vec3 positionOld, Vec3 velocityOld, Vec3 accelerationOld,
										   float dt) {
		velocity.set(velocityOld);
		velocity.plusAccumulate(acceleration.scale(dt));
		
		// Set to value at the beginning of half time step
		position.set(positionOld);
		// Update for full time step with the derivative at midpoint.
		position.plusAccumulate(velocity.scale(dt));
		
		acceleration.set(accelerationOld);
	}
}
