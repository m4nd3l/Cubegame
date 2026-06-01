package dev.m4nd3l.cubegame.engine.rendering.input;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.ShaderProgram;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.uniforms.Matrix4fUniform;
import dev.m4nd3l.cubegame.game.Cubegame;
import dev.m4nd3l.cubegame.engine.coordinates.EntityCoordinates;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static dev.m4nd3l.cubegame.engine.rendering.glfw.GLFW.*;

public class Camera {

    private float nearPlane, farPlane;
    private int width, height;

    private float speed = 16f;
    private float sensitivity = 100.0f;

    private Vector3f cameraPosition,
                     cameraOrientation = new Vector3f(0.0f, 0.0f, -1.0f),
                     upDirection = new Vector3f(0.0f, 1.0f, 0.0f);
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f combinedMatrix = new Matrix4f();

    private FrustumIntersection frustum;
    public transient boolean frustumFreeze;

    public Camera(float nearPlane, float farPlane, int width, int height, EntityCoordinates cameraPosition) { this(nearPlane, farPlane, width, height, cameraPosition.toVector3f()); }
    public Camera(float nearPlane, float farPlane, int width, int height, Vector3f cameraPosition) {
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
        this.width = width;
        this.height = height;
        this.cameraPosition = cameraPosition;
        this.frustum = new FrustumIntersection();
        frustumFreeze = false;
    }

    public Camera() { }

    public Matrix4f getViewMatrix() { return viewMatrix; }
    public Matrix4f getProjectionMatrix() { return projectionMatrix; }

    public void processKeyboard(double deltaTime) {
        float velocity = speed * (float) deltaTime;

        if (InputSystem.isKeyDown(KeyboardKeys.W)) cameraPosition.add(new Vector3f(cameraOrientation).mul(velocity));

        if (InputSystem.isKeyDown(KeyboardKeys.A))
            cameraPosition.add(
                    new Vector3f(cameraOrientation)
                            .cross(upDirection)
                            .normalize()
                            .mul(-velocity)
            );

        if (InputSystem.isKeyDown(KeyboardKeys.S))  cameraPosition.add(new Vector3f(cameraOrientation).mul(-velocity));

        if (InputSystem.isKeyDown(KeyboardKeys.D))  cameraPosition.add(
                    new Vector3f(cameraOrientation)
                            .cross(upDirection)
                            .normalize()
                            .mul(velocity)
            );

        if (InputSystem.isKeyDown(KeyboardKeys.SPACE))  cameraPosition.add(new Vector3f(upDirection).mul(velocity));

        if (InputSystem.isKeyDown(KeyboardKeys.LEFT_SHIFT))  cameraPosition.add(new Vector3f(upDirection).mul(-velocity));

        if (InputSystem.isKeyDown(KeyboardKeys.LEFT_CONTROL)) speed = 32.0f;
        else speed = 16.0f;

    }

    public void processMouseMovement(long glfwWindow) {
        if (InputSystem.isKeyDown(MouseKeys.LEFT)) {
            hideMouseCursor(glfwWindow);

            double mouseX = InputSystem.getX();
            double mouseY = InputSystem.getY();

            float rotationX = sensitivity * (float) (mouseY - height / 2.0f) / height;
            float rotationY = sensitivity * (float) (mouseX - width / 2.0f) / width;

            Vector3f right = new Vector3f(cameraOrientation)
                    .cross(upDirection)
                    .normalize();

            Vector3f newOrientation = new Vector3f(cameraOrientation)
                    .rotateAxis((float) Math.toRadians(-rotationX),
                            right.x, right.y, right.z);

            float angle = newOrientation.angle(upDirection);

            if (Math.abs(angle - Math.toRadians(90.0f)) <= Math.toRadians(85.0f)) cameraOrientation.set(newOrientation);

            cameraOrientation.rotateAxis(
                    (float) Math.toRadians(-rotationY),
                    upDirection.x, upDirection.y, upDirection.z
            );

            setMouseCursorPosition(glfwWindow, width / 2.0, height / 2.0);
        } else showMouseCursor(glfwWindow);

    }

    public void uploadUniforms(ShaderProgram shader) {
        shader.uploadUniform(new Matrix4fUniform("uView", getViewMatrix(), shader.getID()));
        shader.uploadUniform(new Matrix4fUniform("uProjection", getProjectionMatrix(), shader.getID()));
    }// TODO GIVE UP ON THIS AND POLISH CRAFTMINE

    public void updateFrustum() { combinedMatrix.set(projectionMatrix).mul(viewMatrix); frustum.set(combinedMatrix); }

    public void updateMatrices() {
        Vector3f center = new Vector3f(cameraPosition).add(cameraOrientation);
        viewMatrix.identity().lookAt(cameraPosition, center, upDirection);
        projectionMatrix.identity().perspective(
                (float) Math.toRadians(Cubegame.SETTINGS.getFOV()), (float) width / (float) height, nearPlane, farPlane);
    }

    public EntityCoordinates getCoordinates() { return new EntityCoordinates(cameraPosition.x(), cameraPosition.y(), cameraPosition.z()); }

    public boolean isInsideFrustum(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) { return frustum.testAab(minX, minY, minZ, maxX, maxY, maxZ); }
    public Camera resizeWindow(int width, int height) { this.width = width; this.height = height; updateMatrices(); return this; }

    public Camera setSpeed(float speed) { this.speed = speed; return this; }
    public Camera setSensitivity(float sensitivity) { this.sensitivity = sensitivity; return this; }
    public Camera setFOV(float FOV) { Cubegame.SETTINGS.setFOV(FOV); updateMatrices(); return this; }
    public Camera setNearPlane(float nearPlane) { this.nearPlane = nearPlane; updateMatrices(); return this; }
    public Camera setFarPlane(float farPlane) { this.farPlane = farPlane; updateMatrices(); return this; }

}