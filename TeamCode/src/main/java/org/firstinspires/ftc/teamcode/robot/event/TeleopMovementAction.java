package org.firstinspires.ftc.teamcode.robot.event;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.core.util.Util;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.WebConfig;

public class TeleopMovementAction implements RobotAction {

    public Gamepad gamepad1;

    public TeleopMovementAction(Gamepad gamepad1) {
        this.gamepad1 = gamepad1;
    }

    @Override
    public void update() {
        double stickX = Robot.normalizeStickPower(gamepad1.left_stick_x);
        double stickY = Robot.normalizeStickPower(-gamepad1.left_stick_y);
        double stickYaw =
                Robot.normalizeStickPower(gamepad1.right_stick_x)
                        * WebConfig.stickNormYawKmul;
        Robot.setSpeed(stickX*WebConfig.vxMax,
                stickY* WebConfig.vyMax,
                stickYaw*WebConfig.vYawMax);
        double[] currentSpeeds = Util.rotateVector(Robot.getSpeedX(), Robot.getSpeedY(), -Robot.getYaw());
        FtcDashboard.getInstance().getTelemetry().addData("Speed X", currentSpeeds[0]);
        FtcDashboard.getInstance().getTelemetry().addData("Speed Y", currentSpeeds[1]);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
