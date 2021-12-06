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
    final double DRIVEPOWER = .4;
    int FORWARDTICKS = 80;
    int STRAFETICKS = 30;
    long SLEEPYTIME = 3000;
    final double TURNPOWER =.2;
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
        duckSpinnerDrive();
        driveToHub();
        robot.deliverBlock(duckLevel);
        parkInStorage();

    }

    public void duckSpinnerDrive(){
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.initGrabberServo(0);
        robot.turnOnDuckSpinner();
        robot.driveForward(DRIVEPOWER, FORWARDTICKS);
        robot.autoStrafe(TURNPOWER,STRAFETICKS);
        sleep(SLEEPYTIME);
        robot.turnOffDuckSpinner();
    }

    public void driveToHub(){
        int DRIVETICKS = -50;
        long HUBSLEEP = 100;
        int TICKFORWARD = 600;
        robot.driveForward(DRIVEPOWER, DRIVETICKS);
        robot.autoTurn(TURNPOWER, TICKFORWARD);
        sleep(HUBSLEEP);
        robot.driveForward(TURNPOWER, TICKFORWARD);
        sleep(HUBSLEEP);
    }
    public void parkInStorage(){
        int TURNTICKS = 175;
        int DRIVEBACKWARDS = -475;
        robot.autoTurn(TURNPOWER,TURNTICKS);
        robot.driveForward(TURNPOWER,DRIVEBACKWARDS);
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        sleep(SLEEPYTIME);
    }
}