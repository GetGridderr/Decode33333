package org.firstinspires.ftc.teamcode.main.opencv;

import static org.firstinspires.ftc.teamcode.main.config.ConfigValues.cameraConfig;

import android.util.Size;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.teamcode.core.device.trait.Initializable;

import java.util.List;

public class AprilTag implements Initializable {

    private static final AprilTag INSTANCE = new AprilTag();
    private static final boolean USE_WEBCAM = true;
    private AprilTagProcessor aprilTag;
    public VisionPortal visionPortal;
    private double posAprilX = 0;
    private double posAprilY = 0;
    private double posAprilZ = 0;
    private double id = 0;

    private double bearing;
    private double yaw;
    private double range;

    private boolean isInitialized = false;

    @Override
    public void initialize(HardwareMap hardwareMap) {
        Position cameraPosition = new Position(
                DistanceUnit.CM,
                cameraConfig.x,
                cameraConfig.y,
                cameraConfig.z,
                0
        );

        YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(
                AngleUnit.DEGREES,
                cameraConfig.yaw,
                cameraConfig.pitch,
                cameraConfig.roll,
                0
        );
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        builder.setCameraResolution(new Size(640, 360));
        builder.enableLiveView(true);
        builder.addProcessor(aprilTag);

        visionPortal = builder.build();
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    public static org.firstinspires.ftc.teamcode.main.opencv.AprilTag getInstance() { return INSTANCE; }

    public double getPosAprilX() {
        return posAprilX;
    }

    public double getPosAprilY() {
        return posAprilY;
    }

    public double getPosAprilZ() {
        return posAprilZ;
    }

    public double getId() { return id; }

    public double getBearing() { return bearing; }

    public double getRange() { return range; }

    public double getYaw() { return yaw; }


    public void resetApril() {
        posAprilX = 0;
        posAprilZ = 0;
        posAprilY = 0;
        id = 0;
    }

    public void updateAprilTagData() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        FtcDashboard.getInstance().getTelemetry().addData("AprilTags Detected", currentDetections.size());
        resetApril();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {

                posAprilX = detection.ftcPose.x;
                posAprilY = detection.ftcPose.y;
                posAprilZ = detection.ftcPose.z;
                id = detection.id;
                bearing = detection.ftcPose.bearing;
                range = detection.ftcPose.range;

                FtcDashboard.getInstance().getTelemetry().addData("ID", detection.id);
                FtcDashboard.getInstance().getTelemetry().addLine(String.format("XYZ %.2f %.2f %.2f", posAprilX, posAprilY, posAprilZ));
                FtcDashboard.getInstance().getTelemetry().addData("Yaw", detection.ftcPose.yaw);
            }
        }
    }
}