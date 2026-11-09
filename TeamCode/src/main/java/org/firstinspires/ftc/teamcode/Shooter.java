package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Shooter {

    private static boolean shooterFlag=false;

    private DcMotorEx shooterMotor;
    private DcMotorEx shooterMotor2;
    private Servo servoBlocker;
    private Servo turret1;
    private Servo turret2;
    private double lastError=0;
    private double lastPosition=0;
    public static double i=0;
    public static double velocity=0;
    private Odometry odometry;
    private static boolean tlr=true;


    public Shooter(HardwareMap hardwareMap, Odometry odometry){
        this.odometry=odometry;
        shooterMotor=hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor2=hardwareMap.get(DcMotorEx.class, "xtra^");
        shooterMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        servoBlocker=hardwareMap.get(Servo.class, "blocker");
        turret1=hardwareMap.get(Servo.class, "turret1");
        turret2=hardwareMap.get(Servo.class, "turret2");
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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

    public double calcShooterPID(double dTime){
        double position=shooterMotor.getCurrentPosition();
        velocity=(position-lastPosition)/dTime*1000;
        double error=1470-shooterMotor.getVelocity();
        double p=error*Constants.ShooterConstants.KP;
        double d=(error-lastError)/dTime*Constants.ShooterConstants.KD;
        lastError=error;
        //i+=dTime*error;
        return p+d+Constants.ShooterConstants.FF;


    }
    public void updatePower(/*double distance, int velocity*/ double dTime){
        /*if ((2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)*(distance*Math.sin(Constants.ShooterConstants.SHOOTER_ANGLE)-1.2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)))<=0){
            shooterMotor.setPower(0);
            return;
        }*/
        if(shooterFlag==true){
            //shooterMotor.setPower(Range.clip(Constants.ShooterConstants.MAX_EXIT_SPEED*distance*Math.sqrt(9.81/(2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)*(distance*Math.sin(Constants.ShooterConstants.SHOOTER_ANGLE)-1.2*Math.cos(Constants.ShooterConstants.SHOOTER_ANGLE)))), 0, 1));
            //servoBlocker.setDirection(Servo.Direction.REVERSE);
            servoBlocker.setPosition(0.3);
        }
        else {
            //servoBlocker.setDirection(Servo.Direction.FORWARD);
            servoBlocker.setPosition(0);
        }
        //shooterMotor.setPower(shooterMotor.getVelocity() < velocity ? 1:0);
        shooterMotor.setPower(calcShooterPID(dTime));
    }

    public double getPower(){
        return shooterMotor.getPower();
    }

    public void flipTLR() {
        if (tlr == true){
            tlr = false;
        }
        if (tlr==false){
            tlr=true;
        }
    }

    public void updateTurret(){
        double dx=odometry.getxPos();
    }
}
