package org.firstinspires.ftc.teamcode.core.device.sensor;

import org.firstinspires.ftc.teamcode.core.device.Device;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import androidx.annotation.NonNull;

public class SensorColor extends Device {

    private AdafruitI2cColorSensor adafruitSensor;
    private I2cAddr customAddress = null;

    public SensorColor(String name) {
        super(name);
        this.customAddress = I2cAddr.create8bit(0x49);
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
    public void initialize(@NonNull HardwareMap hardwareMap) {
        if (isInitialized()) return;

        adafruitSensor = hardwareMap.get(AdafruitI2cColorSensor.class, getName());
        if (customAddress != null) {
            adafruitSensor.setI2cAddress(customAddress);
        }
        adafruitSensor.enableLed(true);

    }

    public boolean isInitialized() {
        return adafruitSensor != null;
    }

    public int getRed() {
        return adafruitSensor != null ? adafruitSensor.red() : 0;
    }

    public int getGreen() {
        return adafruitSensor != null ? adafruitSensor.green() : 0;
    }

    public int getBlue() {
        return adafruitSensor != null ? adafruitSensor.blue() : 0;
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
        return adafruitSensor != null ? adafruitSensor.alpha() : 0;
    }

    public void setLed(boolean enabled) {
        if (adafruitSensor != null) {
            adafruitSensor.enableLed(enabled);
        }
    }
}