package jade;

import jade.util.Time;
import renderer.Camera;

public abstract class Scene implements AutoCloseable {
    protected Camera camera;
    public Scene(){

    }
    
    public abstract void update(Window window, Time time); 
    public void init(Window window){
        System.out.println(this.getClass().getSimpleName() + " is active!");
    };
    public void close(){

    };
}
