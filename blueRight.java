package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="blueRight", group = "GROUP_NAME")

public class blueRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double BLUERIGHT = .6;
    int duckLevel = 3;
    //int duckLevel = duckDetector.duckLevel();
    final double INITGRABBERSERVOPOSITION = 1;

    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel
    public void runOpMode() {
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BLUERIGHT);

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
        robot.driveForward(.3, 90);
        robot.autoStrafe(.2,-25);
        sleep(2000);
        robot.turnOffDuckSpinner();
        robot.autoStrafe(.2,100);
        robot.autoTurn(.2, -600);
        sleep(100);
        robot.driveForward(.2, 600);
        sleep(100);
        robot.deliverBlock(duckLevel);
        robot.autoTurn(.2,-175);
        robot.driveForward(.2,-700);
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        sleep(6000);
    }
}