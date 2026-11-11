package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Turret {

    private Servo turret1;
    private Servo turret2;
    private Odometry odometry;
    private static double hivex= Constants.TurretConstants.HIVELX;

    public Turret(HardwareMap hardwareMap, Odometry odometry) {
        this.odometry=odometry;
        turret1 = hardwareMap.get(Servo.class, "turret1");
        turret2 = hardwareMap.get(Servo.class, "turret2");
    }

    public double getTarget(double targetx, double targety){
        if (odometry.getxPos()>Constants.TurretConstants.HIVELXPOS){
            hivex=Constants.TurretConstants.HIVELX;
        }
        else if (odometry.getxPos()<Constants.TurretConstants.HIVERXPOS){
            hivex= Constants.TurretConstants.HIVERX;
        }
        double dx=hivex-odometry.getxPos();
        double dy=Constants.TurretConstants.HIVEY-odometry.getyPos();
        double tpos= Math.toDegrees(Math.atan2(dy, dx)-odometry.getHeading());
        return tpos;
    }

    public double turDir2servPos(double turDir){
        return (Range.clip(turDir, -Constants.TurretConstants.HALFROTLIM, Constants.TurretConstants.HALFROTLIM)+Constants.TurretConstants.HALFROTLIM)/Constants.TurretConstants.FULLROTLIM % Constants.TurretConstants.MODULARARITHEMETIC;
    }

    public void updateTurret(){
        turret1.setPosition(turDir2servPos(getTarget(1, 1)));
        turret2.setPosition(turDir2servPos(getTarget(1, 1)));
    }
}
