package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "identifierTester")
public class DuckIdentifier extends LinearOpMode {
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();


    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.UPRIGHT);
        phoneCam.setPipeline(detector);

        detector.getPosition();
        telemetry.addLine(detector.getPosition());
        telemetry.update();
        sleep(5000);
        waitForStart();
    }
}

