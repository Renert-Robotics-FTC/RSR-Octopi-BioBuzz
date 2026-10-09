package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private static boolean intakeFlag=false;
    private DcMotor intakeMotor;
    private static boolean reverse=false;

    public Intake(HardwareMap hardwareMap){
        intakeMotor=hardwareMap.get(DcMotor.class, "intake");
    }
    public boolean getIntakeFlag(){
        return intakeFlag;
    }

    public void setIntakeFlag(boolean newIntakeFlag){
        intakeFlag=newIntakeFlag;
    }

    public void setReverse(boolean newReverse){
        reverse=newReverse;
    }

    public void updatePower(){
        if (reverse==true){
            intakeMotor.setPower(-1);
        }else if(intakeFlag==true){
            intakeMotor.setPower(1);
        }
        else {
            intakeMotor.setPower(0);
        }
    }
}
