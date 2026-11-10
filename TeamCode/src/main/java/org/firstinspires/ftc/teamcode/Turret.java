package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Turret {

    private Servo turret1;
    private Servo turret2;
    private Odometry odometry;
    private static double hivex= Constants.TurretConstants.hiveLx;

    public Turret(HardwareMap hardwareMap, Odometry odometry) {
        this.odometry=odometry;
        turret1 = hardwareMap.get(Servo.class, "turret1");
        turret2 = hardwareMap.get(Servo.class, "turret2");
    }

    public void flipHivex(boolean flip) {
        if (flip==true) {
            if (Timer.getDelay() > 0.5) {
                if (hivex == Constants.TurretConstants.hiveLx) {
                    hivex = Constants.TurretConstants.hiveRx;
                    Timer.updateTriggerTime();
                } else if (hivex == Constants.TurretConstants.hiveRx) {
                    hivex = Constants.TurretConstants.hiveLx;
                    Timer.updateTriggerTime();
                }
            }
        }
    }

    public void updateTurret(){
        double dx=hivex-odometry.getxPos();
        double dy=Constants.TurretConstants.hivey-odometry.getyPos();
        double tpos=Math.toDegrees(Math.atan2(dy, dx)-odometry.getHeading());
        turret1.setPosition(tpos+180/360 % 1);
        turret2.setPosition(tpos+180/360 % 1);
    }
}
