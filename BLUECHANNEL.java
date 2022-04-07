package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
@Autonomous
@Disabled

public class BLUECHANNEL extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private final duckID2 detector = new duckID2("blue");
    int duckLevel = 3;
    public void runOpMode() {
        robot.init();
        identifyDuck();
        telemetry.addData("level:", duckLevel);
        waitForStart();

    }

    public void identifyDuck(){
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
            phoneCam.openCameraDevice();

            phoneCam.setPipeline(detector);
            phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
            sleep(1000);
            duckLevel = detector.duckLevel();
        }
}
