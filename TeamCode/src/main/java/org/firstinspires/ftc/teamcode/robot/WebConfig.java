package org.firstinspires.ftc.teamcode.robot;


import com.acmerobotics.dashboard.config.Config;


@Config
public final /* static data */ class WebConfig {
    // auto pos pids
    public static double xP = 0.1, xI, xD = 0.015,
            yP = 0.1, yI, yD = 0.017,
            yawP = 0.05, yawI, yawD = 0.0055;
    // teleop speed pidfs
    public static double vxP = 0.003, vxI, vxD,
            vyP = 0.003, vyI, vyD,
            vyawP = 0.002, vyawI, vyawD;
    public static double vxMax = 180, vyMax = 180, vYawMax = 420;
    // gun pidfs
    public static double gP = 0.005, gI, gD;
    // lower power limit
    public static double powerLim = 0.15;
    // normalization
    public static boolean normTeleop = false, normAuto = true;
    // pauses
    public static double pauseEat = 1.7; // prev = 2
    public static double pauseShoot = 1.7; // prev = 2
    public static double pauseMoveLimit = 3;
    // Goal
    public static double GoalX = -160;
    public static double GoalY = -160;
    // Gun
    public static double gunAngle = 0.3, gunVel = 950;
    // Door
    public static double doorOpen = 0.68, doorClosed = 0.95; // prev closed = {0.89, 0.84}

    // Gamepad stick normalization coefficients
    public static double stickNormPowExponent = 1.414643504944718;
    public static double stickNormYawKmul = 0.6;

    public static double flowSpeedWithClosedDoor = 0.5;
    public static double flowSpeedBack = -0.5;
    public static double flowSpeedLong = 0.6;

    public static double flowForward = 0.4;
    public static double flowBackward = 0.1;
}
