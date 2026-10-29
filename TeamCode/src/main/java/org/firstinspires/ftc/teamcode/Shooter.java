package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Shooter {

    private static boolean shooterFlag=false;

    private DcMotorEx shooterMotor;
    private Servo servoBlocker;
    private double lastError=0;
    public static double dTime=0;
    public static double i=0;

    public Shooter(HardwareMap hardwareMap){
        shooterMotor=hardwareMap.get(DcMotorEx.class, "shooter");
        servoBlocker=hardwareMap.get(Servo.class, "blocker");
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public boolean getShooterFlag(){
        return shooterFlag;
    }

    public void setShooterFlag(boolean newShooterFlag){
        shooterFlag=newShooterFlag;
    }

    public double getShooterVelocity(){
        return shooterMotor.getVelocity();
    }

    public double calcShooterPID(){
        double error=1450-shooterMotor.getVelocity();
        double p=error*Constants.ShooterConstants.KP;
        double d=(error-lastError)/1.5*Constants.ShooterConstants.KD;
        lastError=error;
        //i+=dTime*error;
        return Constants.ShooterConstants.FF;
    }
    public void updatePower(/*double distance, */int velocity){
        /*if ((2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)*(distance*Math.sin(Constants.ShooterConstants.SHOOTER_ANGLE)-1.2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)))<=0){
            shooterMotor.setPower(0);
            return;
        }*/
        if(shooterFlag==true){
            //shooterMotor.setPower(Range.clip(Constants.ShooterConstants.MAX_EXIT_SPEED*distance*Math.sqrt(9.81/(2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)*(distance*Math.sin(Constants.ShooterConstants.SHOOTER_ANGLE)-1.2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)))), 0, 1));
            //servoBlocker.setDirection(Servo.Direction.REVERSE);
            servoBlocker.setPosition(0.35);
        }
        else {
            //servoBlocker.setDirection(Servo.Direction.FORWARD);
            servoBlocker.setPosition(0);
        }
        //shooterMotor.setPower(shooterMotor.getVelocity() < velocity ? 1:0);
        shooterMotor.setPower(calcShooterPID());
    }

    public double getPower(){
        return shooterMotor.getPower();
    }
}
