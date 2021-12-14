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
    final double INITGRABBERSERVOPOSITION = 1;


    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel
    public void runOpMode() {
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BLUERIGHT);
        identifyDuck();

        waitForStart();

        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();

        robot.initGrabberServo(0);
        driveToDuckSpinner();

        robot.deliverBlock(duckLevel);
        robot.autoTurn(.2, -175);
        robot.driveForward(.2, -700);
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        sleep(6000);
    }

    public void identifyDuck() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(3000);
        duckLevel = detector.duckLevel();
    }

    public void driveToDuckSpinner(){
        final double POWER3 = .3;
        int DRIVETICKS = 90;
        int STRAFETICKS = -25;
        long SLEEP = 2000;
        robot.turnOnDuckSpinner();
        robot.driveForward(POWER3, DRIVETICKS);
        robot.autoStrafe(POWER3, STRAFETICKS);
        sleep(SLEEP);
        robot.turnOffDuckSpinner();
    }

    public void driveToShippingHub(){
        final double POWER2 = .2;

        robot.autoStrafe(.2, 100);
        robot.autoTurn(.2, -600);
        sleep(100);
        robot.driveForward(.2, 600);
        sleep(100);
    }
}