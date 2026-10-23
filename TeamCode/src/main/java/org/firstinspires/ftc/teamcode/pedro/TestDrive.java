
package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.pedropathing.geometry.BezierLine;

@Autonomous(name = "One tip auto", group = "Pedropathing")
public class TestDrive extends OpMode {
    // Hardware
    private DcMotorEx shooter;
    private DcMotorEx intake;
    private DcMotorEx transfer;
    private Servo blocker;

    // Shooter settings
    private static final double SHOOTER_POWER = 0.68;
    private static final double SHOOTING_TIME = 0.6;

    // Blocker servo positions — verify these on your robot
    private static final double BLOCKER_CLOSED = 0.0;
    private static final double BLOCKER_OPEN = 1.0;

    double shooterkp = 0.005;
    double shooterprop = 0;
    double shooterd = 0.005;
    double shooterPE = 0;
    double shooterkd = -0.002;
    double shooterES = 0;
    double shootertarget = 1420;
    double shootererror = 0;
    double leavePercent = 0.99;

    private void shoot(double ms) {
        shootertarget = 1420;
        blocker.setPosition(1); // open blocker while shooting

        double end = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < end) {
            transfer.setVelocity(200);
            intake.setVelocity(2000);
            shootererror = shootertarget - shooter.getVelocity();
            shooterprop = shootererror * shooterkp;
            shooterd = (shooterPE - shootererror) * shooterkd;
            shooterPE = shootererror;
            shooterES += shootererror;
            shooter.setPower(shooterprop + shooterd + (shooterES * 0.00003));
            telemetry.addData("shooterpower: ", shooter.getPower());
            telemetry.addData("shooterror: ", shootererror);
            telemetry.update();
            follower.update();
        }

