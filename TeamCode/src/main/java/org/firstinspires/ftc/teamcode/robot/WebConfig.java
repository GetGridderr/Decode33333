package org.firstinspires.ftc.teamcode.robot;


import com.acmerobotics.dashboard.config.Config;


@Config
public final /* static data */ class WebConfig {
    // auto pos pids
    public static double xP = 0.1, xI, xD = 0.015,
            yP = 0.1, yI, yD = 0.017,
            yawP = 0.05, yawI, yawD = 0.0055;
    // teleop speed pidfs
    public static double vxP, vxI, vxD, vxF = 1,
            vyP, vyI, vyD, vyF = 1,
            vyawP, vyawI, vyawD, vyawF = 1;
    // gun pidfs
    public static double gP = 0.005, gI, gD;
    // lower power limit
    public static double powerLim = 0.2;
    // normalization
    public static boolean normTeleop = false, normAuto = true;
    // pauses
    public static double pauseEat = 2;
    public static double pauseShoot = 2;
    public static double pauseMoveLimit = 3;
    // Goal
    public static double GoalX = -160;
    public static double GoalY = -145;
    // Gun
    public static double gunAngle = 0.3, gunVel = 950;
    // Door
    public static double doorOpen = 0.68, doorClosed = 0.89;
}
