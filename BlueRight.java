  package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "BlueRight", group = "blue", preselectTeleOp = "Gru")
public class BlueRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

    private OpenCvInternalCamera phoneCam;
    private final duckID2 detector = new duckID2("blue");
    final double BOB = .72;
    int duckLevel = 3;
    final double DRIVE_POWER = .4;
    int FORWARD_TICKS = 75;
    long SLEEPY = 3000;
    final double TURN_POWER =.3;
    final double G_SERVO_POSITION = 0;

    public void runOpMode() {
        initRobot();
        identifyDuck();

        waitForStart();

        duckSpinnerDrive();

        driveToHub();
        robot.deliverBlock(duckLevel);
        parkInStorage();
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
        double TIMEOUT = 5000;
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.initGrabberServo(G_SERVO_POSITION);
        sleep(900);
        robot.turnOnDuckSpinner();
        robot.driveForward(DRIVE_POWER, FORWARD_TICKS);
        robot.strafeTimeout(STRAFE_POWER,STRAFE, TIMEOUT);
        //robot.autoStrafe6(STRAFE_POWER, STRAFE);
        sleep(SLEEPY);
        robot.turnOffDuckSpinner();
    }


    public void driveToHub(){
        int STRAFE_POWER = 1;
        int STRAFE_TICKS = 150;
        long SLEEP = 100;
        int TURN_LEFT = -570;
        int TURN_LEFT_ONE = -590;
        int TICK_FORWARD = 350;
        int TICK_FORWARD_ONE = 350;
        //int TICK_FORWARD_TWO = 590;
        robot.autoStrafe6(STRAFE_POWER, STRAFE_TICKS);
        //robot.autoTurn(TURN_POWER, TURN_LEFT);
        if (duckLevel == 1) {
            robot.autoTurn(TURN_POWER, TURN_LEFT_ONE);
        } else {
            robot.autoTurn(TURN_POWER, TURN_LEFT);
        }
        sleep(SLEEP);
        //robot.driveForward(TURN_POWER, TICK_FORWARD);
        if (duckLevel == 1) {
            robot.driveForward(TURN_POWER, TICK_FORWARD_ONE);
        } else {
            robot.driveForward(TURN_POWER, TICK_FORWARD);
        }
        sleep(SLEEP);
        robot.setPowerAll(0);
    }


    public void parkInStorage(){
        int TURN_TICKS = 175;
        int STRAFE2 = 90;
        int BACKWARDS = -590;
        int STRAFE = -50;
        double PARKING_POWER = .2;
        long NAP = 500;

        robot.autoTurnDegrees(TURN_POWER,-35);
        sleep(NAP);

        robot.autoStrafe6(TURN_POWER, STRAFE);

        //sleep(SLEEPY);
        robot.driveForward(PARKING_POWER,BACKWARDS);
        sleep(SLEEPY);
        robot.turnOnGrabberMotor();
        robot.turnOnGrabberMotorAuto() ;
        sleep(1200);
        robot.turnOffGrabberMotor();
        sleep(SLEEPY);

    }
}