        turnoff();
    }

    private void shooterTune() {
        shootererror = shootertarget - shooter.getVelocity();
        shooterprop = shootererror * shooterkp;
        shooterd = (shooterPE - shootererror) * shooterkd;
        shooterPE = shootererror;
        shooterES += shootererror;
        shooter.setPower(shooterprop + shooterd + (shooterES * 0.00003));
    }

    private void waitms(double ms) {
        transfer.setVelocity(0);
        intake.setVelocity(2000);
        double end = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < end) {
            follower.update();
        }
        turnoff();
    }

    private void turnoff() {
        transfer.setVelocity(0);
    }

    // PedroPathing
    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    // Route poses
    private final Pose startPose =
            new Pose(56, 8, Math.toRadians(90));

    private final Pose firstControl =
            new Pose(31, 10);

    private final Pose thirdPose =
            new Pose(7, 7, Math.toRadians(180));

    private final Pose path3Control =
            new Pose(28, 80);

    private final Pose firstShootingPose =
            new Pose(60, 134, Math.toRadians(270));

    private final Pose pointAfterShot1 =
            new Pose(47.5, 110);

    private final Pose point6 =
            new Pose(47.5, 130);

    private final Pose point7 =
            new Pose(47, 110);

    private final Pose pointAfterShot2 =
            new Pose(12, 60);

    private final Pose endPose =
            new Pose(12, 95);

    // Separate paths for the two shooting stops
    private PathChain pathToFirstShot;
    private PathChain pathBetweenShots;
    private PathChain pathAfterSecondShot;
    private void buildPaths() {

        // Path 1: Start -> first shooting location
        pathToFirstShot = follower.pathBuilder()
                .addPath(new BezierCurve(
                        startPose,
                        firstControl,
                        thirdPose
                ))
                .setLinearHeadingInterpolation(
                        Math.toRadians(90),
                        Math.toRadians(180)
                )

                .addPath(new BezierCurve(
                        thirdPose,
                        path3Control,
                        firstShootingPose
                ))
                .setLinearHeadingInterpolation(
                        Math.toRadians(180),
                        Math.toRadians(270)
                )
                .build();


        // Path 2: First shot -> intermediate points -> second shot
        pathBetweenShots = follower.pathBuilder()

                .addPath(new BezierLine(
                        firstShootingPose,
                        pointAfterShot1
                ))
                .setLinearHeadingInterpolation(
                        firstShootingPose.getHeading(),
                        pointAfterShot1.getHeading()
                )

                .addPath(new BezierLine(
                        pointAfterShot1,
                        point6
                ))
                .setLinearHeadingInterpolation(
                        pointAfterShot1.getHeading(),
                        point6.getHeading()
                )

                .addPath(new BezierLine(
                        point6,
                        point7
                ))
                .setLinearHeadingInterpolation(
                        point6.getHeading(),
                        point7.getHeading()
                )

                .addPath(new BezierLine(
                        point7,
                        firstShootingPose
                ))
                .setLinearHeadingInterpolation(
                        point7.getHeading(),
                        firstShootingPose.getHeading()
                )

                .build();

        // Path 3: Second shot -> finish
        pathAfterSecondShot = follower.pathBuilder()

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

    private void autonomousPathUpdate() {

        switch (pathState) {

            // Drive to the first shooting location.
            case 0:
                follower.followPath(pathToFirstShot, 0.5, true);
                setPathState(1);
                break;

            // Wait until the first path is finished.
            case 1:
                if (!follower.isBusy()) {
                    blocker.setPosition(BLOCKER_OPEN);
                    intake.setPower(1.0);
                    transfer.setPower(1.0);
                    setPathState(2);
                }
                break;

            // Shoot at the first location.
            case 2:
                if (pathTimer.getElapsedTimeSeconds() >= SHOOTING_TIME) {
                    intake.setPower(0);
                    transfer.setPower(0);

                    // Close the blocker immediately after shooting.
                    blocker.setPosition(BLOCKER_CLOSED);

                    follower.followPath(pathBetweenShots, 0.5, true);
                    setPathState(3);
                }
                break;

            // Drive through the intermediate points to the second shot.
            case 3:
                if (!follower.isBusy()) {
                    blocker.setPosition(BLOCKER_OPEN);
                    intake.setPower(1.0);
                    transfer.setPower(1.0);
                    setPathState(4);
                }
                break;

            // Shoot at the second location.
            case 4:
                if (pathTimer.getElapsedTimeSeconds() >= SHOOTING_TIME) {
                    intake.setPower(0);
                    transfer.setPower(0);

                    // Close the blocker immediately after shooting.
                    blocker.setPosition(BLOCKER_CLOSED);

                    follower.followPath(pathAfterSecondShot, 0.5, true);
                    setPathState(5);
                }
                break;

            // Finish the final path.
            case 5:
                if (!follower.isBusy()) {
                    shooter.setPower(0);
                    intake.setPower(0);
                    transfer.setPower(0);
                    blocker.setPosition(BLOCKER_CLOSED);
                    setPathState(6);
                }
                break;

            // Autonomous finished.
            case 6:
                shooter.setPower(0);
                intake.setPower(0);
                transfer.setPower(0);
                blocker.setPosition(BLOCKER_CLOSED);
                break;
        }
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
        blocker = hardwareMap.get(
                Servo.class, "blocker");

        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter.setDirection(Constants.Mechanism.SHOOTER_DIR);
        intake.setDirection(Constants.Mechanism.INTAKE_DIR);
        transfer.setDirection(Constants.Mechanism.TRANSFER_DIR);
        blocker.setPosition(BLOCKER_CLOSED);

        shooter.setPower(0);
        intake.setPower(0);
        transfer.setPower(0);

        pathTimer = new Timer();

        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void start() {
        shooter.setPower(SHOOTER_POWER);
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Shooter Power", shooter.getPower());
        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Transfer Power", transfer.getPower());
        telemetry.addData("Path State", pathState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData(
                "Heading (degrees)",
                Math.toDegrees(follower.getPose().getHeading())
        );
        telemetry.update();
    }

    @Override
    public void stop() {
        if (shooter != null) shooter.setPower(0);
        if (intake != null) intake.setPower(0);
        if (transfer != null) transfer.setPower(0);

        if (blocker != null) {
            blocker.setPosition(BLOCKER_CLOSED);
        }
    }
}