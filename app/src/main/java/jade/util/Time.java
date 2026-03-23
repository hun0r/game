package jade.util;

public class Time{
    private final long timeStarted;
    private long beginTime = getNanoTime();
    private long endTime = getNanoTime();
    private long dt = 0;
    
    public Time(){
        this.timeStarted = System.nanoTime();
        this.beginTime = getNanoTime();
        this.endTime = getNanoTime();
        this.dt = 0;
    } 

    public long getNanoTime(){
        return (System.nanoTime() - timeStarted);
    }
    public long getNanoTimeStarted(){
        return timeStarted;
    }
    public long getNanoBeginTime(){
        return beginTime;
    }
    public long getNanoEndTime(){
        return endTime;
    }
    public long getNanoDt(){
        return dt;
    }
    public float getTime(){
        return (float)(getNanoTime() * 1E-9);
    }
    public float getDt(){
        return (float)(dt * 1E-9);
    }
    public void endFrame(){
        endTime = getNanoTime();
        dt = endTime - beginTime;
        beginTime = endTime;
    }
}
