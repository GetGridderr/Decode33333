package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConfig {
    public static double gF = 0.000395727, gB = 0.212187859;
    public static double desiredVoltage = 12.0;
    public static double shootMin = 0.1;
    public static double shootMax = 0.94;
    public static double towerMin = -190, towerMax = 120;
    public static double gunOffset = -5;
}
