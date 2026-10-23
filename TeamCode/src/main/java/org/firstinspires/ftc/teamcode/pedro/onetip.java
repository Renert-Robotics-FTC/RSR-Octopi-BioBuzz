package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "LM1 1 tip", group = "Pedropathing")
public class onetip extends OpMode {

    private Follower follower;

    private DcMotorEx shooter, intake, transfer;
    private Servo blocker;

    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;
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

    /* Updated Poses */
    private final Pose startPose = new Pose(8, 80, Math.toRadians(90));
    private final Pose buddypushStartPose = new Pose(8, 80, Math.toRadians(180));
    private final Pose buddypushPose = new Pose(8, 90, Math.toRadians(180));
    private final Pose shootPose = new Pose(56, 10, Math.toRadians(270));
    private final Pose parkPose = new Pose(8, 102, Math.toRadians(180));

    /* PathChains */
    private PathChain startToBuddyPushStart, buddyPushChain, toShootChain, toParkChain;

    public void buildPaths() {
        startToBuddyPushStart = follower.pathBuilder()
                .addPath(new BezierLine(startPose, buddypushStartPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), buddypushStartPose.getHeading())
                .build();

        buddyPushChain = follower.pathBuilder()
                .addPath(new BezierLine(buddypushStartPose, buddypushPose))
                .setLinearHeadingInterpolation(buddypushStartPose.getHeading(), buddypushPose.getHeading())
                .build();

        toShootChain = follower.pathBuilder()
                .addPath(new BezierLine(buddypushPose, shootPose))
                .setLinearHeadingInterpolation(buddypushPose.getHeading(), shootPose.getHeading())
                .build();

        toParkChain = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, parkPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), parkPose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                shooterTune();
                if (!follower.isBusy()) {
                    follower.followPath(startToBuddyPushStart, 1, true);
                    setPathState(1);
                }
                break;

            case 1:
                shooterTune();
                if (!follower.isBusy()) {
                    follower.followPath(buddyPushChain, 1, true);
                    setPathState(2);
                }
                break;

            case 2:
                if (follower.getCurrentTValue() > leavePercent || !follower.isBusy()) {
                    follower.followPath(toShootChain, 1, true);
                    setPathState(3);
                }
                break;

            case 3:
                if (follower.getCurrentTValue() > leavePercent || !follower.isBusy()) {
                    waitms(500);
                    shoot(1300);
                    follower.followPath(toParkChain, 1, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    setPathState(5);
                }
                break;

            default:
                telemetry.addData("Status", "Autonomous finished");
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

        telemetry.addData("Path State", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);

        shooter = hardwareMap.get(DcMotorEx.class, Constants.Mechanism.SHOOTER);
        intake = hardwareMap.get(DcMotorEx.class, Constants.Mechanism.INTAKE);
        transfer = hardwareMap.get(DcMotorEx.class, Constants.Mechanism.TRANSFER);


        blocker = hardwareMap.get(Servo.class, "blocker");
        blocker.setPosition(1); // default open


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
    public void stop() {}
}
