package linalg;

import java.util.ArrayList;
import java.util.List;

class Mat4 {
    public enum Axis {
        X, Y, Z
    }
    private static final int nR = 4;
    private static final int nC = 4;

    // row major order
    List<Float> values = new ArrayList<>();

    Mat4() {
        setIdentity();
    }

    Mat4(float c) {
        set(c);
    }

    // Rotation about x,y,z axes transform
    Mat4(final float angleInDegrees, Axis axis) {
        setIdentity();
        switch (axis) {
            case X:
                values.set(5, (float) Math.cos(angleInDegrees / 180 * Math.PI));
                values.set(6, (float) -Math.sin(angleInDegrees / 180 * Math.PI));
                values.set(9, (float) Math.sin(angleInDegrees / 180 * Math.PI));
                values.set(10, (float) Math.cos(angleInDegrees / 180 * Math.PI));
                break;
            case Y:
                values.set(0, (float) Math.cos(angleInDegrees / 180 * Math.PI));
                values.set(2, (float) Math.sin(angleInDegrees / 180 * Math.PI));
                values.set(8, (float) -Math.sin(angleInDegrees / 180 * Math.PI));
                values.set(10, (float) Math.cos(angleInDegrees / 180 * Math.PI));
                break;
            case Z:
                values.set(0, (float) Math.cos(angleInDegrees / 180 * Math.PI));
                values.set(1, (float) -Math.sin(angleInDegrees / 180 * Math.PI));
                values.set(4, (float) Math.sin(angleInDegrees / 180 * Math.PI));
                values.set(5, (float) Math.cos(angleInDegrees / 180 * Math.PI));
                break;
        }
    }

    // Translation transform
    Mat4(final Vec3 translation) {
        setIdentity();
        values.set(3, translation.x);
        values.set(7, translation.y);
        values.set(11, translation.z);
    }

    // Scale transform
    Mat4(final float scaleX, final float scaleY, final float scaleZ) {
        setIdentity();
        values.set(0, scaleX);
        values.set(5, scaleY);
        values.set(10, scaleZ);
    }

    // Deep copy
    Mat4(final Mat4 m) {
        values = new ArrayList<>(m.values);
    }

    float get(final int i, final int j) {
        return values.get(i * nC + j);
    }

    void set(final int i, final int j, final float k) {
        values.set(i * nC + j, k);
    }


    Mat4 set(float c) {
        for (int i = 0; i < nR * nC; ++i) {
            values.set(i, c);
        }
        return this;
    }

    Mat4 setIdentity() {
        set(0);
        values.set(0, 1f);
        values.set(5, 1f);
        values.set(10, 1f);
        return this;
    }

    Mat4 transpose() {
        Mat4 result = new Mat4();
        for (int i = 0; i < nR; ++i) {
            for (int j = 0; j < nC; ++j) {
                result.values.set(j * nR + i, values.get(i * nC + j));
            }
        }

        return result;
    }

    Mat4 plus(final Mat4 m) {
        Mat4 result = new Mat4();

        for (int i = 0; i < nR; ++i) {
            for (int j = 0; j < nC; ++j) {
                result.values.set(i * nC + j, values.get(i * nC + j) + m.values.get(i * nC + j));
            }
        }

        return result;
    }

    Mat4 minus(final Mat4 m) {
        Mat4 result = new Mat4();

        for (int i = 0; i < nR; ++i) {
            for (int j = 0; j < nC; ++j) {
                result.values.set(i * nC + j, values.get(i * nC + j) - m.values.get(i * nC + j));
            }
        }

        return result;
    }

    Mat4 mul(final Mat4 m) {
        Mat4 result = new Mat4();

        for (int i = 0; i < nR; ++i) {
            for (int j = 0; j < m.nC; ++j) {
                float elementSum = 0;
                for (int k = 0; k < nC; ++k) {
                    elementSum += values.get(i * nC + k) * m.values.get(k * m.nC + j);
                }
                result.values.set(i * m.nC + j, elementSum);
            }
        }
        return result;
    }

    Mat4 mul(final float m) {
        Mat4 result = new Mat4();

        for (int i = 0; i < nR; ++i) {
            for (int j = 0; j < nC; ++j) {
                result.values.set(i * nC + j, values.get(i * nC + j) * m);
            }
        }

        return result;
    }

    Vec3 transformVector(final Vec3 v) {
        return Vec3.of(values.get(0) * v.x + values.get(1) * v.y + values.get(2) * v.z + values.get(3) * 0,
                values.get(4) * v.x + values.get(5) * v.y + values.get(6) * v.z + values.get(7) * 0,
                values.get(8) * v.x + values.get(9) * v.y + values.get(10) * v.z + values.get(11) * 0);
    }

}
