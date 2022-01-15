package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="redLeft", group = "GROUP_NAME")
//properties file
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
    final double INITGRABBERSERVOPOSITION = 1;

    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel

    public void runOpMode() {
        initRobot();
        identifyDuck();
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
        int STRAFEPOWER = 1;
        int STRAFETICKS = -150;         //sign change
        long HUBSLEEP = 100;
        int TURNFORWARD = 625;
        int TICKFORWARD = 480;
        robot.autoStrafe(STRAFEPOWER, STRAFETICKS);
        robot.autoTurn(TURNPOWER, TURNFORWARD);
        sleep(HUBSLEEP);
        robot.driveForward(TURNPOWER, TICKFORWARD);
        sleep(HUBSLEEP);
        robot.setPowerAll(0);
    }

    public void parkInStorage(){
        int TURNTICKS = 175;
        int DRIVEBACKWARDS = -550;
        robot.autoTurn(TURNPOWER,TURNTICKS);
        robot.driveForward(TURNPOWER,DRIVEBACKWARDS);
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        sleep(SLEEPYTIME);
    }

    public void identifyDuck(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(SLEEPYTIME);
        duckLevel = detector.duckLevel();
    }

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);
    }
}