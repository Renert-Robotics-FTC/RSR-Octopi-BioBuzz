package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Timer {

    private static double time=0;
    private ElapsedTime timer;
    private static double lastTime=0;
    private static double dTime=0;
    public static double triggerTime=0;

    public Timer (ElapsedTime timer){
        this.timer=timer;
    }
    public double getTime(){
        return time;
    }
    public double getLastTime(){
        return lastTime;
    }
    public double getdTime(){
        return dTime;
    }
    public double getTriggerTime(){
        return triggerTime;
    }

    public void updateTimes() {
        time = timer.milliseconds();
        dTime = time - lastTime;
        lastTime = time;
    }
    public void updateTriggerTime(){
        triggerTime=timer.milliseconds();
    }


}
