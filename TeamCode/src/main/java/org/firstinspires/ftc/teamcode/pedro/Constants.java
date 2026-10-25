package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {

    // Shooter settings
    public static final double SHOOTER_POWER = 1500;
    public static final double SHOOTING_TIME = 0.7;

    // Blocker servo positions — verify these on your robot
    public static final double BLOCKER_CLOSED = 0.0;
    public static final double BLOCKER_OPEN = 1.0;

    /* ------------------ PEDRO FOLLOWER CONSTANTS ------------------ */

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(7)
            .forwardZeroPowerAcceleration(-150.46889)
            .lateralZeroPowerAcceleration(-84.125)

            // ----------- TUNED TRANSLATIONAL PIDF -----------
            .translationalPIDFCoefficients(new PIDFCoefficients(
                    0.05,   // P
                    0,
                    0.00,   // D
                    0.02    // F
            ))
            .translationalPIDFSwitch(3.5)
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(
                    0.01,
                    0,
                    0.00,
                    0.002
            ))

            // ----------- TUNED HEADING PIDF -----------
            .headingPIDFCoefficients(new PIDFCoefficients(
                    0.3,
                    0,
                    0.00,
                    0.05
            ))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(
                    1.0,
                    0,
                    0.00,
                    0.002
            ))

            // ----------- TUNED DRIVE PIDF -----------
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.05,   // P
                    0,
                    0,      // D
                    0.6,    // F
                    0.01
            ))
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.003,
                    0,
                    0.00,
                    0.6,
                    0.02
            ))
            .drivePIDFSwitch(20)
            .centripetalScaling(0.0005);

    /* ------------------ DRIVE CONSTANTS ------------------ */

    public static MecanumConstants driveConstants = new MecanumConstants()
            .leftFrontMotorName("FL")
            .leftRearMotorName("BL")
            .rightFrontMotorName("FR")
            .rightRearMotorName("BR")
            // This is Nicholas's configuration. its horrible.
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(52.71)
            .yVelocity(78.38);

    /* ------------------ LOCALIZER CONSTANTS ------------------ */

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-1.0)
            .strafePodX(-1.0)
            // Inverted forward encoder direction so forward motion increases Y positively
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    /* ------------------ PATH CONSTRAINTS ------------------ */

    public static PathConstraints pathConstraints = new PathConstraints(
            4,
            10,
            0.1,
            0.009,
            0,
            1,
            10,
            1
    );

    /* ------------------ MECHANISM CONSTANTS ------------------ */

    public static class Mechanism {
        public static final String SHOOTER = "shooter";
        public static final String INTAKE = "intake";
        public static final String TRANSFER = "transfer";
        public static final String INDEX = "index";
        public static final String HOOD = "hood";

        public static final DcMotorSimple.Direction SHOOTER_DIR = DcMotorSimple.Direction.FORWARD;
        public static final DcMotorSimple.Direction INTAKE_DIR = DcMotorSimple.Direction.REVERSE;
        public static final DcMotorSimple.Direction TRANSFER_DIR = DcMotorSimple.Direction.REVERSE;
        public static final DcMotorSimple.Direction INDEX_DIR = DcMotorSimple.Direction.REVERSE;

        public static final double INDEX_FEED_POWER = 0.7;
        public static final double INDEX_RETRACT_POWER = -0.7;
    }

    /* ------------------ BUILD FOLLOWER ------------------ */

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}