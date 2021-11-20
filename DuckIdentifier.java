package org.firstinspires.ftc.teamcode;


import static org.openftc.easyopencv.OpenCvCameraRotation.UPRIGHT;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "identifierTester")
public class DuckIdentifier extends LinearOpMode {
    private OpenCvWebcam webCam;
    private DuckDetector detector = new DuckDetector();


    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
       // OpenCvCamera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        webCam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        telemetry.addLine("create webcam successful");
        telemetry.update();

        webCam.setPipeline(detector);
        telemetry.addLine("set pipeline successful");
        telemetry.update();
        webCam.setMillisecondsPermissionTimeout(2500);

        webCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webCam.startStreaming(320,240, UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addLine("Could not open camera");
                telemetry.update();

            }
        });
        telemetry.addLine("open camera device successful");
        telemetry.update();


        waitForStart();
        detector.getPosition();
        telemetry.addLine(detector.getPosition());
        telemetry.update();
        sleep(5000);
    }
}
