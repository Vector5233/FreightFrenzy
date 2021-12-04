package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="redLeft", group = "GROUP_NAME")

public class redLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double BOB = 0;
    int duckLevel = 3;
    //int duckLevel = duckDetector.duckLevel();
    final double INITGRABBERSERVOPOSITION = 1;

    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel
    public void runOpMode() {
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(3000);
        duckLevel = detector.duckLevel();

        waitForStart();
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.initGrabberServo(0);
        robot.turnOnDuckSpinner();
        robot.driveForward(.4, 80);
        robot.autoStrafe(.2,30);
        sleep(3000);
        robot.turnOffDuckSpinner();
        robot.driveForward(.4, -50);
        robot.autoTurn(.2, 600);
        sleep(100);
        robot.driveForward(.2, 600);
        sleep(100);
        robot.deliverBlock(duckLevel);
        robot.autoTurn(.2,175);
        robot.driveForward(.2,-475);
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        sleep(600);
    }
}