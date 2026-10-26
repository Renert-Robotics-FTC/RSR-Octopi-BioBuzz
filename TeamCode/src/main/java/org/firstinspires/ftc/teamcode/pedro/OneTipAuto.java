
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
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "One tip auto", group = "Pedropathing")
public class OneTipAuto extends OpMode {

    // Hardware
    private DcMotorEx shooter;
    private DcMotorEx intake;
    private DcMotorEx transfer;
    private Servo blocker;

    // PedroPathing
    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;

    // All poses face right: 0 radians
    private final Pose startPose =
            new Pose(56, 8, Math.toRadians(0));

    private final Pose firstControl =
            new Pose(31, 10, Math.toRadians(0));

    private final Pose thirdPose =
            new Pose(7, 7, Math.toRadians(0));

    private final Pose path3Control =
            new Pose(28, 80, Math.toRadians(0));

    private final Pose firstShootingPose =
            new Pose(60, 134, Math.toRadians(0));

    private final Pose pointAfterShot1 =
            new Pose(47.5, 110, Math.toRadians(0));

    private final Pose point6 =
            new Pose(47.5, 130, Math.toRadians(0));

    private final Pose point7 =
            new Pose(47, 110, Math.toRadians(0));

    private final Pose pointAfterShot2 =
            new Pose(12, 60, Math.toRadians(0));

    private final Pose endPose =
            new Pose(12, 95, Math.toRadians(0));

    // Paths
    private PathChain shotPath1;
    private PathChain shotPath2;
    private PathChain shotPath3;

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    private void buildPaths() {

        // Path 1: Start -> first shooting location
        shotPath1 = follower.pathBuilder()

                .addPath(new BezierLine(
                        startPose,
                        firstControl
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .addPath(new BezierLine(
                        firstControl,
                        thirdPose
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .addPath(new BezierLine(
                        thirdPose,
                        path3Control
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .addPath(new BezierLine(
                        path3Control,
                        firstShootingPose
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .build();


        // Path 2: First shot -> intermediate points -> second shots
        shotPath2 = follower.pathBuilder()

                .addPath(new BezierLine(
                        firstShootingPose,
                        pointAfterShot1
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .addPath(new BezierLine(
                        pointAfterShot1,
                        point6
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .addPath(new BezierLine(
                        point6,
                        point7
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .addPath(new BezierLine(
                        point7,
                        firstShootingPose
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .build();


        // Path 3: Second shot -> finish
        shotPath3 = follower.pathBuilder()

                .addPath(new BezierLine(
                        firstShootingPose,
                        pointAfterShot2
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .addPath(new BezierLine(
                        pointAfterShot2,
                        endPose
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))

                .build();
    }

    @Override
    public void init() {

        follower = Constants.createFollower(hardwareMap);

        shooter = hardwareMap.get(
                DcMotorEx.class,
                Constants.Mechanism.SHOOTER
        );

        intake = hardwareMap.get(
                DcMotorEx.class,
                Constants.Mechanism.INTAKE
        );

        transfer = hardwareMap.get(
                DcMotorEx.class,
                Constants.Mechanism.TRANSFER
        );

        blocker = hardwareMap.get(
                Servo.class,
                "blocker"
        );

        shooter.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.BRAKE
        );

        intake.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.BRAKE
        );

        transfer.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.BRAKE
        );

        shooter.setDirection(Constants.Mechanism.SHOOTER_DIR);
        intake.setDirection(Constants.Mechanism.INTAKE_DIR);
        transfer.setDirection(Constants.Mechanism.TRANSFER_DIR);

        shooter.setVelocity(0);
        intake.setPower(0);
        transfer.setPower(0);

        blocker.setPosition(Constants.BLOCKER_CLOSED);

        pathTimer = new Timer();

        follower.setStartingPose(startPose);

        buildPaths();

        telemetry.addLine("OneTipAuto initialized");
        telemetry.update();
    }

    @Override
    public void start() {

        // Start spinning the shooter while driving.
        shooter.setVelocity(
                Constants.SHOOTER_POWER
        );

        setPathState(0);
    }

    @Override
    public void loop() {

        // Keep PedroPathing updating every loop.
        follower.update();

        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("Shooter Velocity", shooter.getVelocity());
        telemetry.addData(
                "Target Velocity",
                Constants.SHOOTER_POWER
        );
        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Transfer Power", transfer.getPower());
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData(
                "Heading (degrees)",
                Math.toDegrees(follower.getPose().getHeading())
        );

        telemetry.update();
    }

    private void autonomousPathUpdate() {

        switch (pathState) {

            case 0:
                if (pathTimer.getElapsedTimeSeconds() >= 5.0) {
                    blocker.setPosition(Constants.BLOCKER_OPEN);
                    intake.setPower(1.0);
                    transfer.setPower(1.0);

                    setPathState(1);
                }
                break;

            case 1:
                if (pathTimer.getElapsedTimeSeconds()
                        >= Constants.SHOOTING_TIME) {

                    blocker.setPosition(Constants.BLOCKER_CLOSED);
                    intake.setPower(1.0);
                    transfer.setPower(1.0);

                    follower.followPath(shotPath1, 1.0, true);
                    setPathState(2);
                }
                break;

            case 2:
                if (!follower.isBusy() &&
                        pathTimer.getElapsedTimeSeconds() >= 5.0) {

                    blocker.setPosition(Constants.BLOCKER_OPEN);
                    intake.setPower(1.0);
                    transfer.setPower(1.0);

                    follower.followPath(
                            shotPath2,
                            0.5,
                            true
                    );

                    setPathState(3);
                }
                break;

            case 3:
                if (!follower.isBusy()) {
                    blocker.setPosition(Constants.BLOCKER_CLOSED);
                    intake.setPower(1.0);
                    transfer.setPower(1.0);

                    setPathState(4);
                }
                break;

            case 4:
                if (pathTimer.getElapsedTimeSeconds()
                        >= Constants.SHOOTING_TIME) {

                    blocker.setPosition(Constants.BLOCKER_OPEN);
                    intake.setPower(0);
                    transfer.setPower(0);

                    follower.followPath(
                            shotPath3,
                            0.5,
                            true
                    );

                    setPathState(5);
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    shooter.setVelocity(0);
                    intake.setPower(0);
                    transfer.setPower(0);

                    setPathState(6);
                }
                break;

            case 6:
                shooter.setVelocity(0);
                intake.setPower(0);
                transfer.setPower(0);
                blocker.setPosition(Constants.BLOCKER_CLOSED);
                break;
        }
    }
}