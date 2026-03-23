package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import org.lwjgl.opengl.GL;

import jade.util.Time;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;

    public static final Window INSTANCE = new Window();
    private long glfwWindowHandle;

    public long getWindowHandle(){
        return glfwWindowHandle;
    }

    public Scene currentScene;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Game";
    }

    public void run(Scene scene){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        changeScene(scene);
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindowHandle);
        glfwDestroyWindow(glfwWindowHandle);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        glfwWindowHandle = 0;
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindowHandle = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindowHandle == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Setup callbacks
        glfwSetCursorPosCallback(glfwWindowHandle, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindowHandle, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindowHandle, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindowHandle, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindowHandle);
        // Enable v-sync TODO: add framerate controll
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindowHandle);
        GL.createCapabilities();
    }

    public void loop() {
        Time time = new Time();
        while (!glfwWindowShouldClose(glfwWindowHandle)) {
            // Poll events
            glfwPollEvents();

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            currentScene.update(INSTANCE, time);

            glfwSwapBuffers(glfwWindowHandle);

            time.endFrame();
        }
    }

    public void changeScene(Scene newScene){
        if (currentScene != null){
            currentScene.close();
        }
        currentScene = newScene;
        newScene.init(this);
    }
}
