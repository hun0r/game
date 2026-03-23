package jade;

import static jade.Utils.any;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    public static final MouseListener INSTANCE = new MouseListener();
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[5];
    private boolean isDragging; 

    private MouseListener(){
        this.scrollX = 0.0d;
        this.scrollY = 0.0d;
        this.xPos = 0.0d;
        this.yPos = 0.0d;
        this.lastX = 0.0d;
        this.lastY = 0.0d;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        INSTANCE.lastX = INSTANCE.xPos;
        INSTANCE.lastY = INSTANCE.yPos;
        INSTANCE.xPos = xpos;
        INSTANCE.yPos = ypos;
        INSTANCE.isDragging = any(INSTANCE.mouseButtonPressed);
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if (button > INSTANCE.mouseButtonPressed.length) {
            System.err.println("Nice mouse! But sorry button no. " + button + " is not supported.");
            return;
        }
        if (action == GLFW_PRESS){
            INSTANCE.mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            INSTANCE.mouseButtonPressed[button] = false;
            INSTANCE.isDragging = false;
        } else {
            System.err.println("Didn't know you could do " + action + "to a button on a mouse.");
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        INSTANCE.scrollX = xOffset;
        INSTANCE.scrollY = yOffset;
    }

    public static void endFrame(){
        INSTANCE.scrollX = 0;
        INSTANCE.scrollY = 0;
        INSTANCE.lastX = INSTANCE.xPos;
        INSTANCE.lastY = INSTANCE.yPos;
    }

    public static float getX(){
        return (float)INSTANCE.xPos;
    }

    public static float getY(){
        return (float)INSTANCE.yPos;
    }

    public static float getDx(){
        return (float)(INSTANCE.lastX - INSTANCE.xPos);
    }

    public static float getDy(){
        return (float)(INSTANCE.lastY - INSTANCE.yPos);
    }

    public static float getScrollX(){
        return (float)INSTANCE.scrollX;
    }

    public static float getScrollY(){
        return (float)INSTANCE.scrollY;
    }

    public static boolean isDragging(){
        return INSTANCE.isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button < INSTANCE.mouseButtonPressed.length){
            System.err.println("Unsupported mouse button no. " + button + ".");
        }
        return INSTANCE.mouseButtonPressed[button];
    }
}
