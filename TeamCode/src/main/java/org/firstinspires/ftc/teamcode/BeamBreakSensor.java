package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class BeamBreakSensor {

    private AnalogInput sensor1;
    private AnalogInput sensor2;
    private AnalogInput sensor3;
    private AnalogInput sensor4;

    private boolean lastState = true;
    private boolean lastFull=false;

    private int count=0;

    public BeamBreakSensor(HardwareMap hardwareMap) {
        sensor1 = hardwareMap.get(AnalogInput.class, "intakeds1");
        //sensor1.setMode(DigitalChannel.Mode.INPUT);
        sensor2 = hardwareMap.get(AnalogInput.class, "intakeds2");
        //sensor2.setMode(DigitalChannel.Mode.INPUT);
        sensor3 = hardwareMap.get(AnalogInput.class, "intakeds3");
        //sensor3.setMode(DigitalChannel.Mode.INPUT);
        //sensor4 = hardwareMap.get(AnalogInput.class, "intakeds4");
        //sensor4.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean getState(int sensorNum) { // true = threshold crossed = beam broken = object detected
        double distance;
        switch (sensorNum) {
            case 1:
                distance = (sensor1.getVoltage() / Constants.IntakeConstants.MAX_VOLTS) * Constants.IntakeConstants.MAX_DISTANCE_MM;
                break;
            case 2:
                distance = (sensor2.getVoltage() / Constants.IntakeConstants.MAX_VOLTS) * Constants.IntakeConstants.MAX_DISTANCE_MM;
                break;
            case 3:
                distance = (sensor3.getVoltage() / Constants.IntakeConstants.MAX_VOLTS) * Constants.IntakeConstants.MAX_DISTANCE_MM;
                break;
            case 4:
                //distance=sensor4.getVoltage() / Constants.IntakeConstants.MAX_VOLTS * Constants.IntakeConstants.MAX_DISTANCE_MM;
                return false;
            default:
                return false;
        }
        return distance <= Constants.IntakeConstants.SENSOR_THRESHOLD;
    }

    public boolean getIsFull(){
        return getState(1)&&getState(2)&&getState(3);// &&getState(4)
    }

    public boolean getIsFilled(){
        boolean isFilled;
        if (lastFull==false && getIsFull()==true){
            isFilled=true;
        }
        else{
            isFilled=false;
        }
        lastFull=getIsFull();
        return isFilled;
    }
}
