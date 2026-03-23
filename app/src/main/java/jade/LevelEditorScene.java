package jade;

import jade.util.Time;
import renderer.Camera;
import renderer.Shader;
import renderer.Texture;
import renderer.Shader.SafeCloseable;

import static org.lwjgl.opengl.GL30C.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class LevelEditorScene extends Scene {
    private Shader shaderProgram;
    private int vaoID, vboID, eboID;
    private float[] vertexArray = {
        // position             // color                    // UV Coords
        100f, 100f, 0f,         0.0f, 0.0f, 1.0f, 1.0f,     1.0f, 1.0f, // Top right
        100f, 0f,   0f,         1.0f, 0.0f, 0.0f, 1.0f,     1.0f, 0.0f, // Bottom right
        0f,   100f, 0f,         0.0f, 1.0f, 0.0f, 1.0f,     0.0f, 1.0f, // Top left
        0f,   0f,   0f,         1.0f, 1.0f, 0.0f, 1.0f,     0.0f, 0.0f, // Bottom left
    };
    // cc order
    private int[] elementArray = {
        0, 2, 3, 1
    };
    
    public LevelEditorScene() {}
    
    @Override
    public void init(Window window){
        this.camera = new Camera(new Vector2f(0.0f, 0.0f));
        shaderProgram = new Shader("vertex.glsl", "fragment.glsl");
        shaderProgram.uploadTexture("TEX_SAMPLER", new Texture("image.png"));

        // Set Up Buffers
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(Window window, Time time) {
        try (SafeCloseable handle = shaderProgram.use()){
            shaderProgram.uploadMat4f("uProjection", camera.getProjextionMat());
            shaderProgram.uploadMat4f("uView", camera.getViewMat());
            shaderProgram.uploadFloat("uTime", time.getTime());
            glBindVertexArray(vaoID);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glDrawElements(GL_QUADS, elementArray.length, GL_UNSIGNED_INT, 0);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);
        }
    }
}
