/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 * @author Semen Scherbina
 */

package org.firstinspires.ftc.teamcode.robot.opmodes.test;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.core.trait.event.WaitAction;
import org.firstinspires.ftc.teamcode.core.util.Util;
import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

import java.sql.Time;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(
        name = "Positions tuner",
        group = "Dev"
)
@Config
public final class PosTuner extends OpMode {
    public static double robotX = -164.3;
    public static double robotY = 164.45;
    public static double robotYaw = 180;
    public static boolean turnTower = false, enableOdometry = false;

    @Override
    public void init() {
        robotX = -164.3;
        robotY = 164.45;
        robotYaw = 180;
        Robot.teamColor = Robot.TeamColor.RED;
        Robot.initialize(hardwareMap);
    }

    @Override
    public void init_loop() {
        Robot.setPos(robotX, robotY, robotYaw);
    }

    @Override
    public void loop() {
        FieldRenderer.renderRobot();

        RobotAction sleep = new WaitAction(0.05);
        while (!sleep.isFinished()) {
            sleep.update();
        }

        double stickX = Robot.normalizeStickPower(gamepad1.left_stick_x);
        double stickY = Robot.normalizeStickPower(-gamepad1.left_stick_y);
        double stickYaw = Robot.normalizeStickPower(gamepad1.right_stick_x)
                * WebConfig.stickNormYawKmul;
        Robot.setPowers(stickX,
                stickY,
                stickYaw,
                false);
        if (turnTower) Robot.turnTower(Robot.angleToGoal());
        if (enableOdometry) {
            Robot.updateOdometry();
            robotX = Robot.getX();
            robotY = Robot.getY();
            robotYaw = Robot.getYaw();
        } else {
            Robot.setPos(robotX, robotY, robotYaw);
        }

        FtcDashboard.getInstance().getTelemetry().addData("Gamepad lX", stickX);
        FtcDashboard.getInstance().getTelemetry().addData("Gamepad lY", stickY);
        FtcDashboard.getInstance().getTelemetry().addData("Gamepad rX", stickYaw);
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
