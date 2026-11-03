package org.firstinspires.ftc.teamcode;
//imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Octoray Teleop")
public class Teleop extends LinearOpMode{


    @Override
    public void runOpMode(){
        Odometry odometry = new Odometry();
        odometry.initializeOdometry(hardwareMap);

        DriveSubsystem driveSubsystem = new DriveSubsystem(hardwareMap, odometry);

        Shooter shooter=new Shooter(hardwareMap);
        Intake intake =new Intake(hardwareMap);
        Transfer transfer=new Transfer(hardwareMap);

        double time;
        double lastTime=0;

        double lastError=0;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            odometry.updateOdometry();
            double strafe = Math.pow(gamepad1.left_stick_y, 3);
            double forward = -Math.pow(gamepad1.left_stick_x, 3);
            double turn = Math.pow(gamepad1.right_stick_x, 3);
            double heading;

            if (gamepad1.b){
                heading= (odometry.getHeading()>90)? odometry.getHeading()-360 : odometry.getHeading();
                turn = (-90-heading)/180*Constants.DriveConstants.HDP;
            }else if (gamepad1.x){
                heading= (odometry.getHeading()<-90)? odometry.getHeading()+360 : odometry.getHeading();
                turn = (90-odometry.getHeading())/180*Constants.DriveConstants.HDP;
            }

            driveSubsystem.drive(forward, strafe, turn);

            /*if (gamepad1.right_bumper) {
                if (shooter.getShooterFlag()==flse) {
                    shooter.setShooterFlag(true);
                }else{shooter.setShooterFlag(false);}
            }*/

            /*if (gamepad1.b) {
                if (intake.getIntakeFlag()==true) {
                    intake.setIntakeFlag(false);
                }else {
                    intake.setIntakeFlag(true);
                }
            }*/

            shooter.setShooterFlag(gamepad1.right_trigger_pressed);
            intake.setIntakeFlag(gamepad1.left_trigger_pressed || shooter.getShooterFlag());
            intake.setReverse(gamepad1.y);
            intake.setUnjam(gamepad1.a);
            intake.setFlintakePos(gamepad1.right_bumper, gamepad1.left_bumper);


            shooter.updatePower();
            transfer.updatePower(shooter.getShooterFlag() || intake.getIntakeFlag(), gamepad1.y);
            intake.updatePower();

            time=timer.milliseconds();
            shooter.dTime= time-lastTime;
            lastTime=time;

            double error=1421-shooter.getShooterVelocity();
            telemetry.addData("xpos", odometry.getxPos());
            telemetry.addData("ypos", odometry.getyPos());
            telemetry.addData("heading", odometry.getHeading());
            telemetry.addData("power", shooter.getPower());
            telemetry.addData("velocity", shooter.getShooterVelocity());
            telemetry.addData("shooterflag", shooter.getShooterFlag());
            telemetry.addData("error", error);
            telemetry.addData("p",error*Constants.ShooterConstants.KP);
            telemetry.addData("lastError", lastError);
            telemetry.addData("dTime", shooter.dTime);
            telemetry.addData("d", (error-lastError)/shooter.dTime*Constants.ShooterConstants.KD);
            telemetry.update();

            lastError=error;

        }

    }


}