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
    public static double xTarget;
    public static double yTarget;
    public static double yawTarget;
    @Override
    public void init() {
        Robot.initialize(hardwareMap);
    }

    @Override
    public void loop() {

        if((System.nanoTime() * Constants.SECONDS_PER_NANOSECOND) % 4 >= 2) {
            Robot.goTo(0, 0, 0);
        } else {
            Robot.goTo(xTarget, yTarget, yawTarget);
        }
        FieldRenderer.renderRobot();
        FtcDashboard.getInstance().getTelemetry().addData("X", Robot.getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y", Robot.getY());
        FtcDashboard.getInstance().getTelemetry().addData("Yaw", Robot.getYaw());
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
