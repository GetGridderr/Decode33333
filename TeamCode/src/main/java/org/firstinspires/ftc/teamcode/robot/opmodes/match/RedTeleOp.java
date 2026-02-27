package org.firstinspires.ftc.teamcode.robot.opmodes.match;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.WebConfig;
import org.firstinspires.ftc.teamcode.robot.event.GunAction;
import org.firstinspires.ftc.teamcode.robot.event.MoveAction;


/**
 * TeleOp-period program logic of the robot
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(
        name = "Red TeleOp",
        group = "Dev"
)
public class RedTeleOp extends OpMode {
    private GunAction gunAction;
    private MoveAction endPosMoveAction;
    private boolean flowEnabled = false;
    private boolean brushEnabled = false;


    protected void setTeamSign() {
        Robot.teamColor = Robot.TeamColor.RED;
    }

    private void initActions() {
        gunAction = new GunAction();
        endPosMoveAction = new MoveAction(Robot.RED_TELEOP_END_POS);
    }

    @Override
    public void init() {
        Robot.initialize(hardwareMap);
        setTeamSign();
        initActions();
        Robot.setPIDsFromConfig();
        Robot.updateOdometry();
    }

    @Override
    public void init_loop() {
        Robot.setInitPos();
        Robot.updateOdometry();
        FieldRenderer.renderRobot();
    }

    @Override
    public void loop() {
        Robot.updateOdometry();
        gunAction.update();

//        double stickX = gamepad1.left_stick_x;
//        double stickY = -gamepad1.left_stick_y;
//        double stickYaw = gamepad1.right_stick_x;

        // Normalize stick values
        double stickX = Robot.normalizeStickPower(gamepad1.left_stick_x);
        double stickY = Robot.normalizeStickPower(-gamepad1.left_stick_y);
        double stickYaw =
                Robot.normalizeStickPower(gamepad1.right_stick_x)
                * WebConfig.stickNormYawKmul;

        // Enable flow
        if (gamepad1.squareWasPressed()) flowEnabled = !flowEnabled;
        if (flowEnabled) Robot.startFlow();
        else Robot.stopFlow();

        // Enable only brush
        if (gamepad1.circleWasPressed()) flowEnabled = !flowEnabled;
        if (flowEnabled) Robot.startBrush();
        else Robot.startBrush();


        // Keep opened gun door while button is pressed
        if (gamepad1.rightBumperWasPressed()) {
            Robot.openGunDoor();
        } else if (gamepad1.rightBumperWasReleased()) {
            Robot.closeGunDoor();
        }

        // Move robot to the final square stop place
        if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
            endPosMoveAction.update();
        }
        // Else move by gamepad
        else Robot.setPowers(stickX,
                stickY,
                stickYaw,
                false);

        FtcDashboard.getInstance().getTelemetry().addData("Gamepad lX", gamepad1.left_stick_x);
        FtcDashboard.getInstance().getTelemetry().addData("Gamepad lY", -gamepad1.left_stick_y);
        FtcDashboard.getInstance().getTelemetry().addData("Gamepad rX", gamepad1.right_stick_x);
        FtcDashboard.getInstance().getTelemetry().addData("Normalized lX", stickX);
        FtcDashboard.getInstance().getTelemetry().addData("Normalized lY", stickY);
        FtcDashboard.getInstance().getTelemetry().addData("Normalized rX", stickYaw);

        FieldRenderer.renderRobot();
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
