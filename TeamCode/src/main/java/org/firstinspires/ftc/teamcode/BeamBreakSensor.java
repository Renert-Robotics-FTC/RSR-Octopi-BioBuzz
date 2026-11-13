package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class BeamBreakSensor {

    private DigitalChannel sensor1;
    private DigitalChannel sensor2;
    private DigitalChannel sensor3;
    private DigitalChannel sensor4;

    private boolean lastState = true;

    private int count=0;

    public BeamBreakSensor(HardwareMap hardwareMap) {
        sensor1 = hardwareMap.get(DigitalChannel.class, "intakeds1");
        sensor1.setMode(DigitalChannel.Mode.INPUT);
        sensor2 = hardwareMap.get(DigitalChannel.class, "intakeds2");
        sensor2.setMode(DigitalChannel.Mode.INPUT);
        sensor3 = hardwareMap.get(DigitalChannel.class, "intakeds3");
        sensor3.setMode(DigitalChannel.Mode.INPUT);
        //sensor4 = hardwareMap.get(DigitalChannel.class, "intakeds4");
        //sensor4.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean getState(int sensorNum){ // true = threshold crossed = beam broken = object detected
        switch (sensorNum) {
            case 1:
                return sensor1.getState();
            case 2:
                return sensor2.getState();
            case 3:
                return sensor2.getState();
            case 4:
                return false;
            default:
                return false;
        }
    }
}
