package org.firstinspires.ftc.teamcode.main.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.device.sensor.color.SensorColor;

public class ColorCounter {
    private SensorColor leftSensor;
    private SensorColor rightSensor;

    private int purpleCount = 0;
    private int greenCount = 0;
    private boolean objectSeen = false;

    public ColorCounter(String leftName, String rightName) {
        this.leftSensor = new SensorColor(leftName);
        this.rightSensor = new SensorColor(rightName);
    }

    public void initialize(HardwareMap hardwareMap) {
        leftSensor.initialize(hardwareMap);
        rightSensor.initialize(hardwareMap);
    }

    public boolean isInitialized() { return leftSensor.isInitialized() && rightSensor.isInitialized(); }

    public void checkSensors() {
        double hue = leftSensor.getHue();
        boolean seeColor = checkColor(hue);
        if (!seeColor) {
            hue = rightSensor.getHue();
            seeColor = checkColor(hue);
        }

        if (seeColor && !objectSeen) {
            if (hue >= 260 && hue <= 320) {
                purpleCount++;
            } else if (hue >= 70 && hue <= 160) {
                greenCount++;
            }
            objectSeen = true;
        }
        if (!checkColor(leftSensor.getHue()) && !checkColor(rightSensor.getHue())) {
            objectSeen = false;
        }
    }

    private boolean checkColor(double hue) {
        return (hue >= 260 && hue <= 320) || (hue >= 70 && hue <= 160);
    }

    public int getPurpleCount() {
        return purpleCount;
    }

    public int getGreenCount() {
        return greenCount;
    }

    public void reset() {
        purpleCount = 0;
        greenCount = 0;
        objectSeen = false;
    }
}