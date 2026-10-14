package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Shooter {

    private static boolean shooterFlag=false;

    private DcMotor shooterMotor;

    public Shooter(HardwareMap hardwareMap){
        shooterMotor=hardwareMap.get(DcMotor.class, "shooter");
    }

    public boolean getShooterFlag(){
        return shooterFlag;
    }

    public void setShooterFlag(boolean newShooterFlag){
        shooterFlag=newShooterFlag;
    }

    public void updatePower(/*double distance*/){
        /*if ((2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)*(distance*Math.sin(Constants.ShooterConstants.SHOOTER_ANGLE)-1.2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)))<=0){
            shooterMotor.setPower(0);
            return;
        }*/
        if(shooterFlag==true){
            //shooterMotor.setPower(Range.clip(Constants.ShooterConstants.MAX_EXIT_SPEED*distance*Math.sqrt(9.81/(2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)*(distance*Math.sin(Constants.ShooterConstants.SHOOTER_ANGLE)-1.2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)))), 0, 1));
            shooterMotor.setPower(0.5);
        }
        else {
            shooterMotor.setPower(0);
        }
    }
}
