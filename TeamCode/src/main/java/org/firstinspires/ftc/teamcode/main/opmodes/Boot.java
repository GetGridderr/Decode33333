package org.firstinspires.ftc.teamcode.main.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class Boot extends LinearOpMode{
    public static boolean redAlliance = false;

    @Override
    public void runOpMode() throws InterruptedException {
        GoBildaPinpointDriver odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();
        telemetry.clear();

        boolean aPressed = false;
        while (opModeIsActive()) {
            if (gamepad1.a && !aPressed) {
                aPressed = true;
                redAlliance = !redAlliance;
            }
            if (!gamepad1.a) aPressed = false;

            odo.update();
            if (gamepad1.dpad_down) {
                odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
                odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
                odo.setOffsets(105, 50, DistanceUnit.MM);
                odo.resetPosAndIMU();
            }

            telemetry.addLine(redAlliance ? "RED" : "BLUE");
            telemetry.addLine("x - change alliance");
            telemetry.addLine("");
            telemetry.addLine(odo.getDeviceStatus().toString());
            telemetry.addLine("dpad_down - initialize pinpoint");
            telemetry.update();
        }
    }
}
