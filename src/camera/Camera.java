package camera;

import processing.core.PApplet;
import processing.core.PVector;

public class Camera {

    // only necessary to change if you want different start position, orientation, or speeds
    PVector position;
    float theta;
    float phi;
    float moveSpeed;
    float turnSpeed;

    // probably don't need / want to change any of the below variables
    final PApplet parent;
    float fovy;
    float aspectRatio;
    float nearPlane;
    float farPlane;
    PVector negativeMovement;
    PVector positiveMovement;
    PVector negativeTurn;
    PVector positiveTurn;

    public Camera(PApplet parent) {
        this.parent = parent;
        position = new PVector(0, 0, 0); // initial position
        theta = 0; // rotation around Y axis. Starts with forward direction as ( 0, 0, -1 )
        phi = 0; // rotation around X axis. Starts with up direction as ( 0, 1, 0 )
        moveSpeed = 50;
        turnSpeed = 1.57f; // radians/sec

        // dont need to change these
        negativeMovement = new PVector(0, 0, 0);
        positiveMovement = new PVector(0, 0, 0);
        negativeTurn = new PVector(0, 0); // .x for theta, .y for phi
        positiveTurn = new PVector(0, 0);
        fovy = parent.PI / 4;
        aspectRatio = parent.width / (float) parent.height;
        nearPlane = 0.1f;
        farPlane = 10000;
    }

    public void Update(float dt) {
        theta += turnSpeed * (negativeTurn.x + positiveTurn.x) * dt;

        // cap the rotation about the X axis to be less than 90 degrees to avoid gimble lock
        float maxAngleInRadians = 85 * parent.PI / 180;
        phi = parent.min(maxAngleInRadians, parent.max(-maxAngleInRadians, phi + turnSpeed * (negativeTurn.y + positiveTurn.y) * dt));

        // re-orienting the angles to match the wikipedia formulas: https://en.wikipedia.org/wiki/Spherical_coordinate_system
        // except that their theta and phi are named opposite
        float t = theta + parent.PI / 2;
        float p = phi + parent.PI / 2;
        PVector forwardDir = new PVector(parent.sin(p) * parent.cos(t), parent.cos(p), -parent.sin(p) * parent.sin(t));
        PVector upDir = new PVector(parent.sin(phi) * parent.cos(t), parent.cos(phi), -parent.sin(t) * parent.sin(phi));
        PVector rightDir = new PVector(parent.cos(theta), 0, -parent.sin(theta));
        PVector velocity = new PVector(negativeMovement.x + positiveMovement.x, negativeMovement.y + positiveMovement.y, negativeMovement.z + positiveMovement.z);
        position.add(PVector.mult(forwardDir, moveSpeed * velocity.z * dt));
        position.add(PVector.mult(upDir, moveSpeed * velocity.y * dt));
        position.add(PVector.mult(rightDir, moveSpeed * velocity.x * dt));

        aspectRatio = parent.width / (float) parent.height;
        parent.perspective(fovy, aspectRatio, nearPlane, farPlane);
        parent.camera(position.x, position.y, position.z, position.x + forwardDir.x, position.y + forwardDir.y, position.z + forwardDir.z, upDir.x, upDir.y, upDir.z);
    }

    // only need to change if you want difrent keys for the controls
    public void HandleKeyPressed() {
        if (parent.key == 'w') positiveMovement.z = 1;
        if (parent.key == 's') negativeMovement.z = -1;
        if (parent.key == 'a') negativeMovement.x = -1;
        if (parent.key == 'd') positiveMovement.x = 1;
        if (parent.key == 'q') positiveMovement.y = 1;
        if (parent.key == 'e') negativeMovement.y = -1;

        if (parent.keyCode == parent.LEFT) negativeTurn.x = 1;
        if (parent.keyCode == parent.RIGHT) positiveTurn.x = -1;
        if (parent.keyCode == parent.UP) positiveTurn.y = 1;
        if (parent.keyCode == parent.DOWN) negativeTurn.y = -1;
    }

    // only need to change if you want difrent keys for the controls
    public void HandleKeyReleased() {
        if (parent.key == 'w') positiveMovement.z = 0;
        if (parent.key == 'q') positiveMovement.y = 0;
        if (parent.key == 'd') positiveMovement.x = 0;
        if (parent.key == 'a') negativeMovement.x = 0;
        if (parent.key == 's') negativeMovement.z = 0;
        if (parent.key == 'e') negativeMovement.y = 0;

        if (parent.keyCode == parent.LEFT) negativeTurn.x = 0;
        if (parent.keyCode == parent.RIGHT) positiveTurn.x = 0;
        if (parent.keyCode == parent.UP) positiveTurn.y = 0;
        if (parent.keyCode == parent.DOWN) negativeTurn.y = 0;
    }
}
