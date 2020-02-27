package demos;

import camera.QueasyCam;
import math.Vec3;
import physical.WaterColumn;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.ArrayList;

public class ShallowWater2D extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private QueasyCam queasyCam;

    PImage waterTexture;
    ArrayList<ArrayList<WaterColumn>> waterColumns;
    private int gridSize = 100;
    float lengthX = 200, lengthY = 60, lengthZ = 200;
    float columnHeight = 20;
    float momentum = 0;
    private float dz;
    private boolean isPlaying = true;
    private boolean textureOn = true;
    private Vec3 perturbSource = Vec3.of(0, 60, 0);
    private float perturbSourceAmplitude = 0;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        queasyCam = new QueasyCam(this);
        queasyCam.sensitivity = 2f;

        waterTexture = loadImage("water.jpg");
        resetSystem();
    }

    private void resetSystem() {
        waterColumns = new ArrayList<>();
        dz = lengthZ / gridSize;
        for (int i = 0; i < gridSize; i++) {
            waterColumns.add(new ArrayList<>());
            for (int j = 0; j < gridSize; j++) {
                if (Math.abs(i - gridSize / 2) <= 10 && Math.abs(j - gridSize / 2) <= 10) {
                    waterColumns.get(i).add(new WaterColumn(this, 1.5f * columnHeight, momentum));
                } else {
                    waterColumns.get(i).add(new WaterColumn(this, columnHeight, momentum));
                }
            }
        }
    }

    public void draw() {
        if (keyPressed) {
            if (keyCode == UP) {
                movePerturbSource(Vec3.of(2, 0, 0));
            } else if (keyCode == DOWN) {
                movePerturbSource(Vec3.of(-2, 0, 0));
            } else if (keyCode == LEFT) {
                movePerturbSource(Vec3.of(0, 0, -2));
            } else if (keyCode == RIGHT) {
                movePerturbSource(Vec3.of(0, 0, 2));
            }
        }

        long start = millis();
        // update
        try {
            if (isPlaying) {
                for (int i = 0; i < 52; i++) {
                    updateLoop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long update = millis();
        // draw
        background(0);
        directionalLight(128, 128, 128, 0, 1, 0);
        directionalLight(128, 128, 128, 0, 1, 1);
        directionalLight(128, 128, 128, 1, 1, 0);
        drawWater();
        drawPerturbSource();
        if (textureOn) {
            drawWaterTank();
        }
        long draw = millis();

        surface.setTitle("Processing - FPS: " + Math.round(frameRate) + " Update: " + (update - start) + "ms Draw " + (draw - update) + "ms" + " Grid size: " + gridSize);
    }

    private void updateLoop() {
        float dt = 0.003f;
        float g = 1f;
        float damp = 0.15f;

        // Half step
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                WaterColumn w1 = waterColumns.get(i).get(j);
                if (i < gridSize - 1) {
                    WaterColumn w2 = waterColumns.get(i + 1).get(j);

                    w1.midpointHeightZ = (w1.height + w2.height) / 2f;
                    w1.midpointHeightZ += -(dt / 2f) * (w1.momentumZ - w2.momentumZ) / dz;

                    w1.midpointZMomentum_alongZ = (w1.momentumZ + w2.momentumZ) / 2f;
                    w1.midpointZMomentum_alongZ += -(dt / 2f) * (sq(w2.momentumZ) / w2.height
                            - sq(w1.momentumZ) / w1.height
                            + 0.5f * g * sq(w2.height)
                            - 0.5f * g * sq(w1.height)
                    ) / dz;
                    w1.midpointZMomentum_alongX = (w1.momentumZ + w2.momentumZ) / 2f;
                    w1.midpointZMomentum_alongX += -(dt / 2f) * (sq(w2.momentumZ) / w2.height
                            - sq(w1.momentumZ) / w1.height
                            + 0.5f * g * sq(w2.height)
                            - 0.5f * g * sq(w1.height)
                    ) / dz;
                }

                if (j < gridSize - 1) {
                    WaterColumn w3 = waterColumns.get(i).get(j + 1);

                    w1.midpointHeightX = (w1.height + w3.height) / 2f;
                    w1.midpointHeightX += -(dt / 2f) * (w1.momentumZ - w3.momentumZ) / dz;

                    w1.midpointXMomentum_alongZ = (w1.momentumX + w3.momentumX) / 2f;
                    w1.midpointXMomentum_alongZ += -(dt / 2f) * (sq(w3.momentumX) / w3.height
                            - sq(w1.momentumX) / w1.height
                            + 0.5f * g * sq(w3.height)
                            - 0.5f * g * sq(w1.height)
                    ) / dz;
                    w1.midpointXMomentum_alongX = (w1.momentumX + w3.momentumX) / 2f;
                    w1.midpointXMomentum_alongX += -(dt / 2f) * (sq(w3.momentumX) / w3.height
                            - sq(w1.momentumX) / w1.height
                            + 0.5f * g * sq(w3.height)
                            - 0.5f * g * sq(w1.height)
                    ) / dz;
                }
            }
        }

        // Full step
        for (int i = 0; i < gridSize - 2; i++) {
            for (int j = 0; j < gridSize - 2; j++) {
                WaterColumn w1 = waterColumns.get(i + 1).get(j + 1);
                WaterColumn w2 = waterColumns.get(i).get(j + 1);
                WaterColumn w3 = waterColumns.get(i + 1).get(j);

                w1.height += -dt * (w1.midpointZMomentum_alongZ - w2.midpointZMomentum_alongZ) / dz;
                w1.height += -dt * (w1.midpointXMomentum_alongX - w3.midpointXMomentum_alongX) / dz;

                w1.momentumZ += -dt * (sq(w1.midpointZMomentum_alongZ) / w1.midpointHeightZ
                        - sq(w2.midpointZMomentum_alongZ) / w2.midpointHeightZ
                        + 0.5 * g * sq(w1.midpointHeightZ)
                        - 0.5 * g * sq(w2.midpointHeightZ)
                ) / dz;
                w1.momentumZ += -dt * (w1.midpointXMomentum_alongX * w1.midpointZMomentum_alongX / w1.midpointHeightX
                        - w3.midpointXMomentum_alongX * w3.midpointZMomentum_alongX / w3.midpointHeightX
                ) / dz;

                w1.momentumX += -dt * (sq(w1.midpointXMomentum_alongX) / w1.midpointHeightX
                        - sq(w3.midpointXMomentum_alongX) / w3.midpointHeightX
                        + 0.5 * g * sq(w1.midpointHeightX)
                        - 0.5 * g * sq(w3.midpointHeightX)
                ) / dz;
                w1.momentumX += -dt * (w1.midpointXMomentum_alongZ * w1.midpointZMomentum_alongZ / w1.midpointHeightZ
                        - w2.midpointXMomentum_alongZ * w2.midpointZMomentum_alongZ / w2.midpointHeightZ
                ) / dz;

                // Damping
                w1.momentumZ += -dt * damp * w1.momentumZ;
                w1.momentumX += -dt * damp * w1.momentumX;
            }
        }

        boundaryConditions();
    }

    private void boundaryConditions() {
        // Reflecting at walls
        for (int i = 0; i < gridSize; i++) {
            WaterColumn w_i0 = waterColumns.get(i).get(0);
            WaterColumn w_i1 = waterColumns.get(i).get(1);
            WaterColumn w_is = waterColumns.get(i).get(gridSize - 1);
            WaterColumn w_is_1 = waterColumns.get(i).get(gridSize - 2);

            WaterColumn w_0i = waterColumns.get(0).get(i);
            WaterColumn w_1i = waterColumns.get(1).get(i);
            WaterColumn w_si = waterColumns.get(gridSize - 1).get(i);
            WaterColumn w_s_1i = waterColumns.get(gridSize - 2).get(i);

            w_i0.height = w_i1.height;
            w_is.height = w_is_1.height;
            w_0i.height = w_1i.height;
            w_si.height = w_s_1i.height;

            w_i0.momentumZ = w_i1.momentumZ;
            w_is.momentumZ = w_is_1.momentumZ;
            w_0i.momentumZ = -w_1i.momentumZ;
            w_si.momentumZ = -w_s_1i.momentumZ;

            w_i0.momentumX = -w_i1.momentumX;
            w_is.momentumX = -w_is_1.momentumX;
            w_0i.momentumX = w_1i.momentumX;
            w_si.momentumX = w_s_1i.momentumX;
        }
    }

    private void drawWaterTank() {
        pushMatrix();
        stroke(255);
        translate(0, lengthY, 0);
        fill(0, 0);
        box(lengthX, lengthY, lengthZ);
        popMatrix();
    }

    private void drawPerturbSource() {
        pushMatrix();
        translate(perturbSource.x, perturbSource.y + random(-perturbSourceAmplitude, perturbSourceAmplitude), perturbSource.z);
        fill(255);
        box(20);
        popMatrix();
        perturbSourceAmplitude = perturbSourceAmplitude / 1.1f;
    }

    private void movePerturbSource(Vec3 ds) {
        perturbSource.plusAccumulate(ds);
    }

    private void drawWater() {
        float left = -gridSize / 2 * dz;
        float base = 1.5f * lengthY;
        float front = -gridSize / 2 * dz;

        for (int i = 0; i < gridSize - 1; i++) {
            beginShape(TRIANGLE_STRIP);
            if (textureOn) {
                noFill();
                noStroke();
                textureMode(PConstants.NORMAL);
                texture(waterTexture);
            } else {
                fill(0, 80, 255);
                stroke(255);
            }
            for (int j = 0; j < gridSize - 1; j++) {
                WaterColumn column1 = waterColumns.get(i).get(j);
                WaterColumn columnZ = waterColumns.get(i + 1).get(j);

                float x1 = front + j * dz;
                float xZ = front + j * dz;

                float y1 = base - column1.height;
                float yZ = base - columnZ.height;

                float z1 = left + i * dz;
                float zZ = left + (i + 1) * dz;
                if (textureOn) {
                    float u = PApplet.map(j, 0, gridSize - 1, 0, 1);
                    float v1 = PApplet.map(i, 0, gridSize - 1, 0, 1);
                    float v2 = PApplet.map(i + 1, 0, gridSize - 1, 0, 1);
                    vertex(x1, y1, z1, u, v1);
                    vertex(xZ, yZ, zZ, u, v2);
                } else {
                    vertex(x1, y1, z1);
                    vertex(xZ, yZ, zZ);
                }
            }
            endShape();
        }
    }

    private void perturb() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (Math.abs(i - gridSize / 2f - perturbSource.z / 2f) <= 10 && Math.abs(j - gridSize / 2f - perturbSource.x / 2f) <= 10) {
                    waterColumns.get(i).get(j).height *= 1.5;
                }
            }
        }
        perturbSourceAmplitude = 10;
    }

    public void keyPressed() {
        if (key == 'u') {
            isPlaying = !isPlaying;
        }
        if (key == 't') {
            textureOn = !textureOn;
        }
        if (key == 'r') {
            resetSystem();
        }
        if (key == 'p') {
            perturb();
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"demos.ShallowWater2D"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}