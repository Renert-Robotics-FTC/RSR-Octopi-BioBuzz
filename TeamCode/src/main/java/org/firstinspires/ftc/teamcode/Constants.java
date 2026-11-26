package org.firstinspires.ftc.teamcode;

public class Constants {

    public static final class DriveConstants{
        public static final double HDP=0.7;
        public static final double HDD=5;
    }

    public static final class ShooterConstants{
        public static final double MAX_EXIT_SPEED=3;
        public static final double SHOOTER_ANGLE=Math.toRadians(70);
        public static final double KI=1;
        public static final double KP=0.046;
        public static final double KD=0.9;
        public static final double FF=0.7;
        public static final double HIVE_HEIGHT=230;
        public static final double HIVEY_SPEED=20;
        public static final double RIGHT_MOTOR_RATIO=26.0/28.0;
        public static final double LEFT_MOTOR_RATIO=18.0/28.0;
        public static final double HIVEX_TIME=.25;
        public static final double FLIGHT_TIME=Math.sqrt(Math.pow(HIVE_HEIGHT/HIVEY_SPEED, 2)+Math.pow(HIVEX_TIME, 2));
        public static final double TpS2MpS=123.78;
    }

    public static final class TurretConstants{
        public static final double HIVERX =1967.65;
        public static final double HIVELX=1319.95;
        public static final double HIVEY=1696;
        public static final double SPEEDMULT=0.5;
        public static final int HALFROTLIM=180;
        public static final int FULLROTLIM=360;
        public static final double MODULAR_ARITHEMETIC=1.000000000000000000000000000001;
        public static final double HIVELXPOS=1334.14;
        public static final double HIVERXPOS=2323.46;
        public static final int TARGHEADOFFSET=90;
    }

    public static final class IntakeConstants{
        public static final double SENSOR_THRESHOLD = 134;
        public static final double MAX_VOLTS = 3.3;
        public static final double MAX_DISTANCE_MM = 1000.0;;
    }
}
