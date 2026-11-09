package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Timer {

    private static double time=0;
    private static ElapsedTime timer=new ElapsedTime();
    private static double lastTime=0;
    private static double dTime=0;
    public static double triggerTime=0;
    public static double delay=0;

    public static double getTime(){
        return time;
    }
    public double getLastTime(){
        return lastTime;
    }
    public static double getdTime(){
        return dTime;
    }
    public static double getTriggerTime(){
        return triggerTime;
    }
    public static double getDelay(){
        return delay;
    }

    public static void updateTimes() {
        time = timer.milliseconds();
        dTime = time - lastTime;
        lastTime = time;
        delay=time-triggerTime;
    }
    public static void updateTriggerTime(){
        triggerTime=timer.milliseconds();
    }
    public static void resetTimer(){
        timer.reset();
    }

}
