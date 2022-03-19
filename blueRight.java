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
    final double BLUE_RIGHT = .6;
    int duckLevel = 3;
    final double G_SERVO_POSITION = 1;
    final double POWER2 = .2;


    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel
    public void runOpMode() {
        initBlueRight();
        identifyDuck();
        waitForStart();
        driveToDuckSpinner();
        driveToShippingHub();
        robot.deliverBlock(duckLevel);
        parkInBox();
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
        int DRIVE_TICKS = 90;
        int STRAFE_TICKS = -25;
        long SLEEP = 2000;
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.turnOnDuckSpinner();
        robot.initGrabberServo(0);
        robot.driveForward(POWER3, DRIVE_TICKS);
        robot.autoStrafe(POWER3, STRAFE_TICKS);
        sleep(SLEEP);
        robot.turnOffDuckSpinner();
    }

    public void driveToShippingHub(){
        long SLEEP = 100;
        int TURN_TICKS = 600;
        int DRIVE_TICKS = 475;
        int STRAFE_TICKS = 150;

        robot.autoStrafe(POWER2, STRAFE_TICKS);
        robot.autoTurn(POWER2, -TURN_TICKS);
        sleep(SLEEP);
        robot.driveForward(POWER2, DRIVE_TICKS);
        sleep(SLEEP);
    }

    public void parkInBox(){
        long END_SLEEP = 6000;
        int RIGHT_TURN = -175;
        int DRIVE_FORWARD = -650;
        robot.autoTurn(POWER2, RIGHT_TURN);
        robot.driveForward(POWER2, DRIVE_FORWARD);
        robot.initGrabberServo(G_SERVO_POSITION);
        sleep(END_SLEEP);
    }

    public void initBlueRight (){
        robot.init();
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.initCameraServo(BLUE_RIGHT);
    }
}