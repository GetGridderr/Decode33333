package org.firstinspires.ftc.teamcode.robot;


import com.acmerobotics.dashboard.config.Config;


@Config
public final /* static data */ class WebConfig {
    // auto pos pids
    public static double xP, xI, xD,
            yP, yI, yD,
            yawP, yawI, yawD;
    // teleop speed pidfs
    public static double vxP, vxI, vxD, vxF = 1,
            vyP, vyI, vyD, vyF = 1,
            vyawP, vyawI, vyawD, vyawF = 1;
    // lower power limit
    public static double powerLim = 0.2;
    // normalization
    public static boolean normTeleop = false, normAuto = false;
}
