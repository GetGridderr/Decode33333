package org.firstinspires.ftc.teamcode.main.opmodes.teleop;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.gunConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.pinpointConfig;
import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.vehicles;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.modules.gun.GunControl;
import org.firstinspires.ftc.teamcode.main.modules.transfer.Brush;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;
import org.firstinspires.ftc.teamcode.main.opmodes.Boot;

// coding by Matvey Ivanovv

/*
    EDGE - ПОБЕДА!
*/

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="Dev")
@Config
public class TeleOp extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();

    public void initialize() {
        Vehicles.getInstance().initialize(hardwareMap);
        Brush.getInstance().initialize(hardwareMap);
        OdometerPinpoint.getInstance().initialize(hardwareMap);
        GunControl.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().initialize(hardwareMap);
        TransferBall.getInstance().timeForFlow.reset();

        Vehicles.getInstance().setSpeedPID(vehicles.psX, 0, vehicles.dsX, vehicles.psY, 0,
                vehicles.dsY, vehicles.psYaw, vehicles.isYaw, vehicles.dsYaw);
        OdometerPinpoint.getInstance().setOffset(pinpointConfig.xOffset, pinpointConfig.yOffset);
        OdometerPinpoint.getInstance().setPosition(pinpointConfig.xPositionStart, pinpointConfig.yPositionStart, pinpointConfig.headingStart);

        if (Vehicles.getInstance().isInitialized() &&
                GunControl.getInstance().isInitialized() &&
                TransferBall.getInstance().isInitialized() &&
                OdometerPinpoint.getInstance().isInitialized()) {
                telemetry.addLine("Initialized");
        }
        gunConfig.bananPosition = 0.17;
        runtime.reset();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        initialize();

        Pose2D afterResetPos;
        int allianceSign;
        if (Boot.redAlliance) {
            afterResetPos = new Pose2D(DistanceUnit.CM, 0, 0, AngleUnit.DEGREES, 0);
            allianceSign = 1;
        } else {
            afterResetPos = new Pose2D(DistanceUnit.CM, 0, 0, AngleUnit.DEGREES, 0);
            allianceSign = -1;
        }

        double allianceVectorsCorrection = 90;
        waitForStart();
        telemetry.clear();

        while (opModeIsActive()) {
            OdometerPinpoint.getInstance().update();
            Pose2D robotPos = new Pose2D(DistanceUnit.CM, OdometerPinpoint.getInstance().getX(),
                    OdometerPinpoint.getInstance().getY(), AngleUnit.DEGREES, OdometerPinpoint.getInstance().getYaw());
            Pose2D goalPose = new Pose2D(DistanceUnit.CM, gunConfig.posGoalX, gunConfig.posGoalY, AngleUnit.DEGREES, 0);
            double forward = gamepad1.left_stick_y;
            double horizontal = -gamepad1.left_stick_x;
            double rotate = -gamepad1.right_stick_x;
//            DataShots dataShots = GunControl.getInstance().shotToDistance(OdometerPinpoint.getInstance().getDistanceToTarget(gunConfig.posGoalX, gunConfig.posGoalY));
//            gunConfig.velocity = dataShots.speed;
//            gunConfig.bananPosition = dataShots.angle;
//            gunConfig.kP = dataShots.k;

            telemetry.addData("x: ", robotPos.getX(DistanceUnit.CM));
            telemetry.addData("y: ", robotPos.getY(DistanceUnit.CM));
            telemetry.addData("yaw: ", robotPos.getHeading(AngleUnit.DEGREES));
            telemetry.addData("distance", OdometerPinpoint.getInstance().getDistanceToTarget(goalPose.getX(DistanceUnit.CM), goalPose.getY(DistanceUnit.CM)));
            FieldView.renderRobot(Vehicles.getInstance().getPositionOdometerY(), -Vehicles.getInstance().getPositionOdometerX(), OdometerPinpoint.getInstance().getYaw());
//            FieldView.renderGoal(gunConfig.posGoalX, gunConfig.posGoalY);

            GunControl.getInstance().startShot();
//            GunControl.getInstance().setBananServo();
//            GunControl.getInstance().aimToTarget(goalPose, robotPos);

            Vehicles.getInstance().setSpeed(forward, horizontal, rotate, allianceVectorsCorrection);

            if (gamepad1.right_bumper) {
                TransferBall.getInstance().openDoor();
                TransferBall.getInstance().startFlow();
            } else if (gamepad1.left_bumper) {
                TransferBall.getInstance().reverse();
            } else {
                TransferBall.getInstance().startBrush();
                TransferBall.getInstance().startPulseFlow();
                TransferBall.getInstance().closeDoor();
            }

            if (gamepad1.dpad_down) OdometerPinpoint.getInstance().setPosition(afterResetPos);

            telemetry.update();
        }
    }
}