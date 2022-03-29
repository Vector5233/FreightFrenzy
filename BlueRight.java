package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="BlueRight", group = "GROUP_NAME")
public class BlueRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

    private OpenCvInternalCamera phoneCam;
    private final DuckDetector detector = new DuckDetector("blue");
    Menu initMenu = new Menu(this);
    final double BOB = .72;
    int duckLevel = 3;
    final double DRIVE_POWER = .4;
    int FORWARD_TICKS = 75;
    int STRAFE_TICKS = -30;
    int GRABBER_SPEED= 20;
    long SLEEPY = 3000;
    final double TURN_POWER =.2;
    final double G_SERVO_POSITION = .5;

    public void runOpMode() {
        initRobot();
        identifyDuck();

        waitForStart();

        duckSpinnerDrive();
        driveToHub();
        robot.deliverBlock(duckLevel);
        //  parkInStorage();
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
        sleep(SLEEPY);
        duckLevel = detector.duckLevel();
    }

    public void duckSpinnerDrive(){
        int STRAFE = -25;
        double STRAFE_POWER = .9;
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.turnOnDuckSpinner();
        robot.driveForward(DRIVE_POWER, FORWARD_TICKS);
        robot.autoStrafe6(STRAFE_POWER, STRAFE);
        sleep(SLEEPY);
        robot.turnOffDuckSpinner();
    }


    public void driveToHub(){
        int STRAFE_POWER = 1;
        int STRAFE_TICKS = 150;         //sign change
        long SLEEP = 100;
        int TURN_LEFT = -575;
        int TICK_FORWARD = 470;
        int TICK_FORWARD_TWO = 470;
        robot.autoStrafe6(STRAFE_POWER, STRAFE_TICKS);
        robot.autoTurn(TURN_POWER, TURN_LEFT);
        sleep(SLEEP);
        if (duckLevel == 2) {
            robot.driveForward(TURN_POWER, TICK_FORWARD_TWO);
        } else {
            robot.driveForward(TURN_POWER, TICK_FORWARD);
        }
        sleep(SLEEP);
        robot.setPowerAll(0);
    }
    public void parkInStorage(){
        int TURN_TICKS = 175;
        int STRAFE2 = 90;
        int BACKWARDS = -700;
        int STRAFE = 70;
        double PARKING_POWER = .2;

        robot.autoTurn(TURN_POWER,TURN_TICKS);

        if (duckLevel == 2) {
            robot.autoStrafe6(TURN_POWER, STRAFE2);
        } else {
            robot.autoStrafe6(TURN_POWER, STRAFE);
        }
        sleep(SLEEPY);
        robot.driveForward(PARKING_POWER,BACKWARDS);
        robot.turnOnGrabberMotor();
        sleep(500);
        robot.turnOffGrabberMotor();
        sleep(SLEEPY);

    }
}
