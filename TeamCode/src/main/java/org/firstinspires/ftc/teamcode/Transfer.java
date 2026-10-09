package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Transfer {
    private DcMotor transferMotor;
    private boolean intakeFlag=false;
    private boolean shooterFlag=false;

    public Transfer(HardwareMap hardwareMap){
        transferMotor=hardwareMap.get(DcMotor.class, "intake");
    }

    public void setIntakeFlag(boolean newIntakeFlag){
        intakeFlag=newIntakeFlag;
    }

    public void setShooterFlag(boolean newShooterFlag){
        shooterFlag=newShooterFlag;
    }

    public void updatePower(){
        if (intakeFlag==true || shooterFlag==true) {
            transferMotor.setPower(0.5);
        }else {
            transferMotor.setPower(0);
        }
    }
}
