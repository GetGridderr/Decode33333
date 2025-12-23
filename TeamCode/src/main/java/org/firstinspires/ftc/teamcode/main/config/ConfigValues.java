package org.firstinspires.ftc.teamcode.main.config;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDCoefficients;

@Config
public class ConfigValues {
    public static class GunConfig {
        public double velocity = -1100;
        public double degreeTower = 0.35;
        public double  kTower = 0.4;
        public PIDCoefficients gunPid = new PIDCoefficients(0.007, 0.0, 0.0);
        public double kP = 0.009;
        public double kI = 0.0;
        public double kD = 0.0;
        public double alpha = 1;
        public double spdMul = 0.00025;
    }
    public static GunConfig gunConfig = new GunConfig();

    public static class Vehicles {
        public PIDCoefficients xPid = new PIDCoefficients(0.03, 0.0, 0.01);
        public PIDCoefficients yPid = new PIDCoefficients(0.023, 0.0, 0.015);
        public PIDCoefficients yawPid = new PIDCoefficients(0.02, 0.0, 0.02);
        public double targetX = 0;
        public double targetY = 20;
        public double targetYaw = 0;
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
}
