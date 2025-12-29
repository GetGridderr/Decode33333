package org.firstinspires.ftc.teamcode.core.device.odometer;

import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.pinpointConfig;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.core.device.Device;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

public class OdometerPinpoint extends Device {
    private static final OdometerPinpoint INSTANCE = new OdometerPinpoint("pinpoint");
    private GoBildaPinpointDriver pinpoint;
    private double oldYaw = 0;
    private int rotations = 0;
    private Pose2D pos;

    private GoBildaPinpointDriver.EncoderDirection directionX = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    private GoBildaPinpointDriver.EncoderDirection directionY = GoBildaPinpointDriver.EncoderDirection.FORWARD;

    public OdometerPinpoint(String name) {
        super(name);
        pinpoint = null;
    }

    public static OdometerPinpoint getInstance() { return INSTANCE; }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setOffsets(pinpointConfig.xOffset, pinpointConfig.yOffset, DistanceUnit.CM);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        pinpoint.resetPosAndIMU();
        pos = pinpoint.getPosition();
    }

    @Override
    public boolean isInitialized() {
        return pinpoint != null;
    }

    public double getX() {
        pinpoint.update();
        pos = pinpoint.getPosition();
        return pos.getX(DistanceUnit.CM);
    }

    public double getY() {
        pinpoint.update();
        pos = pinpoint.getPosition();
        return pos.getY(DistanceUnit.CM);
    }

    public double getYaw() {
        pinpoint.update();
        pos = pinpoint.getPosition();
        double yaw = pos.getHeading(AngleUnit.DEGREES);
        if(yaw - oldYaw > 360) {
            rotations -= 1;
        }
        if(oldYaw - yaw > 360) {
            rotations += 1;
        }
        oldYaw = yaw;
        return rotations * 360.0 + yaw;
    }

    public void reset() {
        pinpoint.resetPosAndIMU();
    }

    public double getSpeedX() {
        return pinpoint.getVelX(DistanceUnit.CM);
    }

    public double getSpeedY() { return pinpoint.getVelY(DistanceUnit.CM); }

    public GoBildaPinpointDriver.EncoderDirection getDirectionalX() { return directionX; }

    public GoBildaPinpointDriver.EncoderDirection getDirectionalY() { return directionY; }

    public void invertDirectional(GoBildaPinpointDriver.EncoderDirection xDirection,
                                  GoBildaPinpointDriver.EncoderDirection yDirection) {
        pinpoint.setEncoderDirections(xDirection, yDirection);
        directionX = xDirection;
        directionY = yDirection;
    }

    public double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    public double getShortestPathToAngle(double currentAngle, double targetAngle) {
        double difference = targetAngle - currentAngle;
        return normalizeAngle(difference);
    }
}