package org.firstinspires.ftc.teamcode;
//imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name = "Octoray Teleop")
public class Teleop extends LinearOpMode{


    @Override
    public void runOpMode(){
        Odometry odometry = new Odometry();
        odometry.initializeOdometry(hardwareMap);

        DriveSubsystem driveSubsystem = new DriveSubsystem(hardwareMap, odometry);

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            odometry.updateOdometry();
            double strafe = gamepad1.left_stick_y;
            double forward = -gamepad1.left_stick_x;
            double turn = -gamepad1.right_stick_x;

            driveSubsystem.drive(forward, strafe, turn);        }

    }


}
