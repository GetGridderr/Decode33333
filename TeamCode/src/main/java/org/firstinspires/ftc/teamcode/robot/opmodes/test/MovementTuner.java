/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 * @author Semen Scherbina
 */

package org.firstinspires.ftc.teamcode.robot.opmodes.test;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.util.Util;
import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(
        name = "Movement PID tuner",
        group = "Dev"
)
@Config
public final class MovementTuner extends OpMode {
    public static double xTarget;
    public static double yTarget;
    public static double yawTarget;
    public static boolean xGamepad = true, yGamepad = true, yawGamepad = true;


    @Override
    public void init() {
        Robot.teamColor = Robot.TeamColor.RED;
        Robot.initialize(hardwareMap);
        Robot.updateOdometry();
    }

    @Override
    public void init_loop() {
        Robot.setInitPos();
    }

    public static void goTo(double xPos,
                            double yPos,
                            double yaw,
                            double xSpd,
                            double ySpd,
                            double yawSpd) {
        Robot.updateOdometry();
        Robot.setPIDsFromConfig();
        double xErr = xPos - Robot.getX();
        double yErr = yPos - Robot.getY();
        double[] errVector = Util.rotateVector(xErr, yErr, -Robot.getYaw());
        xErr = errVector[0];
        yErr = errVector[1];
        FtcDashboard.getInstance().getTelemetry().addData("XErr", xErr);
        FtcDashboard.getInstance().getTelemetry().addData("YErr", yErr);
        double xPID, yPID, yawPID;
        xPID = Robot.xPosPID.update(-xErr, 0);
        yPID = Robot.yPosPID.update(-yErr, 0);
        yawPID = Robot.yawPosPID.update(-Util.getShortestPathToAngle(Robot.getYaw(), yaw), 0);
        Robot.setPowers(
                xGamepad ? xSpd : xPID,
                yGamepad ? ySpd : yPID,
                yawGamepad ? yawSpd : yawPID,
                WebConfig.normAuto
        );
    }

    @Override
    public void loop() {
        if((System.nanoTime() * Util.SECONDS_PER_NANOSECOND) % 4 >= 2) {
            goTo(0, 0, 0,
                    gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        } else {
            goTo(xTarget, yTarget, yawTarget,
                    gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }
        Robot.turnTower(Robot.angleToGoal());
        FieldRenderer.renderRobot();
    }
}
