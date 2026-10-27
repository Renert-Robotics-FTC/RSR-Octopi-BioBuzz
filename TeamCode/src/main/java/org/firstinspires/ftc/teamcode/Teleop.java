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

        int svmod=0;
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
            double strafe = gamepad1.left_stick_y;
            double forward = -gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            driveSubsystem.drive(forward, strafe, turn);

            /*if (gamepad1.right_bumper) {
                if (shooter.getShooterFlag()==false) {
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
            intake.setReverse(gamepad1.x);
            intake.setUnjam(gamepad1.b);
            if (gamepad1.a){
                svmod--;
            }
            if (gamepad1.y){
                svmod++;
            }
            intake.setFlintakePos(gamepad1.right_bumper, gamepad1.left_bumper);


            shooter.updatePower(1421+svmod);
            transfer.updatePower(shooter.getShooterFlag() || intake.getIntakeFlag(), gamepad1.x);
            intake.updatePower();

            time=timer.seconds();
            shooter.dTime= time-lastTime;
            lastTime=time;

            double error=1421-shooter.getShooterVelocity();
            telemetry.addData("velocity", shooter.getShooterVelocity());
            telemetry.addData("theoretical velocity", 1421+svmod);
            telemetry.addData("shooterflag", shooter.getShooterFlag());
            telemetry.addData("error", error);
            telemetry.addData("p",error*Constants.ShooterConstants.KP);
            telemetry.addData("d", (error-lastError)/shooter.dTime*Constants.ShooterConstants.KD);
            telemetry.update();

            lastError=error;

        }

    }


}
