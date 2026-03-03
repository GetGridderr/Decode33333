package org.firstinspires.ftc.teamcode.main.modules.transfer;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.device.motor.Motor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.main.util.ColorCounter;

@Config
public class Brush  implements Initializable {
    private static final Brush INSTANCE = new Brush();
    private final Motor motorBrush;
    private final ColorCounter counter;
    public static double velocityBrush = 1.0;
    public int countBalls = 0;

    public static Brush getInstance() { return INSTANCE; }

    public Brush() {
        motorBrush = new Motor("motor_brush");
        counter = new ColorCounter("color_sensor_right", "color_sensor_left");
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        motorBrush.initialize(hardwareMap);
        counter.initialize(hardwareMap);
    }

    @Override
    public boolean isInitialized() { return motorBrush.isInitialized() && counter.isInitialized(); }

    public void setVelocityBrush(double velocity) { velocityBrush = velocity; }

    public double getVelocityBrush() { return velocityBrush; }

    public void setVelosityBruh(double vel) {
        velocityBrush = vel;
    }

    public void startBrush() {
        motorBrush.setPower(velocityBrush);
        if (counter.getGreenCount() + counter.getPurpleCount() == 3) {
            stopBrush();
        }
        countBalls = counter.getGreenCount() + counter.getPurpleCount();
        counter.checkSensors();
    }


    public void stopBrush() { motorBrush.setPower(0); }

    public void resetCount() { countBalls = 0; }
}
