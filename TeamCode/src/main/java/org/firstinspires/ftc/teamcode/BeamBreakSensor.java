package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class BeamBreakSensor {

    private DigitalChannel sensor;
    private boolean lastState = true; // true = beam intact

    private int count=0;

    public BeamBreakSensor(HardwareMap hardwareMap) {
        sensor = hardwareMap.get(DigitalChannel.class, "intakeds");
        sensor.setMode(DigitalChannel.Mode.INPUT);
        lastState = sensor.getState();
    }

    public boolean getState(){
        return sensor.getState();
    }
}
