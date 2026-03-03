package org.firstinspires.ftc.teamcode.core.device.sensor.color;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.teamcode.core.device.Device;

public class SensorColor extends Device {

    private AdafruitI2cColorSensor colorSensor;
    private I2cAddr customAddress = null;

    public SensorColor(String name, AdafruitI2cColorSensor sensor) {
        super(name);
        colorSensor = ColorSensorFix.fix(sensor);
    }

    public SensorColor(String name) {
        super(name);
    }

    public SensorColor(String name, I2cAddr address) {
        super(name);
        this.customAddress = address;
    }

    public SensorColor(String name, int address) {
        super(name);
        this.customAddress = I2cAddr.create8bit(address);
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        if (isInitialized()) return;

        colorSensor = hardwareMap.get(AdafruitI2cColorSensor.class, getName());
        if (customAddress != null) {
            colorSensor.setI2cAddress(customAddress);
        }
        colorSensor.enableLed(true);

    }

    public boolean isInitialized() {
        return colorSensor != null;
    }

    public int getRed() {
        return colorSensor != null ? colorSensor.red() : 0;
    }

    public int getGreen() {
        return colorSensor != null ? colorSensor.green() : 0;
    }

    public int getBlue() {
        return colorSensor != null ? colorSensor.blue() : 0;
    }

    public float[] getHSV() {
        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(getRed(), getGreen(), getBlue(), hsv);
        return hsv;
    }

    public float getHue() {
        return getHSV()[0];
    }

    public int getAlpha() {
        return colorSensor != null ? colorSensor.alpha() : 0;
    }

    public void setLed(boolean enabled) {
        if (colorSensor != null) {
            colorSensor.enableLed(enabled);
        }
    }
}