package org.firstinspires.ftc.teamcode.main.config;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.core.util.pid.PIDCoefficients;

@Config
public class ConfigValues {
    public static class GunConfig {
        public double velocity = -1000;
        public double degreeTower = 0.35;
        public PIDCoefficients gunPid = new PIDCoefficients(0.009, 0.0, 0.0);
        public double alpha = 1;
        public double spdMul = 0.00025;
    }
    public static GunConfig gunConfig = new GunConfig();

    public static class SizeRobotConfig {
    }
    public static SizeRobotConfig sizeRobotConfig = new SizeRobotConfig();

    public static class SeparatorConfig {
        public int POSITION_1 = 0;
        public int POSITION_2 = -250;
        public int POSITION_3 = -430;
        public double KICK_SERVO_POSITION = 0.5;
        public double RETURN_SERVO_POSITION = 0.0;
        public double velocitySeparator = 0.0;
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
