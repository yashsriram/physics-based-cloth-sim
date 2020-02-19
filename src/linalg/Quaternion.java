package linalg;

public class Quaternion {
    float real;
    Vec3 imaginary;

    public static Quaternion identity() {
        return new Quaternion(1, Vec3.zero());
    }

    private Quaternion(float real, Vec3 imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
}
