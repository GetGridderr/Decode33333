package org.firstinspires.ftc.teamcode.core.device;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;


// coding by Timofei
@Config
public class RGBChannel extends Device implements Initializable {
    public enum SignalPin {
        MINUS,
        PLUS
    }


    protected Servo device;
    public static double maxBorder, minBorder;
    protected SignalPin signalPin;


    public RGBChannel(String name, SignalPin pin) {
        this(name, 0.999999, 0.650051, pin);
    }

    public RGBChannel(String name, double _maxBorder, double _minBorder, SignalPin pin) {
        super(name);
        minBorder = _minBorder;
        maxBorder = _maxBorder;
        signalPin = pin;
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        if (isInitialized()) return;
        device = hardwareMap.get(Servo.class, name);

        PwmControl pwmInterface = ((PwmControl)device);
        PwmControl.PwmRange range = new PwmControl.PwmRange(
                0.0,
                20000.0,
                7000.0);
        pwmInterface.setPwmRange(range);

        if(signalPin == SignalPin.MINUS) {
            device.setDirection(Servo.Direction.REVERSE);
        } else {
            device.setDirection(Servo.Direction.FORWARD);
        }
    }

    @Override
    public boolean isInitialized() {
        return device != null;
    }

    public void setPower(double power) {
        double powerNorm = Math.max(Math.min(power, 1.0), 0.0);  // 0 - 1
        powerNorm *= maxBorder - minBorder;  // 0 - delta
        powerNorm += minBorder;  // min - max
        device.setPosition(powerNorm);
    }

    public double getPower() {
        double res = device.getPosition();  // min - max
        res -= minBorder;  // 0 - delta
        res /= (maxBorder - minBorder);  // 0 - 1
        return res;
    }
}
