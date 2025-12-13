package org.firstinspires.ftc.teamcode.main.opencv;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;
import org.firstinspires.ftc.teamcode.main.modules.transfer.TransferBall;
import org.firstinspires.ftc.teamcode.main.opencv.AprilTag;
import org.firstinspires.ftc.teamcode.core.device.servomotor.Servomotor;

public class TrackingAprilTag implements Initializable {
    private static final TrackingAprilTag INSTANCE = new TrackingAprilTag();

    private static double targetPosAprilTag = 0;
    private double currentPosAprilTag = 0;


    public TrackingAprilTag() {

    }

    @Override
    public void initialize(HardwareMap hardwareMap) {

    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    public static TrackingAprilTag getInstance() { return INSTANCE; }

    public double getTargetPosAprilTag() { return targetPosAprilTag; }

    public double getCurrentPosAprilTag() { return currentPosAprilTag; }

    public void setTargetPosAprilTag(double newPos) { targetPosAprilTag = newPos; }

}
