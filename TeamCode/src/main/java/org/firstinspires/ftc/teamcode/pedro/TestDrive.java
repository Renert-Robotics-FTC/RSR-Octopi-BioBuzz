package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "move forward", group = "Pedropathing")
public class TestDrive extends OpMode {

    private DcMotorEx shooter;
    private DcMotorEx intake;
    private DcMotorEx transfer;

    private Follower follower;

    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;
    private double leavePercent = 0.98;

    /* Poses */
    private final Pose startPose = new Pose(56, 0, Math.toRadians(90));
    private final Pose secondPose = new Pose(56, 30, Math.toRadians(180));
    private final Pose thirdPose = new Pose(34, 7, Math.toRadians(180));
    private final Pose fourthPose = new Pose(7, 7, Math.toRadians(180));

    private PathChain firstPath;


    public void buildPaths() {
        firstPath = follower.pathBuilder()

                // Path 1: Start to second position
                .addPath(new BezierLine(startPose, secondPose))
                .setLinearHeadingInterpolation(
                        startPose.getHeading(),
                        secondPose.getHeading()
                )

                // Path 2: Second to third position
                .addPath(new BezierLine(secondPose, thirdPose))
                .setLinearHeadingInterpolation(
                        secondPose.getHeading(),
                        thirdPose.getHeading()
                )

                // Path 3: Third to fourth position
                .addPath(new BezierLine(thirdPose, fourthPose))
                .setLinearHeadingInterpolation(
                        thirdPose.getHeading(),
                        fourthPose.getHeading()
                )

                .build();
    }


    public void autonomousPathUpdate() {
        switch (pathState) {

            case 0:
                // State 0: Spin up the flywheel
                shooter.setPower(0.68);
                setPathState(1);
                break;

            case 1:
                // Wait for the shooter to spin up
                if (pathTimer.getElapsedTimeSeconds() >= 5) {
                    intake.setPower(1.0);
                    transfer.setPower(1.0);
                    setPathState(2);
                }
                break;

            case 2:
                // Feed game pieces for 3 seconds
                if (pathTimer.getElapsedTimeSeconds() >= 3) {
                    intake.setPower(0);
                    transfer.setPower(0);

                    // Begin PedroPathing only after feeding
                    follower.followPath(firstPath, 0.5, true);
                    setPathState(3);
                }
                break;

            case 3:
                // State 3: Wait for the path to finish
                if (!follower.isBusy()) {
                    shooter.setPower(0);
                    setPathState(4);
                }
                break;

            case 4:
                // State 4: Autonomous finished
                shooter.setPower(0);
                intake.setPower(0);
                transfer.setPower(0);
                break;
        }
    }


    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Shooter Power", shooter.getPower());
        telemetry.addData("Intake Power", intake.getPower());

        telemetry.addData("Path State", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("Transfer Power", transfer.getPower());

        telemetry.update();
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);

        shooter = hardwareMap.get(
                DcMotorEx.class, Constants.Mechanism.SHOOTER);

        intake = hardwareMap.get(
                DcMotorEx.class, Constants.Mechanism.INTAKE);

        transfer = hardwareMap.get(
                DcMotorEx.class, Constants.Mechanism.TRANSFER);

        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter.setPower(0);
        intake.setPower(0);
        transfer.setPower(0);

        shooter.setDirection(Constants.Mechanism.SHOOTER_DIR);
        intake.setDirection(Constants.Mechanism.INTAKE_DIR);
        transfer.setDirection(Constants.Mechanism.TRANSFER_DIR);

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();

        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
        if (shooter != null) {
            shooter.setPower(0);
        }

        if (intake != null) {
            intake.setPower(0);
        }

        if (transfer != null) {
            transfer.setPower(0);
        }
    }
}