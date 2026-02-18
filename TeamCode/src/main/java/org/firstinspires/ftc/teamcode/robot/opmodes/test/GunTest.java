/**
 * @author Arsen Berezin
 * @author Timofey Istomin
 * @author Semen Scherbina
 */

package org.firstinspires.ftc.teamcode.robot.opmodes.test;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.util.pid.PidfCoefficients;
import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.Robot;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(
        name = "Gun tester",
        group = "Dev"
)
@Config
public final class GunTest extends OpMode {
    public static double gunSpeed;
    public static double gunAngle;
    public static double doorValue;
    // 0.3 900
    // from shooting pos
    public static boolean work = false;


    @Override
    public void init() {
        Robot.teamColor = Robot.TeamColor.RED;
        Robot.initialize(hardwareMap);
    }

    @Override
    public void init_loop() {
        Robot.setPos(Robot.RED_GOAL_INIT_POS);
    }

    @Override
    public void loop() {
        Robot.setPIDsFromConfig();
        Robot.updateOdometry();

        if (work) {
//            Robot.startFlow();
//            Robot.turnTower(Robot.angleToGoal());
//            Robot.goTo(Robot.RED_DISTANCE_FROM_GATES);
//            Robot.setGunVelocity(gunSpeed);
//            Robot.setShootingAngle(gunAngle);
            Robot.gunDoor.setPosition(doorValue);
        } else {
            Robot.stopFlow();
            Robot.setGunVelocity(0);
        }

        FtcDashboard.getInstance().getTelemetry().addData("Gun Motor Power", Robot.gunMotor.getPower());
        FtcDashboard.getInstance().getTelemetry().addData("Gun tower servo position", Robot.towerTurnServo.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("Gun servo position", Robot.gunAngleServo.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("Gun door servo position", Robot.gunDoor.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("Gun velocity", Robot.gunMotor.getVelocity());
        FieldRenderer.renderRobot();
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
