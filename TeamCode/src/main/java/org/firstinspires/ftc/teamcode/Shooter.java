package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Const;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.StandardConstants;

public class Shooter {

    private static boolean shooterFlag=false;

    private DcMotorEx shooterMotor;
    private DcMotorEx shooterMotor2;
    private Servo servoBlocker;
    private Servo hood;
    private double lastError=0;
    private double lastError2=0;
    private double lastPosition=0;
    public static double i=0;
    public static double velocity=0;
    private Odometry odometry;
    private Turret turret;
    private double targetv=1470;
    private double d=0;


    public Shooter(HardwareMap hardwareMap, Odometry odometry){
        this.odometry=odometry;
        shooterMotor=hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor2=hardwareMap.get(DcMotorEx.class, "xtra^");
        shooterMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        servoBlocker=hardwareMap.get(Servo.class, "blocker");
        hood=hardwareMap.get(Servo.class, "door");
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
        targetv=calcShooterSpeed();
        double position=shooterMotor.getCurrentPosition();
        velocity=(position-lastPosition)/dTime*1000;
        double error=targetv-shooterMotor.getVelocity();
        double p=error*Constants.ShooterConstants.KP;
        double d=(error-lastError)/dTime*Constants.ShooterConstants.KD;
        lastError=error;
        //i+=dTime*error;
        return p+d+Constants.ShooterConstants.FF;
    }
    public double calcShooter2PID(double dTime){
        double position=shooterMotor2.getCurrentPosition();
        double error=-targetv-shooterMotor2.getVelocity();
        double p=error*Constants.ShooterConstants.KP;
        double d=(error-lastError2)/dTime*Constants.ShooterConstants.KD;
        lastError2=error;
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
        shooterMotor.setPower(/*calcShooterPID(dTime)*Constants.ShooterConstants.LEFT_MOTOR_RATIO*/0.2);
        //shooterMotor2.setPower(calcShooterPID(dTime)*Constants.ShooterConstants.RIGHT_MOTOR_RATIO);
    }

    public void updateShooterAngle(){
        d=Math.sqrt(Math.pow(odometry.getxPos(), 2)+Math.pow(odometry.getyPos(), 2));
        double shotAngle=Math.atan2(Constants.ShooterConstants.HIVE_HEIGHT, d);
        hood.setPosition(1-(90-shotAngle)/180);
    }

    public double calcShooterSpeed(){
        d=Math.sqrt(Math.pow(odometry.getxPos(), 2)+Math.pow(odometry.getyPos(), 2));
        double xspeed=d/Constants.ShooterConstants.HIVEX_TIME;
        return Math.sqrt(Math.pow(xspeed, 2)+Math.pow(Constants.ShooterConstants.HIVEY_SPEED, 2))* Constants.ShooterConstants.TpS2MpS;
    }

    public double getPower(){
        return shooterMotor.getPower();
    }

}
