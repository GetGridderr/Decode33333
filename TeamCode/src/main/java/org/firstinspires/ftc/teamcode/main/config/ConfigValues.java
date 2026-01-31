package org.firstinspires.ftc.teamcode.main.config;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDCoefficients;
import org.firstinspires.ftc.teamcode.core.util.position.Pose;

@Config
public class ConfigValues {
    public static class GunConfig {
        public double velocity = -980;
        public double degreeTower = 0.35;
        public double degreeAngleGun = 0.1;
        public double kP = 0.0055;
        public double kI = 0.0;
        public double kD = 0.00;
        public double alpha = 1;
        public double spdMul = 0.00004;
        public double offsetTurn = 0.5;
        public double offsetGun = 25;
        public double distanceToTarget = 55;
        public double kTower = 1.5;
        public double bananPosition = 0.17;
        public double posGoalX = 0;
        public double posGoalY = 0;
    }
    public static GunConfig gunConfig = new GunConfig();

    public static class Vehicles {
        public double pY = 0.014;
        public double pX = 0.02;
        public double dX = 0.023;
        public double dY = 0.021;
        public double pYaw = 0.015;
        public double iYaw = 0.0;
        public double dYaw = 0.028;

        public double psY = 0.0005;
        public double psX = 0.0005;
        public double dsX = 0.00015;
        public double dsY = 0.00015;
        public double psYaw = 0.0004;
        public double isYaw = 0.0;
        public double isX = 0.0001;
        public double isY = 0.0001;
        public double dsYaw = 0;
        public double kDrive = 1.0;
    }
    public static Vehicles vehicles = new Vehicles();

    public static class SizeRobotConfig {
        public double robotWidth = 45.0;
        public double robotLength = 45.0;
        public double halfLength = robotLength * 0.5;
        public double halfWidth = robotWidth * 0.5;
    }
    public static SizeRobotConfig sizeRobotConfig = new SizeRobotConfig();

    public static class SeparatorConfig {
        public int POSITION_1 = 0;
        public int POSITION_2 = -250;
        public int POSITION_3 = -430;
        public PIDCoefficients separatorPid = new PIDCoefficients(0.009, 0.0, 0.0);
        public double KICK_SERVO_POSITION = 0.5;
        public double RETURN_SERVO_POSITION = 0.0;
    }
    public static SeparatorConfig separatorConfig = new SeparatorConfig();

    public static class CameraConfig {
        public double yaw = 0.0;
        public double pitch = 0.0;
        public double roll = -90.0;
        public double x = 0.0;
        public double y = 0.0;
        public double z = 0.0;
    }
    public static CameraConfig cameraConfig = new CameraConfig();

    public static class PinpointConfig {
        public double xOffset = 9.5;
        public double yOffset = 7;
    }
    public static PinpointConfig pinpointConfig = new PinpointConfig();

    public static class PositionAutoBlue {
        public Pose posGun = new Pose(30, -60, 45); //x y yaw
        public Pose posFirstEat = new Pose(60, -140, -90);
        public Pose posFirstFinishEat = new Pose(-5, -140, -90);
        public Pose posTwoEat = new Pose(75, -195,-90);
        public Pose posFinishTwoEat = new Pose(-20, -195, -90);
        public Pose posThreeEat = new Pose(75, -260, -90);
        public Pose posFinishTreeEat = new Pose(-20, -260, -90);
        public Pose posToDamper = new Pose(75, -195, -90);
        public Pose posEatFromDamper = new Pose(0, -195, -65);
        public double xSwap = -110;
        public double ySwap = 150;
        public double yawSwap = 0;
    }
    public static PositionAutoBlue positionAutoBlue = new PositionAutoBlue();

    public static class PositionAutoRed {
        public double[] posGun = {65, -15, 0}; //x y yaw
        public double[] posFirstEat = {-60, -140, 90};
        public double[] posFirstFinishEat = {40, 55, 55};
        public double[] posTwoEat = {-75, -195, 90};
        public double[] posFinishTwoEat = {55, -195, 90};
        public double[] posThreeEat = {-75, -260, 90};
        public double[] posFinishTreeEat = {45, -260, 90};
        public double[] posDrop = {40, -165, 180};
        public double xSwap = 110;
        public double ySwap = 150;
        public double yawSwap = 0;
    }
    public static PositionAutoRed positionAutoRed = new PositionAutoRed();
}