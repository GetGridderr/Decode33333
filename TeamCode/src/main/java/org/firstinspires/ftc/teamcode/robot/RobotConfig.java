package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConfig {
    public static double gF = 0.000395727, gB = 0.212187859;
    public static double desiredVoltage = 13.0;
    public static double towerMin = -190, towerMax = 120;
    public static double gunOffset = -1.75;
    public static double xOdometerOffset = 51.5, yOdometerOffset = 99.5;
    // 60 110

    public static double shootMin = 0.1;
    public static double shootMax = 0.94;
    /* servo can be 30 deg to 60 */
    public static double shootPosPerRad = (shootMax - shootMin) / (Math.PI * 0.33333);
    public static double shootBaseOffsetInRad = 0.166667; /* 30 deg */
}
