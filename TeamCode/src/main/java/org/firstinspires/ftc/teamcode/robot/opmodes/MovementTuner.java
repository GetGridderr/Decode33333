/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 * @author Semen Scherbina
 */

package org.firstinspires.ftc.teamcode.robot.opmodes;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.util.Constants;
import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.Robot;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(
        name = "Movement PID tuner",
        group = "Dev"
)
@Config
public final class MovementTuner extends OpMode {
    public static double xTarget1, xTarget2;
    public static double yTarget1, yTarget2;
    public static double yawTarget1, yawTarget2 = 90;
    @Override
    public void init() {
        Robot.initialize(hardwareMap);
    }

    @Override
    public void loop() {

        if((System.nanoTime() * Constants.SECONDS_PER_NANOSECOND) % 4 >= 2) {
            Robot.goTo(xTarget1, yTarget1, yawTarget1);
        } else {
            Robot.goTo(xTarget2, yTarget2, yawTarget2);
        }
        FieldRenderer.renderRobot();
        FtcDashboard.getInstance().getTelemetry().addData("X", Robot.getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y", Robot.getY());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw", Robot.getYaw());
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
