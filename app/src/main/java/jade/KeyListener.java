package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import java.lang.IllegalArgumentException;

public class KeyListener {
    private static final KeyListener INSTANCE = new KeyListener();
    private boolean KeyPressed[] = new boolean[350];

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key > INSTANCE.KeyPressed.length) {
            System.err.println("Didn't know key no. " + key + " existed sorry we ignored it");
            return;
        }
        if (action == GLFW_PRESS){
            INSTANCE.KeyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            INSTANCE.KeyPressed[key] = false;
        } else {
            System.err.println("Didn't know you could do " + action + "to a button on a keyboard.");
        }
    }

    public static boolean isKeyPressed(int keyCode){
        if (keyCode > INSTANCE.KeyPressed.length){
            throw new IllegalArgumentException("There's no key no. " + keyCode + "handled!");
        }
        return INSTANCE.KeyPressed[keyCode];
    }
}
