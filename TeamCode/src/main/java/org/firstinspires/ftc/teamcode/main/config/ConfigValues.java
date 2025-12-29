package org.firstinspires.ftc.teamcode.main.config;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDCoefficients;

@Config
public class ConfigValues {
    public static class GunConfig {
        public double velocity = -920;
        public double degreeTower = 0.35;
        public double degreeAngleGun = 0.1;
        public double kP = 0.003;
        public double kI = 0.0;
        public double kD = 0.00;
        public double alpha = 1;
        public double spdMul = 0.00003;
        public double offsetTurn = 0.14;
        public double offsetGun = 10;
        public double R = 25;
    }
    public static GunConfig gunConfig = new GunConfig();

    public static class Vehicles {
        public PIDCoefficients xPid = new PIDCoefficients(0.03, 0.0, 0.01);
        public PIDCoefficients yPid = new PIDCoefficients(0.023, 0.0, 0.015);
        public PIDCoefficients yawPid = new PIDCoefficients(0.02, 0.0, 0.02);
        public double pXY = 0.02;
        public double dXY = 0.04;
        public double pYaw = 0.018;
        public double iYaw = 0.0;
        public double dYaw = 0.026;
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
        public double xOffset = 0.0;
        public double yOffset = 0.0;
    }
    public static PinpointConfig pinpointConfig = new PinpointConfig();

    public static class PositionAutoBlue {
        public double[] posGun = {30, -60, 45}; //x y yaw
        public double[] posFirstEat = {60, -140, -90};
        public double[] posFirstFinishEat = {-5, -140, -90};
        public double[] posTwoEat = {75, -195,-90};
        public double[] posFinishTwoEat = {-20, -195, -90};
        public double[] posThreeEat = {75, -260, -90};
        public double[] posFinishTreeEat = {-20, -260, -90};
        public double[] posDrop = {-20, -165, -180};
        public double xSwap = -110;
        public double ySwap = 150;
        public double yawSwap = 0;
    }
    public static PositionAutoBlue positionAutoBlue = new PositionAutoBlue();

    public static class PositionAutoRed {
        public double[] posGun = {-30, -60, -45}; //x y yaw
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
    public static PositionAutoBlue positionAutoRed = new PositionAutoRed();

    public static class AutoTime {
        public double One = 2000;
        public double Two = 2500;
        public double Three = 2000;
        public double Four = 3500;
        public double Five = 1500;
        public double Six = 2000;
        public double Seven = 2000;
        public double Eight = 2500;
        public double Nine = 2500;
        public double Ten = 1500;
        public double Eleven = 2500;
        public double Twelve = 3000;
        public double Thirteen = 1500;
        public double Fourteen = 2000;
        public double Fifteen = 3000;
        public double Sixteen = 1000;
        public double Seventeen = 1000;

    }

    public static AutoTime autoTime = new AutoTime();
}