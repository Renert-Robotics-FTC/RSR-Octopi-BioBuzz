package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private static boolean intakeFlag=false;
    private DcMotor intakeMotor;
    private static boolean isReversed=false;
    private static boolean unjam=false;

    public Intake(HardwareMap hardwareMap){
        intakeMotor=hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public boolean getIntakeFlag(){
        return intakeFlag;
    }

    public void setIntakeFlag(boolean newIntakeFlag){
        intakeFlag=newIntakeFlag;
    }

    public void setReverse(boolean newIsReversed){
        isReversed=newIsReversed;
    }

    public void setUnjam(boolean newUnjam){
        unjam=newUnjam;
    }

    public void updatePower(){
        if (isReversed==true){
            intakeMotor.setPower(-1);
        }else if (unjam==true){
            intakeMotor.setPower(-0.6);
        }
        else if(intakeFlag==true){
            intakeMotor.setPower(1);
        }
        else {
            intakeMotor.setPower(0);
        }
    }
}
