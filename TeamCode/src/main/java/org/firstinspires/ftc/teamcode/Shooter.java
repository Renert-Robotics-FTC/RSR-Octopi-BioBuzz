package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {

    private static boolean shooterFlag=false;

    private DcMotor shooterMotor;

    public Shooter(HardwareMap hardwareMap){
        shooterMotor=hardwareMap.get(DcMotor.class, "intake");
    }

    public boolean getShooterFlag(){
        return shooterFlag;
    }

    public void setShooterFlag(boolean newShooterFlag){
        shooterFlag=newShooterFlag;
    }

    public void updatePower(){
        if(shooterFlag==true){
            shooterMotor.setPower(0.5);
        }
        else {
            shooterMotor.setPower(0);
        }
    }
}
