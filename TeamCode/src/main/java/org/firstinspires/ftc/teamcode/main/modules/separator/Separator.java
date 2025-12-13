package org.firstinspires.ftc.teamcode.main.modules.separator;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.separatorConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.core.device.motor.EncoderMotor;
import org.firstinspires.ftc.teamcode.core.device.motor.FFEncoderMotor;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.core.device.motor.Motor;

// coding by Matvey Ivanovv

public class Separator implements Initializable {
    private static final Separator INSTANCE = new Separator();
//    private final Motor separatorMotor;
    private final FFEncoderMotor separatorMotor;
    private final Servomotor servoToGun;
    private final ElapsedTime safeTime = new ElapsedTime();
    private int targetPosition = 0;
    private int oldPosition = 0;

    public static Separator getInstance() { return INSTANCE; }

    public Separator() {
        separatorMotor = new FFEncoderMotor("motor_separator");
        servoToGun = new Servomotor("servo_to_gun");
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        separatorMotor.initialize(hardwareMap);
        separatorMotor.resetEncoder();
        servoToGun.initialize(hardwareMap);
    }

    @Override
    public boolean isInitialized() {
        return separatorMotor.isInitialized();
    }

    public void turnToPosition(int position) { // 1, 2 or 3

        if (position < 1 || position > 3) { return; }
        switch (position) {
            case 1:
                targetPosition = separatorConfig.POSITION_1;
                break;
            case 2:
                targetPosition = separatorConfig.POSITION_2;
                break;
            case 3:
                targetPosition = separatorConfig.POSITION_3;
                break;
        }
        if (oldPosition < targetPosition) {
            if (separatorMotor.getEncoderPosition() < targetPosition) {
                startSeparator(-targetPosition);
            }
            oldPosition = targetPosition;
            separatorMotor.setPower(0);
            separatorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        else {
            if (separatorMotor.getEncoderPosition() > targetPosition) {
                startSeparator(targetPosition);
            }
            oldPosition = targetPosition;
            separatorMotor.setPower(0);
            separatorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

    }

    public void startSeparator(double position) {
        separatorMotor.setPIDCoefficients(separatorConfig.separatorPid);
        separatorMotor.setPoint(position);  // sensorVoltage.calculateCoefficientVoltage(velocity)
        separatorMotor.speedTick();
    }

    public void test() {
        if (!isInitialized()) {
            FtcDashboard.getInstance().getTelemetry().addData("No init motor", true);
        }
        separatorMotor.setPower(1.0);
    }

    public void kickToGun() {
        servoToGun.setServoPosition(separatorConfig.KICK_SERVO_POSITION);
//        servoToGun.setServoPosition(RETURN_SERVO_POSITION);

    }

    public int getEncoderPos() { return separatorMotor.getEncoderPosition(); }

}
