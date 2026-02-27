package org.firstinspires.ftc.teamcode.robot.opmodes.match;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.trait.event.RobotAction;
import org.firstinspires.ftc.teamcode.core.trait.event.WaitAction;
import org.firstinspires.ftc.teamcode.robot.FieldRenderer;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.WebConfig;
import org.firstinspires.ftc.teamcode.robot.event.FlowAction;
import org.firstinspires.ftc.teamcode.robot.event.GunAction;
import org.firstinspires.ftc.teamcode.robot.event.MoveAction;
import org.firstinspires.ftc.teamcode.robot.event.TeleopMovementAction;


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
    private FlowAction flowAction = new FlowAction(false);
    public TeleopMovementAction moveAction = new TeleopMovementAction(gamepad1);


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
        moveAction.gamepad1 = gamepad1;
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
//        gunAction.update();

//        double stickX = gamepad1.left_stick_x;
//        double stickY = -gamepad1.left_stick_y;
//        double stickYaw = gamepad1.right_stick_x;

        RobotAction sleep = new WaitAction(0.05);
        while (!sleep.isFinished()) {
            sleep.update();
        }

        // Enable flow
        if (gamepad1.squareWasPressed()) flowEnabled = !flowEnabled;
        if (flowEnabled) flowAction.startFlow();
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

        moveAction.update();
        FtcDashboard.getInstance().getTelemetry().addData("Speed Yaw", Robot.getSpeedYaw());
        FtcDashboard.getInstance().getTelemetry().addData("Flow speed", Robot.flowMotor.getPower());

        FieldRenderer.renderRobot();
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
