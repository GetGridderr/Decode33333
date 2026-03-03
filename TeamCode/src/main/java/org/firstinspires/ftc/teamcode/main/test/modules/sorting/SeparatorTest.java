package org.firstinspires.ftc.teamcode.main.test.modules.sorting;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;

@TeleOp(name="SeparatorTest", group="Test")
public class SeparatorTest extends OpMode {
    private int count = 1;
    private final Motor separatorMotor = new Motor("motor_separator");
    @Override
    public void init() {
        separatorMotor.initialize(hardwareMap);
    }

    @Override
    public void loop() {
        FtcDashboard.getInstance().getTelemetry().addData("Count Ball:", count);
        separatorMotor.setPower(1.0);
//        Separator.getInstance().turnToPosition(count);
//        if (gamepad1.dpad_right) {
//            if (count == 3) { count = 1; }
//            while (gamepad1.dpad_right) {
//                Separator.getInstance().turnToPosition(count);
//            }
//            count++;
//        }
    }
}
