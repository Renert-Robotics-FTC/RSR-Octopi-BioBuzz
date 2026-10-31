package org.firstinspires.ftc.teamcode;
//imports
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "IDK how to do this")
public class Auto extends LinearOpMode{


    @Override
    public void runOpMode(){
        Odometry odometry = new Odometry();
        odometry.initializeOdometry(hardwareMap);

        DriveSubsystem driveSubsystem = new DriveSubsystem(hardwareMap, odometry);

        Shooter shooter=new Shooter(hardwareMap);
        Intake intake =new Intake(hardwareMap);
        Transfer transfer=new Transfer(hardwareMap);

        int svmod=0;
        double time = 0;
        double lastTime=0;

        double lastError=0;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            time=timer.milliseconds();
            shooter.dTime= time-lastTime;
            lastTime=time;

            double error=1421-shooter.getShooterVelocity();
            shooter.updatePower(1421+svmod);
            transfer.updatePower(shooter.getShooterFlag() || intake.getIntakeFlag(), gamepad1.x);
            intake.updatePower();
            if (time > 9000) {
                shooter.setShooterFlag(TRUE);
            }
            if (time > 11000) {
                driveSubsystem.drive(1, -0.7, 0);
            }
            if (time > 15000){
                driveSubsystem.stop();
                shooter.setShooterFlag(FALSE);
            }

            telemetry.addData("time", time);
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
