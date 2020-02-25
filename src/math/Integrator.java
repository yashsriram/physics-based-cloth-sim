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

    public static void secondOrder(Vec3 position, Vec3 velocity, Vec3 acceleration, float dt) {
        position.plusAccumulate(velocity.scale(dt).plus(acceleration.scale(0.5f * dt * dt)));
        velocity.plusAccumulate(acceleration.scale(dt));
    }
}
