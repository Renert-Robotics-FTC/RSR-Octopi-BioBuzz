package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.sql.Time;

public class Transfer {
    private DcMotor transferMotor;
    private DcMotor tMot;
    private boolean intakeFlag=false;
    private boolean shooterFlag=false;

    public Transfer(HardwareMap hardwareMap){
        transferMotor=hardwareMap.get(DcMotor.class, "transfer");
        transferMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        tMot=hardwareMap.get(DcMotor.class, "intake");
    }

    public void setIntakeFlag(boolean newIntakeFlag){
        intakeFlag=newIntakeFlag;
    }

    public void setShooterFlag(boolean newShooterFlag){
        shooterFlag=newShooterFlag;
    }

    public void updatePower(boolean transferFlag, boolean isReversed){
        if(isReversed==true){
            transferMotor.setPower(-1);
            tMot.setPower(-1);
        }
        else if (transferFlag==true) {
            transferMotor.setPower(1);
            tMot.setPower(1);
        }else {
            transferMotor.setPower(0);
            tMot.setPower(0);
        }
    }
}
