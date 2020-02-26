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

	public static void secondOrderFullStep(Vec3 positionMidpoint, Vec3 velocityMidpoint, Vec3 accelerationMidpoint,
										   Vec3 positionOld, Vec3 velocityOld, float dt) {
		positionMidpoint.set( positionOld.plus(velocityMidpoint.scale(dt)) );
		velocityMidpoint.set( velocityOld.plus(accelerationMidpoint.scale(dt)) );
	}
	
	public static void secondOrderFullStep(Vec3 position, Vec3 velocity, Vec3 acceleration, float dt) {
		position.plusAccumulate(velocity.scale(dt));
        velocity.plusAccumulate(acceleration.scale(dt));
	}
}
