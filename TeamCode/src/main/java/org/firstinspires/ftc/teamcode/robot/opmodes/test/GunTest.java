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
import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;
import org.firstinspires.ftc.teamcode.robot.event.GunAction;
import org.firstinspires.ftc.teamcode.robot.event.FlowAction;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(
        name = "Gun tuner",
        group = "Dev"
)
@Config
public final class GunTest extends OpMode {
    public static double gunSpeed = 900;
    public static double gunAngle = 0.3;
    public static double doorValue;
    public RobotAction action = new GunAction();
    // 0.3 900
    // from shooting pos
    public static boolean work = false;
    public RobotAction flowAction = new FlowAction(false);


    @Override
    public void init() {
        Robot.teamColor = Robot.TeamColor.RED;
        Robot.initialize(hardwareMap);
    }

    @Override
    public void init_loop() {
        Robot.setPos(Robot.RED_CORNER);
    }

    @Override
    public void loop() {
        Robot.setPIDsFromConfig();
        Robot.updateOdometry();

        if (work) {
            Robot.startBrush();
            action.update();
            flowAction.update();
            if(gamepad1.circleWasPressed()) {
                Robot.goTo(Robot.RED_SHOOT_SHORT);
            } else if (gamepad1.squareWasPressed()) {
                Robot.goTo(Robot.RED_SHOOT_LONG);
            } else {
                double stickX = Robot.normalizeStickPower(gamepad1.left_stick_x);
                double stickY = Robot.normalizeStickPower(-gamepad1.left_stick_y);
                double stickYaw = Robot.normalizeStickPower(gamepad1.right_stick_x)
                        * WebConfig.stickNormYawKmul;
                double[] stickCoords = Robot.rotateSticks(stickX, stickY);
                Robot.setPowers(stickCoords[0],
                        stickCoords[1],
                        stickYaw,
                        false);
            }

            if(gamepad1.triangle) {
                Robot.openGunDoor();
            } else {
                Robot.closeGunDoor();
            }
        } else {
            Robot.stopFlow();
            Robot.stopBrush();
            Robot.setGunVelocity(0);
        }

        FtcDashboard.getInstance().getTelemetry().addData("Gun Motor Power", Robot.gunMotor.getPower());
        FtcDashboard.getInstance().getTelemetry().addData("Gun tower servo position", Robot.towerTurnServo.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("Gun servo position", Robot.gunAngleServo.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("Gun door servo position", Robot.gunDoor.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("Gun velocity", Robot.gunMotor.getVelocity());
        FtcDashboard.getInstance().getTelemetry().addData("Flow speed", Robot.flowMotor.getPower());
        FieldRenderer.renderRobot();
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
