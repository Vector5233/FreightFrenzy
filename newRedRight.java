package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "newRedRight", group = "GROUP_NAME")
public class newRedRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();

    final double BOB = .07;
    int duckLevel = 3;
    final double DRIVEPOWER = .4;
    //int FORWARDTICKS = 80;
    //int STRAFETICKS = 30;
    long SLEEPYTIME = 3000;
    final double TURNPOWER =.2;
    final double INITGRABBERSERVOPOSITION = 1;

    public void runOpMode() {
        initRobot();
        identifyDuck();
        telemetry.addData("Level ", duckLevel);
        waitForStart();
        turnAwayFromWall();
        driveToHub();
        robot.deliverBlock(duckLevel);
        parkInWarehouse();
    }



    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);
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

    public void turnAwayFromWall() {
        int DRIVETICKS = 50;
        int STRAFEPOWER = 1;
        int STRAFETICKS = -90;
        long HUBSLEEP = 100;
        int TURNDEGREES = 90;
        robot.autoStrafe(STRAFEPOWER, STRAFETICKS);
        sleep(HUBSLEEP);
        robot.autoTurnDegrees(TURNPOWER, TURNDEGREES);
        sleep(HUBSLEEP);
        robot.setPowerAll(0);
    }


    public void driveToHub () {
        int DRIVETICKS = 240;
        long HUBSLEEP = 100;
        robot.driveForward(DRIVEPOWER, DRIVETICKS);
        sleep(HUBSLEEP);
    }

    public void parkInWarehouse (){
        int DRIVETICKS = 1200;
        long HUBSLEEP = 100;
        int BACKWARDSDRIVETICKS = -100;
        int TURNDEGREES = 90;
        robot.driveForward(DRIVEPOWER, BACKWARDSDRIVETICKS);
        sleep(HUBSLEEP);
        robot.autoTurnDegrees(TURNPOWER, TURNDEGREES);
        sleep(HUBSLEEP);
        robot.driveForward(DRIVEPOWER, DRIVETICKS);
    }

}
