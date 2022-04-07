package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "RedRight", group = "red", preselectTeleOp = "Gru")
public class RedRight extends LinearOpMode {

    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector("red");


    final double BOB = .18;
    int duckLevel = 3;
    final double DRIVE_POWER = .4;
    int FORWARD_TICKS = 80;
    int STRAFE_TICKS = 30;
    long SLEEPY_TIME = 3000;
    final double TURN_POWER =.2;
    final double G_SERVO_POSITION = 0;


    public void runOpMode() {
        initRobot();
        identifyDuck();
        telemetry.addData("Level: ", duckLevel);
        waitForStart();
        turnAwayFromWall();
        driveToHub();
        robot.deliverBlock(duckLevel);
        parkInWarehouse();
    }


    public void initRobot(){
        robot.init();
        robot.initCameraServo(BOB);
    }


    public void identifyDuck(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(SLEEPY_TIME);
        duckLevel = detector.duckLevel();
    }


    public void turnAwayFromWall() {
        int STRAFE_POWER = 1;
        int STRAFE_TICKS = -110;
        long SLEEP = 100;
        int TURN_DEGREES = 92;
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.autoStrafe6(STRAFE_POWER, STRAFE_TICKS);
        sleep(SLEEP);
        robot.autoTurnDegrees(TURN_POWER, TURN_DEGREES);
        sleep(SLEEP);
        robot.setPowerAll(0);
    }


    public void driveToHub () {
        int DRIVE_TICKS = 140;
        long SLEEP = 100;
        robot.driveForward(DRIVE_POWER, DRIVE_TICKS);
        robot.setPowerAll(0);
        sleep(SLEEP);
    }


    public void parkInWarehouse (){
        int DRIVE_TICKS = 1200;
        long SLEEP = 100;
        int BACKWARDS_DRIVE_TICKS = -100;
        int TURN_DEGREES = 90;
        double S_POWER = .6;
        robot.driveForward(DRIVE_POWER, BACKWARDS_DRIVE_TICKS);
        robot.setPowerAll(0);
        sleep(SLEEP);
        robot.autoTurnDegrees(TURN_POWER, TURN_DEGREES);
        sleep(SLEEP);
        robot.driveForward(DRIVE_POWER, DRIVE_TICKS);
        robot.setPowerAll(0);
        robot.autoStrafe6(S_POWER,150);
    }
}