package org.firstinspires.ftc.teamcode.core.device.servomotor;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.core.device.Device;

// coding by Matvey Ivanovv

public class Servomotor extends Device {
    private Servo device;
    private double currentDegree;
    public Servomotor(String name) {
        
        super(name);
        device = null;
        currentDegree = 0;
    }

    @Override
    public void initialize(@NonNull HardwareMap hardwareMap) {
        if (isInitialized()) return;
        device = hardwareMap.get(Servo.class, name);
    }

    @Override
    public boolean isInitialized() { return device != null; }

    public double getCurrentDegree() { return device.getPosition(); }

    public void setServoPosition(double degree) {
        device.setPosition(degree);
        currentDegree = degree;
    }
}