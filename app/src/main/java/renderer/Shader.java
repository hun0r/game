package renderer;

import static jade.Utils.getImportantResource;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {
    private int shaderProgramID;
    private Texture tex;

    public Shader(String vertexPath, String fragmentPath){
        final String vertexShaderSrc = getImportantResource("vertex.glsl");
        int vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, vertexShaderSrc);
        glCompileShader(vertexId);
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.err.println("Error: " + vertexPath + " \n\tVertex Shader compilation failed.");
            System.err.println(glGetShaderInfoLog(vertexId, len));
            assert false : "";
        }
        
        final String fragmentShaderSrc = getImportantResource("fragment.glsl");
        int fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, fragmentShaderSrc);
        glCompileShader(fragmentId);
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.err.println("Error: " + fragmentPath + " \n\tFragment Shader compilation failed.");
            System.err.println(glGetShaderInfoLog(fragmentId, len));
            assert false : "";
        }

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexId);
        glAttachShader(shaderProgramID, fragmentId);
        glLinkProgram(shaderProgramID);
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("Error: liking of shaders " + vertexPath + " and " + fragmentPath + " failed.");
            System.err.println(glGetProgramInfoLog(fragmentId, len));
            assert false : "";
        }

        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
    }
    public interface SafeCloseable extends AutoCloseable{
        @Override
        public void close();
    }
    private class Handle implements SafeCloseable{
        private Shader shader;
        public Handle(Shader shader){
            this.shader = shader;
        }
        @Override
        public void close(){
            shader.detach();
        }
    }
    public Handle use(){
        assert ProgramInUse() != shaderProgramID : "No need to activate it twice.";
        glUseProgram(shaderProgramID);
        tex.bind();
        return new Handle(this);
    }
    public void detach(){
        tex.unbind();
        glUseProgram(0);
    }
    public void uploadMat4f(String varName, Matrix4f mat4){
        assert ProgramInUse() == shaderProgramID : "this is not the current shader!";

        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
    public void uploadMat3f(String varName, Matrix3f mat3){
        assert ProgramInUse() == shaderProgramID : "this is not the current shader!";

        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }
    public void uploadVec4f(String varName, Vector4f vec){
        assert ProgramInUse() == shaderProgramID : "this is not the current shader!";
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }
    public void uploadVec3f(String varName, Vector3f vec){
        assert ProgramInUse() == shaderProgramID : "this is not the current shader!";
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }
    public void uploadVec2f(String varName, Vector2f vec){
        assert ProgramInUse() == shaderProgramID : "this is not the current shader!";
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform2f(varLocation, vec.x, vec.y);
    }
    public void uploadFloat(String varName, float val){
        assert ProgramInUse() == shaderProgramID : "this is not the current shader!";
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1f(varLocation, val);
    }
    public void uploadInt(String varName, int val){
        assert ProgramInUse() == shaderProgramID : "this is not the current shader!";
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, val);
    }
    public void uploadTexture(String varName, Texture tex){
        uploadInt(varName, 0);
        this.tex = tex;
    }

    public int ProgramInUse() {
        IntBuffer intbuf = BufferUtils.createIntBuffer(1);
        glGetIntegerv(GL_CURRENT_PROGRAM, intbuf);
        return intbuf.get();
    }
}
