package org.firstinspires.ftc.teamcode.main.test.devices;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.odometer.OdometerPinpoint;
import org.firstinspires.ftc.teamcode.core.telemetry.FieldView;
import org.firstinspires.ftc.teamcode.main.movement.Vehicles;

@TeleOp
public class PinpointTest extends OpMode {
    private FieldView fieldView;
    @Override
    public void init() {
        OdometerPinpoint.getInstance().initialize(hardwareMap);
    }

    @Override
    public void loop() {
        if (OdometerPinpoint.getInstance().isInitialized()) {
            FtcDashboard.getInstance().getTelemetry().addData("Initialize is ok", true);
        }
        FtcDashboard.getInstance().getTelemetry().addData("OdometerX", OdometerPinpoint.getInstance().getX());
        FtcDashboard.getInstance().getTelemetry().addData("OdometryY", OdometerPinpoint.getInstance().getY());
        FtcDashboard.getInstance().getTelemetry().addData("OdometryYaw", OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().addData("xSpeed", OdometerPinpoint.getInstance().getSpeedX());
        FtcDashboard.getInstance().getTelemetry().addData("ySpeed", OdometerPinpoint.getInstance().getSpeedY());
        FtcDashboard.getInstance().getTelemetry().addData("yawSpeed", OdometerPinpoint.getInstance().getYaw());
//        fieldView.rotatePoints(OdometerPinpoint.getInstance().getX(), OdometerPinpoint.getInstance().getY(), OdometerPinpoint.getInstance().getYaw());
        fieldView.renderRobot(OdometerPinpoint.getInstance().getX(), OdometerPinpoint.getInstance().getY(), OdometerPinpoint.getInstance().getYaw());
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
