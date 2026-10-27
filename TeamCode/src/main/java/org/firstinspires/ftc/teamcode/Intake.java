package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private static boolean intakeFlag=false;
    private DcMotor intakeMotor;
    private static boolean isReversed=false;
    private Servo flintake;
    private static boolean unjam=false;

    public Intake(HardwareMap hardwareMap){
        intakeMotor=hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        flintake=hardwareMap.get(Servo.class, "flintake");
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
    public void setFlintakePos(boolean flintakeFlag,boolean stoppaoppaFlag){
        if (flintakeFlag==true) {
            flintake.setPosition(1);
            intakeMotor.setPower(1);
            return;
        }
        if(stoppaoppaFlag==true){
            flintake.setPosition(0.85);
            return;
        }
        flintake.setPosition(0.42);
    }
}
