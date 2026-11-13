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

    public double calcTargetX(){
        if (odometry.getxPos()>Constants.TurretConstants.HIVELXPOS){
            return Constants.TurretConstants.HIVELX;
        }
        else if (odometry.getxPos()<Constants.TurretConstants.HIVERXPOS){
            return Constants.TurretConstants.HIVERX;
        }
        return (Constants.TurretConstants.HIVELX+Constants.TurretConstants.HIVERX)/2;
    }
    public double calcTargetY(){
        return Constants.TurretConstants.HIVEY;
    }
    public double calcTargetHeading(double hivex, double hivey){
        double dx=hivex-odometry.getxPos();
        double dy=hivey-odometry.getyPos();
        double tpos= -Math.toDegrees(Math.atan2(dy, dx)-odometry.getHeading())+Constants.TurretConstants.TARGHEADOFFSET;
        return tpos;
    }

    public double turDir2servPos(double turDir){
        return (turDir+Constants.TurretConstants.HALFROTLIM)/Constants.TurretConstants.FULLROTLIM % 1;
    }

    public void updateTurret(){
        turret1.setPosition(turDir2servPos(calcTargetHeading(calcTargetX(), calcTargetY())));
        turret2.setPosition(turDir2servPos(calcTargetHeading(calcTargetX(), calcTargetY())));
    }
}
