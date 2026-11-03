
package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.Shooter;
import org.firstinspires.ftc.teamcode.Transfer;

@Autonomous(name = "One tip auto", group = "Pedropathing")
public class OneTipAuto extends OpMode {

    private Shooter shooter;
    private Intake intake;
    private Transfer transfer;

    private Follower follower;
    private Timer pathTimer;
    private double lastTime = 0;
    private int pathState = 0;
    private ElapsedTime shooterTimer;

    private final Pose startPose = new Pose(56, 8, Math.toRadians(90));
    private final Pose point1 = new Pose(-31, 9, Math.toRadians(180));
    private final Pose point2 = new Pose(-7, 7, Math.toRadians(180));
    private final Pose point3 = new Pose(-28, 80, Math.toRadians(270));
    private final Pose point4 = new Pose(-60, 134, Math.toRadians(270));
    private final Pose point5 = new Pose(-47.5, 110, Math.toRadians(90));

    //changed up to here radians

    private final Pose point6 = new Pose(-47.5, 130, Math.toRadians(90));
    private final Pose point7 = new Pose(-47, 110, Math.toRadians(270));
    private final Pose point8 = new Pose(-60, 134, Math.toRadians(270));
    private final Pose point9 = new Pose(-12, 60, Math.toRadians(0));
    private final Pose point10 = new Pose(-12, 95, Math.toRadians(0));

    private PathChain path1;
    private PathChain path2;
    private PathChain path3;
    private PathChain path4;
    private PathChain path5;
    private PathChain path6;
    private PathChain path7;
    private PathChain path8;
    private PathChain path9;
    private PathChain path10;

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    private void buildPaths() {
        path1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, point1))
                .setLinearHeadingInterpolation(
                        startPose.getHeading(), point1.getHeading())
                .build();

        path2 = follower.pathBuilder()
                .addPath(new BezierLine(point1, point2))
                .setConstantHeadingInterpolation(point2.getHeading())
                .build();

        path3 = follower.pathBuilder()
                .addPath(new BezierLine(point2, point3))
                .setLinearHeadingInterpolation(
                        point2.getHeading(), point3.getHeading())
                .build();

        path4 = follower.pathBuilder()
                .addPath(new BezierLine(point3, point4))
                .setConstantHeadingInterpolation(point4.getHeading())
                .build();

        path5 = follower.pathBuilder()
                .addPath(new BezierLine(point4, point5))
                .setLinearHeadingInterpolation(
                        point4.getHeading(), point5.getHeading())
                .build();

        path6 = follower.pathBuilder()
                .addPath(new BezierLine(point5, point6))
                .setConstantHeadingInterpolation(point6.getHeading())
                .build();

        path7 = follower.pathBuilder()
                .addPath(new BezierLine(point6, point7))
                .setLinearHeadingInterpolation(
                        point6.getHeading(), point7.getHeading())
                .build();

        path8 = follower.pathBuilder()
                .addPath(new BezierLine(point7, point8))
                .setConstantHeadingInterpolation(point8.getHeading())
                .build();

        path9 = follower.pathBuilder()
                .addPath(new BezierLine(point8, point9))
                .setLinearHeadingInterpolation(
                        point8.getHeading(), point9.getHeading())
                .build();

        path10 = follower.pathBuilder()
                .addPath(new BezierLine(point9, point10))
                .setConstantHeadingInterpolation(point10.getHeading())
                .build();
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);

        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);

// Start with all mechanisms off.
        shooter.setShooterFlag(false);
        intake.setIntakeFlag(false);

        intake.setFlintakePos(false, false);

        pathTimer = new Timer();
        shooterTimer = new ElapsedTime();

        follower.setStartingPose(startPose);
        buildPaths();

        telemetry.update();
    }

    @Override
    public void start() {
        shooterTimer.reset();
        lastTime = 0;

        shooter.setShooterFlag(false);
        intake.setIntakeFlag(false);

        setPathState(0);
    }

    @Override
    public void loop() {

        // Calculate time between shooter PID updates.
        double currentTime = shooterTimer.milliseconds();
        Shooter.dTime = currentTime - lastTime;
        lastTime = currentTime;

        // Update all three mechanisms.
        shooter.updatePower();

        intake.updatePower();

        transfer.updatePower(
                shooter.getShooterFlag() || intake.getIntakeFlag(),
                false
        );

        // Update PedroPathing and autonomous state machine.
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("Shooter Velocity", shooter.getShooterVelocity());
        telemetry.addData("Shooter Target", 1421);
        telemetry.addData("Shooter Power", shooter.getPower());
        telemetry.addData("Shooter Flag", shooter.getShooterFlag());
        telemetry.addData("Intake Flag", intake.getIntakeFlag());
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
                if (pathTimer.getElapsedTimeSeconds() >= 3.0) {
                    shooter.setShooterFlag(true);
                    intake.setIntakeFlag(true);
                    setPathState(1);
                }
                break;

            case 1:
                if (pathTimer.getElapsedTimeSeconds()
                        >= Constants.SHOOTING_TIME) {

                    shooter.setShooterFlag(false);

                    follower.followPath(path1, true);
                    setPathState(2);
                }
                break;

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(path2, true);
                    setPathState(3);
                }
                break;

            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(path3, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(path4, true);
                    setPathState(5);
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    follower.followPath(path5, true);
                    setPathState(6);
                }
                break;

            case 6:
                if (!follower.isBusy()) {
                    intake.setIntakeFlag(true);
                    intake.setFlintakePos(true, false);

                    follower.followPath(path6, true);
                    setPathState(7);
                }
                break;

            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(path7, true);
                    setPathState(8);
                }
                break;

            case 8:
                if (!follower.isBusy()) {
                    intake.setIntakeFlag(false);
                    intake.setFlintakePos(false, false);

                    follower.followPath(path8, true);
                    setPathState(9);
                }
                break;

            case 9:
                if (!follower.isBusy()) {
                    shooter.setShooterFlag(true);
                    setPathState(10);
                }
                break;

            case 10:
                if (pathTimer.getElapsedTimeSeconds()
                        >= Constants.SHOOTING_TIME) {

                    shooter.setShooterFlag(false);

                    follower.followPath(path9, true);
                    setPathState(11);
                }
                break;

            case 11:
                if (!follower.isBusy()) {
                    follower.followPath(path10, true);
                    setPathState(12);
                }
                break;

            case 12:
                if (!follower.isBusy()) {
                    shooter.setShooterFlag(true);
                    setPathState(13);
                }
                break;

            case 13:
                if (pathTimer.getElapsedTimeSeconds()
                        >= Constants.SHOOTING_TIME) {

                    shooter.setShooterFlag(false);
                    intake.setIntakeFlag(false);
                    setPathState(14);
                }
                break;

            case 14:
                break;
        }
    }
}
