package renderer;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;


public class Camera {
    private Matrix4f projectionMat, viewMat;
    private Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMat = new Matrix4f();
        this.viewMat = new Matrix4f();
        adjustProjection();
    }
    public void adjustProjection(){
        projectionMat.setOrtho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }
    public Matrix4f getViewMat(){
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        return this.viewMat.setLookAt(
            new Vector3f(position.x, position.y, 20),
            cameraFront.add(position.x, position.y, 0.0f),
            cameraUp
        );
    }

    public Matrix4f getProjextionMat(){
        return this.projectionMat;
    }
}
