package org.firstinspires.ftc.teamcode.robot;


import com.acmerobotics.dashboard.config.Config;


@Config
public final /* static data */ class WebConfig {
    // auto pos pids
    public static double xP = 0.2, xI, xD = 0.15,
            yP = 0.1, yI, yD = 0.17,
            yawP = 0.03, yawI, yawD = 0.11;
    // teleop speed pidfs
    public static double vxP, vxI, vxD, vxF = 1,
            vyP, vyI, vyD, vyF = 1,
            vyawP, vyawI, vyawD, vyawF = 1;
    // lower power limit
    public static double powerLim = 0.2;
    // normalization
    public static boolean normTeleop = false, normAuto = true;
}
