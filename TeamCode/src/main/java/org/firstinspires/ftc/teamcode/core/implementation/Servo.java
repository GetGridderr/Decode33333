/**
 * @author Arsen Berezin
 */

package org.firstinspires.ftc.teamcode.core.implementation;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.trait.device.IServo;


/**
 * Just servomotor device.
 *
 * @see IServo
 */
public class Servo implements IServo {
    protected final String name;
    protected com.qualcomm.robotcore.hardware.Servo device;


    public Servo(String name) {
        assert name != null;

        this.name = name;
        device = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        assert hardwareMap != null;

        if (isInitialized()) return;

        device = hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, name);
    }

    @Override
    public boolean isInitialized() {
        return device != null;
    }

    @Override
    public double getPosition() {
        assert isInitialized();
        return device.getPosition();
    }

    @Override
    public void setPosition(double position) {
        assert isInitialized();
        device.setPosition(IServo.normalizePosition(position));
    }
}
