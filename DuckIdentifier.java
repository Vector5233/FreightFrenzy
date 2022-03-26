package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "identifierTester")
@Disabled
public class DuckIdentifier extends LinearOpMode {
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector("red");
    final double BOB = 0;

    @Override
    public void runOpMode() {
        virtualBotObject robot = new virtualBotObject(this);
        robot.init();
        //robot.initCameraServo(BOB);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(2000);

        telemetry.addLine(detector.printCenter());
        telemetry.addLine(detector.printLeft());
        telemetry.addLine(detector.printRight());

        //telemetry.addData();
        detector.getPosition();

        waitForStart();
        telemetry.addLine(detector.getPosition());
        telemetry.update();
        sleep(5000);
    }
}

